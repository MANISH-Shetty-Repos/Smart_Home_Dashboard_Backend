package com.smarthome.smart_home_dashboard.service;

import com.smarthome.smart_home_dashboard.dto.AuthRequest;
import com.smarthome.smart_home_dashboard.dto.AuthResponse;

public interface AuthService {
    AuthResponse authenticate(AuthRequest request);
    AuthResponse refreshToken(String refreshToken);
}