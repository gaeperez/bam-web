package es.uvigo.ei.sing.bam.controller;

import es.uvigo.ei.sing.bam.controller.form.UserSignupForm;
import es.uvigo.ei.sing.bam.entity.UserEntity;
import es.uvigo.ei.sing.bam.service.RoleService;
import es.uvigo.ei.sing.bam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
public class AppController {

    // TODO: 23/04/2020 Download bootstrap and link to them in login views

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping({"/", "index"})
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping(value = {"/logout"})
    public String logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:login";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("signupForm", new UserSignupForm());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute("signupForm") @Valid UserSignupForm signupForm, BindingResult result) {
        // Check if the username already exists in the database
        UserEntity user = userService.findByUsername(signupForm.getUsername());
        if (user != null)
            result.rejectValue("username", null, "There is already account registered with that username");
        else {
            // Check if user already exists by email
            user = userService.findByEmail(signupForm.getEmail());
            if (user != null)
                result.rejectValue("email", null, "There is already account registered with that email");
        }

        // Check if the passwords are the same
        if (!signupForm.getPassword().equals(signupForm.getConfirmPassword()))
            result.rejectValue("confirmPassword", null, "Passwords are not the same");

        // Check if the emails are the same
        if (!signupForm.getEmail().equals(signupForm.getConfirmEmail()))
            result.rejectValue("confirmEmail", null, "Emails are not the same");

        // Go to the form again if there is another error
        if (result.hasErrors())
            return "signup";
        else {
            // Create the DTO with the information of the form
            UserEntity userEntity = new UserEntity()
                    .setUsername(signupForm.getUsername())
                    .setPassword(passwordEncoder.encode(signupForm.getPassword()))
                    .setEmail(signupForm.getEmail())
                    .setFirstname(signupForm.getFirstname())
                    .setSurname(signupForm.getSurname())
                    .setCreated(LocalDateTime.now())
                    .setRole(roleService.findByrole("USER"));

            // If not save user and success
            userService.save(userEntity);
            return "redirect:/login";
        }
    }
}
