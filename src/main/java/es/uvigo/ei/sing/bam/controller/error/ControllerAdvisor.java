package es.uvigo.ei.sing.bam.controller.error;

import es.uvigo.ei.sing.bam.entity.UserEntity;
import es.uvigo.ei.sing.bam.exception.ForbiddenException;
import es.uvigo.ei.sing.bam.exception.NotFoundException;
import es.uvigo.ei.sing.bam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    private final String PATH = "error/";

    @Autowired
    private UserService userService;

    // Common model attribute for all templates
    @ModelAttribute("authUser")
    public UserEntity getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.findByUsername(auth.getName());
    }

    // Handles exception thrown by the controllers
    @ExceptionHandler(ForbiddenException.class)
    public String handleForbiddenException(ForbiddenException ex, WebRequest request, Model model) {
        // Add variables to the view
        model.addAttribute("status", HttpStatus.FORBIDDEN.value());
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("user", request.getRemoteUser());
        model.addAttribute("date", LocalDateTime.now());

        return PATH + "error";
    }

    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundException(NotFoundException ex, WebRequest request, Model model) {
        // Add variables to the view
        model.addAttribute("status", HttpStatus.NOT_FOUND.value());
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("user", request.getRemoteUser());
        model.addAttribute("date", LocalDateTime.now());

        return PATH + "error";
    }
}
