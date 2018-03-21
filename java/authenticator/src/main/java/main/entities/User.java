package main.entities;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String userID;
    private Key userKey;
    private int level;
    private List<Token> accessList;

    public User(String userID, Key userKey, int level) {
        this.userID = userID;
        this.userKey = userKey;
        this.level = level;
        this.accessList = new ArrayList<Token>();  //create empty list
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Key getKey() {
        return userKey;
    }

    public void setKey(Key key) {
        this.userKey = key;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<Token> getAccessList() {
        return accessList;
    }

    public void setAccessList(List<Token> accessList) {
        this.accessList = accessList;
    }
}
