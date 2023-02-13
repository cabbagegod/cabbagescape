package com.cabbagegod.cabbagescape.notifications;

import com.cabbagegod.cabbagescape.data.NotificationSettings;
import com.cabbagegod.cabbagescape.util.ThreadingUtil;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;

public class NotificationManager {
    public static boolean showFlash = false;
    public static String currentNotif = "";
    private Thread currentThread = null;


    NotificationSettings settings;

    public NotificationManager(NotificationSettings settings){
        this.settings = settings;
        HudRenderCallback.EVENT.register(NotificationManager::onHudRender);
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

            if(currentThread != null)
                currentThread.interrupt();

            currentThread = new Thread() {
                public void run() {
                    for (int i = 0; i < 20; i++) {
                        ThreadingUtil.wait(100);

                        if (this.isInterrupted())
                            return;
                    }
                    showFlash = false;
                }
            };

            currentThread.start();
        }
    }

    public static void onHudRender(MatrixStack matrices, float t){
        if(showFlash) {
            int width = MinecraftClient.getInstance().getWindow().getWidth();
            int height = MinecraftClient.getInstance().getWindow().getHeight();

            //The color picker texture
            //Texture transparency for some reason is acting all funny :(
            //drawTexture(matrices, 0, 0, 0, 0, this.scaledWidth - 150, this.scaledHeight - 150);
            DrawableHelper.drawCenteredText(matrices, MinecraftClient.getInstance().textRenderer, new LiteralText(NotificationManager.currentNotif), width / 2, height / 2, -1);
        }
    }
}
