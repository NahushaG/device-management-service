package org.assessment.devicemanagement.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;
import org.assessment.devicemanagement.model.DeviceState;

public record PatchDeviceRequest(
    @Size(min = 1, max = 120) String name,
    @Size(min = 1, max = 120) String brand,
    DeviceState state
) {

  @AssertTrue(message = "At least one field must be provided")
  public boolean isAnyFieldPresent() {
    return name != null || brand != null || state != null;
  }

}
