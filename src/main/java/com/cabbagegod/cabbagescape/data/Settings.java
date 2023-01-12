package com.cabbagegod.cabbagescape.data;

import com.cabbagegod.cabbagescape.client.CabbageScapeClient;

public class Settings {
    public String lastVersion = CabbageScapeClient.version;
    public GroundItemSettings groundItemSettings = new GroundItemSettings();
    public NotificationSettings notificationSettings = new NotificationSettings();

    public Settings(){

    }

}
