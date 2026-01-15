package org.assessment.devicemanagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.assessment.devicemanagement.model.Device;
import org.assessment.devicemanagement.model.DeviceState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
    "spring.flyway.enabled=true",
    "spring.jpa.hibernate.ddl-auto=validate"
})
class DeviceRepositoryTest {

  @Container
  @ServiceConnection
  static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

  @Autowired
  DeviceRepository repo;

  @Test
  void findByBrandIgnoreCase_returnsMatching() {
    repo.saveAll(List.of(
        device("iPhone", "Apple", DeviceState.AVAILABLE),
        device("Pixel", "Google", DeviceState.INACTIVE),
        device("MacBook", "APPLE", DeviceState.AVAILABLE)
    ));

    var result = repo.findByBrandIgnoreCase("apple");

    assertThat(result).extracting(Device::getName)
        .containsExactlyInAnyOrder("iPhone", "MacBook");
  }

  @Test
  void findByState_returnsMatching() {
    repo.saveAll(List.of(
        device("A", "Apple", DeviceState.AVAILABLE),
        device("B", "Apple", DeviceState.IN_USE),
        device("C", "Samsung", DeviceState.IN_USE)
    ));

    var result = repo.findByState(DeviceState.IN_USE);

    assertThat(result).hasSize(2);
    assertThat(result).extracting(Device::getName)
        .containsExactlyInAnyOrder("B", "C");
  }

  @Test
  void findByBrandIgnoreCaseAndState_returnsIntersection() {
    repo.saveAll(List.of(
        device("A", "Apple", DeviceState.AVAILABLE),
        device("B", "Apple", DeviceState.IN_USE),
        device("C", "Samsung", DeviceState.AVAILABLE)
    ));

    var result = repo.findByBrandIgnoreCaseAndState("apple", DeviceState.AVAILABLE);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getName()).isEqualTo("A");
  }

  private static Device device(String name, String brand, DeviceState state) {
    Device d = new Device();
    d.setName(name);
    d.setBrand(brand);
    d.setState(state);
    return d;
  }
}
