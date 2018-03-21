package group_4.adapters;

import com.google.gson.*;
import group_4.entities.ErrorObj;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Set;

public class ErrorAdapter implements JsonDeserializer<ErrorObj>, JsonSerializer<ErrorObj> {

    private ErrorObj parseError(JsonElement jsonElement, JsonDeserializationContext jsonDeserializationContext){

        JsonObject jsonObject = jsonElement.getAsJsonObject();


        Set<String> keys = jsonObject.keySet();
        if (keys.size() == 3){
            if (keys.contains("error") && keys.contains("id") && keys.contains("jsonrpc")){
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
        HashMap<String,Object> error = jsonDeserializationContext.deserialize(jsonObject.get("error"), HashMap.class);

        if(jsonrpc != null && error != null){
           if(id != null){
               if (id.getClass().equals(String.class)){
                   return new ErrorObj((String)id,error,jsonrpc);
               } else {
                   return new ErrorObj(id,error,jsonrpc);
               }
           } else {
               return new ErrorObj(null,error,jsonrpc);
           }
        } else {
            return null;
        }
    }

    private JsonElement errorToJson (ErrorObj errorObj, JsonSerializationContext jsonSerializationContext){
        JsonObject jsonObject = new JsonObject();

        jsonObject.add("jsonrpc", jsonSerializationContext.serialize(errorObj.getJsonrpc()));
        jsonObject.add("error", jsonSerializationContext.serialize(errorObj.getError()));
        jsonObject.add("id", jsonSerializationContext.serialize(errorObj.getId()));

        return jsonObject;
    }

    @Override
    public ErrorObj deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return parseError(jsonElement,jsonDeserializationContext);
    }

    @Override
    public JsonElement serialize(ErrorObj errorObj, Type type, JsonSerializationContext jsonSerializationContext) {
        return errorToJson(errorObj,jsonSerializationContext);
    }
}
