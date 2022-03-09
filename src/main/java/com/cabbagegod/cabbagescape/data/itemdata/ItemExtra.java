package com.cabbagegod.cabbagescape.data.itemdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItemExtra {
    @SerializedName("extra")
    @Expose
    private List<ItemExtra__1> extra = null;
    @SerializedName("text")
    @Expose
    private String text;

    public List<ItemExtra__1> getExtra() {
        return extra;
    }

    public void setExtra(List<ItemExtra__1> extra) {
        this.extra = extra;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
