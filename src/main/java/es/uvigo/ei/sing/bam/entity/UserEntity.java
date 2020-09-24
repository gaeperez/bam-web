package es.uvigo.ei.sing.bam.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "user", schema = "bam_web_db", indexes = {@Index(columnList = ("username"), name = "user_username_unique", unique = true),
        @Index(columnList = ("email"), name = "user_email_unique", unique = true)})
@Accessors(chain = true)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "username", nullable = false, length = 45)
    private String username;
    @Basic
    @Column(name = "password", nullable = false)
    private String password;
    @Basic
    @Column(name = "email", nullable = false, length = 60)
    private String email;
    @Basic
    @Column(name = "firstname", nullable = false, length = 80)
    private String firstname;
    @Basic
    @Column(name = "surname", nullable = false, length = 100)
    private String surname;
    @Basic
    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @OneToMany(mappedBy = "user")
    private Set<ModelEntity> models = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<AgentEntity> agents = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<EventEntity> events = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    private RoleEntity role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return id == that.id &&
                Objects.equals(username, that.username) &&
                Objects.equals(password, that.password) &&
                Objects.equals(email, that.email) &&
                Objects.equals(firstname, that.firstname) &&
                Objects.equals(surname, that.surname) &&
                Objects.equals(created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, email, firstname, surname, created);
    }
}
