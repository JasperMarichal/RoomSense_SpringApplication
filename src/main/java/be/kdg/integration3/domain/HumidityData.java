package be.kdg.integration3.domain;

import be.kdg.integration3.util.deserializer.HumidityDataDeserializer;
import com.google.gson.annotations.JsonAdapter;

import java.sql.Timestamp;
import java.time.Instant;

@JsonAdapter(HumidityDataDeserializer.class)
public class HumidityData implements RawDataRecord{

    private final Timestamp timestamp;
    private final int humidity;

    public HumidityData(long timestamp, int humidity) {
        this.timestamp = Timestamp.from(Instant.ofEpochMilli(timestamp));
        this.humidity = humidity;
    }

    public HumidityData(Timestamp timestamp, int humidity) {
        this.timestamp = timestamp;
        this.humidity = humidity;
    }

    @Override
    public Timestamp getTimestamp() {
        return timestamp;
    }

    @Override
    public int getValue() {
        return humidity;
    }

    @Override
    public String toString() {
        return "HumidityData{" +
                "timestamp=" + timestamp +
                ", humidity=" + humidity +
                '}';
    }
}
