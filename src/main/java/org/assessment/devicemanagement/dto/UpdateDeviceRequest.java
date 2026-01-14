package org.assessment.devicemanagement.dto;

import org.assessment.devicemanagement.model.DeviceState;

public record UpdateDeviceRequest(
    String name,
    String brand,
    DeviceState state
) {

}
