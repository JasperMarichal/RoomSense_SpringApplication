package be.kdg.integration3.domain;

import be.kdg.integration3.util.deserializer.CO2DataDeserializer;
import com.google.gson.annotations.JsonAdapter;

import java.sql.Timestamp;
import java.time.Instant;

@JsonAdapter(CO2DataDeserializer.class)
public class CO2Data implements RawDataRecord{

    private final Timestamp timestamp;
    private final int analogConcentration;

    public CO2Data(long timestamp, int analogConcentration) {
        this.timestamp = Timestamp.from(Instant.ofEpochMilli(timestamp));
        this.analogConcentration = analogConcentration;
    }

    public CO2Data(Timestamp timestamp, int analogConcentration) {
        this.timestamp = timestamp;
        this.analogConcentration = analogConcentration;
    }

    public static int convertAnalogToPPM(int analog) {
        // Conversion formula:
        // ppm_per_analog_step = sensor_max_ppm / (sensor_max_voltage/analoginput_max_voltage * analoginput_steps)
        // 10000 / (2/5 * 1023) = 24,4379276...
        return (int) (analog * 24.4379277);
    }

    @Override
    public Timestamp getTimestamp() {
        return timestamp;
    }

    @Override
    public int getValue() {
        return convertAnalogToPPM(analogConcentration);
    }

    @Override
    public String toString() {
        return "CO2Data{" +
                "timestamp=" + timestamp +
                ", analogConcentration=" + analogConcentration +
                '}';
    }
}
