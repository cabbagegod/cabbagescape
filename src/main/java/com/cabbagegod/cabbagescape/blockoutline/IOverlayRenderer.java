package com.cabbagegod.cabbagescape.blockoutline;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.entity.Entity;

public interface IOverlayRenderer {
    boolean render(MatrixStack matrices, Vector3d camera, Entity entity, AbstractConnectedBlock block);
}
