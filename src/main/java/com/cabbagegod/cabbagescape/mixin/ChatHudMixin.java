package com.cabbagegod.cabbagescape.mixin;

import com.cabbagegod.cabbagescape.callbacks.ReceiveChatMessageCallback;
import com.cabbagegod.cabbagescape.client.LevelUpManager;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//Used to read chat messages
//If any other systems are going to use this then the message should be fed into a central system that
//other systems can read from instead of going to differently classes directly.

@Mixin(ChatHud.class)
public class ChatHudMixin {
    @Inject(at = @At("RETURN"), method = "addMessage(Lnet/minecraft/text/Text;)V")
    public void addMessage(Text message, CallbackInfo ci){
        ReceiveChatMessageCallback.EVENT.invoker().interact(message.getString());
    }
}
