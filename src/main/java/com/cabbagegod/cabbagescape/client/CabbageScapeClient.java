package com.cabbagegod.cabbagescape.client;

import com.cabbagegod.cabbagescape.client.barrows.BarrowsHelper;
import com.cabbagegod.cabbagescape.client.blockoutline.BlockOutlineManager;
import com.cabbagegod.cabbagescape.client.blockoutline.PersistentOutlineRenderer;
import com.cabbagegod.cabbagescape.client.grounditems.GroundItemsManager;
import com.cabbagegod.cabbagescape.commands.Commands;
import com.cabbagegod.cabbagescape.data.DataHandler;
import com.cabbagegod.cabbagescape.data.DelayedScreenshot;
import com.cabbagegod.cabbagescape.data.Settings;
import com.cabbagegod.cabbagescape.notifications.NotificationManager;
import com.cabbagegod.cabbagescape.ui.UpdateScreen;
import com.cabbagegod.cabbagescape.util.FileUtil;
import com.cabbagegod.cabbagescape.util.ScreenshotUtil;
import com.google.gson.Gson;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CabbageScapeClient implements ClientModInitializer {
    public static String settingsDir = "settings";
    public static String version = "a1.4+1.18.2";
    public static Settings settings;

    public static NotificationManager notificationManager;

    //Keybinds - F to pay respects
    private static KeyBinding debugKey;

    @Override
    public void onInitializeClient() {
        debugKey = KeybindHandler.createKeybind("debug_key", "category.cabbagescape", 0);

        loadSettings();
        VersionChecker.Verify();

        notificationManager = new NotificationManager(settings.notificationSettings);

        Commands.register();
        setupEvents();
        EventRegisterer.register();
        GroundItemsManager.register();
        BarrowsHelper.register();

        //PotionTimerManager potionTimerManager = PotionTimerManager.getInstance();

        BlockOutlineManager.getInstance().add(PersistentOutlineRenderer.getInstance());
    }

    //Loads json file as settings
    private void loadSettings(){
        String settingsJson = DataHandler.ReadStringFromFile(settingsDir);

        if(settingsJson.isEmpty() || settingsJson.isBlank()){
            settings = new Settings();
            return;
        }

        Gson gson = new Gson();
        settings = gson.fromJson(settingsJson, Settings.class);
    }

    //Saves settings as a json file
    public static void saveSettings(){
        Gson gson = new Gson();
        String settingsJson = gson.toJson(settings);

        DataHandler.WriteStringToFile(settingsJson, settingsDir);
    }

    //These are all the events that the mod uses
    //In the future these should probably get moved into their own classes
    private void setupEvents(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            delayedScreenshotQueue();

            if(client.world != null) {
                checkKeyPress(client);
            }
        });

        ClientPlayConnectionEvents.JOIN.register(((handler, sender, client) -> {
            if(VersionChecker.shouldShowUpdate())
                client.setScreen(new UpdateScreen());
        }));
    }

    //Is called every client tick to see if a hotkey was pressed
    private void checkKeyPress(MinecraftClient client){
        assert client.player != null;

        if(debugKey.wasPressed()){
            NativeImage image = ScreenshotRecorder.takeScreenshot(client.getFramebuffer());
            try {
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss");;
                String currentDate = dateFormat.format(LocalDateTime.now());

                String screenshotDir = FileUtil.directoryPath + "screenshots/";
                FileUtil.CreateNewDirecotryIfNotExists(screenshotDir);
                image.writeTo(new File(screenshotDir + currentDate + ".png"));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    //Allows you to take a screenshot from another thread and or with a delay
    public static void takeScreenshotAfterDelayOnMainThread(int msDelay){
        screenshotQueue.add(new DelayedScreenshot(msDelay, System.currentTimeMillis()));
    }

    static List<DelayedScreenshot> screenshotQueue = new ArrayList<>();
    private void delayedScreenshotQueue(){
        for (int i = screenshotQueue.size() - 1; i >= 0; i--) {
            DelayedScreenshot currentDelay = screenshotQueue.get(i);

            if(currentDelay.startTime + currentDelay.msDelay < System.currentTimeMillis()){
                ScreenshotUtil.saveScreenshot();

                screenshotQueue.remove(i);
            }
        }
    }
}
