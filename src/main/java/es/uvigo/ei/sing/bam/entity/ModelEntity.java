package es.uvigo.ei.sing.bam.entity;

import es.uvigo.ei.sing.bam.util.HashMapConverter;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "model", schema = "bam_web_db", indexes = {@Index(columnList = ("hash"),
        name = "model_hash_unique", unique = true)})
@Accessors(chain = true)
public class ModelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "model", nullable = false, columnDefinition = "JSON")
    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> model;
    @Basic
    @Column(name = "hash", nullable = false)
    private String hash;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "model_type_id", referencedColumnName = "id", nullable = false)
    private ModelInformationEntity modelInformation;

    @ManyToMany
    @JoinTable(name = "model_has_agent",
            joinColumns = @JoinColumn(name = "model_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "agent_id", referencedColumnName = "id", nullable = false))
    private Set<AgentEntity> agents = new HashSet<>();
    @ManyToMany
    @JoinTable(name = "model_has_event",
            joinColumns = @JoinColumn(name = "model_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id", nullable = false))
    private Set<EventEntity> events = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelEntity that = (ModelEntity) o;
        return id == that.id &&
                Objects.equals(model, that.model) &&
                Objects.equals(hash, that.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, model, hash);
    }
}
