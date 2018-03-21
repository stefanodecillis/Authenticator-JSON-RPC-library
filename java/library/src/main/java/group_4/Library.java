package group_4;

import group_4.entities.*;
import group_4.handlers.ParseManager;

public class Library {

    public static Request parseRequest(String json) {
        return ParseManager.getInstance().parseRequest(json);
    }
    public static Response parseResponse(String json) {
        return ParseManager.getInstance().parseResponse(json);
    }
    public static ErrorObj parseError(String json) {return ParseManager.getInstance().parseErrorObj(json);}
    public static String reqToJson(Request request) {return ParseManager.getInstance().requestToJson(request);}
    public static String responseToJson (Response response) {return ParseManager.getInstance().responseToJson(response);}
    public static String errorToJson (ErrorObj errorObj) {return ParseManager.getInstance().errorToJson(errorObj);}
    public static String batchToJson (Batch batch) {return ParseManager.getInstance().batchToJson(batch);}
    public static Batch parseBatch (String json) {return ParseManager.getInstance().parseBatch(json);}
    public static Notification parseNotification (String json) {return ParseManager.getInstance().parseNotification(json);}
    //notificationToJson is never used but it could be used in another project
    public static String notificationToJson (Notification notification) {return  ParseManager.getInstance().notificationToJson(notification);}

}
