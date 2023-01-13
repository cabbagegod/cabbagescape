package com.cabbagegod.cabbagescape.data;

import com.cabbagegod.cabbagescape.ui.settings.GroundItemsScreen;

public enum MinescapePlugins {
    GROUNDITEMS("Ground Items", GroundItemsScreen.class);

    public String name;
    public Class screen;
    MinescapePlugins(String name, Class settingsScreen){
        this.name = name;
        screen = settingsScreen;
    }
}
