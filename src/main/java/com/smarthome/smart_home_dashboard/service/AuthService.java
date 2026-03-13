package com.smarthome.smart_home_dashboard.service;

import com.smarthome.smart_home_dashboard.dto.AuthRequest;
import com.smarthome.smart_home_dashboard.dto.AuthResponse;
import com.smarthome.smart_home_dashboard.dto.UserDto;

public interface AuthService {
    AuthResponse register(UserDto userDto);
    AuthResponse authenticate(AuthRequest request);
    AuthResponse refreshToken(String refreshToken);
}