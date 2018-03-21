package main.handlers;

import main.entities.Client;
import main.entities.ServerSingleton;
import main.entities.Resource;
import group_4.entities.Request;
import main.zeroMQChannels.zeroMQDealerChannel;
import main.zeroMQChannels.zeroMQReqChannel;
import main.zeroMQChannels.zeroMQRouterChannel;

import java.util.ArrayList;
import java.util.List;

public class EntitiesManager {

    private static EntitiesManager instance = null;
    private static Client client = new Client();
    private static ServerSingleton server = ServerSingleton.getInstance();
    private static List<Request>  logsRequest = new ArrayList<>();

    //instance for communications
    private static zeroMQReqChannel zeroMQReqChannel = new zeroMQReqChannel();
    private static zeroMQDealerChannel zeroMQDealerChannel = new zeroMQDealerChannel("dealer identity");
    private static zeroMQRouterChannel zeroMQRouterChannel = new zeroMQRouterChannel();

    private EntitiesManager(){

        //resource examples

        Resource res1 = new Resource("calculator",2, "www.calculator.com");
        Resource res2 = new Resource("akinator", 1,"www.akinator.to");
        Resource res3 = new Resource("dev-tools",5,"www.dev-tools.ie");
        Resource res4 = new Resource("search-engine", 3, "www.google.uk");
        Resource res5 = new Resource("comoCity", 4, "www.comocity.it");


        server.addRes(res1);
        server.addRes(res2);
        server.addRes(res3);
        server.addRes(res4);
        server.addRes(res5);
    }

    public static EntitiesManager getInstance() {
        if (instance == null) {
            instance = new EntitiesManager();
        }
        return instance;
    }

    public ServerSingleton getServer(){
        return server;
    }

    public Client getClient() {
        return client;
    }

    public List<Request> getLogsRequest() {
        return logsRequest;
    }

    public void deleteLogReq(Request request){
        logsRequest.remove(request);
    }

    public static main.zeroMQChannels.zeroMQReqChannel getZeroMQReqChannel() {
        return zeroMQReqChannel;
    }

    public static main.zeroMQChannels.zeroMQDealerChannel getZeroMQDealerChannel() {
        return zeroMQDealerChannel;
    }

    public static main.zeroMQChannels.zeroMQRouterChannel getZeroMQRouterChannel() {
        return zeroMQRouterChannel;
    }
}
