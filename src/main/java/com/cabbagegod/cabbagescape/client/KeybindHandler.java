package com.cabbagegod.cabbagescape.client;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class KeybindHandler {
    public static KeyBinding createKeybind(String id, String category, int keycode){
        return KeyBindingHelper.registerKeyBinding(new KeyBinding("key.cabbagescape." + id, InputUtil.Type.KEYSYM, keycode, category));
    }
}
