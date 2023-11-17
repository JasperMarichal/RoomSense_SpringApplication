package be.kdg.integration3.presentation;

import be.kdg.integration3.domain.CO2Data;
import be.kdg.integration3.domain.HumidityData;
import be.kdg.integration3.domain.TemperatureData;
import be.kdg.integration3.service.DashboardService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardService service;
    private JdbcTemplate jdbcTemplate;
    private final String account = "roman.gordon@student.kdg.be";

    public DashboardController(DashboardService service, JdbcTemplate jdbcTemplate) {
        this.service = service;
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public String getDashboardView(Model model) {

        model.addAttribute("chooseRoom", "Please choose a room to show data for.");

        addRoomsToModel(model);

        return "dashboard";
    }

    @PostMapping
    public String redirectRoomPage(int roomId){
        return "redirect:/dashboard/" + roomId;
    }

    @GetMapping("/{roomId}")
    public String searchForRoom(Model model, @PathVariable int roomId){
        if (!service.getTemperatureList(roomId).isEmpty()){
            model.addAttribute("tempList", service.getTemperatureList(roomId).stream().map(TemperatureData::getValue).toList());
            model.addAttribute("tempList_rawTimes", service.getTemperatureList(roomId).stream().map(TemperatureData::getTimestamp).toList());
        }

        if (!service.getHumidityList(roomId).isEmpty()){
            model.addAttribute("humidList", service.getHumidityList(roomId).stream().map(HumidityData::getValue).toList());
            model.addAttribute("humidList_rawTimes", service.getHumidityList(roomId).stream().map(HumidityData::getTimestamp).toList());
        }

        if (!service.getCO2List(roomId).isEmpty()){
            model.addAttribute("CO2List", service.getCO2List(roomId).stream().map(CO2Data::getValue).toList());
            model.addAttribute("CO2List_rawTimes", service.getCO2List(roomId).stream().map(CO2Data::getTimestamp).toList());
        }

        if (service.getTemperatureList(roomId).isEmpty() && service.getHumidityList(roomId).isEmpty() && service.getCO2List(roomId).isEmpty()){
            model.addAttribute("chooseRoom", "There is no data available for your room of choice");
        }

        addRoomsToModel(model);

        return "dashboard";
    }

    @PostMapping("/{room}")
    public String redirectToRoomPage(@PathVariable int room, int roomId){
        return "redirect:/dashboard/" + roomId;
    }

    private void addRoomsToModel(Model model){
        List<Map<Integer, String>> rooms = jdbcTemplate.query("SELECT * FROM room WHERE account = ?", (rs, rowNum) -> Map.of(rs.getInt("room_id"), rs.getString("room_name")), account);

        model.addAttribute("userRooms", rooms);
    }

}
