package be.kdg.integration3.presentation;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping("/")
    public String indexMapping(HttpSession session){
        if (session.getAttribute("userEmail") == null) return "redirect:/about-us";
        else return "redirect:/dashboard";
    }
}
