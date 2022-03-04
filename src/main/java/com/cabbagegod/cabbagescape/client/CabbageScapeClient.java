package com.cabbagegod.cabbagescape.client;

import com.cabbagegod.cabbagescape.commands.Commands;
import com.cabbagegod.cabbagescape.data.DataHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
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

public class CabbageScapeClient implements ClientModInitializer {
    public static String searchTagDir = "searchTags";

    //Keybinds
    private static KeyBinding homeKey;
    private static KeyBinding buildModeKey;
    private static KeyBinding debugKey;

    //Ground items
    public static List<String> searchTags = new ArrayList<String>();
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
        String tagData = DataHandler.ReadStringFromFile(searchTagDir);
        if(tagData.isEmpty())
            return;

        String[] tags = tagData.split(",");
        for(String tag : tags){
            searchTags.add(tag);
        }
    }

    public static void saveSettings(){
        String tagData = "";
        for(String tag : searchTags){
            tagData += tag + ",";
        }

        //Remove hanging ','
        StringUtils.chop(tagData);
        DataHandler.WriteStringToFile(tagData, searchTagDir);
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

                for(String tag : searchTags){
                    if(Formatting.strip(itemStack.getName().getString().toLowerCase()).equals(tag)){
                        client.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, .05f, 1);
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
