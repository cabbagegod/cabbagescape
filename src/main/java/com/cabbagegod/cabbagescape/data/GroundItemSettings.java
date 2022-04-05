package com.cabbagegod.cabbagescape.data;

import org.checkerframework.checker.units.qual.C;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GroundItemSettings {
    public List<String> searchTags = new ArrayList<String>();
    public List<String> containsTags = new ArrayList<String>();

    public float volume = .05f;
    public double itemRed = 151;
    public double itemGreen = 0;
    public double itemBlue = 251;
    public double particleCount = 50;

    public boolean spiralEnabled = true;
    public boolean blockHighlightEnabled = false;

    public GroundItemSettings(){

    }
}
