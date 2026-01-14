package org.assessment.devicemanagement.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.assessment.devicemanagement.model.Device;
import org.assessment.devicemanagement.model.DeviceState;
import org.assessment.devicemanagement.service.DeviceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DeviceController.class)
class DeviceControllerTest {

  @Autowired private MockMvc mvc;

  @MockitoBean
  private DeviceService service;

  @Test
  void create_returns201_andBody() throws Exception {
    UUID id = UUID.randomUUID();

    Device d = new Device();
    d.setName("iPhone");
    d.setBrand("Apple");
    d.setState(DeviceState.AVAILABLE);

    // Because id + createdAt have no setters, set them via reflection for test realism
    ReflectionTestUtils.setField(d, "id", id);
    ReflectionTestUtils.setField(d, "createdAt", Instant.parse("2025-01-01T00:00:00Z"));

    when(service.create(any())).thenReturn(d);

    mvc.perform(post("/v1/devices")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"name":"iPhone","brand":"Apple","state":"AVAILABLE"}
                """))
        .andExpect(status().isCreated())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(id.toString()))
        .andExpect(jsonPath("$.name").value("iPhone"))
        .andExpect(jsonPath("$.brand").value("Apple"))
        .andExpect(jsonPath("$.state").value("AVAILABLE"))
        .andExpect(jsonPath("$.createdAt").exists()); // or $.createdAt depending on your DTO field name
  }

  @Test
  void getById_returns200() throws Exception {
    UUID id = UUID.randomUUID();

    Device d = new Device();
    d.setName("Pixel");
    d.setBrand("Google");
    d.setState(DeviceState.INACTIVE);
    ReflectionTestUtils.setField(d, "id", id);
    ReflectionTestUtils.setField(d, "createdAt", Instant.parse("2025-01-01T00:00:00Z"));

    when(service.get(id)).thenReturn(d);

    mvc.perform(get("/v1/devices/{id}", id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id.toString()))
        .andExpect(jsonPath("$.name").value("Pixel"))
        .andExpect(jsonPath("$.brand").value("Google"))
        .andExpect(jsonPath("$.state").value("INACTIVE"));
  }

  @Test
  void getAll_withoutFilters_callsServiceWithNulls() throws Exception {
    when(service.getDevices(null, null)).thenReturn(List.of());

    mvc.perform(get("/v1/devices"))
        .andExpect(status().isOk());

    verify(service).getDevices(null, null);
  }

  @Test
  void getAll_withBrandAndState_callsServiceWithFilters() throws Exception {
    when(service.getDevices("Apple", DeviceState.AVAILABLE)).thenReturn(List.of());

    mvc.perform(get("/v1/devices")
            .param("brand", "Apple")
            .param("state", "AVAILABLE"))
        .andExpect(status().isOk());

    verify(service).getDevices("Apple", DeviceState.AVAILABLE);
  }

  @Test
  void update_returns200() throws Exception {
    UUID id = UUID.randomUUID();

    Device d = new Device();
    d.setName("Updated");
    d.setBrand("Apple");
    d.setState(DeviceState.AVAILABLE);
    ReflectionTestUtils.setField(d, "id", id);
    ReflectionTestUtils.setField(d, "createdAt", Instant.parse("2025-01-01T00:00:00Z"));

    when(service.update(eq(id), any())).thenReturn(d);

    mvc.perform(put("/v1/devices/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"name":"Updated","brand":"Apple","state":"AVAILABLE"}
                """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Updated"));
  }

  @Test
  void patch_returns200() throws Exception {
    UUID id = UUID.randomUUID();

    Device d = new Device();
    d.setName("Name");
    d.setBrand("NewBrand");
    d.setState(DeviceState.AVAILABLE);
    ReflectionTestUtils.setField(d, "id", id);
    ReflectionTestUtils.setField(d, "createdAt", Instant.parse("2025-01-01T00:00:00Z"));

    when(service.patch(eq(id), any())).thenReturn(d);

    mvc.perform(patch("/v1/devices/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"brand":"NewBrand"}
                """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.brand").value("NewBrand"));
  }

  @Test
  void delete_returns204_andCallsService() throws Exception {
    UUID id = UUID.randomUUID();

    doNothing().when(service).delete(id);

    mvc.perform(delete("/v1/devices/{id}", id))
        .andExpect(status().isNoContent());

    verify(service).delete(id);
  }
}
