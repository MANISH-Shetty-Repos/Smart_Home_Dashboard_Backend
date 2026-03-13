package com.smarthome.smart_home_dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceHistoryDto {
    private Long id;
    private Long deviceId;
    private String deviceName;
    private String userEmail;
    private String action;
    private LocalDateTime timestamp;
}
