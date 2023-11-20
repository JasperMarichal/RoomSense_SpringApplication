package be.kdg.integration3.util.deserializer;

import be.kdg.integration3.domain.SoundData;
import com.google.gson.*;

import java.lang.reflect.Type;

public class SoundDataDeserializer implements JsonDeserializer<SoundData> {
    @Override
    public SoundData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        long timestamp = jsonObject.get("timestamp").getAsLong();
        int sound = jsonObject.get("sound").getAsInt();
        return new SoundData(timestamp, sound);
    }
}
