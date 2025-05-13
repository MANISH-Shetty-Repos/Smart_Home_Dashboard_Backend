package com.smarthome.smart_home_dashboard.repository;

import com.smarthome.smart_home_dashboard.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    boolean existsByName(String name);
    Optional<Room> findByName(String name);
}