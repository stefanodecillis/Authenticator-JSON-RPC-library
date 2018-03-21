package group_4.entities;

import com.google.gson.annotations.SerializedName;

public abstract class ResponseObject {

    @SerializedName("jsonrpc")
    protected String jsonrpc;

    @SerializedName("id")
    protected Object id;

    //constructor
    ResponseObject(Object id, String jsonrpc){
        if (id == null){
            this.id = null;
        } else {
            if (id.getClass().equals(String.class)) {
                this.id = id.toString();
            } else {
                Number num = (Number) id;
                if ((num.doubleValue() == Math.floor(num.doubleValue())) && !Double.isInfinite(num.doubleValue())) {
                    this.id = num.intValue();
                } else
                    this.id = num;
            }
        }
        this.jsonrpc = jsonrpc;
    }


    //methods
    public Object getId() {
        if(this.id == null) return "null";
        if (this.id.getClass().equals(String.class)){
            return this.id;
        } else {
            return (Number) this.id;
        }
    }

    public String getJsonrpc() {
        return jsonrpc;
    }
}
