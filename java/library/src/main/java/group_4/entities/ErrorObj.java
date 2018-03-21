package group_4.entities;

import com.google.gson.annotations.SerializedName;
import group_4.enumerations.ErrorType;

import java.util.HashMap;

public class ErrorObj extends ResponseObject {

    @SerializedName("error")
    private HashMap<String,Object> error = null;

    public ErrorObj(int id, ErrorType errortype, String jsonrpc) {
        super(id,jsonrpc);
        this.error = new HashMap<String,Object>();
        this.error.put("code", errortype.getCode());
        this.error.put("message", errortype.getMsg());

    }

    public ErrorObj(String id, HashMap<String,Object> errortype, String jsonrpc) {
        super(id,jsonrpc);
        this.error = new HashMap<String,Object>();
        this.error = errortype;

    }

    public ErrorObj(Object id, HashMap<String,Object> errortype, String jsonrpc) {
        super(id,jsonrpc);
        this.error = new HashMap<String,Object>();
        this.error = errortype;

    }

    public ErrorObj(String id,ErrorType errortype,String jsonrpc) {
        super(id,jsonrpc);
        this.error = new HashMap<String,Object>();
        this.error.put("code", errortype.getCode());
        this.error.put("message", errortype.getMsg());
    }

    public ErrorObj(Object id, ErrorType errortype,String jsonrpc) {
        super(id,jsonrpc);
        this.error = new HashMap<String,Object>();
        this.error.put("code", errortype.getCode());
        this.error.put("message", errortype.getMsg());
    }

    public ErrorObj(int id,String msg, int code, String jsonrpc) {
        super(id,jsonrpc);
        this.error = new HashMap<String,Object>();
        this.error.put("code", code);
        this.error.put("message", msg);

    }

    public ErrorObj(String id,String msg, int code ,String jsonrpc) {
        super(id,jsonrpc);
        this.error = new HashMap<String,Object>();
        this.error.put("code", code);
        this.error.put("message", msg);
    }

    public HashMap<String, Object> getError() {
        return error;
    }
}