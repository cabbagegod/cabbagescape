package com.cabbagegod.cabbagescape.blockoutline;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class BlockOutlineManager {
    private static final BlockOutlineManager INSTANCE = new BlockOutlineManager();
    private final MinecraftClient client;

    public static BlockOutlineManager getInstance() {
        return INSTANCE;
    }


    private final List<IRenderable> renderers = new ArrayList<>();

    public void add(IRenderable renderer) {
        renderers.add(renderer);
    }

    private BlockOutlineManager() {
        client = MinecraftClient.getInstance();
    }

    public void render(MatrixStack matrices, Camera camera) {
        Vec3d vec = camera.getPos();
        Vector3d cam = new Vector3d(vec.getX(), vec.getY(), vec.getZ());
        for (IRenderable render : renderers) {
            render.render(matrices, cam, client.player);
        }
    }
}
