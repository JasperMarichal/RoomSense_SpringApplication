package be.kdg.integration3.presentation;

import be.kdg.integration3.domain.HumidityData;
import be.kdg.integration3.domain.TemperatureData;
import be.kdg.integration3.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Timestamp;

@Controller
@RequestMapping("/")
public class DashboardController {
    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @GetMapping
    public String getDashboardView(Model model) {
        if(!service.getTemperatureList().isEmpty()) {
            model.addAttribute("tempList", service.getTemperatureList().stream().map(TemperatureData::getValue).toList());
            long firstDataTemperature = service.getTemperatureList().get(0).getTimestamp().getTime();
            model.addAttribute("tempList_timestamp", service.getTemperatureList().stream().map(temperatureData -> temperatureData.getTimestamp().getTime() - firstDataTemperature).toList());
        }
        if(!service.getHumidityList().isEmpty()) {
            model.addAttribute("humidList", service.getHumidityList().stream().map(HumidityData::getValue).toList());
            long firstDataHumidity = service.getTemperatureList().get(0).getTimestamp().getTime();
            model.addAttribute("humidList_timestamp", service.getHumidityList().stream().map(humidityData -> humidityData.getTimestamp().getTime() - firstDataHumidity).toList());
        }
        return "dashboard";
    }
}
