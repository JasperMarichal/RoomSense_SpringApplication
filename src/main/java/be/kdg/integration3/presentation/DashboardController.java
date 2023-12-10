package be.kdg.integration3.presentation;

import be.kdg.integration3.domain.*;
import be.kdg.integration3.presentation.viewmodel.DashboardViewModel;
import be.kdg.integration3.service.DashboardService;
import be.kdg.integration3.util.exception.DatabaseException;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    /**
     * Shows the dashboard view before any room, time or date has been selected, if you are not logged in,
     * automatically redirects to /login
     * @param model The mode for the view
     * @param session The HTTP session to retrieve dashboardViewModel and userEmail from
     * @return Returns the dashboard view
     */
    @GetMapping
    public String getDashboardView(Model model, HttpSession session) {
        if (session.getAttribute("userEmail") == null) return "redirect:/login";

        model.addAttribute("chooseRoom", "Please choose a room to show data for.");

        DashboardViewModel viewModelFromSession = (DashboardViewModel) session.getAttribute("dashboardViewModel");
        if (viewModelFromSession == null){
            viewModelFromSession = new DashboardViewModel();
            session.setAttribute("dashboardViewModel", viewModelFromSession);
        }

        model.addAttribute("dashboardViewModel", viewModelFromSession);

        List<Room> rooms = addRoomsToModel(model, session);

        List<List[]> allUserRoomData = new ArrayList<>();

        rooms.forEach(room -> {
            LocalDateTime startDateTime = service.getLastTime(room.getId());
            LocalDateTime endDateTime = startDateTime.minusMinutes(30);
            service.getData(room.getId(), startDateTime, endDateTime, false);

            List[] roomData = new List[5];

            roomData[0] = new ArrayList<>(List.of(room.getId(), room.getName()));

            if (!service.getTemperatureList().isEmpty()) roomData[1] = service.getTemperatureList().stream().map(TemperatureData::getValue).toList();
            else roomData[1] = new ArrayList<>();

            if (!service.getHumidityList().isEmpty()) roomData[2] = service.getHumidityList().stream().map(HumidityData::getValue).toList();
            else roomData[2] = new ArrayList<>();

            if (!service.getCO2List().isEmpty()) roomData[3] = service.getCO2List().stream().map(CO2Data::getValue).toList();
            else roomData[3] = new ArrayList<>();

            if (!service.getNoiseList().isEmpty()) roomData[4] = service.getNoiseList().stream().map(SoundData::getValue).toList();
            else roomData[4] = new ArrayList<>();

            allUserRoomData.add(roomData);
        });

        model.addAttribute("roomOverview", allUserRoomData);

        return "dashboardHome";
    }

    /**
     * Redirects the user to the room they selected based on the viewModel
     * @param dashboardViewModel The dashboardViewModel that contains the user inputted data
     * @param session The HTTP session to retrieve dashboardViewModel from
     * @return redirect to the appropriate page
     */
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
     * If the user does not own the room returns dashboard
     *
     * @param model The model for the view
     * @param roomId The roomId to look for from the path
     * @return Returns the dashboard page with the data
     */
    @GetMapping("/{roomId}")
    public String searchForRoom(Model model, @PathVariable int roomId, HttpSession session){
        if (session.getAttribute("userEmail") == null) return "redirect:/login";

        DashboardViewModel viewModelFromSession = (DashboardViewModel) session.getAttribute("dashboardViewModel");
        if (viewModelFromSession == null){
            viewModelFromSession = new DashboardViewModel();
        }

        viewModelFromSession.setRoomId(roomId);
        session.setAttribute("dashboardViewModel", viewModelFromSession);

        model.addAttribute("dashboardViewModel", viewModelFromSession);

        List<Room> userRooms = service.getUserRooms(String.valueOf(session.getAttribute("userEmail")));

        if (userRooms.stream().anyMatch(room -> room.getId() == roomId)) {
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

                System.out.println(startDateTime + "-" + endDateTime);

                service.getData(viewModelFromSession.getRoomId(), startDateTime, endDateTime, true);

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

                if (!service.getSpikeList().isEmpty()){
                    model.addAttribute("soundSpikes", service.getSpikeList());
                }

                if (service.getTemperatureList().isEmpty() && service.getHumidityList().isEmpty() && service.getCO2List().isEmpty()
                        && service.getNoiseList().isEmpty() && service.getSpikeList().isEmpty()) {
                    model.addAttribute("chooseRoom", "There is no data available for your room of choice");
                }

                addRoomsToModel(model, session);
            } catch (DatabaseException e) {
                model.addAttribute("databaseError", e.getMessage());
            }
        } else {
            return "redirect:/dashboard";
        }

        return "dashboard";
    }

    /**
     * Redirects to appropriate room based on the viewModel
     * @param room The roomId from the URL
     * @param dashboardViewModel The dashboardViewModel that contains the user inputted data
     * @param session The HTTP session to retrieve dashboardViewModel from
     * @return redirect to the appropriate page
     */
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
    private List<Room> addRoomsToModel(Model model, HttpSession session){
        String account = (String) session.getAttribute("userEmail");
        List<Room> rooms;
        if (account == null){
            rooms = new ArrayList<>();
        } else {
            rooms = service.getUserRooms(account);
        }

        model.addAttribute("userRooms", rooms);
        return rooms;
    }

    /**
     * Gets information of the selected spike to show on the chart, if the user is not logged in, redirects to login,
     * if the user does not own the room redirects to dashboard
     * if the spike does not exist redirects to the room page
     *
     * @param roomId the ID of the room to look for
     * @param spikeId The ID of the spike to look for
     * @param dashboardViewModel The dashboardViewModel that contains the user inputted data
     * @param model The mode for the view
     * @param session The HTTP session to retrieve dashboardViewModel and userEmail from
     * @return Returns the dashboard view
     */
    @GetMapping("/{roomId}/{spikeId}")
    public String getSpikePage(@PathVariable int roomId, @PathVariable int spikeId, @ModelAttribute("dashboardViewModel") DashboardViewModel dashboardViewModel, Model model, HttpSession session){
        if (session.getAttribute("userEmail") == null) return "redirect:/login";

        model.addAttribute("roomId", roomId);
        model.addAttribute("spikeId", spikeId);

        List<Room> userRooms = service.getUserRooms(String.valueOf(session.getAttribute("userEmail")));

        if (userRooms.stream().anyMatch(room -> room.getId() == roomId)) {
            List<SoundData> spikeData = service.getSpikeData(roomId, spikeId);

            if (!spikeData.isEmpty()) {
                model.addAttribute("spikeList", spikeData.stream().map(SoundData::getValue).toList());
                model.addAttribute("spikeListTimes", spikeData.stream().map(SoundData::getTimestamp).toList());
            } else {
                return "redirect:/dashboard/" + roomId;
            }
        } else {
            return "redirect:/dashboard";
        }

        return "spikePage";
    }

    @PostMapping("/{roomId}/{spikeId}")
    public String spikeToDashboard(@PathVariable int roomId, @PathVariable int spikeId, @ModelAttribute("dashboardViewModel") DashboardViewModel dashboardViewModel, Model model, HttpSession session){
        return "redirect:/dashboard/" + roomId;
    }

}
