package com.cabbagegod.cabbagescape.client.barrows;

import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.Range;

public class Bounds {
    public Range<Integer> xRange;
    public Range<Integer> yRange;
    public Range<Integer> zRange;

    public Bounds(int xMin, int xMax, int yMin, int yMax, int zMin, int zMax){
        setBounds(xMin, xMax, yMin, yMax, zMin, zMax);
    }

    public Bounds(Vec3d min, Vec3d max){
        setBounds((int) Math.floor(min.x), (int) Math.floor(max.x), (int) Math.floor(min.y), (int) Math.floor(max.y), (int) Math.floor(min.z), (int) Math.floor(max.z));
    }

    private void setBounds(int xMin, int xMax, int yMin, int yMax, int zMin, int zMax){
        xRange = Range.between(xMin, xMax);
        yRange = Range.between(yMin, yMax);
        zRange = Range.between(zMin, zMax);
    }

    public boolean isWithinBounds(Vec3d pos){
        if(xRange.contains((int) Math.floor(pos.x))){
            if(yRange.contains((int) Math.floor(pos.y))){
                if(zRange.contains((int) Math.floor(pos.z))){
                    return true;
                }
            }
        }

        return false;
    }
}
