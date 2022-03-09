package com.cabbagegod.cabbagescape.data.itemdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

//Class used to read raw JSON from NBT data
public class ItemLore {
    @SerializedName("italic")
    @Expose
    private Boolean italic;
    @SerializedName("extra")
    @Expose
    private List<ItemExtra> extra = null;
    @SerializedName("text")
    @Expose
    private String text;

    public Boolean getItalic() {
        return italic;
    }

    public void setItalic(Boolean italic) {
        this.italic = italic;
    }

    public List<ItemExtra> getExtra() {
        return extra;
    }

    public void setExtra(List<ItemExtra> extra) {
        this.extra = extra;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
