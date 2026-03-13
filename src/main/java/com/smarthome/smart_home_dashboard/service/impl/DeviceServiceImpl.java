package com.smarthome.smart_home_dashboard.service.impl;

import com.smarthome.smart_home_dashboard.dto.DeviceDto;
import com.smarthome.smart_home_dashboard.exception.ResourceNotFoundException;
import com.smarthome.smart_home_dashboard.model.Device;
import com.smarthome.smart_home_dashboard.model.Room;
import com.smarthome.smart_home_dashboard.repository.DeviceRepository;
import com.smarthome.smart_home_dashboard.repository.RoomRepository;
import com.smarthome.smart_home_dashboard.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import com.smarthome.smart_home_dashboard.repository.DeviceHistoryRepository;
import com.smarthome.smart_home_dashboard.repository.UserRepository;
import com.smarthome.smart_home_dashboard.model.DeviceHistory;
import com.smarthome.smart_home_dashboard.model.User;
import com.smarthome.smart_home_dashboard.dto.DeviceHistoryDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final RoomRepository roomRepository;
    private final DeviceHistoryRepository historyRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final SimpMessagingTemplate messagingTemplate;

    private User getCurrentUser() {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private void logHistory(Device device, String action) {
        DeviceHistory history = DeviceHistory.builder()
                .device(device)
                .user(getCurrentUser())
                .action(action)
                .timestamp(LocalDateTime.now())
                .build();
        historyRepository.save(history);
        broadcastDeviceUpdate(device, action);
    }

    private void broadcastDeviceUpdate(Device device, String action) {
        DeviceDto dto = modelMapper.map(device, DeviceDto.class);
        messagingTemplate.convertAndSend("/topic/devices", dto);
    }


    @Override
    @Transactional(readOnly = true)
    public List<DeviceDto> getAllDevices() {
        return deviceRepository.findAll().stream()
                .map(device -> modelMapper.map(device, DeviceDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DeviceDto getDeviceById(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found with id: " + id));
        return modelMapper.map(device, DeviceDto.class);
    }

    @Override
    @Transactional
    public DeviceDto createDevice(DeviceDto deviceDto) {
        Room room = roomRepository.findById(deviceDto.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + deviceDto.getRoomId()));

        Device device = modelMapper.map(deviceDto, Device.class);
        device.setRoom(room);
        Device savedDevice = deviceRepository.save(device);
        logHistory(savedDevice, "CREATED");
        return modelMapper.map(savedDevice, DeviceDto.class);
    }

    @Override
    @Transactional
    public DeviceDto updateDevice(Long id, DeviceDto deviceDto) {
        Device existingDevice = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found with id: " + id));

        Room room = roomRepository.findById(deviceDto.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + deviceDto.getRoomId()));

        existingDevice.setName(deviceDto.getName());
        existingDevice.setType(deviceDto.getType());
        existingDevice.setStatus(deviceDto.isStatus());
        existingDevice.setRoom(room);

        Device updatedDevice = deviceRepository.save(existingDevice);
        logHistory(updatedDevice, "UPDATED");
        return modelMapper.map(updatedDevice, DeviceDto.class);
    }

    @Override
    @Transactional
    public DeviceDto toggleDevice(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found with id: " + id));
        
        device.setStatus(!device.isStatus());
        Device updatedDevice = deviceRepository.save(device);
        
        logHistory(updatedDevice, updatedDevice.isStatus() ? "TOGGLED_ON" : "TOGGLED_OFF");
        
        return modelMapper.map(updatedDevice, DeviceDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceHistoryDto> getDeviceHistory(Long deviceId) {
        if (!deviceRepository.existsById(deviceId)) {
            throw new ResourceNotFoundException("Device not found with id: " + deviceId);
        }
        
        return historyRepository.findByDeviceIdOrderByTimestampDesc(deviceId).stream()
                .map(history -> DeviceHistoryDto.builder()
                        .id(history.getId())
                        .deviceId(history.getDevice().getId())
                        .deviceName(history.getDevice().getName())
                        .userEmail(history.getUser().getEmail())
                        .action(history.getAction())
                        .timestamp(history.getTimestamp())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeviceDto> getDevicesByRoom(Long roomId) {
        return deviceRepository.findByRoomId(roomId).stream()
                .map(device -> modelMapper.map(device, DeviceDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteDevice(Long id) {

        if (!deviceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Device not found with id: " + id);
        }
        deviceRepository.deleteById(id);
    }
}