package be.kdg.integration3.presentation;

import be.kdg.integration3.presentation.viewmodel.AddRoomViewModel;
import be.kdg.integration3.service.DashboardService;
import be.kdg.integration3.util.exception.DatabaseException;
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
@RequestMapping("/add-room")
public class AddRoomController {
    private final Logger logger = LoggerFactory.getLogger(AddRoomController.class);
    private DashboardService dashboardService;

    public AddRoomController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public String getAddRoomView(Model model, HttpSession session) {
        if (session.getAttribute("userEmail") == null) return "redirect:/login";
        model.addAttribute("addroomviewmodel", new AddRoomViewModel());
        logger.info("Request for add room view!");
        return "add-room";
    }

    @PostMapping
    public String addRoom(@Valid @ModelAttribute("addroomviewmodel") AddRoomViewModel addRoomViewModel, BindingResult errors, Model model, HttpSession session) {
        logger.info(String.format("Processing room name: %s, width: %s, length: %s, height: %s",
                addRoomViewModel.getRoomName(), addRoomViewModel.getWidth(), addRoomViewModel.getLength(), addRoomViewModel.getHeight()));

        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(error -> logger.error(error.toString()));
            model.addAttribute("addRoomError", "Incorrect values.");
            return "add-room";
        }

        try {
            dashboardService.addRoom(addRoomViewModel.getRoomName(), addRoomViewModel.getWidth(), addRoomViewModel.getHeight(), addRoomViewModel.getHeight(), (String) session.getAttribute("userEmail"));
        } catch (DatabaseException e){
            return "error";
        }

        return "redirect:/dashboard";
    }
}
