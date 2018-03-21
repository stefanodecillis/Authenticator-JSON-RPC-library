package main;

import group_4.entities.Request;
import main.handlers.EntitiesManager;
import main.handlers.RequestManager;

import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {


        ServerThread serverThread = new ServerThread();
        serverThread.start();

        ClientDealerThread clientDealerThread = new ClientDealerThread();
        //clientDealerThread.start();                        //delete '//' to run the client dealer.

        ClientReqThread clientReqThread = new ClientReqThread();
        clientReqThread.start();

        while (true) {
            if (clientReqThread.isInterrupted()) {
                System.exit(0);
            }
        }

    }

    public  static class ClientReqThread extends Thread {
        @Override
        public void run() {
            System.out.println("thread clientReq in esec");


            EntitiesManager.getZeroMQReqChannel().connect("localhost",(short)5555); //213.32.98.205

            /*simple user interface to use the authenticator in this project.  */

            while(true){
                System.out.println("Choose what you want me to do: ");
                System.out.println("- getList");
                System.out.println("- getAuthentication");
                System.out.println("- getAuthorization");
                System.out.println("- checkToken");
                System.out.println("- clearAuth");
                System.out.println("- exit");

                Scanner scanner = new Scanner(System.in);
                String request = scanner.next();
                switch (request){
                    case "getList":
                        EntitiesManager.getZeroMQReqChannel().send(RequestManager.getInstance().reqListResource());
                        String response = EntitiesManager.getZeroMQReqChannel().receive();
                        RequestManager.batchDiscriminator(response);
                        break;
                    case "getAuthentication":
                        System.out.println("write your userID (without whitespace):");
                        String userID = scanner.next();
                        System.out.println("choose your level (from 1 to 5. Otherwise you will be rejected):");
                        int level = scanner.nextInt();
                        System.out.println("choose how many days the authentication will be valid: ");
                        int days = scanner.nextInt();
                        if (days == 0){
                            System.out.println("authentication valid for zero days equals to don't ask authentication");
                            break;
                        }
                        long date = new Date().getTime()+(1000*60*60*24*(long)days);
                        EntitiesManager.getZeroMQReqChannel().send(RequestManager.getInstance().reqAuthentication(userID,level, date));
                        System.out.println("request sent!");
                        String responseKey = EntitiesManager.getZeroMQReqChannel().receive();   //req is used in lock-step --> it doesn't need while loop
                        RequestManager.batchDiscriminator(responseKey);
                        break;
                    case "getAuthorization":
                        if(EntitiesManager.getInstance().getClient().getKey() == null){
                            System.out.println("You need to authenticate first!!");
                            break;
                        }
                        System.out.println("choose the resource (the name of it):");
                        String resourceName = scanner.next();
                        EntitiesManager.getZeroMQReqChannel().send(RequestManager.getInstance().reqAuthorization(EntitiesManager.getInstance().getClient().getKey().getKeyID(),resourceName));
                        System.out.println("request sent!");
                        String responseToken = EntitiesManager.getZeroMQReqChannel().receive();
                        RequestManager.batchDiscriminator(responseToken);
                        if(EntitiesManager.getInstance().getClient().getToken() != null){
                            System.out.println("this is your tokenID: " + EntitiesManager.getInstance().getClient().getToken().getTokenID());
                        }
                        break;
                    case "checkToken":
                        System.out.println("write a tokenID");
                        String tokenID = scanner.next();
                        EntitiesManager.getZeroMQReqChannel().send(RequestManager.getInstance().reqCheckToken(tokenID));
                        System.out.println("request sent!");
                        String responseValidToken = EntitiesManager.getZeroMQReqChannel().receive();
                        RequestManager.batchDiscriminator(responseValidToken);
                        break;
                    case "clearAuth":
                        if (EntitiesManager.getInstance().getClient().getKey() == null){
                            System.out.println("you're not authenticated. You cannot remove something you don't own");
                            break;
                        }
                        System.out.println("are you sure you wanna cancel your authentication? write yes or no: ");
                        String responseClear = scanner.next();
                        switch (responseClear){
                            case "yes":
                                EntitiesManager.getZeroMQReqChannel().send(RequestManager.getInstance().reqClearAuth(EntitiesManager.getInstance().getClient().getKey().getKeyID())); //delete my account and key
                                System.out.println("request sent!");
                                String clearAuth = EntitiesManager.getZeroMQReqChannel().receive();
                                RequestManager.batchDiscriminator(clearAuth);
                                break;
                            case "no":
                                break;
                        }
                        break;
                    case "exit":
                        this.interrupt();
                        break;

                        default:
                            System.out.println("method not found");

                }
                System.out.println("--------------------------");
            }
        }
    }

    /* this thread is a client dealer. We won't use it but this project works with req and dealer in the same way.
     * You can try it deleting the comment in the main method. */
    public  static class ClientDealerThread extends Thread {
        @Override
        public void run() {
            System.out.println("thread clientDealer in esec");

            EntitiesManager.getZeroMQDealerChannel().connect("localhost",(short) 5555);

            System.out.println(RequestManager.getInstance().randomReq("methodExample", null));
            EntitiesManager.getZeroMQDealerChannel().send(RequestManager.getInstance().randomReq("methodExample", null));


            while(!Thread.currentThread().isInterrupted()){
                String response = EntitiesManager.getZeroMQDealerChannel().receive();
                System.out.println(response);
                RequestManager.elaborateResponse(response);
            }

        }
    }

    public  static class ServerThread extends Thread {
        @Override
        public void run() {
            System.out.println("thread server in esec");

            EntitiesManager.getZeroMQRouterChannel().bind("*", (short) 5555);
            while (!Thread.currentThread().isInterrupted()) {
                group_4.Pair<String,String> request = EntitiesManager.getZeroMQRouterChannel().receive();
                EntitiesManager.getZeroMQRouterChannel().reply(request.getKey(),RequestManager.batchDiscriminator(request.getValue()));
            }
        }
    }


}
