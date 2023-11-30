package be.kdg.integration3.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping("/")
    public String indexMapping(){
        return "redirect:/login";
    }
}
