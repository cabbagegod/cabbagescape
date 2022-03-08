package com.cabbagegod.cabbagescape.client.cluetracker;

import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.client.util.math.Vector3d;

public class ClueStep {
    public enum Difficulty {
        beginner,
        easy,
        medium,
        hard,
        elite,
        master
    }

    private int id;
    private Difficulty difficulty;
    private String info;
    private int x = 0;
    private int y = 0;
    private int z = 0;

    public ClueStep(int id, Difficulty difficulty, String info){
        this.id = id;
        this.difficulty = difficulty;
        this.info = info;
    }

    public ClueStep setCoords(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;

        return this;
    }

    public Vector3d getCoords(){
        return new Vector3d(x,y,z);
    }

    public String getInfo(){
        return this.info;
    }

    public int getId(){
        return id;
    }
}
