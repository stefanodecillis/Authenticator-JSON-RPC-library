package main.entities;

import java.util.Date;

public class Token {

    private String tokenID;
    private Date deadline;
    private String userID; //owner
    private String resourceID;


    //server-side
    public Token(String tokenID, String userID, String resourceID) {
        this.tokenID = tokenID;
        Date date = new Date();
        long item = date.getTime();
        item+=(60*60*24*1000);
        date.setTime(item);
        this.deadline = date;
        this.userID= userID;
        this.resourceID = resourceID;
    }


    //client-side
    public Token(String tokenID, String userID, String resourceID, long deadline) {
        this.tokenID = tokenID;
        this.deadline = new Date(deadline);
        this.userID = userID;
        this.resourceID = resourceID;
    }

    public String getTokenID() {
        return tokenID;
    }

    public Date getDeadline() {
        return deadline;
    }

    public String getUserID() {
        return userID;
    }

    public String getResourceID() {
        return resourceID;
    }

    public Boolean isValid() {
        Date date = new Date();
        if (this.deadline.after(date)){
            return true;
        } else {
            return false;
        }
    }
}
