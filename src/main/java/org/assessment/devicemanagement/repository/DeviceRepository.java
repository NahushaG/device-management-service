package org.assessment.devicemanagement.repository;

import java.util.List;
import java.util.UUID;
import org.assessment.devicemanagement.model.Device;
import org.assessment.devicemanagement.model.DeviceState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, UUID> {
  List<Device> findByBrandIgnoreCase(String brand);
  List<Device> findByState(DeviceState state);
  List<Device> findByBrandIgnoreCaseAndState(String brand, DeviceState state);
}
