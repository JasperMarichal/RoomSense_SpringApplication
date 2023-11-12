package be.kdg.integration3.domain;

import be.kdg.integration3.util.deserializers.TemperatureDataDeserializer;
import com.google.gson.annotations.JsonAdapter;

import java.sql.Timestamp;
import java.time.Instant;

@JsonAdapter(TemperatureDataDeserializer.class)
public class TemperatureData implements RawDataRecord {
    private final Timestamp timestamp;
    private final int temperature;


    public TemperatureData(long timestamp, int temperature) {
        this.timestamp = Timestamp.from(Instant.ofEpochMilli(timestamp));
        this.temperature = temperature;
    }

    public TemperatureData(Timestamp timestamp, int temperature) {
        this.timestamp = timestamp;
        this.temperature = temperature;
    }

    @Override
    public Timestamp getTimestamp() {
        return timestamp;
    }

    @Override
    public int getValue() {
        return temperature;
    }

    @Override
    public String toString() {
        return "TemperatureData{" +
                "timestamp=" + timestamp +
                ", temperature=" + temperature +
                '}';
    }
}
