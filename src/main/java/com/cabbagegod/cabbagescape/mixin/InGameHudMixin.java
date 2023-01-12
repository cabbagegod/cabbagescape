package com.cabbagegod.cabbagescape.mixin;

import com.cabbagegod.cabbagescape.client.potiontimers.PotionInstance;
import com.cabbagegod.cabbagescape.client.potiontimers.PotionTimerManager;
import com.cabbagegod.cabbagescape.notifications.NotificationManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {
    @Shadow private int scaledWidth;

    @Shadow private int scaledHeight;

    @Shadow public abstract TextRenderer getTextRenderer();

    @Inject(method = "render", at = @At("RETURN"))
    public void render(MatrixStack matrices, float tickDelta, CallbackInfo info){
        //Display any potion timers
        int x = 11;
        for(PotionInstance potion : PotionTimerManager.getInstance().potionThreads.keySet()) {
            String resourceLocation = potion.resourceLocation.isEmpty() || potion.resourceLocation.isBlank() ? "textures/custom/colors/white.png" : potion.resourceLocation;

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1, 1, 1, 1.0F);
            Identifier potionIcon = new Identifier("minecraft", resourceLocation);
            RenderSystem.setShaderTexture(0, potionIcon);

            drawTexture(matrices, x, 5, 0, 0, 22, 22, 22, 22);
            long currentTime = (potion.startTime / 1000) + potion.length - (System.currentTimeMillis() / 1000);

            drawCenteredText(matrices, this.getTextRenderer(), new LiteralText("" + Math.round(currentTime)), x + 11, 20, -1);

            x += 30;
        }

        //Display flash notification
        if(NotificationManager.showFlash) {


            //The color picker texture
            //Texture transparency for some reason is acting all funny :(
            //drawTexture(matrices, 0, 0, 0, 0, this.scaledWidth - 150, this.scaledHeight - 150);
            drawCenteredText(matrices, this.getTextRenderer(), new LiteralText(NotificationManager.currentNotif), scaledWidth / 2, scaledHeight / 2, -1);
        }
    }
}
