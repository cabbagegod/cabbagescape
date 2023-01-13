package com.cabbagegod.cabbagescape.ui.settings;

import com.cabbagegod.cabbagescape.data.Settings;
import com.cabbagegod.cabbagescape.ui.ButtonOption;
import com.cabbagegod.cabbagescape.data.MinescapePlugins;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Option;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class PluginsScreen extends GameOptionsScreen {
    private static Option[] OPTIONS;
    private ButtonListWidget buttonList;
    private final Settings settings;

    public PluginsScreen(Screen parent, GameOptions gameOptions, Settings settings) {
        super(parent, gameOptions, new LiteralText("Plugins Settings"));

        this.settings = settings;
    }

    protected void init(){
        try {
            setup();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        this.buttonList = new ButtonListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        this.buttonList.addAll(OPTIONS);

        addSelectableChild(this.buttonList);
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        buttonList.render(matrices, mouseX, mouseY, delta);

        super.render(matrices, mouseX, mouseY, delta);
    }

    private void setup() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<Option> options = new ArrayList<>();

        for (MinescapePlugins plugin: MinescapePlugins.values()) {
            Object screen = plugin.screen.getDeclaredConstructor().newInstance();

            Option newOption = new ButtonOption(plugin.toString(), (widget) -> {
                MinecraftClient.getInstance().setScreen((Screen) screen);
            });
            options.add(newOption);
        }

        OPTIONS = options.toArray(new Option[0]);
    }
}