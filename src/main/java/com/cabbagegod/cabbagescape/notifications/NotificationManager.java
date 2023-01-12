package com.cabbagegod.cabbagescape.notifications;

import com.cabbagegod.cabbagescape.data.NotificationSettings;
import com.cabbagegod.cabbagescape.util.ThreadingUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.SoundEvents;

public class NotificationManager {
    public static boolean showFlash = false;
    public static String currentNotif = "";

    NotificationSettings settings;

    public NotificationManager(NotificationSettings settings){
        this.settings = settings;
    }

    public void sendNotification(String text){
        if(MinecraftClient.getInstance().player == null)
            return;

        ClientPlayerEntity player = MinecraftClient.getInstance().player;

        if(settings.soundEnabled){
            player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, settings.volume, 1);
        }
        if(settings.flashEnabled){
            showFlash = true;
            currentNotif = text;
            new Thread(() -> {
                ThreadingUtil.wait(2000);
                showFlash = false;
            }).start();
        }
    }
}
