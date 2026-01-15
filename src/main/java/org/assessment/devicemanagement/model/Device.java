package org.assessment.devicemanagement.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "devices")
@Getter
@NoArgsConstructor
public class Device {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(nullable = false, updatable = false)
  private UUID id;

  // Domain update methods (keeps rules closer to entity)
  @Setter
  @Column(nullable = false)
  private String name;

  @Setter
  @Column(nullable = false)
  private String brand;

  @Setter
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private DeviceState state;

  @Setter(AccessLevel.NONE)
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @PrePersist
  void prePersist() {
    if (createdAt == null) createdAt = Instant.now();
  }

}
