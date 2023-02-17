package com.cabbagegod.cabbagescape.events;

import com.cabbagegod.cabbagescape.callbacks.ReceiveChatMessageCallback;
import com.cabbagegod.cabbagescape.client.LevelUpManager;

public class ChatMessageHandler implements EventHandler {


    @Override
    public void start() {
        ReceiveChatMessageCallback.EVENT.register((String message) -> {
            LevelUpManager.receiveMessage(message);

            return null;
        });
    }
}
