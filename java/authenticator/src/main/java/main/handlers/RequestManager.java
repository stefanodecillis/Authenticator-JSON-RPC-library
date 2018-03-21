package main.handlers;


import group_4.Utils;
import group_4.enumerations.ErrorType;
import main.Constants;
import main.entities.*;
import main.enumerations.ErrorTypeAuth;
import main.Tools;
import group_4.Elem;

import group_4.Library;

import group_4.entities.*;
import group_4.entities.Notification;
import group_4.entities.Request;
import group_4.entities.Response;
import java.util.Date;
import java.util.List;

import static main.Constants.JSONRPC_VERSION;

public class RequestManager {

    private static RequestManager instance = null;
    private int idNum = 0;

    private int assignID (){
        int id = this.idNum;
        this.idNum++;
        return id;
    }

    private RequestManager() {

    }

    public static RequestManager getInstance() {
        if (instance == null) {
            instance = new RequestManager();
        }
        return instance;
    }



    public static String batchDiscriminator(String json){
        Batch batch = Library.parseBatch(json);

        if (batch != null){
            if (batch.getBatchJsonObject() != null){
                System.out.println("in the batch");
                Batch response = new Batch();
                for (int i = 0; i < batch.getBatchJsonObject().size(); i++){
                    System.out.println("---------------next object in batch ------------------");
                    //System.out.println(rpcDiscriminator(batch.getBatchJsonObject().get(i)));
                    String jsonBatch = rpcDiscriminator(batch.getBatchJsonObject().get(i));
                    if (jsonBatch != null)
                        response.getBatchJsonObject().add(jsonBatch);
                }
                return Library.batchToJson(response); // we need to reply with another batch
            } else {
                System.out.println("batch of jsonObject null");
                return null;
            }
        } else {
            return rpcDiscriminator(json);
        }
    }

    /** sorting of json string.
     * it will use elaborateResponse and elaborateRequest.
     * Used on channel.recv()
     **/
    public static String rpcDiscriminator(String json){
        if(!Utils.isValidJSON(json)){
            return errorCreator(null, ErrorType.PARSE_ERROR);
        }

        Request request = Library.parseRequest(json);
        Response response = Library.parseResponse(json);
        ErrorObj errorObj = Library.parseError(json);
        Notification notification = Library.parseNotification(json);

        if (request != null && response == null && errorObj == null && notification == null){ //isRequest
           return elaborateRequest(json,false);
        } else if (request == null && response != null && errorObj == null && notification == null) { //isResponse
            return elaborateResponse(json);
        } else if (request == null && response == null && errorObj != null && notification == null) { //isError
            return elaborateResponse(json);
        } else if (request == null && response == null && errorObj == null && notification != null) { //isNotification
            return elaborateRequest(json,true);
        } else {
            return errorCreator(null,ErrorType.INVALID_REQUEST);
        }
    }

