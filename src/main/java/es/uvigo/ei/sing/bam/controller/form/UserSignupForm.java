package es.uvigo.ei.sing.bam.controller.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserSignupForm {
    @Size(min = 3, max = 45, message = "Range between 5 and 45 characters")
    private String username;
    @Size(min = 6, max = 255, message = "Range between 6 and 255 characters")
    private String password;
    @Size(min = 6, max = 255, message = "Range between 6 and 255 characters")
    private String confirmPassword;
    @Email
    @Size(min = 10, max = 60, message = "Range between 10 and 60 characters")
    private String email;
    @Email
    @Size(min = 10, max = 60, message = "Range between 10 and 60 characters")
    private String confirmEmail;
    @Size(min = 1, max = 80, message = "Range between 1 and 80 characters")
    private String firstname;
    @Size(min = 1, max = 100, message = "Range between 1 and 100 characters")
    private String surname;
}
