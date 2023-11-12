package be.kdg.integration3.util.deserializers;

import be.kdg.integration3.domain.HumidityData;
import be.kdg.integration3.domain.TemperatureData;
import com.google.gson.*;

import java.lang.reflect.Type;

public class HumidityDataDeserializer implements JsonDeserializer<HumidityData> {
    @Override
    public HumidityData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        long timestamp = jsonObject.get("timestamp").getAsLong();
        int humidity = jsonObject.get("humidity").getAsInt();
        return new HumidityData(timestamp, humidity);
    }
}
