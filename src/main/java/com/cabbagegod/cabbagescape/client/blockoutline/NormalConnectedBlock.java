package com.cabbagegod.cabbagescape.client.blockoutline;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;

public class NormalConnectedBlock extends AbstractConnectedBlock {
    public NormalConnectedBlock(BlockPosState block) {
        super(block);
    }

    @Override
    public void populate(MinecraftClient client, Entity entity) {
        // Just setup this shape. No children.
        updateShape(client, entity);
    }
}
