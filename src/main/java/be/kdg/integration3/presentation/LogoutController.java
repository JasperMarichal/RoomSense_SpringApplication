package be.kdg.integration3.presentation;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/log-out")
public class LogoutController {
    private final Logger logger = LoggerFactory.getLogger(LogoutController.class);

    @GetMapping
    public String getLogoutView(Model model, HttpSession session) {
        session.removeAttribute("userEmail");
        session.setAttribute("loggedIn", false);
        logger.info("Request for logout view!");
        return "redirect:/login";
    }


}
