package com.smarthome.smart_home_dashboard.dto;

import lombok.Data;

@Data
public class DeviceDto {
    private Long id;
    private String name;
    private String type;
    private boolean status;
    private Long roomId;
}