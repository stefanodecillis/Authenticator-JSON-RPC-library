package main.entities;

import java.util.Date;

public class Key {

    private String keyID;
    private int level;
    private Date deadline;

    public Key(String keyID, int level, long date) {
        this.keyID = keyID;
        this.level = level;
        this.deadline = new Date(date);
    }

    public String getKeyID() {
        return keyID;
    }

    public int getLevel() {
        return level;
    }

    public Date getDeadline() {
        return deadline;
    }
    public long getDeadlineLong() { return deadline.getTime(); }
}
