package com.smarthome.smart_home_dashboard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeviceDto {
    private Long id;

    @NotBlank(message = "Device name is required")
    private String name;

    @NotBlank(message = "Device type is required")
    private String type;

    private boolean status;

    @NotNull(message = "Room ID is required")
    private Long roomId;
}