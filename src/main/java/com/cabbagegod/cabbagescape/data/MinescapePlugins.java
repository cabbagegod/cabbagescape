package com.cabbagegod.cabbagescape.data;

import com.cabbagegod.cabbagescape.ui.settings.BarrowsHelperScreen;
import com.cabbagegod.cabbagescape.ui.settings.GroundItemsScreen;
import com.cabbagegod.cabbagescape.ui.settings.NotificationSettingsScreen;

public enum MinescapePlugins {
    GROUNDITEMS("Ground Items", GroundItemsScreen.class),
    NOTIFICATIONS("Notifications", NotificationSettingsScreen.class),
    BARROWS_HELPER("Barrows Helper", BarrowsHelperScreen.class);

    public String name;
    public Class screen;
    MinescapePlugins(String name, Class settingsScreen){
        this.name = name;
        screen = settingsScreen;
    }
}
