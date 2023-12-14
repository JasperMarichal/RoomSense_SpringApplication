package be.kdg.integration3.presentation;

import be.kdg.integration3.domain.Room;
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
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/rooms")
public class RoomOverviewController {
    private final Logger logger = LoggerFactory.getLogger(RoomOverviewController.class);
    private final DashboardService service;
    public RoomOverviewController(DashboardService service) {
        this.service = service;
    }

    @GetMapping
    public String showRooms(Model model, HttpSession session){
        if (session.getAttribute("userEmail") == null) return "redirect:/login";

        List<Room> rooms = service.getUserRooms((String) session.getAttribute("userEmail"));
        rooms.sort(Comparator.comparingInt(Room::getId));
        model.addAttribute("rooms", rooms);

        return "roomOverview";
    }

    @GetMapping("/{roomID}")
    public String showRoomEditPage(@PathVariable int roomID, Model model, HttpSession session){
        if (session.getAttribute("userEmail") == null) return "redirect:/login";

        List<Room> userRooms = service.getUserRooms(String.valueOf(session.getAttribute("userEmail")));
        Room selectedRoom = userRooms.stream().filter(room -> room.getId() == roomID).findFirst().orElse(null);

        if (selectedRoom != null) {
            model.addAttribute("viewModel", new AddRoomViewModel(selectedRoom.getName(),
                    selectedRoom.getWidth(), selectedRoom.getLength(), selectedRoom.getHeight()));
            model.addAttribute("room", selectedRoom);
        } else {
            return "roomOverview";
        }

        return "roomEdit";
    }

    @PostMapping("/{roomID}")
    public String saveRoomChanges(@PathVariable int roomID, @Valid @ModelAttribute("viewModel") AddRoomViewModel viewModel, BindingResult errors, Model model, HttpSession session){

        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(error -> logger.error(error.toString()));
            return "roomEdit";
        }
        try {
            service.updateRoom(roomID, viewModel.getRoomName(), viewModel.getWidth(),
                    viewModel.getLength(), viewModel.getHeight(), (String) session.getAttribute("userEmail"));
        } catch (DatabaseException e){
            return "errorPage";
        }
        return "redirect:/rooms";
    }
}
