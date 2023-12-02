package be.kdg.integration3.util.deserializer;

import be.kdg.integration3.domain.TemperatureData;
import com.google.gson.*;

import java.lang.reflect.Type;

public class TemperatureDataDeserializer implements JsonDeserializer<TemperatureData> {
    @Override
    public TemperatureData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        long timestamp = jsonObject.get("timestamp").getAsLong();
        int temperature = jsonObject.get("temperature").getAsInt();
        return new TemperatureData(timestamp, temperature);
    }
}
