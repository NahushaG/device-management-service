package org.assessment.devicemanagement.dto;

import org.assessment.devicemanagement.model.DeviceState;

public record CreateDeviceRequest(
    String name,
    String brand,
    DeviceState state
) {

}
