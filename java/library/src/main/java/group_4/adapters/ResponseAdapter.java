package group_4.adapters;

import com.google.gson.*;
import group_4.Elem;
import group_4.entities.Response;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Set;

public class ResponseAdapter implements JsonDeserializer<Response>, JsonSerializer<Response> {

    private Response parseResponse (JsonElement jsonElement, JsonDeserializationContext jsonDeserializationContext){
        JsonObject jsonObject = jsonElement.getAsJsonObject();


        Set<String> keys = jsonObject.keySet();

        if (keys.size() == 3){
            if (keys.contains("result") && keys.contains("id") && keys.contains("jsonrpc")){
                //
            } else {
                return null;
            }
        } else {
            return null;
        }

        if (!jsonObject.get("jsonrpc").getAsString().equalsIgnoreCase("2.0")){
            return null;
        }
        String jsonrpc = jsonDeserializationContext.deserialize(jsonObject.get("jsonrpc"),String.class);
        Object id = jsonDeserializationContext.deserialize(jsonObject.get("id"),Object.class);
        Elem result = jsonDeserializationContext.deserialize(jsonObject.get("result"), Elem.class);

        if (jsonrpc != null) {
            if (id.getClass().equals(String.class)){
                return new Response((String)id,result,jsonrpc);
            } else {
                return new Response((Number)id,result,jsonrpc);
            }
        }

        return null;

    }

    private JsonElement responseToJson (Response response, JsonSerializationContext jsonSerializationContext){
        JsonObject jsonObject = new JsonObject();

        jsonObject.add("jsonrpc", jsonSerializationContext.serialize(response.getJsonrpc()));
        jsonObject.add("result", jsonSerializationContext.serialize(response.getResult()));
        jsonObject.add("id", jsonSerializationContext.serialize(response.getId()));

        return jsonObject;
    }


    @Override
    public Response deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        return parseResponse(jsonElement,jsonDeserializationContext);
    }

    @Override
    public JsonElement serialize(Response response, Type type, JsonSerializationContext jsonSerializationContext) {
        return responseToJson(response,jsonSerializationContext);
    }
}
