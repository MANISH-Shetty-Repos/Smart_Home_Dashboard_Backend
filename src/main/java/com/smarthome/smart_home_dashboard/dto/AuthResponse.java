package com.smarthome.smart_home_dashboard.dto;

import com.smarthome.smart_home_dashboard.model.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;
    private String refreshToken;
    private UserDto user;
}