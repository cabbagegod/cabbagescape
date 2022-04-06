package com.cabbagegod.cabbagescape.ui;

import com.cabbagegod.cabbagescape.client.CabbageScapeClient;
import com.cabbagegod.cabbagescape.client.VersionChecker;
import com.cabbagegod.cabbagescape.data.GroundItemSettings;
import com.cabbagegod.cabbagescape.data.Settings;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.DoubleOption;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Option;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class GroundItemsScreen extends GameOptionsScreen {
    private static Option[] OPTIONS;
    private ButtonListWidget buttonList;
    private final Settings settings;
    private TextFieldWidget groundItemsListTextField;
    private TextFieldWidget containsKeysTextField;

    public GroundItemsScreen(Screen parent, GameOptions gameOptions, Settings settings) {
        super(parent, gameOptions, new LiteralText("Ground Items Options"));

        this.settings = settings;
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

        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        float red = (float) settings.groundItemSettings.itemRed;
        float green = (float) settings.groundItemSettings.itemGreen;
        float blue = (float) settings.groundItemSettings.itemBlue;

        RenderSystem.setShaderColor(red / 255, green / 255, blue / 255, 1.0F);
        Identifier test = new Identifier("minecraft", "textures/custom/colors/white.png");
        RenderSystem.setShaderTexture(0, test);

        //The color picker texture
        int x = width / 2 + 132 + 32;
        int y = 40;
        drawTexture(matrices, x,y,0,0,32,32);

        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 5, 16777215);

        drawCenteredText(matrices, this.textRenderer, new LiteralText("Ground Items Keys:"), this.width / 2, 110, -1);

        drawCenteredText(matrices, this.textRenderer, new LiteralText("Contains Word Keys:"), this.width / 2, 148, -1);

        this.groundItemsListTextField.render(matrices, mouseX, mouseY, delta);
        this.containsKeysTextField.render(matrices, mouseX, mouseY, delta);

        super.render(matrices, mouseX, mouseY, delta);
    }

    void setupOptions(){
        GroundItemSettings settings = this.settings.groundItemSettings;

        Option redOption = new DoubleOption("options.itemcolor.red", 0D, 255.0D, 1.0F, gameOptions -> settings.itemRed, (gameOptions, red) -> {
            settings.itemRed = red;
        },(gameOptions, option) -> {
            double d = option.get(gameOptions);
            return new LiteralText("Highlight Red: " + d);
        });
        Option greenOption = new DoubleOption("options.itemcolor.green", 0D, 255.0D, 1.0F, gameOptions -> settings.itemGreen, (gameOptions, green) -> {
            settings.itemGreen = green;
        },(gameOptions, option) -> {
            double d = option.get(gameOptions);
            return new LiteralText("Highlight Green: " + d);
        });
        Option blueOption = new DoubleOption("options.itemcolor.blue", 0D, 255.0D, 1.0F, gameOptions -> settings.itemBlue, (gameOptions, blue) -> {
            settings.itemBlue = blue;
        },(gameOptions, option) -> {
            double d = option.get(gameOptions);
            return new LiteralText("Highlight Blue: " + d);
        });
        Option particleOption = new DoubleOption("options.spiral.particlecount", 1, 100, 1.0F, gameOptions -> settings.particleCount, (gameOptions, count) -> {
            settings.particleCount = count;
        },(gameOptions, option) -> {
            double d = option.get(gameOptions);
            return new LiteralText("Particle Count: " + d);
        });
        Option spiralEnabled = CyclingOption.create("options.spiralenabled", gameOptions -> settings.spiralEnabled, (gameOptions, option, spiral) -> settings.spiralEnabled = spiral);
        Option highlightEnabled = CyclingOption.create("options.highlightenabled", gameOptions -> settings.blockHighlightEnabled, (gameOptions, option, highlight) -> settings.blockHighlightEnabled = highlight);

        StringBuilder parsedTags = new StringBuilder();
        for(String tag : settings.searchTags){
            parsedTags.append(tag).append(",");
        }
        parsedTags = new StringBuilder(StringUtils.chop(parsedTags.toString()));

        this.groundItemsListTextField = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 122, 200, 20, new LiteralText("Enter Item List"));
        this.groundItemsListTextField.setMaxLength(Integer.MAX_VALUE);
        this.groundItemsListTextField.setText(parsedTags.toString());
        this.groundItemsListTextField.setChangedListener(parsedKeys -> {
            String[] keys = parsedKeys.split(",");
            settings.searchTags = new ArrayList<String>(Arrays.asList(keys));
        });

        parsedTags = new StringBuilder();
        for(String tag : settings.containsTags){
            parsedTags.append(tag).append(",");
        }
        parsedTags = new StringBuilder(StringUtils.chop(parsedTags.toString()));

        this.containsKeysTextField = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 160, 200, 20, new LiteralText("Enter Contains List"));
        this.containsKeysTextField.setMaxLength(Integer.MAX_VALUE);
        this.containsKeysTextField.setText(parsedTags.toString());
        this.containsKeysTextField.setChangedListener(parsedKeys -> {
            String[] keys = parsedKeys.split(",");
            settings.containsTags = new ArrayList<String>(Arrays.asList(keys));
        });

        addSelectableChild(this.containsKeysTextField);
        addSelectableChild(this.groundItemsListTextField);

        addDrawableChild(new ButtonWidget(this.width / 2 - (150/2), 185, 150, 20, new LiteralText("Save"), button -> {
            CabbageScapeClient.saveSettings();

            assert this.client != null;
            this.client.setScreen(this.parent);
        }));

        OPTIONS = new Option[]{spiralEnabled, redOption, highlightEnabled, greenOption, particleOption ,blueOption};
    }
}