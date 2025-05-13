package com.smarthome.smart_home_dashboard.service;

import com.smarthome.smart_home_dashboard.dto.UserDto;
import com.smarthome.smart_home_dashboard.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.List;

public interface UserService extends UserDetailsService {
    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    UserDto createUser(UserDto userDto);
    UserDto updateUser(Long id, UserDto userDto);
    void deleteUser(Long id);
    User loadUserByUsername(String email);
}