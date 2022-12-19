package com.cabbagegod.cabbagescape.mixin;

import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {
    @ModifyArg(method = "renderLabelIfPresent(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"), index = 1)
    public Text renderLabelIfPresent(Text par2){
        if(par2.getString().contains("CabbageGod")){
            String nameStr = par2.getString();

            StringBuilder sb = new StringBuilder(nameStr);
            sb.insert(0, "\2477");
            int index = sb.toString().indexOf("CabbageGod");
            sb.insert(index, "逬\2472");
            index = sb.toString().indexOf("CabbageGod");
            sb.insert(index + 10, "\247r\2477");

            par2 = new LiteralText(sb.toString());
        }

        return par2;
    }
}
