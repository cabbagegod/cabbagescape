package com.cabbagegod.cabbagescape.ui;

import com.cabbagegod.cabbagescape.client.CabbageScapeClient;
import com.cabbagegod.cabbagescape.client.VersionChecker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.language.LanguageAdapter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.DoubleOption;
import net.minecraft.client.option.Option;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
public class SettingsScreen extends Screen {

    public SettingsScreen() {
        super(new TranslatableText("cabbagescape.settingsScreen"));
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);

        addDrawableChild(new ButtonWidget(this.width / 2 - 102, this.height / 4 + 96 + -16, 204, 20, new LiteralText("Ground Items"), button -> {
            //this.client.setScreen(new OptionsScreen(this, this.client.options));
            MinecraftClient.getInstance().setScreen(new GroundItemsScreen(this, this.client.options, CabbageScapeClient.settings));
        }));

        super.render(matrices, mouseX, mouseY, delta);
    }
}
