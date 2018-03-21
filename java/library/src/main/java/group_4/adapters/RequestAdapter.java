package group_4.adapters;

import com.google.gson.*;
import group_4.Elem;
import group_4.Utils;
import group_4.entities.Request;

import java.lang.reflect.Type;
import java.util.Set;

public class RequestAdapter implements JsonDeserializer<Request>, JsonSerializer<Request> {

    private Request parseRequest(JsonElement jsonElement, JsonDeserializationContext jsonDeserializationContext){
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        boolean paramsFound = true;    //found params key in jsonObject

       Set<String> keys = jsonObject.keySet();
        if (keys.size() == 4){
            if (keys.contains("method") && keys.contains("params") && keys.contains("id") && keys.contains("jsonrpc")){
                //
            } else {
                return null;
            }
        } else if (keys.size() == 3){
            if (keys.contains("method") && keys.contains("id") && keys.contains("jsonrpc")){
                paramsFound = false;     //not found params key
            } else {
                return null;
            }
        } else {
            return null;
        }
        if (!jsonObject.get("jsonrpc").getAsString().equalsIgnoreCase("2.0")){
            return null;
        }

        if (!jsonObject.get("id").isJsonPrimitive()){
            return null;
        }

        String jsonrpc = jsonDeserializationContext.deserialize(jsonObject.get("jsonrpc"),String.class);
        Object id = jsonDeserializationContext.deserialize(jsonObject.get("id"),Object.class);
        String method = null;
        Elem params = null;
        if (jsonObject.get("method").isJsonPrimitive()){
            if (jsonObject.get("method").getAsJsonPrimitive().isString()){
                method = jsonDeserializationContext.deserialize(jsonObject.get("method"),String.class);
                if (jsonObject.get("params") == null){
                    if (paramsFound){
                        return null;
                    }
                } else if (jsonObject.get("params").isJsonNull()){
                    if (paramsFound){
                        return null;
                    }
                    //else branch does nothing
                } else if (!jsonObject.get("params").isJsonPrimitive()){
                    params = jsonDeserializationContext.deserialize(jsonObject.get("params"),Elem.class);
                } else {
                    return null; //params must be a structured value --> array, hashmap
                }
            } else {
                return null;  //it must be string
            }
        } else {
            return null; // not jsonrpc 2.0
        }

        if (jsonrpc != null && id != null && method != null){
            if (jsonObject.get("id").getAsJsonPrimitive().isString()) {
                return new Request((String) id, method, params, jsonrpc);
            } else {
                return new Request((Number)id,method,params,jsonrpc);
            }
        } else {
            return null;
        }
    }

    private JsonElement requestToJson (Request request, JsonSerializationContext jsonSerializationContext){

        JsonObject jsonObject = new JsonObject();

        jsonObject.add("jsonrpc", jsonSerializationContext.serialize(request.getJsonrpc()));
        jsonObject.add("method", jsonSerializationContext.serialize(request.getMethod()));
        jsonObject.add("params", jsonSerializationContext.serialize(request.getParams()));
        jsonObject.add("id", jsonSerializationContext.serialize(request.getId()));

        return jsonObject;
    }

    @Override
    public Request deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return parseRequest(jsonElement,jsonDeserializationContext);
    }

    @Override
    public JsonElement serialize(Request request, Type type, JsonSerializationContext jsonSerializationContext) {
        return requestToJson(request,jsonSerializationContext);
    }
}
