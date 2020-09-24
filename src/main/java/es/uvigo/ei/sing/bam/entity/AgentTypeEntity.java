package es.uvigo.ei.sing.bam.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "agent_type", schema = "bam_web_db", indexes = {@Index(columnList = ("hash"),
        name = "agent_type_hash_unique", unique = true)})
@Accessors(chain = true)
public class AgentTypeEntity {
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
    @Column(name = "custom_parameters_schema", nullable = false, columnDefinition = "JSON")
    private String customParametersSchema;
    @Basic
    @Column(name = "hash", nullable = false)
    private String hash;

    // Remove the associated agents if the type is eliminated in DB
    @JsonIgnore
    @OneToMany(mappedBy = "type", cascade = CascadeType.REMOVE)
    private Set<AgentEntity> agents = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgentTypeEntity that = (AgentTypeEntity) o;
        return id == that.id &&
                Objects.equals(type, that.type) &&
                Objects.equals(description, that.description) &&
                Objects.equals(customParametersSchema, that.customParametersSchema) &&
                Objects.equals(hash, that.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, description, customParametersSchema, hash);
    }
}
