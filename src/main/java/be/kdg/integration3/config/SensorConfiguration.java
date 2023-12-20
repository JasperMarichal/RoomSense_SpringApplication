package be.kdg.integration3.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "metric")
public class SensorConfiguration {

    private Co2 co2;
    private Humidity humidity;
    private Temperature temperature;
    private long refreshInterval;

    @Data
    public static class Co2 {
        private double threshold;
    }
    @Data
    public static class Humidity {
        private double threshold;
    }
    @Data
    public static class Temperature {
        private double threshold;
    }

}
