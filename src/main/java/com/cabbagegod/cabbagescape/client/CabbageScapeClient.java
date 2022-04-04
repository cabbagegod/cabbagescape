package com.cabbagegod.cabbagescape.client;

import com.cabbagegod.cabbagescape.client.blockoutline.BlockOutlineManager;
import com.cabbagegod.cabbagescape.client.blockoutline.PersistentOutlineRenderer;
import com.cabbagegod.cabbagescape.client.blockoutline.Vector3f;
import com.cabbagegod.cabbagescape.commands.Commands;
import com.cabbagegod.cabbagescape.data.DataHandler;
import com.cabbagegod.cabbagescape.data.Settings;
import com.cabbagegod.cabbagescape.ui.UpdateScreen;
import com.cabbagegod.cabbagescape.util.ParticleUtil;
import com.google.gson.Gson;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CabbageScapeClient implements ClientModInitializer {
    public static String settingsDir = "settings";
    public static String version = "pre-a1.1.1+1.18.2";
    public static Settings settings;

    //Keybinds
    private static KeyBinding homeKey;
    private static KeyBinding buildModeKey;
    private static KeyBinding debugKey;

    //Ground items
    private List<Entity> armorStands = new ArrayList<Entity>();

    @Override
    public void onInitializeClient() {
        homeKey = KeybindHandler.createKeybind("home_key", "category.cabbagescape", GLFW.GLFW_KEY_H);
        buildModeKey = KeybindHandler.createKeybind("buildmode_key", "category.cabbagescape", GLFW.GLFW_KEY_B);
        debugKey = KeybindHandler.createKeybind("debug_key", "category.cabbagescape", 0);

        loadSettings();
        VersionChecker.Verify();

        Commands.register();
        setupEvents();
        EventRegisterer.register();

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

    BlockPos[] positions = new BlockPos[4];
    Color[] colors = {new Color(6, 145, 180), new Color(77, 127, 69), new Color(194, 182, 70), new Color(172, 55, 125)};

    //These are all the events that the mod uses
    //In the future these should probably get moved into their own classes
    private void setupEvents(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(client.world != null) {
                checkKeyPress(client);
                checkSelectedDropItems(client);

                for (int i = 0; i < positions.length; i++) {
                    if(positions[i] != null){
                        BlockPos pos = positions[i];
                        ParticleUtil.CreateSpiralParticle(new Vector3f(pos.getX() + .5f, pos.getY(), pos.getZ() + .5f), colors[i]);
                    }
                }
            }
        });
        ClientEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if(entity.getType() == EntityType.ARMOR_STAND){
                armorStands.add(entity);
            }
        });
        ClientPlayConnectionEvents.JOIN.register(((handler, sender, client) -> {
            if(VersionChecker.shouldShowUpdate())
                client.setScreen(new UpdateScreen());
        }));
        ClientEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {
            if(entity.getType() == EntityType.ARMOR_STAND){
                armorStands.remove(entity);

                BlockPos entityPos = entity.getBlockPos().add(0,2,1);

                if(PersistentOutlineRenderer.getInstance().contains(entityPos)){
                    PersistentOutlineRenderer.getInstance().removePos(entityPos);
                }
            }
        });
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
            BlockPos pos = client.player.getBlockPos();

            for (int i = 0; i < positions.length; i++) {
                if(positions[i] == null){
                    positions[i] = pos;
                    return;
                }
            }

            positions = new BlockPos[4];
        }
    }

    //This disaster logic is used to find/handle ground items
    private void checkSelectedDropItems(MinecraftClient client){
        for (Entity entity: armorStands) {
            for (ItemStack itemStack : entity.getArmorItems()){
                if(itemStack == null || itemStack.isEmpty() || itemStack.getItem() == Items.AIR)
                    continue;

                //Oh god what have I done

                boolean wasTriggered = false;

                BlockPos entityPos = new BlockPos(entity.getBlockX(), entity.getBlockY(), entity.getBlockZ()).add(0,2,1);

                if(settings.groundItemSettings.searchTags.contains(Formatting.strip(itemStack.getName().getString().toLowerCase()))){
                    wasTriggered = true;

                    client.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, settings.groundItemSettings.volume, 1);
                    client.player.clientWorld.addParticle(ParticleTypes.ANGRY_VILLAGER, entityPos.getX(), entityPos.getY() - 1, entityPos.getZ(), 1, 1, 1);
                    client.player.clientWorld.addParticle(ParticleTypes.ANGRY_VILLAGER, entityPos.getX(), entityPos.getY() - 1, entityPos.getZ() + 1, 1, 1, 1);
                    client.player.clientWorld.addParticle(ParticleTypes.ANGRY_VILLAGER, entityPos.getX() + 1, entityPos.getY() - 1, entityPos.getZ() + 1, 1, 1, 1);
                    client.player.clientWorld.addParticle(ParticleTypes.ANGRY_VILLAGER, entityPos.getX() + 1, entityPos.getY() - 1, entityPos.getZ(), 1, 1, 1);
                } else {
                    for(String tag : settings.groundItemSettings.containsTags){
                        if(Formatting.strip(itemStack.getName().getString().toLowerCase()).contains(tag)){
                            wasTriggered = true;

                            client.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, settings.groundItemSettings.volume, 1);
                            client.player.clientWorld.addParticle(ParticleTypes.ANGRY_VILLAGER, entityPos.getX(), entityPos.getY() - 1, entityPos.getZ(), 1, 1, 1);
                            client.player.clientWorld.addParticle(ParticleTypes.ANGRY_VILLAGER, entityPos.getX(), entityPos.getY() - 1, entityPos.getZ() + 1, 1, 1, 1);
                            client.player.clientWorld.addParticle(ParticleTypes.ANGRY_VILLAGER, entityPos.getX() + 1, entityPos.getY() - 1, entityPos.getZ() + 1, 1, 1, 1);
                            client.player.clientWorld.addParticle(ParticleTypes.ANGRY_VILLAGER, entityPos.getX() + 1, entityPos.getY() - 1, entityPos.getZ(), 1, 1, 1);
                            break;
                        }
                    }
                }

                if(wasTriggered) {
                    if(!PersistentOutlineRenderer.getInstance().contains(entity.getBlockPos()))
                        PersistentOutlineRenderer.getInstance().addPos(entityPos);
                }
            }
        }
    }
}
