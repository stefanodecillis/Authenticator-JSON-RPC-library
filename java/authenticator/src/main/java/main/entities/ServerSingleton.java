package main.entities;

import main.interfaces.Auth;
import main.Tools;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServerSingleton implements Auth {

    private static ServerSingleton instance = null;

    /* list of resources kept in memory */
    private List<Resource> resourcesList = new ArrayList<Resource>();
    private List<User> usersList = new ArrayList<User>();
    private List<Token> tokensList = new ArrayList<Token>();


    protected ServerSingleton() {

    }

    public static ServerSingleton getInstance() {
        if(instance == null) {
            instance = new ServerSingleton();
        }
        return instance;
    }
    @Override
    public void addRes(Resource res){
        this.resourcesList.add(res);
    }

    @Override
    public void deleteRes(Resource res){
        this.resourcesList.remove(res);
    }

    @Override
    public List<Resource> getListRes() {
        return resourcesList;
    }

    @Override
    public Key getAuthentication(String userID, Date deadline, int lvl) {

        Key key = createKey(userID, lvl, deadline);

        //update list
        User user = new User(userID,key,lvl);
        this.usersList.add(user);

        return key;
    }

    @Override
    public Token getAuthorization(Key key, String resourceName) {

        Resource res = null;
        Boolean found = false;
        User user = null;

        for(int i = 0; i < this.resourcesList.size() ;i++) {
            if (resourceName.equalsIgnoreCase(this.resourcesList.get(i).getName())){
                found = true;
                res = this.resourcesList.get(i);
                break;
            }
        }

        if (!found) {                                                    //resource not found
            return null;
        } else {
            for (int i = 0; i < usersList.size(); i++) {                          //Key check
                if (usersList.get(i).getKey().equals(key)) {
                    user = usersList.get(i);
                    break;  //Key found
                }
            }
            if (user != null && res != null && user.getLevel() >= res.getLevel()){

                Token token = createToken(res, user);
                return token;

            } else {
                return null;
            }
        }
    }

    @Override
    public long checkToken(Token token) {
        for (int i = 0; i < tokensList.size(); i++) {

            if (tokensList.get(i).equals(token)) {

                long remainTime = 0;

                if (tokensList.get(i).isValid()) {

                    Date date = new Date();
                    remainTime= tokensList.get(i).getDeadline().getTime()-date.getTime();
                }
                return remainTime;  //found token
            }
        }
        return 0;
    }

    @Override
    public void clearAuth(int i) {
        this.getUsersList().remove(i);
    }

    private Key createKey(String userID, int level, Date deadline) {

        Key key = new Key(Tools.keyIDCreator(20), level, deadline.getTime());  //create key
        User user = new User(userID, key, level);                                     //create user
        usersList.add(user);                                                          //update list
        return key;
    }

    private Token createToken(Resource res, User user) {

        Token token = new Token(Tools.keyIDCreator(10), user.getUserID() , res.getName());  //create token
        tokensList.add(token);                                             //update list
        for (int i = 0; i < this.usersList.size(); i++) {
            if (this.usersList.get(i).equals(user)){
                this.usersList.get(i).getAccessList().add(token);
                return token;
            }
        }
        return token;
    }

    public Boolean checkResource(String resourceName) {
        for(int i = 0; i < this.resourcesList.size() ;i++){
            if (resourceName.equalsIgnoreCase(this.resourcesList.get(i).getName())){
                return true;
            }
        }
        return false;
    }

    public List<User> getUsersList() {
        return usersList;
    }

    public List<Token> getTokensList() {
        return tokensList;
    }
}
