package com.cabbagegod.cabbagescape.data.itemdata;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

//Class used to read raw JSON from NBT data
public class ItemDisplay {
    //Each string of lore list should be treated as it's own list of ItemLore
    //even though it seems to only have 1 element per line
    //I have no clue why... NBT creator is just evil I guess
    @SerializedName("Lore")
    private List<String> lore;
    @SerializedName("Name")
    public String name;

    public List<ItemLore> getLore(){
        List<ItemLore> lore = new ArrayList<ItemLore>();
        Gson gson = new Gson();

        for(String string : this.lore){
            lore.add(gson.fromJson(string, ItemLore.class));
        }

        return lore;
    }
}
