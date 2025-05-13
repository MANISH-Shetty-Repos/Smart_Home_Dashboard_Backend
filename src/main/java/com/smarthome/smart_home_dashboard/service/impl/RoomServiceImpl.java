package com.smarthome.smart_home_dashboard.service.impl;

import com.smarthome.smart_home_dashboard.dto.RoomDto;
import com.smarthome.smart_home_dashboard.exception.ResourceExistsException;
import com.smarthome.smart_home_dashboard.exception.ResourceNotFoundException;
import com.smarthome.smart_home_dashboard.model.Room;
import com.smarthome.smart_home_dashboard.repository.RoomRepository;
import com.smarthome.smart_home_dashboard.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RoomDto> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(room -> modelMapper.map(room, RoomDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RoomDto getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
        return modelMapper.map(room, RoomDto.class);
    }

    @Override
    @Transactional
    public RoomDto createRoom(RoomDto roomDto) {
        if (roomRepository.existsByName(roomDto.getName())) {
            throw new ResourceExistsException("Room with name '" + roomDto.getName() + "' already exists");
        }

        Room room = modelMapper.map(roomDto, Room.class);
        Room savedRoom = roomRepository.save(room);
        return modelMapper.map(savedRoom, RoomDto.class);
    }

    @Override
    @Transactional
    public RoomDto updateRoom(Long id, RoomDto roomDto) {
        Room existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));

        if (!existingRoom.getName().equals(roomDto.getName()) &&
                roomRepository.existsByName(roomDto.getName())) {
            throw new ResourceExistsException("Room with name '" + roomDto.getName() + "' already exists");
        }

        existingRoom.setName(roomDto.getName());

        Room updatedRoom = roomRepository.save(existingRoom);
        return modelMapper.map(updatedRoom, RoomDto.class);
    }

    @Override
    @Transactional
    public void deleteRoom(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new ResourceNotFoundException("Room not found with id: " + id);
        }
        roomRepository.deleteById(id);
    }
}