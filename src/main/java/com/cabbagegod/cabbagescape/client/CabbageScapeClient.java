package com.cabbagegod.cabbagescape.client;

import com.cabbagegod.cabbagescape.commands.Commands;
import com.cabbagegod.cabbagescape.data.DataHandler;
import com.cabbagegod.cabbagescape.data.Settings;
import com.cabbagegod.cabbagescape.data.itemdata.ItemDisplay;
import com.cabbagegod.cabbagescape.data.itemdata.ItemLore;
import com.cabbagegod.cabbagescape.ui.UpdateScreen;
import com.google.gson.Gson;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientBlockEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.datafixer.fix.ItemLoreToTextFix;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtTypes;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.CallbackI;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

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
        homeKey = KeybindHandler.createKeybind("home_key", "category.cabbagescape", GLFW.GLFW_KEY_H);
        buildModeKey = KeybindHandler.createKeybind("buildmode_key", "category.cabbagescape", GLFW.GLFW_KEY_B);
        debugKey = KeybindHandler.createKeybind("debug_key", "category.cabbagescape", 0);

        loadSettings();
        VersionChecker.Verify();

        Commands.register();
        setupEvents();
        EventRegisterer.register();
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
                checkSelectedDropItems(client);
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
            client.setScreen(new UpdateScreen());
        }
    }

    //This disaster logic is used to find/handle ground items
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
