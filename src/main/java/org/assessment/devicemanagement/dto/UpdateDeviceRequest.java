package org.assessment.devicemanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.assessment.devicemanagement.model.DeviceState;

public record UpdateDeviceRequest(
    @NotBlank @Size(max = 120) String name,
    @NotBlank @Size(max = 120) String brand,
    @NotNull DeviceState state
) {

}
