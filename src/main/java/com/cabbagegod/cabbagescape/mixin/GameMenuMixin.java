package com.cabbagegod.cabbagescape.mixin;

import com.cabbagegod.cabbagescape.ui.SettingsScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public class GameMenuMixin extends Screen {
    protected GameMenuMixin(Text title) {
        super(title);
    }

    @Inject (
        method = "initWidgets()V",
        at = @At(value = "HEAD")
    )
    public void initWidgets(CallbackInfo ci){
        addDrawableChild(new ButtonWidget(this.width / 2 - 102, this.height / 4 + 144 + -16, 204, 20, new LiteralText("§2CabbageScape §rSettings"), button -> {
            MinecraftClient.getInstance().setScreen(new SettingsScreen());
        }));
    }
}
