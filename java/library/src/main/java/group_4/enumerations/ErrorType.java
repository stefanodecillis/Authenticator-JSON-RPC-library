package group_4.enumerations;

import com.google.gson.annotations.SerializedName;

public enum ErrorType {
    PARSE_ERROR(-32700,"Parse error"),
    INVALID_REQUEST(-32600,"Invalid Request"),
    METHOD_NOT_FOUND(-32601,"Method not found"),
    INVALID_PARAMS(-32602,"Invalid params"),
    INTERNAL_ERROR(-32603,"Internal error");

    @SerializedName("code")
    protected int code;
    @SerializedName("message")
    protected String msg;

    ErrorType(int code,String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
