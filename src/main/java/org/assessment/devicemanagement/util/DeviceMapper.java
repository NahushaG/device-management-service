package org.assessment.devicemanagement.util;

import org.assessment.devicemanagement.dto.DeviceResponse;
import org.assessment.devicemanagement.model.Device;

public class DeviceMapper {


  public static DeviceResponse toResponse(Device device) {
    return new DeviceResponse(
        device.getId().toString(),
        device.getName(),
        device.getBrand(),
        device.getState(),
        device.getCreatedAt()
    );
  }

}
