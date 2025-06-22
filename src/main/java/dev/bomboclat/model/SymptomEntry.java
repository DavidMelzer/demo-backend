package dev.bomboclat.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Entity
public class SymptomEntry extends PanacheEntityBase {
    @Id
    @GeneratedValue
    public UUID entryId;

    @ManyToOne(optional = false)
    public User user;

    public OffsetDateTime timestamp;

    public int painLevel;

    public int stiffnessLevel;

    public String notes;

    public LocalDateTime deletedAt;

    @PrePersist
    public void prePersist() {
        if (timestamp == null) {
            timestamp = OffsetDateTime.now();
        }
    }
}
