package com.cabbagegod.cabbagescape.client;

import com.cabbagegod.cabbagescape.Main;
import com.cabbagegod.cabbagescape.client.barrows.BarrowsHelper;
import com.cabbagegod.cabbagescape.client.blockoutline.BlockOutlineManager;
import com.cabbagegod.cabbagescape.client.blockoutline.PersistentOutlineRenderer;
import com.cabbagegod.cabbagescape.client.grounditems.GroundItemsManager;
import com.cabbagegod.cabbagescape.commands.Commands;
import com.cabbagegod.cabbagescape.data.DataHandler;
import com.cabbagegod.cabbagescape.data.DelayedScreenshot;
import com.cabbagegod.cabbagescape.data.Settings;
import com.cabbagegod.cabbagescape.events.EventHandler;
import com.cabbagegod.cabbagescape.notifications.NotificationManager;
import com.cabbagegod.cabbagescape.ui.UpdateScreen;
import com.cabbagegod.cabbagescape.util.FileUtil;
import com.cabbagegod.cabbagescape.util.ScreenshotUtil;
import com.google.gson.Gson;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;
import org.reflections.Reflections;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CabbageScapeClient implements ClientModInitializer {
    public static String settingsDir = "settings";
    public static String version = "a1.4.1+1.18.2";
    public static Settings settings;

    public static NotificationManager notificationManager;

    //Keybinds - F to pay respects
    public static KeyBinding debugKey;

    @Override
    public void onInitializeClient() {
        debugKey = KeybindHandler.createKeybind("debug_key", "category.cabbagescape", 0);

        loadSettings();

        notificationManager = new NotificationManager(settings.notificationSettings);

        setupEvents();

        BlockOutlineManager.getInstance().add(PersistentOutlineRenderer.getInstance());

        loadEventHandlers();
    }

    private void loadEventHandlers(){
        //Load all event handlers
        Reflections reflections = new Reflections("com.cabbagegod.cabbagescape");
        Set<Class<? extends EventHandler>> classes = reflections.getSubTypesOf(EventHandler.class);
        for (Class handler: classes) {
            try {
                Constructor<?> hConst = handler.getConstructor();

                EventHandler eventHandler = (EventHandler) hConst.newInstance();

                eventHandler.start();

                Main.LOGGER.info("Started Event: " + eventHandler.getClass().getTypeName());
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException ignored) {
            }
        }
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
        });
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
