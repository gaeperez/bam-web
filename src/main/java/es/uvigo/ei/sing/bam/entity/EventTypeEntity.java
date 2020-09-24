package es.uvigo.ei.sing.bam.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "event_type", schema = "bam_web_db", indexes = {@Index(columnList = ("hash"),
        name = "event_type_hash_unique", unique = true)})
@Accessors(chain = true)
public class EventTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "type", nullable = false, length = 45)
    private String type;
    @Basic
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;
    @Basic
    @Column(name = "custom_information_schema", nullable = false, columnDefinition = "JSON")
    private String customInformationSchema;
    @Basic
    @Column(name = "hash", nullable = false)
    private String hash;

    // Remove the associated events if the type is eliminated in DB
    @OneToMany(mappedBy = "type", cascade = CascadeType.REMOVE)
    private Set<EventEntity> events = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventTypeEntity that = (EventTypeEntity) o;
        return id == that.id &&
                Objects.equals(type, that.type) &&
                Objects.equals(description, that.description) &&
                Objects.equals(customInformationSchema, that.customInformationSchema) &&
                Objects.equals(hash, that.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, description, customInformationSchema, hash);
    }
}

