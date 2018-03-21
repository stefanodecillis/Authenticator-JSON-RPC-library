package group_4.entities;

import group_4.Elem;

public class Notification extends RequestObject {

    public Notification( String method, Elem params,String jsonrpc) {
        this.method = method;
        this.params = params;
        this.jsonrpc = jsonrpc;

    }

}
