package es.uvigo.ei.sing.bam.controller;

import es.uvigo.ei.sing.bam.controller.form.UserForm;
import es.uvigo.ei.sing.bam.entity.UserEntity;
import es.uvigo.ei.sing.bam.exception.ForbiddenException;
import es.uvigo.ei.sing.bam.service.RoleService;
import es.uvigo.ei.sing.bam.service.UserService;
import es.uvigo.ei.sing.bam.util.Constants;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Log4j2
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String list(Model model) {
        // Check user role
        UserEntity authUser = getAuthenticatedUser();

        // Add values to the view depending on the user role
        if (authUser.getRole().getRole().equalsIgnoreCase(Constants.ROLE_ADMIN))
            model.addAttribute("users", userService.findAll());
        else
            model.addAttribute("users", authUser);

        return "user/list";
    }

    @GetMapping(path = {"/edit/{id}"})
    public String edit(Model model, @PathVariable("id") Integer id) throws NullPointerException {
        // Get auth user
        UserEntity authUser = getAuthenticatedUser();

        // Only valid if the user has an admin role or a regular user is editing itself
        if (canAccessResource(authUser, id)) {
            // Get the info for the current user
            UserEntity possibleEntity = userService.findById(id);

            // Fill the form with the info of the retrieved agent
            UserForm userForm = new UserForm();
            userForm.setPassword(possibleEntity.getPassword());
            userForm.setFirstname(possibleEntity.getFirstname());
            userForm.setSurname(possibleEntity.getSurname());
            userForm.setRole(possibleEntity.getRole());
            userForm.setAllRoles(roleService.findAll());

            // Pass the variables to the model
            model.addAttribute("userForm", userForm);

            return "user/edit";
        } else
            throw new ForbiddenException("The user is trying to access a forbidden resource!");
    }

    @PostMapping(path = "/edit/{id}")
    public String edit(@PathVariable("id") Integer id, @ModelAttribute("userForm") @Valid UserForm userForm,
                       BindingResult result) {
        // Check if the user edited is valid
        UserEntity authUser = getAuthenticatedUser();
        String authRole = authUser.getRole().getRole();

        // Only valid if the user has an admin role or a regular user is editing itself
        if (canAccessResource(authUser, id)) {
            // Obtain the user from DB
            UserEntity possibleEntity = userService.findById(id);
            if (possibleEntity != null) {
                // If password changed, validate its size
                String password = userForm.getPassword();
                if (!password.isEmpty()) {
                    if (password.length() >= 6 && password.length() <= 255)
                        possibleEntity.setPassword(passwordEncoder.encode(userForm.getPassword()));
                    else
                        result.rejectValue("password", null, "The password size must be between 6 and 255 characters");
                }

                // Only admin can changes the role
                if (authRole.equalsIgnoreCase(Constants.ROLE_ADMIN))
                    possibleEntity.setRole(userForm.getRole());

                // Go to the form again if there is an error
                if (result.hasErrors()) {
                    userForm.setAllRoles(roleService.findAll());
                    return "/user/edit";
                } else {
                    possibleEntity
                            .setFirstname(userForm.getFirstname())
                            .setSurname(userForm.getSurname());
                    userService.save(possibleEntity);
                }
            }

            return "redirect:/user";
        } else
            throw new ForbiddenException("The user is trying to modify a forbidden resource!");
    }

    @GetMapping(path = "/delete/{id}")
    public String delete(@PathVariable("id") Integer id) throws NullPointerException {
        userService.deleteById(id);
        return "redirect:/user";
    }

    private boolean canAccessResource(UserEntity authUser, int userId) {
        // Admin can edit everything. Regular users can only edit their agents
        if (authUser.getRole().getRole().equalsIgnoreCase(Constants.ROLE_ADMIN)
                || userId == authUser.getId())
            return true;
        else
            return false;
    }

    private UserEntity getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = userService.findByUsername(auth.getName());

        return userEntity;
    }
}
