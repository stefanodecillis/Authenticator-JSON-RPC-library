package group_4.interfaces;


public interface ListenChannel {

    void bind(String ip, short port);
    group_4.Pair<String, String> receive();
    void reply(String identity,String message);
    void close();

}