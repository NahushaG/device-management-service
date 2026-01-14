package org.assessment.devicemanagement.service;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.assessment.devicemanagement.dto.CreateDeviceRequest;
import org.assessment.devicemanagement.dto.PatchDeviceRequest;
import org.assessment.devicemanagement.dto.UpdateDeviceRequest;
import org.assessment.devicemanagement.exception.DomainValidationException;
import org.assessment.devicemanagement.exception.NotFoundException;
import org.assessment.devicemanagement.model.Device;
import org.assessment.devicemanagement.model.DeviceState;
import org.assessment.devicemanagement.repository.DeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeviceServiceImpl implements DeviceService {

  private final DeviceRepository repo;

  @Override
  public Device create(CreateDeviceRequest req) {
    Device d = new Device();
    d.setName(req.name());
    d.setBrand(req.brand());
    d.setState(req.state());
    // creationTime is set in @PrePersist
    return repo.save(d);
  }

  @Override
  @Transactional(readOnly = true)
  public Device get(UUID id) {
    return repo.findById(id)
        .orElseThrow(() -> new NotFoundException("Device not found: " + id));
  }

  @Override
  @Transactional(readOnly = true)
  public List<Device> getDevices(String brand, DeviceState state) {
    if (brand != null && state != null) {
      return repo.findByBrandIgnoreCaseAndState(brand, state);
    }
    if (brand != null) {
      return repo.findByBrandIgnoreCase(brand);
    }
    if (state != null) {
      return repo.findByState(state);
    }
    return repo.findAll();
  }

  @Override
  public Device update(UUID id, UpdateDeviceRequest req) {
    Device existing = get(id);

    // Validation ensures name/brand/state are non-null for PUT
    assertMutableIdentityIfInUse(existing, req.name(), req.brand());

    existing.setName(req.name());
    existing.setBrand(req.brand());
    existing.setState(req.state());

    // creationTime is immutable and not in DTO
    return repo.save(existing);
  }

  @Override
  public Device patch(UUID id, PatchDeviceRequest req) {
    Device existing = get(id);

    // Only check if a change is requested (PATCH allows nulls)
    assertMutableIdentityIfInUse(existing, req.name(), req.brand());

    if (req.name() != null) {
      existing.setName(req.name());
    }
    if (req.brand() != null) {
      existing.setBrand(req.brand());
    }
    if (req.state() != null) {
      existing.setState(req.state());
    }

    return repo.save(existing);
  }

  @Override
  public void delete(UUID id) {
    Device existing = get(id);
    assertDeletable(existing);
    repo.delete(existing);
  }

  private void assertDeletable(Device device) {
    if (device.getState() == DeviceState.IN_USE) {
      throw new DomainValidationException("IN_USE devices cannot be deleted.");
    }
  }

  /**
   * Rule: Name and brand cannot be updated if device is IN_USE. For PUT: newName/newBrand are
   * always non-null (validated). For PATCH: newName/newBrand can be null; null means "no change
   * requested".
   */
  private void assertMutableIdentityIfInUse(Device existing, String newName, String newBrand) {
    if (existing.getState() != DeviceState.IN_USE) {
      return;
    }

    boolean nameChangeRequested = newName != null && !newName.equals(existing.getName());
    boolean brandChangeRequested = newBrand != null && !newBrand.equals(existing.getBrand());

    if (nameChangeRequested || brandChangeRequested) {
      throw new DomainValidationException("Name/brand cannot be updated when device is IN_USE.");
    }
  }
}
