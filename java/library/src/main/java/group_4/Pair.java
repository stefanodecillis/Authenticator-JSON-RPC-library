package group_4;

public class Pair<K,V> {
    private K key;
    private V value;

    public Pair (K key, V value){
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}

/** Pair of Java was not standard and we cannot create the jar. We create another one! */