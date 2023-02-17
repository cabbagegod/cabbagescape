package com.cabbagegod.cabbagescape.ui.settings;

import com.cabbagegod.cabbagescape.client.CabbageScapeClient;
import com.cabbagegod.cabbagescape.data.GroundItemSettings;
import com.cabbagegod.cabbagescape.data.Settings;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.DoubleOption;
import net.minecraft.client.option.Option;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class NotificationSettingsScreen extends GameOptionsScreen {
    private static Option[] OPTIONS;
    private ButtonListWidget buttonList;
    private final Settings settings;

    public NotificationSettingsScreen() {
        super(new PluginsScreen(null, CabbageScapeClient.settings), null, new LiteralText("Notification Settings"));

        this.settings = CabbageScapeClient.settings;
    }

    protected void init(){
        setupOptions();

        this.buttonList = new ButtonListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        this.buttonList.addAll(OPTIONS);

        addSelectableChild(this.buttonList);
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        buttonList.render(matrices, mouseX, mouseY, delta);

        super.render(matrices, mouseX, mouseY, delta);
    }

    void setupOptions(){
        Option flashEnabled = CyclingOption.create("options.notifications.flashenabled", gameOptions -> settings.notificationSettings.flashEnabled, (gameOptions, option, toggle) -> settings.notificationSettings.flashEnabled = toggle);
        Option soundEnabled = CyclingOption.create("options.notifications.soundenabled", gameOptions -> settings.notificationSettings.soundEnabled, (gameOptions, option, toggle) -> settings.notificationSettings.soundEnabled = toggle);

        Option volumeOption = new DoubleOption("volume", 0D, 100.0D, 1.0F, gameOptions -> (double) (settings.notificationSettings.volume * 100), (gameOptions, volume) -> {
            settings.notificationSettings.volume = (float) (volume / 100);
        },(gameOptions, option) -> {
            double d = option.get(gameOptions);
            return new LiteralText("Volume: " + Math.round(d) + "%");
        });

        addDrawableChild(new ButtonWidget(this.width / 2 - (150/2), 185, 150, 20, new LiteralText("Save"), button -> {
            CabbageScapeClient.saveSettings();

            assert this.client != null;
            this.client.setScreen(this.parent);
        }));

        OPTIONS = new Option[]{flashEnabled, soundEnabled, volumeOption};
    }
}