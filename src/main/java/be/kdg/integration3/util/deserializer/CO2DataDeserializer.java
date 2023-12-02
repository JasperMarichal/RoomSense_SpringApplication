package be.kdg.integration3.util.deserializer;

import be.kdg.integration3.domain.CO2Data;
import com.google.gson.*;

import java.lang.reflect.Type;

public class CO2DataDeserializer implements JsonDeserializer<CO2Data> {
    @Override
    public CO2Data deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        long timestamp = jsonObject.get("timestamp").getAsLong();
        int CO2 = jsonObject.get("analogConcentration").getAsInt();
        return new CO2Data(timestamp, CO2);
    }
}
