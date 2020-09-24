package es.uvigo.ei.sing.bam.controller.form;

import es.uvigo.ei.sing.bam.entity.RoleEntity;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
public class UserForm {
    private String password;
    @Size(min = 1, max = 80, message = "Range between 1 and 80 characters")
    private String firstname;
    @Size(min = 1, max = 100, message = "Range between 1 and 100 characters")
    private String surname;
    private RoleEntity role;

    private Set<RoleEntity> allRoles;
}
