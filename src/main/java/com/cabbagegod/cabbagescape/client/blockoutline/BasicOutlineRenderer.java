package com.cabbagegod.cabbagescape.client.blockoutline;

import com.cabbagegod.cabbagescape.client.CabbageScapeClient;
import com.cabbagegod.cabbagescape.data.GroundItemSettings;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class BasicOutlineRenderer implements IOverlayRenderer {
    public BasicOutlineRenderer() {}

    @Override
    public boolean render(MatrixStack matrices, Vector3d camera, Entity entity, AbstractConnectedBlock block) {
        /*
        ConnectType type = (ConnectType) ConfigStorage.General.CONNECT_TYPE.config.getOptionListValue();
        if (type == ConnectType.NONE || type == ConnectType.BLOCKS || block.getChildren().size() == 0) {
            renderShape(matrices, camera, entity, block.getBlock(), block.getShape());
            if (type == ConnectType.BLOCKS) {
                for (AbstractConnectedBlock child : block.getChildren()) {
                    renderShape(matrices, camera, entity, child.getBlock(), child.getShape());
                }
            }
            return true;
        }*/

        VoxelShape shape = block.getShape();
        for (AbstractConnectedBlock child : block.getChildren()) {
            Vec3i offset = child.getOffset();
            shape = VoxelShapes.union(shape, child.getShape().offset(offset.getX(), offset.getY(), offset.getZ()));
        }
        renderShape(matrices, camera, entity, block.block, shape);

        /*
        if (type == ConnectType.SEAMLESS) {
            VoxelShape shape = block.getShape();
            for (AbstractConnectedBlock child : block.getChildren()) {
                Vec3i offset = child.getOffset();
                shape = VoxelShapes.union(shape, child.getShape().offset(offset.getX(), offset.getY(), offset.getZ()));
            }
            renderShape(matrices, camera, entity, block.getBlock(), shape);
        }*/

        //matrices.push();
        return true;
    }

    public void renderShape(MatrixStack matrices, Vector3d camera, Entity entity, BlockPosState block, VoxelShape outline) {
        Vector3d camDif = RenderingUtil.getCameraOffset(camera, block.getPos());
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        // Setup rendering
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        //RenderUtils.setupBlend(); // Alpha actually does stuff
        RenderingUtil.setDepth(false); // See through
        RenderSystem.disableCull();
        RenderSystem.depthMask(false);

        GroundItemSettings settings = CabbageScapeClient.settings.groundItemSettings;

        // Allow glass and other translucent/transparent objects to render properly
        Color fillColor = new Color((int) settings.itemRed,(int) settings.itemGreen,(int) settings.itemBlue, 0);
        if (fillColor.getAlpha() > 0) {
            drawOutlineBoxes(tessellator, matrices, buffer, camDif, fillColor, outline);
        }

        Color lineColor = new Color((int) settings.itemRed,(int) settings.itemGreen,(int) settings.itemBlue, 255);
        if (lineColor.getAlpha() > 0) {
            drawOutlineLines(tessellator, matrices, buffer, camDif, lineColor, outline);
        }

        RenderSystem.depthMask(true);
        RenderingUtil.setDepth(true);
        RenderSystem.enableCull();
    }

    /**
     * Draws boxes for an outline. Depth and blending should be set before this is called.
     */
    private void drawOutlineBoxes(Tessellator tessellator, MatrixStack matrices, BufferBuilder buffer, Vector3d camDif, Color color, VoxelShape outline) {
        MatrixStack.Entry entry = matrices.peek();

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        // Divide into each edge and draw all of them
        outline.forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> {
            // Fix Z fighting
            minX -= .001;
            minY -= .001;
            minZ -= .001;
            maxX += .001;
            maxY += .001;
            maxZ += .001;
            RenderingUtil.drawBox(entry, buffer, camDif, (float) minX, (float) minY, (float) minZ, (float) maxX, (float) maxY, (float) maxZ, color);
        });
        tessellator.draw();
    }

    /**
     *  Renders an outline.
     *  Before calling blend and depth should be set
     */

    private void drawOutlineLines(Tessellator tessellator, MatrixStack matrices, BufferBuilder buffer, Vector3d camDif, Color color, VoxelShape outline) {
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        RenderSystem.setShader(GameRenderer::getRenderTypeLinesShader);
        RenderSystem.lineWidth((float) 4f);

        buffer.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);

        drawOutlineLine(tessellator, matrices.peek(), buffer, camDif, color, outline);

        // Revert some changes
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

    /**
     *  Draws an outline. Setup should be done before this method is called.
     */
    private void drawOutlineLine(Tessellator tessellator, MatrixStack.Entry entry, BufferBuilder buffer, Vector3d camDif, Color color, VoxelShape outline) {
        outline.forEachEdge((minX, minY, minZ, maxX, maxY, maxZ) -> {
            // Fix Z fighting
            minX -= .001;
            minY -= .001;
            minZ -= .001;
            maxX += .001;
            maxY += .001;
            maxZ += .001;
            RenderingUtil.drawLine(entry, buffer, camDif, new Vector3f(minX, minY, minZ), new Vector3f(maxX, maxY, maxZ), color);
        });
        tessellator.draw();
    }
}
