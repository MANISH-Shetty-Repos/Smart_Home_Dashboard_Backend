package com.smarthome.smart_home_dashboard.repository;

import com.smarthome.smart_home_dashboard.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findByRoomId(Long roomId);
    Optional<Device> findByName(String name);
}