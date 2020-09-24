package es.uvigo.ei.sing.bam.controller.error;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

// Handles exceptions not thrown by the controllers (e.g. Spring security)
@Controller
@RequestMapping("/error")
public class CustomErrorController implements ErrorController {

    @GetMapping
    public String error(HttpServletRequest request, HttpServletResponse response, Model model) {
        // Add variables to the view
        int status = response.getStatus();
        String message;

        if (status == HttpStatus.BAD_REQUEST.value())
            message = "The request is not valid!";
        else if (status == HttpStatus.UNAUTHORIZED.value())
            message = "You have no authorization to be here!";
        else if (status == HttpStatus.FORBIDDEN.value())
            message = "This resource is forbidden for you!";
        else if (status == HttpStatus.NOT_FOUND.value())
            message = "The desired resource is not found!";
        else
            message = "Unexpected error has occurred. Please, try again.";

        // Add attributes to the view
        model.addAttribute("status", status);
        model.addAttribute("message", message);
        model.addAttribute("user", request.getRemoteUser());
        model.addAttribute("date", LocalDateTime.now());

        return "error/error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
