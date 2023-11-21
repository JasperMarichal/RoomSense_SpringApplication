package be.kdg.integration3.presentation;

import be.kdg.integration3.domain.*;
import be.kdg.integration3.presentation.viewmodel.DashboardViewModel;
import be.kdg.integration3.service.DashboardService;
import be.kdg.integration3.util.exception.DatabaseException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpSession;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardService service;
    private JdbcTemplate jdbcTemplate;

    //TODO: Make this linked to user that is logged in
    private final String account = "roman.gordon@student.kdg.be";

    public DashboardController(DashboardService service, JdbcTemplate jdbcTemplate) {
        this.service = service;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Shows the dashboard view before any room, time or date has been selected
     * @param model The mode for the view
     * @return Returns the dashboard view
     */
    @GetMapping
    public String getDashboardView(Model model, HttpSession session) {

        model.addAttribute("chooseRoom", "Please choose a room to show data for.");

        DashboardViewModel viewModelFromSession = (DashboardViewModel) session.getAttribute("dashboardViewModel");
        if (viewModelFromSession == null){
            viewModelFromSession = new DashboardViewModel();
            session.setAttribute("dashboardViewModel", viewModelFromSession);
        }

        model.addAttribute("dashboardViewModel", viewModelFromSession);

        addRoomsToModel(model);

        return "dashboard";
    }

    @PostMapping
    public String redirectRoomPage(@ModelAttribute("dashboardViewModel") DashboardViewModel dashboardViewModel, HttpSession session){
        DashboardViewModel viewModelFromSession = (DashboardViewModel) session.getAttribute("dashboardViewModel");
        if (viewModelFromSession == null){
            viewModelFromSession = new DashboardViewModel();
            session.setAttribute("dashboardViewModel", viewModelFromSession);
        } else {
            session.setAttribute("dashboardViewModel", dashboardViewModel);
        }

        return "redirect:/dashboard/" + dashboardViewModel.getRoomId();
    }

    /**
     * Uses the roomId from the path is order to display the data for the right room, requests data from service and sends it to model
     * @param model The model for the view
     * @param roomId The roomId to look for from the path
     * @return Returns the dashboard page with the data
     */
    @GetMapping("/{roomId}")
    public String searchForRoom(Model model, @PathVariable int roomId, HttpSession session){
        DashboardViewModel viewModelFromSession = (DashboardViewModel) session.getAttribute("dashboardViewModel");
        if (viewModelFromSession == null){
            viewModelFromSession = new DashboardViewModel();
        }

        viewModelFromSession.setRoomId(roomId);
        session.setAttribute("dashboardViewModel", viewModelFromSession);

        model.addAttribute("dashboardViewModel", viewModelFromSession);

        LocalDateTime endDateTime;
        LocalDateTime startDateTime;

        try {
            if (viewModelFromSession.getDateTimeStart() != null) {
                startDateTime = viewModelFromSession.getDateTimeStart();
            } else {
                startDateTime = service.getLastTime(viewModelFromSession.getRoomId());
            }

            long startDateTimeMillis = startDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            long endDateTimeMillis = startDateTimeMillis - (viewModelFromSession.getTimePeriod() * 60000L);
            endDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(endDateTimeMillis), ZoneId.systemDefault());

            service.getData(viewModelFromSession.getRoomId(), startDateTime, endDateTime);

            if (!service.getTemperatureList().isEmpty()) {
                model.addAttribute("tempList", service.getTemperatureList().stream().map(TemperatureData::getValue).toList());
                model.addAttribute("tempListTimes", service.getTemperatureList().stream().map(TemperatureData::getTimestamp).toList());
            }

            if (!service.getHumidityList().isEmpty()) {
                model.addAttribute("humidList", service.getHumidityList().stream().map(HumidityData::getValue).toList());
                model.addAttribute("humidListTimes", service.getHumidityList().stream().map(HumidityData::getTimestamp).toList());
            }

            if (!service.getCO2List().isEmpty()) {
                model.addAttribute("CO2List", service.getCO2List().stream().map(CO2Data::getValue).toList());
                model.addAttribute("CO2ListTimes", service.getCO2List().stream().map(CO2Data::getTimestamp).toList());
            }

            if (!service.getNoiseList().isEmpty()) {
                model.addAttribute("noiseList", service.getNoiseList().stream().map(SoundData::getValue).toList());
                model.addAttribute("noiseListTimes", service.getNoiseList().stream().map(SoundData::getTimestamp).toList());
            }

            if (service.getTemperatureList().isEmpty() && service.getHumidityList().isEmpty() && service.getCO2List().isEmpty()) {
                model.addAttribute("chooseRoom", "There is no data available for your room of choice");
            }

            addRoomsToModel(model);
        } catch (DatabaseException e){
            model.addAttribute("databaseError", e.getMessage());
        }

        return "dashboard";
    }

    @PostMapping("/{room}")
    public String redirectToRoomPage(@PathVariable int room, @ModelAttribute("dashboardViewModel") DashboardViewModel dashboardViewModel, HttpSession session){
        DashboardViewModel viewModelFromSession = (DashboardViewModel) session.getAttribute("dashboardViewModel");
        if (viewModelFromSession == null){
            viewModelFromSession = new DashboardViewModel();
            viewModelFromSession.setRoomId(dashboardViewModel.getRoomId());
            session.setAttribute("dashboardViewModel", viewModelFromSession);
        } else {
            dashboardViewModel.setRoomId(dashboardViewModel.getRoomId());
            session.setAttribute("dashboardViewModel", dashboardViewModel);
        }

        return "redirect:/dashboard/" + dashboardViewModel.getRoomId();
    }

    /**
     * Get all the rooms owned by the user and adds them to the model
     * @param model The model for the view
     */
    private void addRoomsToModel(Model model){
        List<Room> rooms = service.getUserRooms(account);

        model.addAttribute("userRooms", rooms);
    }

}
