package main.zeroMQChannels;

import group_4.interfaces.SendChannel;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class zeroMQReqChannel implements SendChannel {


    private ZContext zContext = null;
    private ZMQ.Socket client = null;
    private String identity = "reqID";

    public zeroMQReqChannel(){
        zContext = new ZContext(1);
        client = zContext.createSocket(ZMQ.REQ);
    }

    @Override
    public void connect(String ip, short port) {

        client.setIdentity(getChannelIdentity().getBytes(ZMQ.CHARSET));
        client.connect("tcp://"+ip+":"+port);

    }

    @Override
    public String getChannelIdentity() {       //req has identity yet
        return identity;
    }

    @Override
    public void send(String message) {
        client.send(message, 0);
    }

    @Override
    public void close() {
        zContext.destroy();
    }

    @Override
    public String receive() {
            String content = client.recvStr();
            return content;
    }
}
