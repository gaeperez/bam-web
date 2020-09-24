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
@Table(name = "model_information", schema = "bam_web_db", indexes = {@Index(columnList = ("type"),
        name = "model_information_type_unique", unique = true)})
@Accessors(chain = true)
public class ModelInformationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "type", nullable = false, length = 45)
    private String type;

    @OneToMany(mappedBy = "modelInformation")
    private Set<ModelEntity> models = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelInformationEntity that = (ModelInformationEntity) o;
        return id == that.id &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }
}
