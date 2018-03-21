package group_4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Elem {

    private Object primitive = null;
    private ArrayList<Elem> elemArrayList = null;
    private Map<String, Elem> elemMap = null;

    public Elem(){

    }

    public Elem(String primitive) {
        this.primitive = primitive;
        elemArrayList = null;
        elemMap = null;
    }

    public Elem(Number primitive) {
        this.primitive = primitive;
        elemArrayList = null;
        elemMap = null;
    }

    public Elem(Boolean primitive) {
        this.primitive = primitive;
        elemArrayList = null;
        elemMap = null;
    }

    public Elem(Elem elem) {
        add(elem);
    }

    public Elem(String key, Elem elem) {
        add(key, elem);
    }

    public void add(Elem element) {
        if (elemArrayList==null) {
            elemArrayList = new ArrayList<Elem>();
            elemMap = null;
            primitive = null;
        }

        elemArrayList.add(element);
    }

    public void add(String key, Elem element) {
        if (elemMap==null) {
            elemMap = new HashMap<String, Elem>();
            elemArrayList = null;
            primitive = null;
        }

        elemMap.put(key, element);
    }

    public String getString() {
        if (this.primitive != null){
            return (String)primitive;
        } else {
            return null;
        }
    }

    public Number getNumber() {
        if (this.primitive != null){
            if (this.primitive.getClass().equals(String.class)){    //super-check
                return Integer.getInteger(getString());
            } else
            return (Number) primitive;
        } else {
            return null;
        }
    }

    public Boolean getBoolean() {
        if (this.primitive != null){
            return (Boolean) primitive;
        } else {
            return null;
        }
    }


    public Elem getElemByKey(String key) {
        if (elemMap == null) {
            return null;
        }
        return elemMap.get(key);
    }

    public Elem getElemAt(int index) {
        if (elemArrayList == null) {
            return null;
        }
        return elemArrayList.get(index);
    }


    //check if this elem is an array, maps or primitive obj
    public boolean isMap(){
        return elemMap != null;
    }

    public boolean isArray(){
        return elemArrayList != null;
    }

    public int size(){
        return this.elemArrayList.size();
    }

    public Set<String> getSetKey(){
        return this.elemMap.keySet();
    }


    /* override the original method to check if this class stores recursively the elements */
    @Override
    public String toString() {

        String result =  "Elem{";

        if (primitive!=null) {
            result+="primitive=" + primitive;
        }

        if (elemArrayList!=null) {
            result+=", elemArrayList=" + elemArrayList;
        }

        if (elemMap!=null) {
            result+=", elemMap=" + elemMap;
        }

        result+='}';

        return result;
    }

    public void setString (String string){
        this.primitive = string;
    }
}
