package com.cabbagegod.cabbagescape.events;

import com.cabbagegod.cabbagescape.client.potiontimers.PotionInstance;
import com.cabbagegod.cabbagescape.client.potiontimers.PotionTimerManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

public class PotionTimerHandler implements EventHandler {
    @Override
    public void start(){
        HudRenderCallback.EVENT.register(PotionTimerHandler::onHudRender);
    }


    public static void onHudRender(MatrixStack matrices, float tickDelta){
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        //Display any potion timers
        int x = 11;
        for(PotionInstance potion : PotionTimerManager.getInstance().potionThreads.keySet()) {
            String resourceLocation = potion.resourceLocation.isEmpty() || potion.resourceLocation.isBlank() ? "textures/custom/colors/white.png" : potion.resourceLocation;

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1, 1, 1, 1.0F);
            Identifier potionIcon = new Identifier("minecraft", resourceLocation);
            RenderSystem.setShaderTexture(0, potionIcon);

            DrawableHelper.drawTexture(matrices, x, 5, 0, 0, 22, 22, 22, 22);
            long currentTime = (potion.startTime / 1000) + potion.length - (System.currentTimeMillis() / 1000);

            DrawableHelper.drawCenteredText(matrices, textRenderer, new LiteralText("" + Math.round(currentTime)), x + 11, 20, -1);

            x += 30;
        }
    }
}
