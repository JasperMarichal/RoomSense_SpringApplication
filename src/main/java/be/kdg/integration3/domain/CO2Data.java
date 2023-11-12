package be.kdg.integration3.domain;

import be.kdg.integration3.util.deserializers.CO2DataDeserializer;
import be.kdg.integration3.util.deserializers.TemperatureDataDeserializer;
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
        //TODO: Conversion
        return analog;
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
