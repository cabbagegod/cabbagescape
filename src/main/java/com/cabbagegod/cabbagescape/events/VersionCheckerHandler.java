package com.cabbagegod.cabbagescape.events;

import com.cabbagegod.cabbagescape.client.VersionChecker;
import com.cabbagegod.cabbagescape.ui.UpdateScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class VersionCheckerHandler implements EventHandler{
    @Override
    public void start() {
        VersionChecker.Verify();

        ClientPlayConnectionEvents.JOIN.register(((handler, sender, client) -> {
            if(VersionChecker.shouldShowUpdate())
                client.setScreen(new UpdateScreen());
        }));
    }
}
