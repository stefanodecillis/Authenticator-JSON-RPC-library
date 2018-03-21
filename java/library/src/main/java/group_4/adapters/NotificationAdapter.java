package group_4.adapters;

import com.google.gson.*;
import group_4.Elem;
import group_4.entities.Notification;

import java.lang.reflect.Type;
import java.util.Set;

public class NotificationAdapter implements JsonDeserializer<Notification>, JsonSerializer<Notification> {

    private Notification parseNotification (JsonElement jsonElement, JsonDeserializationContext jsonDeserializationContext) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        Set<String> keys = jsonObject.keySet();

        if (keys.size() == 3) {
            if (keys.contains("method") && keys.contains("params") && keys.contains("jsonrpc")) {
                //
            } else {
                return null;
            }
        } else if (keys.size() == 2) {    /* notification without params */
            if (keys.contains("method") && keys.contains("jsonrpc")) {
                //
            } else {
                return null;
            }
        } else {
            return null;
        }
        if (!jsonObject.get("jsonrpc").getAsString().equalsIgnoreCase("2.0")) {
            return null;
        }

        String jsonrpc = jsonDeserializationContext.deserialize(jsonObject.get("jsonrpc"),String.class);
        String method = null;
        Elem params = null;

        /* duplicated code is needed to complete the adapter  --> found equal to requestAdapter */

        if (jsonObject.get("method").isJsonPrimitive()){
            if (jsonObject.get("method").getAsJsonPrimitive().isString()){
                method = jsonDeserializationContext.deserialize(jsonObject.get("method"),String.class);
                if (jsonObject.get("params") == null){
                    params = null;
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

        if (jsonrpc != null  && method != null){
            return new Notification(method,params,jsonrpc);
        } else {
            return null;
        }
    }

    private JsonElement notificationToJson(Notification notification, JsonSerializationContext jsonSerializationContext){
        JsonObject jsonObject = new JsonObject();

        jsonObject.add("jsonrpc", jsonSerializationContext.serialize(notification.getJsonrpc()));
        jsonObject.add("params", jsonSerializationContext.serialize(notification.getParams()));
        jsonObject.add("method", jsonSerializationContext.serialize(notification.getMethod()));

        return jsonObject;
    }


    @Override
    public Notification deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return parseNotification(jsonElement,jsonDeserializationContext);
    }

    @Override
    public JsonElement serialize(Notification notification, Type type, JsonSerializationContext jsonSerializationContext) {
        return notificationToJson(notification,jsonSerializationContext);
    }
}
