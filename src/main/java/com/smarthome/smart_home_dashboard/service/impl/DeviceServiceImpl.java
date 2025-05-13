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

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;

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
        return modelMapper.map(updatedDevice, DeviceDto.class);
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