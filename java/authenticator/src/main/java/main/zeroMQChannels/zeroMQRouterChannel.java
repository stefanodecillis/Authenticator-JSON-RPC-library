package main.zeroMQChannels;

import group_4.interfaces.ListenChannel;
import org.zeromq.ZContext;
import org.zeromq.ZFrame;

import org.zeromq.ZMQ;

import org.zeromq.ZMsg;


public class zeroMQRouterChannel implements ListenChannel {

    private ZContext zContext = null;
    private ZMQ.Socket listener = null;
    private String identity = null;

    public zeroMQRouterChannel() {
        zContext = new ZContext(1);
        listener = zContext.createSocket(ZMQ.ROUTER);
    }

    public String getChannelIdentity(){
        return this.identity;
    }

    @Override
    public void bind(String ip, short port) {
        listener.bind("tcp://"+ip+":"+port);
    }

    @Override
    public void close() {
        zContext.destroy();
    }

    @Override
    public void reply(String identity,String message) {

        listener.sendMore(identity);   //id
        listener.sendMore("");   //separator
        listener.send(message);        //content
    }

    /** this code works well for this project. We use the other method 'receive' because it's more flexible and it works well
     * with the integration.
     */
   /* public Pair<String,String> receive() {
        String identity = listener.recvStr();             //id
        listener.recv();                                  //Envelope separator
        String content = new String(listener.recv());     //content


        System.out.println(identity.toString());
        System.out.println(content.toString());
        return new Pair<String, String>(identity,content);
    }*/


    //receive() with ZMsg
    @Override
    public group_4.Pair<String,String> receive() {
        ZMsg msg = ZMsg.recvMsg(listener);
        ZFrame identity = msg.pop();
        ZFrame separator = msg.pop();
        ZFrame content = msg.pop();
        msg.destroy();

        boolean isRequest = separator.getData().length==0;


        String idString = new String(identity.getData());
        String contentString = null;
        if (isRequest) {
            contentString = new String(content.getData());
        } else {
            contentString = new String(separator.getData());
        }

        return new group_4.Pair<String,String>(idString, contentString);
    }

}
