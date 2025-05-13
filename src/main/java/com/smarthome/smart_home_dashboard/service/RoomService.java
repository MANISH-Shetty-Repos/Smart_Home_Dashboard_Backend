package com.smarthome.smart_home_dashboard.service;

import com.smarthome.smart_home_dashboard.dto.RoomDto;
import java.util.List;

public interface RoomService {
    List<RoomDto> getAllRooms();
    RoomDto getRoomById(Long id);
    RoomDto createRoom(RoomDto roomDto);
    RoomDto updateRoom(Long id, RoomDto roomDto);
    void deleteRoom(Long id);
}