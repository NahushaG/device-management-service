package org.assessment.devicemanagement.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.assessment.devicemanagement.dto.CreateDeviceRequest;
import org.assessment.devicemanagement.dto.DeviceResponse;
import org.assessment.devicemanagement.model.DeviceState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestPropertySource(properties = {
    "spring.flyway.enabled=true",
    "spring.jpa.hibernate.ddl-auto=validate"
})
class DeviceApiIntegrationTest {

  @Container
  @ServiceConnection
  static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

  @Autowired
  TestRestTemplate rest;

  @Test
  void create_then_getById_works() {
    CreateDeviceRequest req = new CreateDeviceRequest("iPhone", "Apple", DeviceState.AVAILABLE);

    ResponseEntity<DeviceResponse> created =
        rest.postForEntity("/v1/devices", req, DeviceResponse.class);

    assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(created.getBody()).isNotNull();

    UUID id = UUID.fromString(created.getBody().id());

    ResponseEntity<DeviceResponse> fetched =
        rest.getForEntity("/v1/devices/" + id, DeviceResponse.class);

    assertThat(fetched.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(fetched.getBody()).isNotNull();
    assertThat(fetched.getBody().name()).isEqualTo("iPhone");
  }
}
