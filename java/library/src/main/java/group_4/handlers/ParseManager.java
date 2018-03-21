package group_4.handlers;

import com.google.gson.*;
import group_4.Utils;
import group_4.entities.*;

public class ParseManager {

    /* design pattern singleton in order to handle all the parse request with jsonrpc */

    private static ParseManager instance = null;

    private ParseManager() {

    }

    public static ParseManager getInstance() {
        if (instance==null) {
            instance = new ParseManager();
        }
        return instance;
    }

    public Request parseRequest(String json) {
        Gson gson = Utils.getGson();
        try {
            Request request = gson.fromJson(json, Request.class);
            return request;
        } catch (JsonParseException e){
            System.out.println(e.getMessage());
            return null;
        }
    }


    public Response parseResponse(String json) {
        Gson gson = Utils.getGson();
        try {
            Response response = gson.fromJson(json, Response.class);
            return response;
        } catch (JsonParseException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public ErrorObj parseErrorObj(String json) {
        Gson gson = Utils.getGson();
        try {
            ErrorObj error = gson.fromJson(json, ErrorObj.class);
            return error;
        } catch (JsonParseException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Batch parseBatch(String json){
        Gson gson = Utils.getGson();
        try {
            JsonObject[] jsonArray = gson.fromJson(json, JsonObject[].class);

            Batch batch = new Batch();
            for (int i = 0; i < jsonArray.length; i++){
                batch.getBatchJsonObject().add(jsonArray[i].toString());
            }
            return batch;

        } catch (JsonParseException e){
            //System.out.println(e.getMessage());  //debug
            return null;
        }
    }


    public String requestToJson(Request request){
        Gson gson = Utils.getGson();
        try {
            String json = gson.toJson(request, Request.class);
            return json;
        } catch (JsonParseException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String responseToJson(Response response){
        Gson gson = Utils.getGson();
        try {
            String json = gson.toJson(response, Response.class);
            return json;
        } catch (JsonParseException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String errorToJson(ErrorObj errorObj){
        Gson gson = Utils.getGson();
        try {
            String json = gson.toJson(errorObj, ErrorObj.class);
            return json;
        } catch (JsonParseException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Notification parseNotification(String json) {
        Gson gson = Utils.getGson();
        try {
            Notification notification = gson.fromJson(json, Notification.class);
            return notification;
        } catch (JsonParseException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String notificationToJson(Notification notification){
        Gson gson = Utils.getGson();
        try {
            String json = gson.toJson(notification, Notification.class);
            return json;
        } catch (JsonParseException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String batchToJson(Batch batch){
        Gson gson = Utils.getGson();
        try{
            JsonObject[] jsonArray = new JsonObject[batch.getBatchJsonObject().size()];
            for (int i = 0; i < batch.getBatchJsonObject().size(); i++){
                JsonObject object = gson.fromJson(batch.getBatchJsonObject().get(i), JsonObject.class);
                jsonArray[i] = object;
            }
            return gson.toJson(jsonArray);
        } catch (JsonParseException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