    public static String elaborateRequest(String jsonRequest, Boolean isNotification) {

        if (isNotification) {
            Notification notification = Library.parseNotification(jsonRequest);   //in our project, notification means to add/delete item from resources list
            if (notification == null){
                return null;
            }

            if (notification.getMethod().equalsIgnoreCase("addRes")){
                if (notification.getParams().getElemByKey("name") == null ||      //check for nullPointer
                        notification.getParams().getElemByKey("level") == null ||
                        notification.getParams().getElemByKey("path") == null){
                    return null;
                }
                double level = notification.getParams().getElemByKey("level").getNumber().doubleValue();
                if (!((level == Math.floor(level)) && !Double.isInfinite(level))) {
                    return null;
                }
                Resource res = new Resource(notification.getParams().getElemByKey("name").getString(),
                        Tools.toInt(notification.getParams().getElemByKey("level").getNumber()),
                        notification.getParams().getElemByKey("path").getString());
                EntitiesManager.getInstance().getServer().addRes(res);
            } else if (notification.getMethod().equalsIgnoreCase("remRes")){
                if (notification.getParams().getElemByKey("name") == null ||      //check for nullPointer
                        notification.getParams().getElemByKey("level") == null ||
                        notification.getParams().getElemByKey("path") == null){
                    return null;
                }
                double level = notification.getParams().getElemByKey("level").getNumber().doubleValue();
                if (!((level == Math.floor(level)) && !Double.isInfinite(level))) {
                    return null;
                }
                Resource res = new Resource(notification.getParams().getElemByKey("name").getString(),
                        Tools.toInt(notification.getParams().getElemByKey("level").getNumber()),
                        notification.getParams().getElemByKey("path").getString());
                EntitiesManager.getInstance().getServer().deleteRes(res);
            } else {
                System.out.println("notification with invalid method");
            }

            //notification always returns null
            return null;

        } else {
            Request request = Library.parseRequest(jsonRequest);
            if(request.getMethod().equalsIgnoreCase("getList")) {

                if (request.getParams()!=null) {
                    return errorCreator(request, ErrorType.INVALID_PARAMS);
                }

                List<Resource> list = EntitiesManager.getInstance().getServer().getListRes();  //this method has got no params

                if(list==null || list.isEmpty()){
                    /*
                    list will never be empty in the project examples
                     */
                    System.out.println("no trace of resources in memory \n"
                            + "- server");
                    return errorServerCreator(request, ErrorTypeAuth.NO_LIST_RES.getMsg(), ErrorTypeAuth.NO_LIST_RES.getCode());
                } else {

                    Elem result = new Elem();
                    for(int i=0; i<list.size(); i++){

                        Elem nameElem = new Elem(list.get(i).getName());
                        Elem lvlElem = new Elem(list.get(i).getLevel());
                        Elem urlElem = new Elem(list.get(i).getPathURL());
                        Elem resourceElem = new Elem();
                        resourceElem.add("name", nameElem);
                        resourceElem.add( "level", lvlElem);
                        resourceElem.add( "URL", urlElem);
                        result.add(resourceElem);
                    }

                    Response respObj = new Response(request.getId(), result, JSONRPC_VERSION);

                    return Library.responseToJson(respObj);
                }

            } else if (request.getMethod().equalsIgnoreCase("getAuthentication")) {

                Elem userId = request.getParams().getElemByKey("userID");
                Elem deadline = request.getParams().getElemByKey("deadline");
                Elem level = request.getParams().getElemByKey("lvl");

                if(request.getParams().getSetKey().size() != 3 || userId==null || deadline==null || level==null) {
                    return errorCreator(request,ErrorType.INVALID_PARAMS );
                }

                if(level.getNumber().intValue()>5 || level.getNumber().intValue()<1) {
                    return errorServerCreator(request,  ErrorTypeAuth.LVL_NOT_VALID.getMsg(), ErrorTypeAuth.LVL_NOT_VALID.getCode());
                }

                if (userId == null || deadline == null || level == null){
                    return errorCreator(request,ErrorType.INTERNAL_ERROR);
                }
                Date deadlineDate = new Date(deadline.getNumber().longValue());

                if (deadlineDate.before(new Date())){
                    return errorServerCreator(request,ErrorTypeAuth.TIME_MACHINE.getMsg(), ErrorTypeAuth.TIME_MACHINE.getCode());
                }
                Key key = EntitiesManager.getInstance().getServer().getAuthentication(userId.getString(),deadlineDate,level.getNumber().intValue()) ;

                Elem keyIDElem = new Elem(key.getKeyID());
                Elem levelElem = new Elem(key.getLevel());
                Elem deadlineElem = new Elem(key.getDeadlineLong());
                Elem result = new Elem();
                result.add("keyId", keyIDElem);
                result.add("level", levelElem);
                result.add("deadline", deadlineElem);

                Response respObj =new Response(request.getId(),result, JSONRPC_VERSION);
                return Library.responseToJson(respObj);

            } else if (request.getMethod().equalsIgnoreCase("getAuthorization")) {

                Elem keyId = request.getParams().getElemByKey("keyID");
                Elem resource = request.getParams().getElemByKey("resource");

                if(request.getParams().getSetKey().size() !=2 || keyId==null || resource==null) {
                    return errorCreator(request,ErrorType.INVALID_PARAMS );
                }

                if(!EntitiesManager.getInstance().getServer().checkResource(resource.getString())){    // check resource
                    return errorServerCreator(request, ErrorTypeAuth.NO_RESOURCES.getMsg(), ErrorTypeAuth.NO_RESOURCES.getCode());
                }

                Key key = null;

                for (int i = 0; i < EntitiesManager.getInstance().getServer().getUsersList().size();i++){
                    if(keyId.getString().equalsIgnoreCase(EntitiesManager.getInstance().getServer().getUsersList().get(i).getKey().getKeyID())){
                        key = EntitiesManager.getInstance().getServer().getUsersList().get(i).getKey();
                        break;
                    }
                }

                if (key == null){
                    return errorServerCreator(request, ErrorTypeAuth.NO_LOGGED.getMsg(), ErrorTypeAuth.NO_LOGGED.getCode());
                }

                Token token = EntitiesManager.getInstance().getServer().getAuthorization(key, resource.getString());

                if (token == null){
                    return errorServerCreator(request, ErrorTypeAuth.ERROR_LVL.getMsg(), ErrorTypeAuth.ERROR_LVL.getCode());
                }

                Elem tokenIDElem = new Elem(token.getTokenID());
                Elem tokenDeadlineElem = new Elem(token.getDeadline().getTime());
                Elem tokenUserIDElem = new Elem (token.getUserID());
                Elem tokenResourceIDElem = new Elem(token.getResourceID());
                Elem result = new Elem();
                result.add("tokenID", tokenIDElem);
                result.add("deadline", tokenDeadlineElem);
                result.add("userID", tokenUserIDElem);
                result.add("resource", tokenResourceIDElem);

                Response respObj = new Response(request.getId(), result, JSONRPC_VERSION);
                return Library.responseToJson(respObj);

            } else if (request.getMethod().equalsIgnoreCase("checkToken")) {

                Elem tokenID = request.getParams().getElemByKey("tokenID");

                if(request.getParams().getSetKey().size()!= 1 || tokenID==null) {
                    return errorCreator(request,ErrorType.INVALID_PARAMS );
                }

                Token token = null;
                for (int i = 0; i < EntitiesManager.getInstance().getServer().getTokensList().size();i++){
                    if(tokenID.getString().equalsIgnoreCase(EntitiesManager.getInstance().getServer().getTokensList().get(i).getTokenID())){
                        token = EntitiesManager.getInstance().getServer().getTokensList().get(i);
                        break;
                    }
                }

                if (token == null){
                    return errorServerCreator(request,ErrorTypeAuth.TOKEN_NOT_FOUND.getMsg(), ErrorTypeAuth.TOKEN_NOT_FOUND.getCode());
                }
                Long remainTime = EntitiesManager.getInstance().getServer().checkToken(token);

                if (remainTime > 0){
                    Elem remainTimeElem = new Elem(remainTime);
                    Elem result = new Elem("remainTime", remainTimeElem);

                    Response respObj = new Response(request.getId(), result, JSONRPC_VERSION);
                    return Library.responseToJson(respObj);
                } else {

                    String errorMessage = "Token received is not valid";   //????
                    Elem errorMessageElem = new Elem(errorMessage);
                    Elem result = new Elem("negativeResult", errorMessageElem);

                    Response respObj = new Response(request.getId(), result, JSONRPC_VERSION);
                    return Library.responseToJson(respObj);

                }

            } else if (request.getMethod().equalsIgnoreCase("clearAuth")) {

                Elem keyId = request.getParams().getElemByKey("keyId");

                if(request.getParams().getSetKey().size()!=1 || keyId==null) {
                    return errorCreator(request,ErrorType.INVALID_PARAMS );
                }

                int i = 0;
                User user = null;
                for (i = 0; i < EntitiesManager.getInstance().getServer().getUsersList().size();i++){
                    if(keyId.getString().equalsIgnoreCase(EntitiesManager.getInstance().getServer().getUsersList().get(i).getKey().getKeyID())){
                        user = EntitiesManager.getInstance().getServer().getUsersList().get(i);
                        break;
                    }
                }
                if (user == null){
                    return errorServerCreator(request,ErrorTypeAuth.USER_NOT_FOUND.getMsg(),ErrorTypeAuth.USER_NOT_FOUND.getCode());
                } else {
                    EntitiesManager.getInstance().getServer().clearAuth(i);
                    System.out.println("user deleted");
                    Elem result = new Elem();
                    result.setString("User deleted");
                    Response respObj = new Response(request.getId(), result, JSONRPC_VERSION);

                    return Library.responseToJson(respObj);
                }

            } else {
                System.out.println("no method found");
                return errorCreator(request, ErrorType.METHOD_NOT_FOUND);
            }
        }

    }

