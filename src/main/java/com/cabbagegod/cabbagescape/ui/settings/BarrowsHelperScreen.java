package com.cabbagegod.cabbagescape.ui.settings;

import com.cabbagegod.cabbagescape.client.CabbageScapeClient;
import com.cabbagegod.cabbagescape.data.GroundItemSettings;
import com.cabbagegod.cabbagescape.data.Settings;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.Option;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class BarrowsHelperScreen extends GameOptionsScreen {
    private static Option[] OPTIONS;
    private ButtonListWidget buttonList;
    private final Settings settings;

    public BarrowsHelperScreen() {
        super(new PluginsScreen(null, CabbageScapeClient.settings), null, new LiteralText("Barrows Helper Options"));

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
        Option doorMarkersEnabled = CyclingOption.create("options.doormarkers", gameOptions -> settings.barrowsHelperSettings.doorMarkers, (gameOptions, option, toggle) -> settings.barrowsHelperSettings.doorMarkers = toggle);

        OPTIONS = new Option[]{doorMarkersEnabled};
    }
}