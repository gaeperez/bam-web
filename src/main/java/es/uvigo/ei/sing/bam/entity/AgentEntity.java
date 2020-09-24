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
@Table(name = "agent", schema = "bam_web_db",
        indexes = {@Index(columnList = ("hash"), name = "agent_hash_unique", unique = true),
                @Index(columnList = ("name,molecular_weight,diffusion_rate,shape_id,type_id,user_id"),
                        name = "agent_compound_unique", unique = true)})
@Accessors(chain = true)
public class AgentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Basic
    @Column(name = "molecular_weight", nullable = false)
    private float molecularWeight;
    @Basic
    @Column(name = "diffusion_rate", nullable = false)
    private double diffusionRate;
    @Basic
    @Column(name = "color", nullable = false, length = 20)
    private String color;
    @Basic
    @Column(name = "custom_parameters", nullable = false, columnDefinition = "JSON")
    private String customParameters;
    @Basic
    @Column(name = "dimension", nullable = false, columnDefinition = "JSON")
    private String dimensions;
    @Basic
    @Column(name = "hash", nullable = false)
    private String hash;

    @ManyToOne
    @JoinColumn(name = "shape_id", referencedColumnName = "id", nullable = false)
    private ShapeEntity shape;

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id", nullable = false)
    private AgentTypeEntity type;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;

    // Remove the associated layers if the agent is removed. Save the layers when saving the agent
    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LayerEntity> layers = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "model_has_agent",
            joinColumns = @JoinColumn(name = "agent_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "model_id", referencedColumnName = "id", nullable = false))
    private Set<ModelEntity> models = new HashSet<>();

    // Remove the associated events if one of the agents is eliminated in DB
    @ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinTable(name = "event_has_source_agent",
            joinColumns = @JoinColumn(name = "agent_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id", nullable = false))
    private Set<EventEntity> sourceEvents = new HashSet<>();
    @ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinTable(name = "event_has_target_agent",
            joinColumns = @JoinColumn(name = "agent_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id", nullable = false))
    private Set<EventEntity> targetEvents = new HashSet<>();
    @ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinTable(name = "event_has_result_agent",
            joinColumns = @JoinColumn(name = "agent_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id", nullable = false))
    private Set<EventEntity> resultEvents = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgentEntity that = (AgentEntity) o;
        return id == that.id &&
                Double.compare(that.molecularWeight, molecularWeight) == 0 &&
                Double.compare(that.diffusionRate, diffusionRate) == 0 &&
                Objects.equals(name, that.name) &&
                Objects.equals(color, that.color) &&
                Objects.equals(hash, that.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, molecularWeight, diffusionRate, color, hash);
    }
}
