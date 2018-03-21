package group_4.adapters;

import com.google.gson.*;
import group_4.Elem;
import java.lang.reflect.Type;
import java.util.Set;

public class ElemAdapter implements JsonDeserializer<Elem>, JsonSerializer<Elem> {

    private Elem parseElem(JsonElement jsonElement, JsonDeserializationContext jsonDeserializationContext) {

        if (jsonElement.isJsonPrimitive()) {
            JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
            if (jsonPrimitive.isNumber()) {
                Number number = jsonDeserializationContext.deserialize(jsonElement, Number.class);
                return new Elem(number);
            } else if (jsonPrimitive.isString()) {
                String string = jsonDeserializationContext.deserialize(jsonElement, String.class);
                return new Elem(string);
            } else if (jsonPrimitive.isBoolean()) {
                Boolean b = jsonDeserializationContext.deserialize(jsonElement, Boolean.class);
                return new Elem(b);
            }
        } else if (jsonElement.isJsonObject()) {
            Elem result = null;
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Set<String> keys = jsonObject.keySet();
            for (String key : keys) {
                JsonElement element = jsonObject.get(key);
                Elem elem = parseElem(element, jsonDeserializationContext);
                if (result==null) {
                    result = new Elem(key, elem);
                } else {
                    result.add(key, elem);
                }
            }
            return result;
        } else if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            Elem result = null;
            for (JsonElement element : jsonArray) {
                Elem elem = parseElem(element, jsonDeserializationContext);
                if (result==null) {
                    result = new Elem(elem);
                } else {
                    result.add(elem);
                }
            }
            return result;
        }
        return null;
    }

    private JsonElement parseJson(Elem elem, JsonSerializationContext jsonSerializationContext){

        if(elem.isMap()){
            JsonObject jsonObject = new JsonObject();
            Set<String> keys = elem.getSetKey();
            for (String key : keys){
                JsonElement element = parseJson(elem.getElemByKey(key),jsonSerializationContext);
                jsonObject.add(key,element);
            }
            return jsonObject;
        } else if (elem.isArray()){
            JsonArray jsonArray = new JsonArray();
            for (int i = 0; i < elem.size(); i++){
                Elem elem1 = elem.getElemAt(i);
                if (elem1 != null) {
                    JsonElement jsonElement = parseJson(elem1, jsonSerializationContext);
                    jsonArray.add(jsonElement);
                }
            }
            return jsonArray;
        } else if (elem.getNumber() != null){
            Number number = elem.getNumber();
            return jsonSerializationContext.serialize(number, Number.class);
        } else if (elem.getString() != null){
            return jsonSerializationContext.serialize(elem.getString(), String.class);
        } else if (elem.getBoolean() != null){
            return jsonSerializationContext.serialize(elem.getBoolean(), Boolean.class);
        }
        return null;
    }

    public Elem deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return parseElem(jsonElement, jsonDeserializationContext);
    }

    public JsonElement serialize(Elem elem, Type type, JsonSerializationContext jsonSerializationContext) {
        return parseJson(elem, jsonSerializationContext);
    }
}
