package be.kdg.integration3.presentation;

import be.kdg.integration3.presentation.viewmodel.LoginViewModel;
import jakarta.servlet.http.HttpSession;
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
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @GetMapping
    public String getLoginView(Model model) {
        model.addAttribute("loginviewmodel", new LoginViewModel());
        logger.info("Request for login view!");
        return "/login";
    }

    // get rid of jdbcTemplate and use Connection, use try with resources
    @PostMapping
    public String logIn(@Valid @ModelAttribute("loginviewmodel") LoginViewModel loginViewModel, BindingResult errors, Model model, HttpSession httpSession) {
        logger.info(String.format("Processing email: %s and password: %s", loginViewModel.getEmail(), loginViewModel.getPassword()));
        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(error -> logger.error(error.toString()));
            return "/login";
        }
        String query = "SELECT COUNT(*) FROM user_account WHERE email = ? AND passwd = ?";
        Optional<Integer> count = Optional.ofNullable(jdbcTemplate.queryForObject(query, Integer.class, loginViewModel.getEmail(), loginViewModel.getPassword()));

        if (count.orElse(0) == 1) {
            logger.info("login successful");
            httpSession.setAttribute("loggedIn", true);
            httpSession.setAttribute("userEmail", loginViewModel.getEmail());

            // later direct to another page
            return "redirect:/dashboard";
        } else {
            logger.info("login unsuccessful");
            model.addAttribute("loginError", "User not found.");
            return "/login";
        }
    }
}