    public static String elaborateResponse(String jsonResponse){
        Response response = Library.parseResponse(jsonResponse);
        ErrorObj error = Library.parseError(jsonResponse);
        Boolean isError = (response == null && error != null);
        if (isError){
            System.out.println("errorObject with error code: " + Tools.toInt((Number) error.getError().get("code")) + " message: " + error.getError().get("message"));
            return (String) error.getError().get("message");
        } else{
         EntitiesManager entitiesManager = EntitiesManager.getInstance();
         List<Request> logs = entitiesManager.getLogsRequest();
         for (int i = 0; i < logs.size(); i++){
             if (logs.get(i).getId().equals((response.getId()))){
                 if (logs.get(i).getMethod().equalsIgnoreCase("getAuthentication")) {

                     Key key = new Key(response.getResult().getElemByKey("keyId").getString(),
                             response.getResult().getElemByKey("level").getNumber().intValue(),
                             response.getResult().getElemByKey("deadline").getNumber().longValue());

                     EntitiesManager.getInstance().getClient().setKey(key);

                     /* debug */
                     System.out.println("Got Key!");

                     entitiesManager.getLogsRequest().remove(logs.get(i));  //delete pending requests

                     return null;
                 } else if (logs.get(i).getMethod().equalsIgnoreCase("getAuthorization")){
                     //
                     Token token = new Token(response.getResult().getElemByKey("tokenID").getString(),
                             response.getResult().getElemByKey("userID").getString(),
                             response.getResult().getElemByKey("resource").getString(),
                             response.getResult().getElemByKey("deadline").getNumber().longValue());

                     entitiesManager.getClient().setToken(token);

                     /* debug */
                     System.out.println("Got Token!");

                     entitiesManager.getLogsRequest().remove(logs.get(i));  //delete pending requests
                     return null;

                 } else if (logs.get(i).getMethod().equalsIgnoreCase("checkToken")) {

                     Elem remainTimeElem = response.getResult().getElemByKey("remainTime");

                     if(remainTimeElem==null) {
                         //Token is not valid
                         System.out.println(response.getResult().getElemByKey("negativeResult"));
                     } else {
                         //Token is valid
                         long remainTime = remainTimeElem.getNumber().longValue();
                         long second = (remainTime / 1000) % 60;
                         long minute = (remainTime / (1000 * 60)) % 60;
                         long hour = (remainTime / (1000 * 60 * 60));

                         if (hour == 24){
                             String time = String.format("%02dh:%02dm:%02ds", hour, minute, second);
                             System.out.println("Your token is valid: it will expire in " + time);
                         } else {
                             String time = String.format("%02dh:%02dm:%02ds", hour%24, minute, second);
                             System.out.println("Your token is valid: it will expire in " + time);
                         }


                     }
                     entitiesManager.getLogsRequest().remove(logs.get(i));  //delete pending requests
                     return null;
                 } else if (logs.get(i).getMethod() == "getList"){
                     for (int j = 0; j < response.getResult().size(); j++){
                         System.out.println("name: " + response.getResult().getElemAt(j).getElemByKey("name").getString() +
                                 " level: " + response.getResult().getElemAt(j).getElemByKey("level").getNumber()+
                                 " URL: " + response.getResult().getElemAt(j).getElemByKey("URL").getString());  //resources list should be an array
                     }
                     entitiesManager.getLogsRequest().remove(logs.get(i));  //delete pending requests
                     System.out.println("Gotcha! List Printed");
                     return null;
                 } else if(logs.get(i).getMethod().equalsIgnoreCase("clearAuth")){
                     System.out.println(response.getResult().getString());
                     EntitiesManager.getInstance().getClient().setKey(null);
                     return null;
                 }else {
                     return ("method not found");
                 }
             }
         }
            System.out.println("request not found in logs");
         return "request not found in logs";
        }
    }


