package group_4.entities;

import com.google.gson.annotations.SerializedName;
import group_4.Elem;

public class Request extends RequestObject {

    @SerializedName("id")
    private Object id;

    public Request(String id, String method, Elem params,String jsonrpc) {
        this.id = id;
        this.method = method;
        this.params = params;
        this.jsonrpc = jsonrpc;
    }

    public Request(Number id, String method, Elem params,String jsonrpc) {
        if ((id.doubleValue() == Math.floor(id.doubleValue())) && !Double.isInfinite(id.doubleValue())) {   //if number is not a double, cast int
            this.id = id.intValue();
        } else
        this.id = id;  /*else store number as Number (with decimals). Jsonrpc claims that the value
        SHOULD normally not be Null and Numbers SHOULD NOT contain fractional parts*/
        this.method = method;
        this.params = params;
        this.jsonrpc = jsonrpc;
    }


    public Object getId() {
        return id;
    }

    public String getJsonrpc(){
        return jsonrpc;
    }

}
