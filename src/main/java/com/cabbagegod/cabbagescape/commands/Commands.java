package com.cabbagegod.cabbagescape.commands;

import com.cabbagegod.cabbagescape.client.CabbageScapeClient;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import io.netty.util.internal.StringUtil;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import com.cabbagegod.cabbagescape.commands.ClientMessageArgumentType;
import net.minecraft.command.argument.MessageArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.apache.commons.lang3.StringUtils;

public class Commands {
    public static void register(){
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
