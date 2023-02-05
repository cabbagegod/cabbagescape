package com.cabbagegod.cabbagescape.commands;

import com.cabbagegod.cabbagescape.client.CabbageScapeClient;
import com.cabbagegod.cabbagescape.client.blockoutline.PersistentOutlineRenderer;
import com.cabbagegod.cabbagescape.events.EventHandler;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.apache.commons.lang3.StringUtils;

public class Commands implements EventHandler {
    @Override
    public void start() {
        register();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(client.world != null) {
                checkKeyPress(client);
            }
        });
    }

    //Is called every client tick to see if a hotkey was pressed
    private void checkKeyPress(MinecraftClient client){
        assert client.player != null;

        if(CabbageScapeClient.debugKey.wasPressed()){
            //test something
        }
    }

    private void register(){
        ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("grounditems")
                .then(ClientCommandManager.literal("add")
                        .then(ClientCommandManager.argument("item", ClientMessageArgumentType.message())
                                .executes(context -> {
                                    Text text = ClientMessageArgumentType.getMessage(context, "item");

                                    if(CabbageScapeClient.settings.groundItemSettings.searchTags.contains(text.getString().toLowerCase())){
                                        MinecraftClient.getInstance().player.sendMessage(new LiteralText("Item " + text.getString() + " was already in your list."), false);
                                    } else {
                                        CabbageScapeClient.settings.groundItemSettings.searchTags.add(text.getString().toLowerCase());
                                        CabbageScapeClient.saveSettings();
                                        MinecraftClient.getInstance().player.sendMessage(new LiteralText("Item " + text.getString() + " has been added to your list."), false);
                                    }
                                    return 1;
                                })))
                .then(ClientCommandManager.literal("remove")
                        .then(ClientCommandManager.argument("item", ClientMessageArgumentType.message())
                                .executes(context -> {
                                    Text text = ClientMessageArgumentType.getMessage(context, "item");

                                    if(CabbageScapeClient.settings.groundItemSettings.searchTags.contains(text.getString().toLowerCase())){
                                        CabbageScapeClient.settings.groundItemSettings.searchTags.remove(text.getString().toLowerCase());
                                        CabbageScapeClient.saveSettings();
                                        MinecraftClient.getInstance().player.sendMessage(new LiteralText("Item " + text.getString() + " has been removed from your list."), false);
                                    } else {
                                        MinecraftClient.getInstance().player.sendMessage(new LiteralText("Item " + text.getString() + " was not in your list."), false);
                                    }
                                    return 1;
                                })))
                .then(ClientCommandManager.literal("volume")
                        .then(ClientCommandManager.argument("num", FloatArgumentType.floatArg(0,1))
                                .executes(context -> {
                                    float volume = FloatArgumentType.getFloat(context, "num");

                                    CabbageScapeClient.settings.groundItemSettings.volume = volume;
                                    CabbageScapeClient.saveSettings();

                                    return 1;
                                })))
                .then(ClientCommandManager.literal("reload")
                                .executes(context -> {
                                    PersistentOutlineRenderer.getInstance().clear();

                                    return 1;
                                }))
                .then(ClientCommandManager.literal("contains")
                    .then(ClientCommandManager.literal("add")
                        .then(ClientCommandManager.argument("item", ClientMessageArgumentType.message())
                                .executes(context -> {
                                    Text text = ClientMessageArgumentType.getMessage(context, "item");

                                    if(CabbageScapeClient.settings.groundItemSettings.containsTags.contains(text.getString().toLowerCase())){
                                        MinecraftClient.getInstance().player.sendMessage(new LiteralText("Item " + text.getString() + " was already in your list."), false);
                                    } else {
                                        CabbageScapeClient.settings.groundItemSettings.containsTags.add(text.getString().toLowerCase());
                                        CabbageScapeClient.saveSettings();
                                        MinecraftClient.getInstance().player.sendMessage(new LiteralText("Item " + text.getString() + " has been added to your list."), false);
                                    }
                                    return 1;
                                })))
                    .then(ClientCommandManager.literal("remove")
                        .then(ClientCommandManager.argument("item", ClientMessageArgumentType.message())
                                .executes(context -> {
                                    Text text = ClientMessageArgumentType.getMessage(context, "item");

                                    if(CabbageScapeClient.settings.groundItemSettings.containsTags.contains(text.getString().toLowerCase())){
                                        CabbageScapeClient.settings.groundItemSettings.containsTags.remove(text.getString().toLowerCase());
                                        CabbageScapeClient.saveSettings();
                                        MinecraftClient.getInstance().player.sendMessage(new LiteralText("Item " + text.getString() + " has been removed from your list."), false);
                                    } else {
                                        MinecraftClient.getInstance().player.sendMessage(new LiteralText("Item " + text.getString() + " was not in your list."), false);
                                    }
                                    return 1;
                                })))
                    .then(ClientCommandManager.literal("list")
                          .executes(context -> {
                              assert MinecraftClient.getInstance().player != null;
                              String tagList = "Contains Item List: ";
                              for(String tag : CabbageScapeClient.settings.groundItemSettings.containsTags){
                                  tagList += tag + ",";
                              }
                              tagList = StringUtils.chop(tagList);

                              MinecraftClient.getInstance().player.sendMessage(new LiteralText(tagList), false);
                              return 1;
                          })))
                .then(ClientCommandManager.literal("list")
                        .executes(context -> {
                            assert MinecraftClient.getInstance().player != null;
                            String tagList = "Ground Item List: ";
                            for(String tag : CabbageScapeClient.settings.groundItemSettings.searchTags){
                                tagList += tag + ",";
                            }
                            tagList = StringUtils.chop(tagList);

                            MinecraftClient.getInstance().player.sendMessage(new LiteralText(tagList), false);
                            return 1;
                        })));
    }
}
