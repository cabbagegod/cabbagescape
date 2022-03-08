package com.cabbagegod.cabbagescape.client;

import com.cabbagegod.cabbagescape.client.cluetracker.ClueTracker;

//Used to register all events to remove clutter from CabbageScapeClient
public class EventRegisterer {
    public static ClueTracker clueTracker = new ClueTracker();

    public static void register(){
        clueTracker.register();
    }
}