    public String reqListResource(){
        String method = "getList";
        Request request = new Request(assignID(),method,null, JSONRPC_VERSION);
        EntitiesManager.getInstance().getLogsRequest().add(request);
        return Library.reqToJson(request);
    }

    public String reqAuthentication(String userID, int level, long deadline){
        String method = "getAuthentication";

        Elem elem = new Elem();
        elem.add("userID", new Elem(userID));
        elem.add("lvl", new Elem(level));
        elem.add("deadline", new Elem(deadline));

        Request request = new Request(assignID(), method, elem, JSONRPC_VERSION);
        EntitiesManager.getInstance().getLogsRequest().add(request);
        return Library.reqToJson(request);
    }

    public String reqAuthorization(String keyID, String resource){
        String method = "getAuthorization";

        Elem elem = new Elem();
        elem.add("keyID", new Elem(keyID));
        elem.add("resource", new Elem(resource));

        Request request = new Request(assignID(),method,elem, JSONRPC_VERSION);
        EntitiesManager.getInstance().getLogsRequest().add(request);
        return Library.reqToJson(request);
    }

    public String reqCheckToken(String tokenID){
        String method = "checkToken";

        Elem elem = new Elem();
        elem.add("tokenID", new Elem(tokenID));

        Request request = new Request(assignID(),method, elem, JSONRPC_VERSION);
        EntitiesManager.getInstance().getLogsRequest().add(request);
        return Library.reqToJson(request);
    }

