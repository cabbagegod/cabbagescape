package com.cabbagegod.cabbagescape.data;

import com.cabbagegod.cabbagescape.client.CabbageScapeClient;
import com.cabbagegod.cabbagescape.data.itemdata.BarrowsHelperSettings;

public class Settings {
    public String lastVersion = CabbageScapeClient.version;
    public GroundItemSettings groundItemSettings = new GroundItemSettings();
    public NotificationSettings notificationSettings = new NotificationSettings();
    public BarrowsHelperSettings barrowsHelperSettings = new BarrowsHelperSettings();

    public Settings(){

    }

}
