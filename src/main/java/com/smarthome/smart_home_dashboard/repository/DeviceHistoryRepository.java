package com.smarthome.smart_home_dashboard.repository;

import com.smarthome.smart_home_dashboard.model.DeviceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DeviceHistoryRepository extends JpaRepository<DeviceHistory, Long> {
    List<DeviceHistory> findByDeviceIdOrderByTimestampDesc(Long deviceId);
}
