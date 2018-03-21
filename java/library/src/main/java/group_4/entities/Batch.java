package group_4.entities;

import java.util.ArrayList;

public class Batch {
    private ArrayList<String> batchJsonObject;


    public ArrayList<String> getBatchJsonObject() {
        return batchJsonObject;
    }

    public Batch(){
        this.batchJsonObject = new ArrayList<String>();
    }

}
