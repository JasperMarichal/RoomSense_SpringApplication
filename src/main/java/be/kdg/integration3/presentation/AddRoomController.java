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
        return "/add-room";
    }

    @PostMapping
    public String addRoom(@Valid @ModelAttribute("addroomviewmodel")AddRoomViewModel addRoomViewModel, Model model, BindingResult errors) {
        logger.info(String.format("Processing room name: %s, width: %f, length: %f, height: %f",
                addRoomViewModel.getRoomName(), addRoomViewModel.getWidth(), addRoomViewModel.getLength(), addRoomViewModel.getHeight()));



        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(error -> logger.error(error.toString()));
            return "/add-room";
        }

        System.out.println(addRoomViewModel);

        dataRepository.addRoom(new Room(addRoomViewModel.getRoomName(), addRoomViewModel.getWidth(), addRoomViewModel.getLength(), addRoomViewModel.getHeight()));

        return "redirect:/dashboard";
    }

}
