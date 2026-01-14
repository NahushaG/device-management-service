package org.assessment.devicemanagement.service;

import java.util.List;
import java.util.UUID;
import org.assessment.devicemanagement.dto.CreateDeviceRequest;
import org.assessment.devicemanagement.dto.PatchDeviceRequest;
import org.assessment.devicemanagement.dto.UpdateDeviceRequest;
import org.assessment.devicemanagement.model.Device;
import org.assessment.devicemanagement.model.DeviceState;

public interface DeviceService {

  Device create(CreateDeviceRequest req);

  Device get(UUID id);

  List<Device> getDevices(String brand, DeviceState state);

  Device update(UUID id, UpdateDeviceRequest req);   // PUT

  Device patch(UUID id, PatchDeviceRequest req);     // PATCH

  void delete(UUID id);
}
