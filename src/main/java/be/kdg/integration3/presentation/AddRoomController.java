package be.kdg.integration3.presentation;

import be.kdg.integration3.domain.Room;
import be.kdg.integration3.presentation.viewmodel.AddRoomViewModel;
import be.kdg.integration3.repository.DataRepository;
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
    private DataRepository dataRepository;

    public AddRoomController(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @GetMapping
    public String getAddRoomView(Model model) {
        model.addAttribute("addroomviewmodel", new AddRoomViewModel());
        logger.info("Request for add room view!");
        return "add-room";
    }

    @PostMapping
    public String addRoom(@Valid @ModelAttribute("addroomviewmodel") AddRoomViewModel addRoomViewModel, BindingResult errors, Model model) {
        logger.info(String.format("Processing room name: %s, width: %s, length: %s, height: %s",
                addRoomViewModel.getRoomName(), addRoomViewModel.getWidth(), addRoomViewModel.getLength(), addRoomViewModel.getHeight()));

        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(error -> logger.error(error.toString()));
            model.addAttribute("addRoomError", "Incorrect values.");
            return "/add-room";
        }

        System.out.println(addRoomViewModel);

        dataRepository.addRoom(new Room(addRoomViewModel.getRoomName(), Double.parseDouble(addRoomViewModel.getWidth()), Double.parseDouble(addRoomViewModel.getLength()), Double.parseDouble(addRoomViewModel.getHeight())));

        return "redirect:/dashboard";
    }
}
