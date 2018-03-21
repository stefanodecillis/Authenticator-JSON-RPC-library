package main.enumerations;

import com.google.gson.annotations.SerializedName;

public enum ErrorTypeAuth {
    NO_LIST_RES(-32010,"No trace of resources list in memory \n - server"),
    NO_LOGGED(-32020,"KeyID not found. First you need to authenticate"),
    NO_RESOURCES(-32030, "No trace of resource in memory \n - server"),
    ERROR_LVL(-32040, "You're not authorizated"),
    USER_NOT_FOUND(-32060,"No trace of your key in memory"),
    LVL_NOT_VALID(-3270, "Level is not valid"),
    TIME_MACHINE(-32100, "This is not a time machine. You can only choose days after this moment"),
    TOKEN_NOT_FOUND(-32080, "Token not found");

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String msg;

    ErrorTypeAuth(int code,String msg) {
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

/** the idea about this errorType ---> errors from library (jsonrpc)
 * are different and not related with errors from the server */