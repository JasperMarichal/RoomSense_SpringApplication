package be.kdg.integration3.domain;

import be.kdg.integration3.util.deserializers.SoundDataDeserializer;
import be.kdg.integration3.util.deserializers.TemperatureDataDeserializer;
import com.google.gson.annotations.JsonAdapter;

import java.sql.Timestamp;
import java.time.Instant;

@JsonAdapter(SoundDataDeserializer.class)
public class SoundData implements RawDataRecord{
    private final Timestamp timestamp;
    private final int analogAmplitude;

    public SoundData(long timestamp, int analogAmplitude) {
        this.timestamp = Timestamp.from(Instant.ofEpochMilli(timestamp));
        this.analogAmplitude = analogAmplitude;
    }

    public SoundData(Timestamp timestamp, int analogAmplitude) {
        this.timestamp = timestamp;
        this.analogAmplitude = analogAmplitude;
    }

    @Override
    public Timestamp getTimestamp() {
        return timestamp;
    }

    @Override
    public int getValue() {
        return analogAmplitude;
    }
}
