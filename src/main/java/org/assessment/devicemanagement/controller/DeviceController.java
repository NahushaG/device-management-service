package org.assessment.devicemanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.assessment.devicemanagement.dto.CreateDeviceRequest;
import org.assessment.devicemanagement.dto.DeviceResponse;
import org.assessment.devicemanagement.dto.PatchDeviceRequest;
import org.assessment.devicemanagement.dto.UpdateDeviceRequest;
import org.assessment.devicemanagement.model.DeviceState;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@Tag(name = "Devices API", description = "Endpoints for managing device resources")
@RestController
@RequestMapping("/v1/devices")
public class DeviceController {

  //
  @Operation(summary = "Create a new device")
  @PostMapping
  public ResponseEntity<DeviceResponse> create(@RequestBody CreateDeviceRequest request) {
    //  Dummy response for contractual phase to be repace by actual service call
    DeviceResponse response = new DeviceResponse(
        UUID.randomUUID().toString(),
        request.name(),
        request.brand(),
        request.state(),
        Instant.now()
    );
    return ResponseEntity.status(CREATED).body(response);
  }

  @Operation(summary = "Fetch a single device by id")
  @GetMapping("/{id}")
  public ResponseEntity<DeviceResponse> getById(@PathVariable String id) {
    // Dummy response for contractual phase to be repace by actual service call
    DeviceResponse response = new DeviceResponse(
        id,
        "sample-name",
        "sample-brand",
        DeviceState.AVAILABLE,
        Instant.now()
    );
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Fetch all devices (optionally filter by brand and/or state)")
  @GetMapping
  public ResponseEntity<List<DeviceResponse>> getAll(
      @RequestParam(required = false) String brand,
      @RequestParam(required = false) DeviceState state
  ) {
    //Dummy response for contractual phase to be repace by actual service call
    return ResponseEntity.ok(List.of());
  }

  @PutMapping("/{id}")
  public ResponseEntity<DeviceResponse> update(@PathVariable String id,
      @RequestBody UpdateDeviceRequest request) {
    //Dummy response for contractual phase to be repace by actual service call
    DeviceResponse response = new DeviceResponse(
        id,
        request.name(),
        request.brand(),
        request.state(),
        Instant.now() // will be persisted createdAt later; contract includes it
    );
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Partially update an existing device")
  @PatchMapping("/{id}")
  public ResponseEntity<DeviceResponse> patch(@PathVariable String id,
      @RequestBody PatchDeviceRequest request) {
    //Dummy response for contractual phase to be repace by actual service call
    DeviceResponse response = new DeviceResponse(
        id,
        request.name() != null ? request.name() : "sample-name",
        request.brand() != null ? request.brand() : "sample-brand",
        request.state() != null ? request.state() : DeviceState.AVAILABLE,
        Instant.now()
    );
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Delete a single device by id")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    ///Dummy response for contractual phase to be repace by actual service call
    return ResponseEntity.noContent().build();
  }

}
