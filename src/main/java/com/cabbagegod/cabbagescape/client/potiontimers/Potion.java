package com.cabbagegod.cabbagescape.client.potiontimers;

public class Potion {
    private final int lengthSeconds;
    private final String literalName;
    private final String resourcePath;

    Potion(int lengthSeconds, String literalName, String resourcePath){
        this.lengthSeconds = lengthSeconds;
        this.literalName = literalName;
        this.resourcePath = resourcePath;
    }

    Potion(int lengthSeconds, String literalName){
        this.lengthSeconds = lengthSeconds;
        this.literalName = literalName;
        this.resourcePath = "";
    }

    public String getLiteralName(){
        return literalName;
    }

    public int getLength(){
        return lengthSeconds;
    }

    public String getResourcePath() { return resourcePath; };
}
