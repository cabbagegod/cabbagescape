package com.cabbagegod.cabbagescape.ui.settings;

import com.cabbagegod.cabbagescape.client.CabbageScapeClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

@Environment(EnvType.CLIENT)
public class SettingsScreen extends Screen {

    public SettingsScreen() {
        super(new TranslatableText("cabbagescape.settingsScreen"));
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);

        addDrawableChild(new ButtonWidget(this.width / 2 - 102, this.height / 4 + 96 + -16, 204, 20, new LiteralText("Plugin Settings"), button -> {
            MinecraftClient.getInstance().setScreen(new PluginsScreen(this, CabbageScapeClient.settings));
        }));
        addDrawableChild(new ButtonWidget(this.width / 2 - 102, this.height / 4 + 96 + 16, 204, 20, new LiteralText("Done"), button -> {
            MinecraftClient.getInstance().setScreen(new GameMenuScreen(true));
        }));

        super.render(matrices, mouseX, mouseY, delta);
    }
}