    public String reqClearAuth(String keyId){
        String method = "clearAuth";

        Elem elem = new Elem();
        elem.add("keyId", new Elem(keyId));

        Request request = new Request(assignID(),method, elem, JSONRPC_VERSION);
        EntitiesManager.getInstance().getLogsRequest().add(request);
        return Library.reqToJson(request);
    }

    public String randomReq (String method, Elem params){
        Request request = new Request(assignID(),method,params, JSONRPC_VERSION);
        EntitiesManager.getInstance().getLogsRequest().add(request);
        return Library.reqToJson(request);
    }


    private static String errorCreator(Request request, ErrorType errorType) {
        if (request == null) {
            ErrorObj error = new ErrorObj("null", errorType, JSONRPC_VERSION);
            return Library.errorToJson(error);
        }
        if (request.getId() != null) {
            ErrorObj error = new ErrorObj(request.getId(), errorType, Constants.JSONRPC_VERSION);
            return Library.errorToJson(error);
        } else {
            System.out.println(" error to who? (id null)");
            return null;
        }
    }

    private static String errorServerCreator(Request request, String msg, int code){
        if (request == null){
            ErrorObj error = new ErrorObj("null", msg, code, JSONRPC_VERSION);
            return Library.errorToJson(error);
        }
        if (request.getId() != null){
            if (request.getId() instanceof Number  || request.getId().getClass() == Integer.class){
                ErrorObj error = new ErrorObj(Tools.toInt((Number) request.getId()), msg, code, JSONRPC_VERSION);
                return Library.errorToJson(error);
            } else if (request.getId() instanceof String) {
                ErrorObj error = new ErrorObj((String) request.getId(), msg, code, JSONRPC_VERSION);
                return Library.errorToJson(error);
            } else {
                return null;
            }
        } else {
            System.out.println(" error to who? (id null)");
            return null;
        }
    }


}


