package be.kdg.integration3.presentation;

import be.kdg.integration3.presentation.viewmodel.SignupViewModel;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/sign-up")
public class SignupController {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final Logger logger = LoggerFactory.getLogger(SignupController.class);

    @GetMapping
    public String getSignupView(Model model) {
        model.addAttribute("signupviewmodel", new SignupViewModel());
        logger.info("Request for sign up view");
        return "/signup";
    }

    @PostMapping
    public String signUp(@Valid @ModelAttribute("signupviewmodel") SignupController signupController, BindingResult errors, Model model) {
        logger.info("Processing data...");
        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(error -> logger.error(error.toString()));
            return "/sign-up";
        }

        return "dashboard";

    }
}


