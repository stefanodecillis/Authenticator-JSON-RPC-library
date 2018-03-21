package main.entities;


public class Resource {

    private String name;
    private int level;
    private String pathURL;

    public Resource(String name, int level, String pathURL) {
        this.name = name;
        this.level = level;
        this.pathURL = pathURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getPathURL() {
        return pathURL;
    }

    public void setPathURL(String pathURL) {
        this.pathURL = pathURL;
    }
}
