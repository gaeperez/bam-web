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
@Table(name = "shape", schema = "bam_web_db", indexes = {@Index(columnList = ("hash"),
        name = "shape_hash_unique", unique = true)})
@Accessors(chain = true)
public class ShapeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "form", nullable = false, length = 45)
    private String form;
    @Basic
    @Column(name = "dimension_schema", nullable = false, columnDefinition = "JSON")
    private String dimensionSchema;
    @Basic
    @Column(name = "hash", nullable = false)
    private String hash;

    // Remove the associated agents if the shape is eliminated in DB
    @JsonIgnore
    @OneToMany(mappedBy = "shape", cascade = CascadeType.REMOVE)
    private Set<AgentEntity> agents = new HashSet<>();

    // Layers are removed when the agent is removed
    @JsonIgnore
    @OneToMany(mappedBy = "shape")
    private Set<LayerEntity> layers = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShapeEntity that = (ShapeEntity) o;
        return id == that.id &&
                Objects.equals(form, that.form) &&
                Objects.equals(dimensionSchema, that.dimensionSchema) &&
                Objects.equals(hash, that.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, form, dimensionSchema, hash);
    }
}
