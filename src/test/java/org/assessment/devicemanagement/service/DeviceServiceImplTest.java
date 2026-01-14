package org.assessment.devicemanagement.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.assessment.devicemanagement.dto.PatchDeviceRequest;
import org.assessment.devicemanagement.dto.UpdateDeviceRequest;
import org.assessment.devicemanagement.exception.DomainValidationException;
import org.assessment.devicemanagement.exception.NotFoundException;
import org.assessment.devicemanagement.model.Device;
import org.assessment.devicemanagement.model.DeviceState;
import org.assessment.devicemanagement.repository.DeviceRepository;
import org.junit.jupiter.api.Test;

public class DeviceServiceImplTest {

  private final DeviceRepository repo = mock(DeviceRepository.class);
  private final DeviceServiceImpl service = new DeviceServiceImpl(repo);

  @Test
  void get_whenNotFound_throwsNotFound() {
    UUID id = UUID.randomUUID();
    when(repo.findById(id)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> service.get(id));
  }

  @Test
  void getDevices_withBrandAndState_callsCombinedRepoMethod() {
    String brand = "Apple";
    DeviceState state = DeviceState.AVAILABLE;

    when(repo.findByBrandIgnoreCaseAndState(brand, state)).thenReturn(List.of());

    service.getDevices(brand, state);

    verify(repo).findByBrandIgnoreCaseAndState(brand, state);
    verify(repo, never()).findAll();
  }

  @Test
  void update_whenInUse_andNameChanges_throwsDomainValidation() {
    UUID id = UUID.randomUUID();
    Device existing = new Device();
    existing.setName("Old");
    existing.setBrand("Brand");
    existing.setState(DeviceState.IN_USE);

    when(repo.findById(id)).thenReturn(Optional.of(existing));

    UpdateDeviceRequest req = new UpdateDeviceRequest("New", "Brand", DeviceState.IN_USE);

    assertThrows(DomainValidationException.class, () -> service.update(id, req));
    verify(repo, never()).save(any());
  }

  @Test
  void update_whenInUse_andOnlyStateChanges_allowsUpdate() {
    UUID id = UUID.randomUUID();
    Device existing = new Device();
    existing.setName("Name");
    existing.setBrand("Brand");
    existing.setState(DeviceState.IN_USE);

    when(repo.findById(id)).thenReturn(Optional.of(existing));
    when(repo.save(any(Device.class))).thenAnswer(inv -> inv.getArgument(0));

    UpdateDeviceRequest req = new UpdateDeviceRequest("Name", "Brand", DeviceState.INACTIVE);

    Device updated = service.update(id, req);

    assertEquals(DeviceState.INACTIVE, updated.getState());
    assertEquals("Name", updated.getName());
    assertEquals("Brand", updated.getBrand());
    verify(repo).save(existing);
  }

  @Test
  void patch_whenInUse_andBrandWouldChange_throwsDomainValidation() {
    UUID id = UUID.randomUUID();
    Device existing = new Device();
    existing.setName("Name");
    existing.setBrand("Brand");
    existing.setState(DeviceState.IN_USE);

    when(repo.findById(id)).thenReturn(Optional.of(existing));

    PatchDeviceRequest req = new PatchDeviceRequest(null, "NewBrand", null);

    assertThrows(DomainValidationException.class, () -> service.patch(id, req));
    verify(repo, never()).save(any());
  }

  @Test
  void delete_whenInUse_throwsDomainValidation() {
    UUID id = UUID.randomUUID();
    Device existing = new Device();
    existing.setState(DeviceState.IN_USE);

    when(repo.findById(id)).thenReturn(Optional.of(existing));

    assertThrows(DomainValidationException.class, () -> service.delete(id));
    verify(repo, never()).delete(any());
  }

  @Test
  void delete_whenNotInUse_deletes() {
    UUID id = UUID.randomUUID();
    Device existing = new Device();
    existing.setState(DeviceState.AVAILABLE);

    when(repo.findById(id)).thenReturn(Optional.of(existing));

    assertDoesNotThrow(() -> service.delete(id));
    verify(repo).delete(existing);
  }
}
