package org.assessment.devicemanagement.controller;

import static org.assessment.devicemanagement.util.DeviceMapper.toResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.assessment.devicemanagement.dto.CreateDeviceRequest;
import org.assessment.devicemanagement.dto.DeviceResponse;
import org.assessment.devicemanagement.dto.PatchDeviceRequest;
import org.assessment.devicemanagement.dto.UpdateDeviceRequest;
import org.assessment.devicemanagement.model.DeviceState;
import org.assessment.devicemanagement.service.DeviceService;
import org.assessment.devicemanagement.util.DeviceMapper;
import org.springframework.http.HttpStatus;
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

@Tag(name = "Devices API", description = "Endpoints for managing device resources")
@RestController
@RequestMapping("/v1/devices")
@RequiredArgsConstructor
public class DeviceController {

  public final DeviceService service;

  //
  @Operation(summary = "Create a new device")
  @PostMapping
  public ResponseEntity<DeviceResponse> create(@RequestBody CreateDeviceRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(service.create(request)));
  }

  @Operation(summary = "Fetch a single device by id")
  @GetMapping("/{id}")
  public ResponseEntity<DeviceResponse> getById(@PathVariable UUID id) {
    return ResponseEntity.ok(toResponse(service.get(id)));
  }

  @Operation(summary = "Fetch all devices (optionally filter by brand and/or state)")
  @GetMapping
  public ResponseEntity<List<DeviceResponse>> getAll(
      @RequestParam(required = false) String brand,
      @RequestParam(required = false) DeviceState state
  ) {
    var result = service.getDevices(brand, state).stream().map(DeviceMapper::toResponse).toList();
    return ResponseEntity.ok(result);
  }

  @PutMapping("/{id}")
  public ResponseEntity<DeviceResponse> update(@PathVariable UUID id,
      @RequestBody UpdateDeviceRequest request) {
    return ResponseEntity.ok(toResponse(service.update(id, request)));
  }

  @Operation(summary = "Partially update an existing device")
  @PatchMapping("/{id}")
  public ResponseEntity<DeviceResponse> patch(@PathVariable UUID id,
      @RequestBody PatchDeviceRequest request) {
    return ResponseEntity.ok(toResponse(service.patch(id, request)));
  }

  @Operation(summary = "Delete a single device by id")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }

}
