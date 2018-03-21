package main.zeroMQChannels;

import group_4.interfaces.SendChannel;
import org.zeromq.ZContext;

  import org.zeromq.ZMQ;


public class zeroMQDealerChannel implements SendChannel {

    private String identity = null;
    private ZContext zContext = null;
    private ZMQ.Socket client = null;

    public zeroMQDealerChannel(String identity) {

        this.identity = identity;
        zContext = new ZContext(1);
        client = zContext.createSocket(ZMQ.DEALER);
    }

    @Override
    public void connect(String ip, short port) {

        client.setIdentity(getChannelIdentity().getBytes(ZMQ.CHARSET));
        client.connect("tcp://"+ip+":"+port);
    }

    @Override
    public String getChannelIdentity() {
        return identity;
    }

    @Override
    public void send(String message) {
        client.sendMore("");
        client.send(message, 0);
    }

    @Override
    public void close() {
        zContext.destroy();
    }


    @Override
    public String receive() {
        client.recvStr();
        String content = client.recvStr();
        return content;
    }
}
