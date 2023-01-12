package com.cabbagegod.cabbagescape.ui;

import com.cabbagegod.cabbagescape.client.cluetracker.ClueStep;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

@Environment(EnvType.CLIENT)
public class ClueHintScreen extends Screen {
    ClueStep currentStep;

    public ClueHintScreen(ClueStep currentStep) {
        super(new TranslatableText("cabbagescape.clueHint"));

        this.currentStep = currentStep;
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);

        Vector3d coords = currentStep.getCoords();

        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 20, -1);
        drawCenteredText(matrices, this.textRenderer, currentStep.getInfo(), this.width / 2, this.height / 4, -1);
        drawCenteredText(matrices, this.textRenderer, "Next step: " + coords.x + " " + coords.y + " " + coords.z, this.width / 2, this.height / 3, -1);
        addDrawableChild(new ButtonWidget(this.width / 2 - (150/2), this.height / 2, 150, 20, new LiteralText("Track Step"), button -> {
            assert this.client != null;
            this.client.setScreen(null);

            assert this.client.player != null;
            this.client.player.sendChatMessage("/compass set " + coords.x + " " + coords.y + " " + coords.z);
        }));
        super.render(matrices, mouseX, mouseY, delta);
    }
}
