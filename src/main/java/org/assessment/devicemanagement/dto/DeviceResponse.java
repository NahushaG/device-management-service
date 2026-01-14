package org.assessment.devicemanagement.dto;

import java.time.Instant;
import org.assessment.devicemanagement.model.DeviceState;

public record DeviceResponse(String id,
                             String name,
                             String brand,
                             DeviceState state,
                             Instant createdAt) {

}
