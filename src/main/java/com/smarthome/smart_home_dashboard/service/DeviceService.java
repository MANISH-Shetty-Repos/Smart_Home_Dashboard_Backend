package com.smarthome.smart_home_dashboard.service;

import com.smarthome.smart_home_dashboard.dto.DeviceDto;
import java.util.List;

public interface DeviceService {
    List<DeviceDto> getAllDevices();
    DeviceDto getDeviceById(Long id);
    DeviceDto createDevice(DeviceDto deviceDto);
    DeviceDto updateDevice(Long id, DeviceDto deviceDto);
    DeviceDto toggleDevice(Long id);
    List<DeviceDto> getDevicesByRoom(Long roomId);
    void deleteDevice(Long id);

    List<com.smarthome.smart_home_dashboard.dto.DeviceHistoryDto> getDeviceHistory(Long deviceId);
}