package group_4.entities;

import com.google.gson.annotations.SerializedName;
import group_4.Elem;

public abstract class RequestObject {

    @SerializedName("jsonrpc")
    protected String jsonrpc = null;

    @SerializedName("method")
    protected String method = null;

    @SerializedName("params")
    protected Elem params = null;

    public String getMethod() {
        return method;
    }

    public Elem getParams() {
        return params;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }
}
