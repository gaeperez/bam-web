package es.uvigo.ei.sing.bam.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "layer", schema = "bam_web_db", indexes = {@Index(columnList = ("hash"),
        name = "layer_hash_unique", unique = true)})
@Accessors(chain = true)
public class LayerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Basic
    @Column(name = "color", nullable = false, length = 20)
    private String color;
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
    @JoinColumn(name = "agent_id", referencedColumnName = "id", nullable = false)
    private AgentEntity agent;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LayerEntity that = (LayerEntity) o;
        return id == that.id &&
                Objects.equals(name, that.name) &&
                Objects.equals(color, that.color) &&
                Objects.equals(hash, that.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, hash);
    }
}
