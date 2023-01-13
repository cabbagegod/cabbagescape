package com.cabbagegod.cabbagescape.ui;

import com.cabbagegod.cabbagescape.client.CabbageScapeClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Option;
import net.minecraft.text.LiteralText;

import java.util.function.Function;

public class ButtonOption extends Option {
    private ButtonWidget.PressAction action;

    public ButtonOption(String key, ButtonWidget.PressAction action) {
        super(key);

        this.key = key;
        this.action = action;
    }

    String key;

    @Override
    public ClickableWidget createButton(GameOptions options, int x, int y, int width) {
        return new ButtonWidget(x,y,width,20,new LiteralText(key), action);
    }
}
