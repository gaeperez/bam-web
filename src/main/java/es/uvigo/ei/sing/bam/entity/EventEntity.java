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
@Table(name = "event", schema = "bam_web_db")
@Accessors(chain = true)
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "custom_information", nullable = false, columnDefinition = "JSON")
    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> customInformation;

    @ManyToOne
    @JoinColumn(name = "event_type_id", referencedColumnName = "id", nullable = false)
    private EventTypeEntity type;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;

    @ManyToMany
    @JoinTable(name = "model_has_event",
            joinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "model_id", referencedColumnName = "id", nullable = false))
    private Set<ModelEntity> models = new HashSet<>();

    // Create the intermediate tables between events and agents if the agents are set to this entity
    @ManyToMany
    @JoinTable(name = "event_has_source_agent",
            joinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "agent_id", referencedColumnName = "id", nullable = false))
    private Set<AgentEntity> sourceAgents = new HashSet<>();
    @ManyToMany
    @JoinTable(name = "event_has_target_agent",
            joinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "agent_id", referencedColumnName = "id", nullable = false))
    private Set<AgentEntity> targetAgents = new HashSet<>();
    @ManyToMany
    @JoinTable(name = "event_has_result_agent",
            joinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "agent_id", referencedColumnName = "id", nullable = false))
    private Set<AgentEntity> resultAgents = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventEntity that = (EventEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
