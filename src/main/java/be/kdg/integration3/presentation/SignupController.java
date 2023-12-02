package be.kdg.integration3.presentation;

import be.kdg.integration3.domain.UserAccount;
import be.kdg.integration3.presentation.viewmodel.SignupViewModel;
import be.kdg.integration3.service.SignupService;
import be.kdg.integration3.util.exception.DatabaseException;
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

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/sign-up")
public class SignupController {
    private final Logger logger = LoggerFactory.getLogger(SignupController.class);
    private SignupService service;

    public SignupController(SignupService signupService) {
        this.service = signupService;
    }

    @GetMapping
    public String getSignupView(Model model) {
        model.addAttribute("signupviewmodel", new SignupViewModel());
        List<UserAccount.UseCaseType> useCases = Arrays.stream(UserAccount.UseCaseType.values()).toList();
        model.addAttribute("useCases", useCases);
        logger.debug("Request for sign up view");
        return "/sign-up";
    }

    @PostMapping
    public String signUp(@Valid @ModelAttribute("signupviewmodel") SignupViewModel signupViewModel, BindingResult errors, Model model) {
        logger.debug("Processing signup...");
        try {
            if (errors.hasErrors()) {
                errors.getAllErrors().forEach(error -> logger.error(error.toString()));
                List<UserAccount.UseCaseType> useCases = Arrays.stream(UserAccount.UseCaseType.values()).toList();
                model.addAttribute("useCases", useCases);
                return "/sign-up";
            } else if (service.isEmailTaken(signupViewModel.getEmail())) {
                model.addAttribute("userTaken", "");
                List<UserAccount.UseCaseType> useCases = Arrays.stream(UserAccount.UseCaseType.values()).toList();
                model.addAttribute("useCases", useCases);
                return "/sign-up";
            } else {
                service.addUserAccount(signupViewModel.getEmail(),
                        signupViewModel.getUsername(),
                        signupViewModel.getPassword(),
                        signupViewModel.getUseCase());
                return "redirect:/login";
            }
        } catch (DatabaseException e){
            logger.debug("Could not connect to the database");
            return "errorPage";
        }
    }
}


