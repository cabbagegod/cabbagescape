package com.cabbagegod.cabbagescape.blockoutline;

import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractConnectedBlock {

    protected final BlockPosState block;
    protected final Vec3i offset;
    protected VoxelShape shape;
    protected ArrayList<AbstractConnectedBlock> children = new ArrayList<>();

    public AbstractConnectedBlock(BlockPosState block) {
        this(block, new Vec3i(0, 0, 0));
    }

    public AbstractConnectedBlock(BlockPosState block, Vec3i offset) {
        this.block = block;
        this.offset = offset;
    }

    /**
     * The offset from the parent block in X,Y,Z. Should be in block units.
     */
    public Vec3i getOffset() {
        return offset;
    }

    /**
     * The shape of the block that is used to render.
     */
    public VoxelShape getShape() {
        return shape;
    }

    /**
     * Update's the voxel shape. By default, it just grabs the cube outline if configured, or just the vanilla one.
     */
    public void updateShape(MinecraftClient client, Entity entity) {
        shape = VoxelShapes.fullCube();
        return;

        /*
        if (ConfigStorage.General.CUBE_OUTLINE.config.getBooleanValue()) {
            shape = VoxelShapes.fullCube();
            return;
        }
        this.shape = block.getState().getOutlineShape(client.world, block.getPos(), ShapeContext.of(entity));*/
    }

    /**
     * Constructs the children and voxel shapes for everything. This is called when it's getting
     * ready to pass the data.
     */
    public abstract void populate(MinecraftClient client, Entity entity);

    /**
     * Gets the children that are connected to this block. Cannot be null, but can be empty.
     */
    public List<AbstractConnectedBlock> getChildren() {
        return children;
    }
}
