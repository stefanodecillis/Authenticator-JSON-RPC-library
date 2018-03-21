package group_4.interfaces;

public interface SendChannel {

    void connect(String ip, short port);
    String getChannelIdentity();
    void send(String message);
    String receive();
    void close();

}
