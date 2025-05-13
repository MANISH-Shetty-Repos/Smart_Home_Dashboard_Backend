package com.smarthome.smart_home_dashboard.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}