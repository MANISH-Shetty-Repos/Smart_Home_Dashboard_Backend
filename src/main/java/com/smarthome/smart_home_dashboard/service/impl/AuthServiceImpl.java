package com.smarthome.smart_home_dashboard.service.impl;

import com.smarthome.smart_home_dashboard.dto.AuthRequest;
import com.smarthome.smart_home_dashboard.dto.AuthResponse;
import com.smarthome.smart_home_dashboard.dto.UserDto;
import com.smarthome.smart_home_dashboard.exception.BadCredentialsException; // Added missing import
import com.smarthome.smart_home_dashboard.model.User;
import com.smarthome.smart_home_dashboard.repository.UserRepository;
import com.smarthome.smart_home_dashboard.service.AuthService;
import com.smarthome.smart_home_dashboard.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.smarthome.smart_home_dashboard.model.Role;
import com.smarthome.smart_home_dashboard.exception.ResourceExistsException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public AuthResponse register(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new ResourceExistsException("User with email '" + userDto.getEmail() + "' already exists");
        }

        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(Role.USER); // Default role

        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser);
        String refreshToken = jwtService.generateRefreshToken(savedUser);

        UserDto responseUserDto = UserDto.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .build();

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .user(responseUserDto)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .build();

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .user(userDto)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse refreshToken(String refreshToken) {
        String userEmail = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        String newToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .build();

        return AuthResponse.builder()
                .token(newToken)
                .refreshToken(newRefreshToken)
                .user(userDto)
                .build();
    }
}