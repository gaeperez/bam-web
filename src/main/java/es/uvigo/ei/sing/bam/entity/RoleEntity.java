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
@Table(name = "role", schema = "bam_web_db", indexes = {@Index(columnList = ("role"),
        name = "role_unique_unique", unique = true)})
@Accessors(chain = true)
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "role", nullable = false, length = 10)
    private String role;

    // Remove the users if the role is eliminated
    @OneToMany(mappedBy = "role", orphanRemoval = true)
    private Set<UserEntity> users = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleEntity that = (RoleEntity) o;
        return id == that.id &&
                Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, role);
    }
}