/** first version of rpcDiscriminator method. It works well but the new implementation is more efficient.
 */

  /*  public static String rpcDiscriminator(String json){
        Batch batch = Library.parseBatch(json);

        if (batch != null){
            if (batch.getBatchJsonObject() != null){
                System.out.println("in the batch");
                Batch response = new Batch();
                for (int i = 0; i < batch.getBatchJsonObject().size(); i++){
                    System.out.println("---------------next object in batch ------------------");
                    //System.out.println(rpcDiscriminator(batch.getBatchJsonObject().get(i)));
                    String jsonBatch = rpcDiscriminator(batch.getBatchJsonObject().get(i));
                    if (jsonBatch != null)
                    response.getBatchJsonObject().add(jsonBatch);
                }
                return Library.batchToJson(response); // we need to reply with another batch
            } else {
                System.out.println("batch of jsonObject null");
            }
        }

        if(!Utils.isValidJSON(json)){
            return errorCreator(null, ErrorType.PARSE_ERROR);
        }
        Request request = ParseManager.getInstance().parseRequest(json);
        Response response = ParseManager.getInstance().parseResponse(json);
        ErrorObj errorObj = ParseManager.getInstance().parseErrorObj(json);

        if(request == null || response == null || errorObj == null){
            System.out.println("json string not compatible");
            return errorCreator(request, ErrorType.INVALID_REQUEST);
        }

        if (request.getJsonrpc() == null ||
                response.getJsonrpc() == null ||
                errorObj.getJsonrpc() == null){
            return errorCreator(null,ErrorType.INVALID_REQUEST);
        }

        System.out.println("version: " + response.getJsonrpc()); //debug

        if(request.getId() != null && response.getId() != null && errorObj.getId() != null){
            if (!isIdValid(request.getId())){
                return errorCreator(request,ErrorType.INVALID_REQUEST);
            }
            if (request.getJsonrpc().equalsIgnoreCase(Constants.JSONRPC_VERSION) && response.getJsonrpc().equalsIgnoreCase(Constants.JSONRPC_VERSION) && errorObj.getJsonrpc().equalsIgnoreCase(Constants.JSONRPC_VERSION)){
                if (request.getMethod() != null){
                    if (request.getMethod() instanceof String){
                        System.out.println("request");
                        return elaborateRequest(json,false);   //request can have params null --> getList
                    } else {
                        return errorCreator(request,ErrorType.INVALID_REQUEST);
                    }
                } else {
                    //it's not a request obj
                    if (response.getResult() !=null){
                        System.out.println("getResult");
                       return elaborateResponse(json);
                    } else {
                        //then it's an error
                        if (errorObj.getError() != null){
                            System.out.println("it's an error");
                            return elaborateResponse(json);
                        } else {
                            //it's not jsonrpc
                            System.out.println("it's not jsonrpc");
                           return errorCreator(request,ErrorType.INVALID_REQUEST);
                        }
                    }
                }
            } else {
                System.out.println("jsonrpc version else branch");
                return errorCreator(request,ErrorType.INVALID_REQUEST);
            }
        } else {
            //it's a notification
            if (request.getJsonrpc().equalsIgnoreCase(Constants.JSONRPC_VERSION) && response.getJsonrpc().equalsIgnoreCase(Constants.JSONRPC_VERSION) && errorObj.getJsonrpc().equalsIgnoreCase(Constants.JSONRPC_VERSION)){
                if (request.getMethod() != null){
                    System.out.println("notification");
                    return elaborateRequest(json, true);
                } else if (errorObj.getError()!= null){
                    //errorObj with id null
                    System.out.println("errorObj with id null makes no sense");
                   return null;
                } else if (response.getResult() != null) {
                    //then it is response with id null
                    System.out.println("response with id null makes no sense");
                    return null;
                }else {
                    return errorCreator(request,ErrorType.INVALID_REQUEST);
                }
            } else {
                System.out.println("jsonrpc version else branch");
                return errorCreator(request,ErrorType.INVALID_REQUEST);
            }

        }
    }
*/