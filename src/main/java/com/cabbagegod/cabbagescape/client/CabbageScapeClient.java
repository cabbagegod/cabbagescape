package com.cabbagegod.cabbagescape.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.LiteralText;
import org.lwjgl.glfw.GLFW;

public class CabbageScapeClient implements ClientModInitializer {
    private static KeyBinding homeKey;
    private static KeyBinding buildModeKey;
    private static KeyBinding debugKey;

    @Override
    public void onInitializeClient() {
        homeKey = KeybindHandler.createKeybind("home_key", "cabbagescape", GLFW.GLFW_KEY_H);
        buildModeKey = KeybindHandler.createKeybind("buildmode_key", "cabbagescape", GLFW.GLFW_KEY_B);
        debugKey = KeybindHandler.createKeybind("debug_key", "cabbagescape", 0);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (homeKey.wasPressed()) {
                client.player.sendChatMessage("/home");
                client.player.sendMessage(new LiteralText("Teleporting you back to your home."), true);
            }
            if (buildModeKey.wasPressed()) {
                client.player.sendChatMessage("/con buildmode");
                client.player.sendMessage(new LiteralText("Switching between build mode..."), true);
            }
        });
    }
}
