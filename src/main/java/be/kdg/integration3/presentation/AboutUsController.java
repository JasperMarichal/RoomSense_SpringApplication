package be.kdg.integration3.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/about-us")
public class AboutUsController {
    private final Logger logger = LoggerFactory.getLogger(AboutUsController.class);

    @GetMapping
    public String getAboutUsView(Model model) {
        logger.info("Request for about us view!");
        return "about-us";
    }
}
