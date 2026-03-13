package com.smarthome.smart_home_dashboard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RoomDto {
    private Long id;

    @NotBlank(message = "Room name is required")
    @Size(max = 100, message = "Room name is too long")
    private String name;

    private java.util.List<DeviceDto> devices;
}