package com.cabbagegod.cabbagescape.data.itemdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemExtra__1 {
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("text")
    @Expose
    private String text;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
