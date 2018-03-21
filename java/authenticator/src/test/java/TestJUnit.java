
import com.google.gson.Gson;
import group_4.Utils;
import group_4.entities.Request;
import main.entities.Key;
import main.entities.Resource;
import main.handlers.EntitiesManager;
import main.handlers.RequestManager;
import org.junit.Test;

import java.util.Date;

public class TestJUnit {

    @Test
    public void jsonHandlerTest() {

        String[] jsonStrings = {"{\"jsonrpc\": \"2.0\", \"method\": \"[\"hello\"]\", \"params\": [42, 23], \"id\": 1}",
                "{\"jsonrpc\": \"2.0\", \"ciao\": 19, \"id\": 1}",
                "{\"jsonrpc\": \"2.0\", \"method\": \"subtract\", \"params\": 23, \"id\": 2}",
                "{\"jsonrpc\": \"2.0\", \"result\": -19, \"id\": 2}",
                "{\"jsonrpc\": \"2.0\", \"method\": \"subtract\", \"params\": {\"subtrahend\": 23, \"minuend\": 42}, \"id\": 3}",
                "{\"jsonrpc\": \"2.0\", \"result\": 19, \"id\": 3}",
                "{\"jsonrpc\": \"2.0\", \"method\": \"subtract\", \"params\": {\"minuend\": 42, \"subtrahend\": 23}, \"id\": 4}",
                "{\"jsonrpc\": \"2.0\", \"result\": 19, \"id\": 4}",
                "{\"jsonrpc\": \"2.0\", \"method\": \"update\", \"params\": [1,2,3,4,5]}",
                "{\"jsonrpc\": \"2.0\", \"method\": \"foobar\"}",
                "{\"jsonrpc\": \"2.0\", \"method\": \"foobar\", \"id\": \"1\"}",
                "{\"jsonrpc\": \"2.0\", \"error\": {\"code\": -32601, \"message\": \"Method not found\"}, \"id\": \"1\"}",
                "{\"jsonrpc\": \"2.0\", \"method\": \"foobar, \"params\": \"bar\", \"baz]",
                "{\"jsonrpc\": \"2.0\", \"error\": {\"code\": -32700, \"message\": \"Parse error\"}, \"id\": null}",
                "{\"jsonrpc\": \"2.0\", \"method\": 1, \"params\": \"bar\"}",
                "{\"jsonrpc\": \"2.0\", \"error\": {\"code\": -32600, \"message\": \"Invalid Request\"}, \"id\": null}",
                "[\n" +
                        "  {\"jsonrpc\": \"2.0\", \"method\": \"sum\", \"params\": [1,2,4], \"id\": \"1\"},\n" +
                        "  {\"jsonrpc\": \"2.0\", \"method\"\n" +
                        "]",
                "{\"jsonrpc\": \"2.0\", \"error\": {\"code\": -32700, \"message\": \"Parse error\"}, \"id\": null}",
                "[]",
                "{\"jsonrpc\": \"2.0\", \"error\": {\"code\": -32600, \"message\": \"Invalid Request\"}, \"id\": null}",
                "[1]",
                "[\n" +
                        "  {\"jsonrpc\": \"2.0\", \"error\": {\"code\": -32600, \"message\": \"Invalid Request\"}, \"id\": null}\n" +
                        "]",
                "[1,2,3]",
                "[\n" +
                        "  {\"jsonrpc\": \"2.0\", \"error\": {\"code\": -32600, \"message\": \"Invalid Request\"}, \"id\": null},\n" +
                        "  {\"jsonrpc\": \"2.0\", \"error\": {\"code\": -32600, \"message\": \"Invalid Request\"}, \"id\": null},\n" +
                        "  {\"jsonrpc\": \"2.0\", \"error\": {\"code\": -32600, \"message\": \"Invalid Request\"}, \"id\": null}\n" +
                        "]",
                "[\n" +
                        "        {\"jsonrpc\": \"2.0\", \"method\": \"sum\", \"params\": [1,2,4], \"id\": \"1\"},\n" +
                        "        {\"jsonrpc\": \"2.0\", \"method\": \"notify_hello\", \"params\": [7]},\n" +
                        "        {\"jsonrpc\": \"2.0\", \"method\": \"subtract\", \"params\": [42,23], \"id\": \"2\"},\n" +
                        "        {\"foo\": \"boo\"},\n" +
                        "        {\"jsonrpc\": \"2.0\", \"method\": \"foo.get\", \"params\": {\"name\": \"myself\"}, \"id\": \"5\"},\n" +
                        "        {\"jsonrpc\": \"2.0\", \"method\": \"get_data\", \"id\": \"9\"} \n" +
                "    ]",
        "[\n" +
                "        {\"jsonrpc\": \"2.0\", \"result\": 7, \"id\": \"1\"},\n" +
                "        {\"jsonrpc\": \"2.0\", \"result\": 19, \"id\": \"2\"},\n" +
                "        {\"jsonrpc\": \"2.0\", \"error\": {\"code\": -32600, \"message\": \"Invalid Request\"}, \"id\": null},\n" +
                "        {\"jsonrpc\": \"2.0\", \"error\": {\"code\": -32601, \"message\": \"Method not found\"}, \"id\": \"5\"},\n" +
                "        {\"jsonrpc\": \"2.0\", \"result\": [\"hello\", 5], \"id\": \"9\"}\n" +
                "    ]",
        "[\n" +
                "        {\"jsonrpc\": \"2.0\", \"method\": \"notify_sum\", \"params\": [1,2,4]},\n" +
                "        {\"jsonrpc\": \"2.0\", \"method\": \"notify_hello\", \"params\": [7]}\n" +
                "    ]"};


        String[] clientMessageSet = new String[] {

                //no params
                "{\"jsonrpc\": \"2.0\", \"method\": \"test\", \"id\": 1}",

                //with positional parameters
                "{\"jsonrpc\": \"2.0\", \"method\": \"subtract\", \"params\": [42, 23], \"id\": 1}",
                "{\"jsonrpc\": \"2.0\", \"method\": \"subtract\", \"params\": [23, 42], \"id\": 2}",

                //with named parameters
                "{\"jsonrpc\": \"2.0\", \"method\": \"subtract\", \"params\": {\"subtrahend\": 23, \"minuend\": 42}, \"id\": 3}",
                "{\"jsonrpc\": \"2.0\", \"method\": \"subtract\", \"params\": {\"minuend\": 42, \"subtrahend\": 23}, \"id\": 4}",

                //notification
                "{\"jsonrpc\": \"2.0\", \"method\": \"update\", \"params\": [1,2,3,4,5]}",

                //invalid json
                //"{\"jsonrpc\": \"2.0\", \"method\": \"foobar, \"params\": \"bar\", \"baz]",

                //invalid params (primitive)
                "{\"jsonrpc\": \"2.0\", \"method\": \"subtract\", \"params\": 10, \"id\": 1}",

                //Invalid id, with fractional parts
                "{\"jsonrpc\": \"2.0\", \"method\": \"subtract\", \"params\": [10], \"id\": 1.5}",

                //null id
                "{\"jsonrpc\": \"2.0\", \"method\": \"subtract\", \"params\": [10], \"id\": null}",

                //http://kodi.wiki/view/JSON-RPC_API/Examples (Nested, difficult to parse)
                "{ \"jsonrpc\": \"2.0\", \"method\": \"JSONRPC.Introspect\", \"params\": { \"filter\": { \"id\": \"AudioLibrary.GetAlbums\", \"type\": \"method\" } }, \"id\": 1 }",
                "{\"jsonrpc\": \"2.0\", \"method\": \"Player.GetItem\", \"params\": { \"properties\": [\"title\", \"album\", \"artist\", \"season\", \"episode\", \"duration\", \"showtitle\", \"tvshowid\", \"thumbnail\", \"file\", \"fanart\", \"streamdetails\"], \"playerid\": 1 }, \"id\": \"VideoGetItem\"}",

                "{\"jsonrpc\": \"2.0\", \"method\": \"VideoLibrary.GetTVShows\", \"params\": { \"filter\": {\"field\": \"playcount\", \"operator\": \"is\", \"value\": \"0\"}, \"limits\": { \"start\" : 0, \"end\": 75 }, \"properties\": [\"art\", \"genre\", \"plot\", \"title\", \"originaltitle\", \"year\", \"rating\", \"thumbnail\", \"playcount\", \"file\", \"fanart\"], \"sort\": { \"order\": \"ascending\", \"method\": \"label\" } }, \"id\": \"libTvShows\"}",
                "{\"jsonrpc\": \"2.0\", \"method\": \"VideoLibrary.GetMusicVideos\", \"params\": { \"properties\": [ \"title\", \"thumbnail\", \"artist\", \"album\", \"genre\", \"lastplayed\", \"year\", \"runtime\", \"fanart\", \"file\", \"streamdetails\" ], \"sort\": { \"order\": \"ascending\", \"method\": \"artist\", \"ignorearticle\": true } }, \"id\": \"libMusicVideos\"}"

        };


        String[] serverMessageSet = new String[] {
                //with positional parameters
                "{\"jsonrpc\": \"2.0\", \"result\": 19, \"id\": 1}",
                "{\"jsonrpc\": \"2.0\", \"result\": -19, \"id\": 2}",

                //with named parameters
                "{\"jsonrpc\": \"2.0\", \"result\": 19, \"id\": 3}",
                "{\"jsonrpc\": \"2.0\", \"result\": 19, \"id\": 4}",

                //non-existent method
                "{\"jsonrpc\": \"2.0\", \"error\": {\"code\": -32601, \"message\": \"Method not found\"}, \"id\": \"1\"}",
                "{\"jsonrpc\": \"2.0\", \"error\": {\"code\": -32700, \"message\": \"Parse error\"}, \"id\": null}",

                //Invalid id, with fractional parts
                "{\"jsonrpc\": \"2.0\", \"result\": 19, \"id\": 4.345683}",

                "{\"id\": 1, \"jsonrpc\": \"2.0\", \"result\": [ { \"playerid\": 0, \"type\": \"audio\" } ]}"
        };

        //json taken from jsonrpc website

        String json = "{\"jsonrpc\": \"2.0\", \"method\": \"subtract\", \"params\": [42, 23], \"id\": 1}";


        System.out.println(" *********** START JSONRPC TEST JUNIT *********** ");
         /*
         now we start the test
          */
        System.out.println(RequestManager.batchDiscriminator(json));

        Gson gson = Utils.getGson();

        for (int i = 0; i < jsonStrings.length; i++){
            System.out.println("-----------------------NEXT JSONRPC ----------------------");
            System.out.println(jsonStrings[i]);
            System.out.println(RequestManager.batchDiscriminator(jsonStrings[i]));
        }

        System.out.println(" ******************* CLIENT MSG SET START ******************* ");

        for (int i = 0; i < clientMessageSet.length; i++){
            System.out.println("-----------------------NEXT JSONRPC ----------------------");
            System.out.println(clientMessageSet[i]);
            System.out.println(RequestManager.batchDiscriminator(clientMessageSet[i]));
        }

        System.out.println(" ******************* SERVER MSG SET START ******************* ");

        for (int i = 0; i < serverMessageSet.length; i++){
            System.out.println("-----------------------NEXT JSONRPC ----------------------");
            System.out.println(serverMessageSet[i]);
            System.out.println(RequestManager.batchDiscriminator(serverMessageSet[i]));
        }

    }

