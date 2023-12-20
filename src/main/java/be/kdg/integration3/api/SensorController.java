package be.kdg.integration3.api;

import be.kdg.integration3.api.dto.SensorsOverviewResponse;
import be.kdg.integration3.api.exception.Unauthenticated;
import be.kdg.integration3.config.SensorConfiguration;
import be.kdg.integration3.service.DashboardService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.List;

import static be.kdg.integration3.api.dto.SensorsOverviewResponse.SensorOverviewResponse;

@AllArgsConstructor
@RestController("Sensors api")
@RequestMapping("/api/sensors")
public class SensorController {
    static final String CO2_METRIC_NAME = "CO2";
    static final String HUMIDITY_METRIC_NAME = "HUMIDITY";
    static final String TEMP_METRIC_NAME = "TEMPERATURE";

    DashboardService dashboardService;
    SensorConfiguration metricConfiguration;

    @GetMapping("")
    public SensorsOverviewResponse getOverview(HttpSession session, @RequestParam int roomId, @RequestParam int intervalSeconds) {
        if (session.getAttribute("userEmail") == null) {
            throw new Unauthenticated();
        }

        Calendar calendarFrom = Calendar.getInstance();
        calendarFrom.add(Calendar.SECOND, -(intervalSeconds * 8));
        Calendar calendarTo = Calendar.getInstance();

        double avgCo2 = dashboardService.getAverageCo2(roomId, calendarFrom.toInstant(), calendarTo.toInstant());
        double avgHumidity = dashboardService.getAverageHumidity(roomId, calendarFrom.toInstant(), calendarTo.toInstant());
        double avgTemperature = dashboardService.getAverageTemp(roomId, calendarFrom.toInstant(), calendarTo.toInstant());

        List<SensorOverviewResponse> allSensors = List.of(
                new SensorOverviewResponse(CO2_METRIC_NAME,
                        avgCo2,
                        metricConfiguration.getCo2().getThreshold() < avgCo2),
                new SensorOverviewResponse(HUMIDITY_METRIC_NAME,
                        avgHumidity,
                        metricConfiguration.getHumidity().getThreshold() < avgHumidity),
                new SensorOverviewResponse(TEMP_METRIC_NAME,
                        avgTemperature,
                        metricConfiguration.getTemperature().getThreshold() < avgTemperature));
        return new SensorsOverviewResponse(allSensors, calendarFrom.getTime(), calendarTo.getTime());
    }

    @GetMapping("co2")
    public SensorsOverviewResponse getCo2Average(HttpSession session, @RequestParam int roomId, @RequestParam int intervalSeconds) throws Unauthenticated {
        if (session.getAttribute("userEmail") == null) {
            throw new Unauthenticated();
        }
        Calendar calendarFrom = Calendar.getInstance();
        calendarFrom.add(Calendar.SECOND, -intervalSeconds);
        Calendar calendarTo = Calendar.getInstance();
        double avg = dashboardService.getAverageCo2(roomId, calendarFrom.toInstant(), calendarTo.toInstant());

        return new SensorsOverviewResponse(List.of(new SensorOverviewResponse(CO2_METRIC_NAME,
                avg,
                metricConfiguration.getCo2().getThreshold() < avg)),
                calendarFrom.getTime(), calendarTo.getTime()
        );
    }

    @GetMapping("humidity")
    public SensorsOverviewResponse getHumidityAverage(HttpSession session, @RequestParam int roomId, @RequestParam int intervalSeconds) throws Unauthenticated {
        if (session.getAttribute("userEmail") == null) {
            throw new Unauthenticated();
        }
        Calendar calendarFrom = Calendar.getInstance();
        calendarFrom.add(Calendar.SECOND, -intervalSeconds);
        Calendar calendarTo = Calendar.getInstance();
        double avg = dashboardService.getAverageHumidity(roomId, calendarFrom.toInstant(), calendarTo.toInstant());
        return new SensorsOverviewResponse(List.of(new SensorOverviewResponse(HUMIDITY_METRIC_NAME,
                avg,
                metricConfiguration.getHumidity().getThreshold() < avg)),
                calendarFrom.getTime(), calendarTo.getTime()
        );
    }

    @GetMapping("temperature")
    public SensorsOverviewResponse getTemperatureAverage(HttpSession session, @RequestParam int roomId, @RequestParam int intervalSeconds) throws Unauthenticated {
        if (session.getAttribute("userEmail") == null) {
            throw new Unauthenticated();
        }
        Calendar calendarFrom = Calendar.getInstance();
        calendarFrom.add(Calendar.SECOND, -intervalSeconds);
        Calendar calendarTo = Calendar.getInstance();
        double avg = dashboardService.getAverageTemp(roomId, calendarFrom.toInstant(), calendarTo.toInstant());

        return new SensorsOverviewResponse(List.of(new SensorOverviewResponse(TEMP_METRIC_NAME,
                avg,
                metricConfiguration.getTemperature().getThreshold() < avg)),
                calendarFrom.getTime(), calendarTo.getTime()
        );
    }
}
