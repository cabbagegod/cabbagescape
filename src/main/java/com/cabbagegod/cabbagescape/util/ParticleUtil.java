package com.cabbagegod.cabbagescape.util;

import com.cabbagegod.cabbagescape.blockoutline.Vector3f;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3f;

import java.awt.*;

public class ParticleUtil {
    public static void CreateSpiralParticle(Vector3f position, Color color){
        CreateSpiralParticle(position, color, 50);
    }

    public static void CreateSpiralParticle(Vector3f position, Color color, float size){
        for (int i = 0; i < size; i++) {
            var circleHeight = 2 / size;
            var circleSize = 1 - (i * (circleHeight/2));

            var xPos = Math.sin(i * .25f) * circleSize;
            var yPos = i * circleHeight;
            var zPos = Math.cos(i * .25f) * circleSize;

            assert MinecraftClient.getInstance().player != null;

            float r = color.getRed();
            float g = color.getGreen();
            float b = color.getBlue();

            DustParticleEffect dust = new DustParticleEffect(new Vec3f(r / 255,g / 255,b / 255), 1);
            MinecraftClient.getInstance().player.clientWorld.addParticle(dust, position.x + xPos, position.y + yPos, position.z  + zPos, 0, 0, 0);
        }
    }

    public static void CreateParticleAtBlock(ParticleEffect particleEffect, BlockPos pos){
        if(MinecraftClient.getInstance().player == null)
            return;

        MinecraftClient.getInstance().player.clientWorld.addParticle(particleEffect, pos.getX() + .5f, pos.getY(), pos.getZ() + .5f, 0, 0, 0);
    }
}
