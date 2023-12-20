package be.kdg.integration3.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class SensorsOverviewResponse {
    private List<SensorOverviewResponse> results;
    private Date timestampFrom;
    private Date timestampTo;

    @Data
    @AllArgsConstructor
    public static class SensorOverviewResponse {
        private String metricName;
        private double value;
        private boolean aboveThreshold;
    }
}
