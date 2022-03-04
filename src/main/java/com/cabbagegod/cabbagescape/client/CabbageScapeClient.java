package com.cabbagegod.cabbagescape.client;

import com.cabbagegod.cabbagescape.commands.Commands;
import com.cabbagegod.cabbagescape.data.DataHandler;
import com.cabbagegod.cabbagescape.data.GroundItemSettings;
import com.cabbagegod.cabbagescape.data.Settings;
import com.google.gson.Gson;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.realms.util.JsonUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CabbageScapeClient implements ClientModInitializer {
    public static String settingsDir = "settings";

    public static Settings settings;

    //Keybinds
    private static KeyBinding homeKey;
    private static KeyBinding buildModeKey;
    private static KeyBinding debugKey;

    //Ground items
    private List<Entity> armorStands = new ArrayList<Entity>();

    @Override
    public void onInitializeClient() {
        homeKey = KeybindHandler.createKeybind("home_key", "cabbagescape", GLFW.GLFW_KEY_H);
        buildModeKey = KeybindHandler.createKeybind("buildmode_key", "cabbagescape", GLFW.GLFW_KEY_B);
        debugKey = KeybindHandler.createKeybind("debug_key", "cabbagescape", 0);

        loadSettings();

        Commands.register();
        setupEvents();
    }

    private void loadSettings(){
        String settingsJson = DataHandler.ReadStringFromFile(settingsDir);

        if(settingsJson.isEmpty() || settingsJson.isBlank()){
            settings = new Settings();
            return;
        }

        Gson gson = new Gson();
        settings = gson.fromJson(settingsJson, Settings.class);
    }

    public static void saveSettings(){
        Gson gson = new Gson();
        String settingsJson = gson.toJson(settings);

        DataHandler.WriteStringToFile(settingsJson, settingsDir);
    }

    private void setupEvents(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            checkKeyPress(client);
            checkSelectedDropItems(client);
        });

        ClientEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if(entity.getType() == EntityType.ARMOR_STAND){
                armorStands.add(entity);
            }
        });

        ClientEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {
            if(entity.getType() == EntityType.ARMOR_STAND){
                if(armorStands.contains(entity)) {
                    armorStands.remove(entity);
                }
            }
        });
    }

    private void checkKeyPress(MinecraftClient client){
        if (homeKey.wasPressed()) {
            client.player.sendChatMessage("/home");
            client.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        }
        if (buildModeKey.wasPressed()) {
            client.player.sendChatMessage("/con buildmode");
            client.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        }
    }

    private void checkSelectedDropItems(MinecraftClient client){
        for (Entity entity: armorStands) {
            for (ItemStack itemStack : entity.getArmorItems()){
                if(itemStack == null || itemStack.isEmpty() || itemStack.getItem() == Items.AIR)
                    continue;

                //Oh god what have I done

                if(settings.groundItemSettings.searchTags.contains(Formatting.strip(itemStack.getName().getString().toLowerCase()))){
                    client.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, settings.groundItemSettings.volume, 1);
                    client.player.clientWorld.addParticle(ParticleTypes.ANGRY_VILLAGER, entity.getX() + .5f, entity.getY() + 1, entity.getZ() + 1, 1, 1, 1);
                    client.player.clientWorld.addParticle(ParticleTypes.ANGRY_VILLAGER, entity.getX() - .5f, entity.getY() + 1, entity.getZ() + 1, 1, 1, 1);
                    client.player.clientWorld.addParticle(ParticleTypes.ANGRY_VILLAGER, entity.getX() + .5f, entity.getY() + 1, entity.getZ(), 1, 1, 1);
                    client.player.clientWorld.addParticle(ParticleTypes.ANGRY_VILLAGER, entity.getX() - .5f, entity.getY() + 1, entity.getZ(), 1, 1, 1);
                } else {
                    for(String tag : settings.groundItemSettings.containsTags){
                        if(Formatting.strip(itemStack.getName().getString().toLowerCase()).contains(tag)){
                            client.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, settings.groundItemSettings.volume, 1);
                            client.player.clientWorld.addParticle(ParticleTypes.ANGRY_VILLAGER, entity.getX() + .5f, entity.getY() + 1, entity.getZ() + 1, 1, 1, 1);
                            client.player.clientWorld.addParticle(ParticleTypes.ANGRY_VILLAGER, entity.getX() - .5f, entity.getY() + 1, entity.getZ() + 1, 1, 1, 1);
                            client.player.clientWorld.addParticle(ParticleTypes.ANGRY_VILLAGER, entity.getX() + .5f, entity.getY() + 1, entity.getZ(), 1, 1, 1);
                            client.player.clientWorld.addParticle(ParticleTypes.ANGRY_VILLAGER, entity.getX() - .5f, entity.getY() + 1, entity.getZ(), 1, 1, 1);
                        }
                    }
                }
            }
        }
    }
}