    /** this test is made to debug and test a single json string at time */
    @Test
    public void JsonStringTest() {
        System.out.println("             ******************* START JSON TEST JUNIT *******************");

        String json = "{\"jsonrpc\": \"2.0\", \"method\": \"addRes\", \"params\": {\"name\": \"exampleRes\", \"level\": 3, \"path\": \"blabla.com\"}}";

        System.out.println(json);
        System.out.println(RequestManager.batchDiscriminator(json));
    }

    @Test
    public void AuthenticatorTest() {
        System.out.println("             ******************* START AUTH TEST JUNIT *******************");

        String[] jsonReq = {
                "{\"jsonrpc\": \"2.0\", \"method\": \"getList\", \"id\": 5}",
                "{\"jsonrpc\": \"2.0\", \"method\": \"getAuthentication\", \"params\": {\"userID\": \"step96\", \"deadline\": 1000, \"lvl\": 3}, \"id\": 3}",
                "{\"jsonrpc\": \"2.0\", \"method\": \"getAuthorization\", \"params\": {\"keyID\": \"paolo96\", \"resource\": \"calcolatrice\"}, \"id\": 4}\n",

                "{\"jsonrpc\": \"2.0\", \"method\": \"checkToken\", \"params\": {\"tokenID\": \"1010djjdd3\"}, \"id\": 6}",
                "{\"jsonrpc\": \"2.0\", \"method\": \"clearAuth\", \"params\": {\"keyId\": \"something\"}, \"id\": 6}"};

        for (int i = 0; i < jsonReq.length; i++){
            System.out.println("-----------------------NEXT JSONRPC ----------------------");
            System.out.println(jsonReq[i]);
            System.out.println(RequestManager.batchDiscriminator(jsonReq[i]));
        }

    }

