package com.cabbagegod.cabbagescape.events;

import com.cabbagegod.cabbagescape.client.cluetracker.ClueTracker;

public class ClueTrackerHandler implements EventHandler {

    @Override
    public void start() {
        new ClueTracker().register();
    }

}
