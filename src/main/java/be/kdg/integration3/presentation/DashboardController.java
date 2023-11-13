package be.kdg.integration3.presentation;

import be.kdg.integration3.domain.CO2Data;
import be.kdg.integration3.domain.HumidityData;
import be.kdg.integration3.domain.TemperatureData;
import be.kdg.integration3.service.DashboardService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Timestamp;

@Controller
@RequestMapping("/dashboard")
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
            model.addAttribute("tempList_timestamp", service.getTemperatureList().stream().map(temperatureData -> (temperatureData.getTimestamp().getTime() - firstDataTemperature)/1000).toList());

            model.addAttribute("tempList_rawTimes", service.getTemperatureList().stream().map(TemperatureData::getTimestamp).toList());
        }
        if(!service.getHumidityList().isEmpty()) {
            model.addAttribute("humidList", service.getHumidityList().stream().map(HumidityData::getValue).toList());
            long firstDataHumidity = service.getHumidityList().get(0).getTimestamp().getTime();
            model.addAttribute("humidList_timestamp", service.getHumidityList().stream().map(humidityData -> (humidityData.getTimestamp().getTime() - firstDataHumidity)/1000).toList());

            model.addAttribute("humidList_rawTimes", service.getHumidityList().stream().map(HumidityData::getTimestamp).toList());
        }
        if(!service.getCO2List().isEmpty()) {
            model.addAttribute("CO2List", service.getCO2List().stream().map(CO2Data::getValue).toList());
            long firstDataCO2 = service.getCO2List().get(0).getTimestamp().getTime();
            model.addAttribute("CO2List_timestamp", service.getCO2List().stream().map(CO2Data -> (CO2Data.getTimestamp().getTime() - firstDataCO2)/1000).toList());

            model.addAttribute("CO2List_rawTimes", service.getCO2List().stream().map(CO2Data::getTimestamp).toList());
        }
        return "dashboard";
    }
}
