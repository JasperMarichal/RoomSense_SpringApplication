package be.kdg.integration3.presentation;

import be.kdg.integration3.presentation.viewmodel.LoginViewModel;
import be.kdg.integration3.service.SignupService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {
    private final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private SignupService service;

    public LoginController(SignupService service) {
        this.service = service;
    }

    @GetMapping
    public String getLoginView(Model model) {
        model.addAttribute("loginviewmodel", new LoginViewModel());
        logger.info("Request for login view!");
        return "/login";
    }

    @PostMapping
    public String logIn(@Valid @ModelAttribute("loginviewmodel") LoginViewModel loginViewModel, BindingResult errors, Model model, HttpSession httpSession) {
        logger.info(String.format("Processing email: %s and password: %s", loginViewModel.getEmail(), loginViewModel.getPassword()));
        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(error -> logger.error(error.toString()));
            return "/login";
        }

        if (service.correctUserDetails(loginViewModel.getEmail(), loginViewModel.getPassword())) {
            logger.info("login successful");
            httpSession.setAttribute("loggedIn", true);
            httpSession.setAttribute("userEmail", loginViewModel.getEmail());

            return "redirect:/dashboard";
        } else {
            logger.info("login unsuccessful");
            model.addAttribute("loginError", "User not found.");
            return "/login";
        }
    }
}
