package group_4.entities;


import com.google.gson.annotations.SerializedName;
import group_4.Elem;

public class Response extends ResponseObject {

    @SerializedName("result")
    private Elem result;

    public Response(Object id, Elem result,String jsonrpc){
        super(id,jsonrpc);
        this.result = result;
    }

    public Elem getResult() {
        return result;
    }
}
