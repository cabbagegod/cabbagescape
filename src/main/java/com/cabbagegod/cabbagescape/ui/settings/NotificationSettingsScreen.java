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
        GroundItemSettings settings = this.settings.groundItemSettings;

        OPTIONS = new Option[]{};
    }
}