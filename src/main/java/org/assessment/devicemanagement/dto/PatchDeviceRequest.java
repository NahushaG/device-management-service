package org.assessment.devicemanagement.dto;

import org.assessment.devicemanagement.model.DeviceState;

public record PatchDeviceRequest(
    String name,
    String brand,
    DeviceState state
) {

}