    @Test
    public void ServerTest() {
        System.out.println("             ******************* START SERVER TEST JUNIT *******************");


        boolean resourceFound = false;
        boolean keyCreated = false;
        boolean tokenCreated = false;
        boolean resourceDeleted = false;
        boolean keyDeleted = false;

        Key key = null;

        //resource
        Resource res1 = new Resource("calculator", 2, "www.calculator.com");
        Resource res2 = new Resource("akinator", 1, "www.akinator.to");
        Resource res3 = new Resource("dev-tools", 5, "www.dev-tools.ie");
        Resource res4 = new Resource("search-engine", 3, "www.google.uk");
        Resource res5 = new Resource("comoCity", 4, "www.comocity.it");
        Resource res6 = new Resource("Trani", 3, "www.traniviva.net");

        System.out.println("-------------- [1] addResource func ---------------");
        EntitiesManager.getInstance().getServer().addRes(res6);

        for(int i = 0;  i < EntitiesManager.getInstance().getServer().getListRes().size(); i++){
            if (EntitiesManager.getInstance().getServer().getListRes().get(i) == res6){
                resourceFound = true;
            }
        }

        if (!resourceFound)
            System.exit(-1);
        else
            System.out.println("PASSED");

        System.out.println("-------------- [2] getAuthentication func ---------------");

        EntitiesManager.getInstance().getServer().getAuthentication("stefano", new Date(new Date().getTime()+(1000*60*60*24*10)), 4); //valid for ten days

        for(int i = 0;  i < EntitiesManager.getInstance().getServer().getUsersList().size(); i++){
            if (EntitiesManager.getInstance().getServer().getUsersList().get(i).getUserID() == "stefano"){
                if (EntitiesManager.getInstance().getServer().getUsersList().get(i).getKey() != null) {
                    keyCreated = true;
                    key = EntitiesManager.getInstance().getServer().getUsersList().get(i).getKey();
                }
            }
        }

        if (!keyCreated){
            System.exit(-1);
        } else
            System.out.println("PASSED");


        System.out.println("-------------- [3] getAuthentication func ---------------");

        EntitiesManager.getInstance().getServer().getAuthorization(key,"Trani");

        for(int i = 0;  i < EntitiesManager.getInstance().getServer().getUsersList().size(); i++){
            if (EntitiesManager.getInstance().getServer().getUsersList().get(i).getUserID() == "stefano"){
                for(int j = 0; j < EntitiesManager.getInstance().getServer().getUsersList().get(i).getAccessList().size(); j++){
                    if (EntitiesManager.getInstance().getServer().getUsersList().get(i).getAccessList().get(j).getResourceID() == "Trani"){
                        tokenCreated = true;
                    }
                }
            }
        }

        if (!tokenCreated){
            System.exit(-1);
        } else
            System.out.println("PASSED");


        System.out.println("-------------- [4] delete Resource func ---------------");

        EntitiesManager.getInstance().getServer().deleteRes(res1);

        for(int i = 0;  i < EntitiesManager.getInstance().getServer().getListRes().size(); i++){
            if (EntitiesManager.getInstance().getServer().getListRes().get(i).getName().equalsIgnoreCase("calculator")){
               resourceDeleted = true;
            }
        }

        if (!resourceDeleted){
            System.exit(-1);
        } else
            System.out.println("PASSED");

        System.out.println("-------------- [5] delete Token func ---------------");


        for(int i = 0;  i < EntitiesManager.getInstance().getServer().getUsersList().size(); i++){
            if (EntitiesManager.getInstance().getServer().getUsersList().get(i).getKey().equals(key)){
                EntitiesManager.getInstance().getServer().clearAuth(i);
                keyDeleted = true;
            }
        }

        if (!keyDeleted){
            System.exit(-1);
        } else
            System.out.println("PASSED");
    }
}
