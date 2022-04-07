package com.cabbagegod.cabbagescape.client.potiontimers;

public class PotionInstance {
    public long startTime;
    public int length;
    public String name;
    public String resourceLocation;

    public PotionInstance(long startTime, int length, String name, String resourceLocation){
        this.startTime = startTime;
        this.length = length;
        this.name = name;
        this.resourceLocation = resourceLocation;
    }
}
