package com.cabbagegod.cabbagescape.ui;

import com.cabbagegod.cabbagescape.client.VersionChecker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.SkinOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

@Environment(EnvType.CLIENT)
public class UpdateScreen extends Screen {

    public UpdateScreen() {
        super(new TranslatableText("cabbagescape.newUpdate"));
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);

        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 20, -1);
        drawCenteredText(matrices, this.textRenderer, "Looks like update " + VersionChecker.getCurrentVersion() + " of CabbageScape is available!", this.width / 2, this.height / 8, -1);
        drawCenteredText(matrices, this.textRenderer, "Consider updating to receive all the latest and greatest updates. Happy scaping! :D", this.width / 2, this.height / 6, -1);
        addDrawableChild(new ButtonWidget(this.width / 2 - (150/2), this.height / 2, 150, 20, new LiteralText("Okay"), button -> {
            assert this.client != null;
            this.client.setScreen(null);
            VersionChecker.disableUpdateMessage();
        }));
        super.render(matrices, mouseX, mouseY, delta);
    }
}
