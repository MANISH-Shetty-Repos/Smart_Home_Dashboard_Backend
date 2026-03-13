package com.smarthome.smart_home_dashboard.controller;

import com.smarthome.smart_home_dashboard.dto.DeviceDto;
import com.smarthome.smart_home_dashboard.service.DeviceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/devices")
@RequiredArgsConstructor
@io.swagger.v3.oas.annotations.tags.Tag(name = "Devices", description = "Endpoints for managing smart devices, toggling status, and viewing history")
public class DeviceController {


    private final DeviceService deviceService;

    @GetMapping
    public ResponseEntity<List<DeviceDto>> getAllDevices() {
        return ResponseEntity.ok(deviceService.getAllDevices());
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<DeviceDto>> getDevicesByRoom(@PathVariable Long roomId) {
        return ResponseEntity.ok(deviceService.getDevicesByRoom(roomId));
    }


    @GetMapping("/{id}")
    public ResponseEntity<DeviceDto> getDeviceById(@PathVariable Long id) {
        return ResponseEntity.ok(deviceService.getDeviceById(id));
    }

    @PostMapping
    public ResponseEntity<DeviceDto> createDevice(@Valid @RequestBody DeviceDto deviceDto) {
        return ResponseEntity.ok(deviceService.createDevice(deviceDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeviceDto> updateDevice(@PathVariable Long id, @Valid @RequestBody DeviceDto deviceDto) {
        return ResponseEntity.ok(deviceService.updateDevice(id, deviceDto));
    }


    @PatchMapping("/{id}/toggle")
    public ResponseEntity<DeviceDto> toggleDevice(@PathVariable Long id) {
        return ResponseEntity.ok(deviceService.toggleDevice(id));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<com.smarthome.smart_home_dashboard.dto.DeviceHistoryDto>> getDeviceHistory(@PathVariable Long id) {
        return ResponseEntity.ok(deviceService.getDeviceHistory(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }
}