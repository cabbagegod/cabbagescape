package com.cabbagegod.cabbagescape.client.potiontimers;

public class Potion {
    private int lengthSeconds;
    private String literalName;

    Potion(int lengthSeconds, String literalName){
        this.lengthSeconds = lengthSeconds;
        this.literalName = literalName;
    }

    public String getLiteralName(){
        return literalName;
    }

    public int getLength(){
        return lengthSeconds;
    }
}
