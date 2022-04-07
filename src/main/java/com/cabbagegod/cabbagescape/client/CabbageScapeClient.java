package com.cabbagegod.cabbagescape.client;

import com.cabbagegod.cabbagescape.client.blockoutline.BlockOutlineManager;
import com.cabbagegod.cabbagescape.client.blockoutline.PersistentOutlineRenderer;
import com.cabbagegod.cabbagescape.client.grounditems.GroundItemsManager;
import com.cabbagegod.cabbagescape.client.potiontimers.PotionTimerManager;
import com.cabbagegod.cabbagescape.commands.Commands;
import com.cabbagegod.cabbagescape.data.DataHandler;
import com.cabbagegod.cabbagescape.data.Settings;
import com.cabbagegod.cabbagescape.notifications.NotificationManager;
import com.cabbagegod.cabbagescape.ui.UpdateScreen;
import com.google.gson.Gson;
import jdk.jshell.spi.ExecutionControl;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import org.lwjgl.glfw.GLFW;

public class CabbageScapeClient implements ClientModInitializer {
    public static String settingsDir = "settings";
    public static String version = "pre-a1.1.1+1.18.2";
    public static Settings settings;

    public static NotificationManager notificationManager;

    //Keybinds
    private static KeyBinding homeKey;
    private static KeyBinding buildModeKey;
    private static KeyBinding debugKey;

    @Override
    public void onInitializeClient() {
        homeKey = KeybindHandler.createKeybind("home_key", "category.cabbagescape", GLFW.GLFW_KEY_H);
        buildModeKey = KeybindHandler.createKeybind("buildmode_key", "category.cabbagescape", GLFW.GLFW_KEY_B);
        debugKey = KeybindHandler.createKeybind("debug_key", "category.cabbagescape", 0);

        loadSettings();
        VersionChecker.Verify();

        notificationManager = new NotificationManager(settings.notificationSettings);

        Commands.register();
        setupEvents();
        EventRegisterer.register();
        GroundItemsManager.register();

        PotionTimerManager potionTimerManager = PotionTimerManager.getInstance();

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

        if (homeKey.wasPressed()) {
            client.player.sendChatMessage("/home");
            client.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        }
        if (buildModeKey.wasPressed()) {
            client.player.sendChatMessage("/con buildmode");
            client.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        }
        if(debugKey.wasPressed()){
            //
        }
    }
}
