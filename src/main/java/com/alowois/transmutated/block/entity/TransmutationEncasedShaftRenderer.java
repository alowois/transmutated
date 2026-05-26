package com.alowois.transmutated.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.simibubi.create.foundation.gui.AllIcons;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

import com.mojang.math.Axis;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

/**
 * Renderer for the Encased Transmutation Shaft block entity.
 * Handles rendering the rotating shaft and the filter item on the block's faces.
 */
public class TransmutationEncasedShaftRenderer extends KineticBlockEntityRenderer<TransmutationEncasedShaftBlockEntity> {
    public TransmutationEncasedShaftRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(TransmutationEncasedShaftBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
        BlockState shaftState = getRenderedBlockState(be);
        RenderType type = getRenderType(be, shaftState);
        renderRotatingBuffer(be, getRotatedModel(be, shaftState), ms, buffer.getBuffer(type), light);

        renderFilter(be, ms, buffer, light, overlay);
    }

    /**
     * Renders the filter item on all valid faces of the block.
     *
     * @param be      The block entity.
     * @param ms      The pose stack.
     * @param buffer  The multi-buffer source.
     * @param light   The light level.
     * @param overlay The overlay level.
     */
    private void renderFilter(TransmutationEncasedShaftBlockEntity be, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        ItemStack filter = be.filtering.getFilter();
        if (filter.isEmpty())
            return;

        Direction.Axis axis = be.getBlockState().getValue(BlockStateProperties.AXIS);

        for (Direction side : Direction.values()) {
            // Only render on faces not occupied by the shaft axis
            if (side.getAxis() == axis)
                continue;

            ms.pushPose();
            // Center of the block
            ms.translate(0.5, 0.5, 0.5);

            // Move to the face
            float offset = 0.501f;
            ms.translate(side.getStepX() * offset, side.getStepY() * offset, side.getStepZ() * offset);

            // Rotate to face the direction
            if (side == Direction.UP) {
                ms.mulPose(Axis.XP.rotationDegrees(90));
            } else if (side == Direction.DOWN) {
                ms.mulPose(Axis.XP.rotationDegrees(-90));
            } else if (side == Direction.NORTH) {
                // Default
            } else if (side == Direction.EAST) {
                ms.mulPose(Axis.YP.rotationDegrees(270));
            } else if (side == Direction.WEST) {
                ms.mulPose(Axis.YP.rotationDegrees(90));
            } else if (side == Direction.SOUTH) {
                ms.mulPose(Axis.YP.rotationDegrees(180));
            }


            // Render the item
            ms.pushPose();
            ms.translate(0, 0, -0.002); // slight offset from background
            ms.scale(0.5f, 0.5f, 0.5f);
            ValueBoxRenderer.renderItemIntoValueBox(filter, ms, buffer, light, overlay);
            ms.popPose();

            ms.popPose();
        }
    }

    @Override
    protected BlockState getRenderedBlockState(TransmutationEncasedShaftBlockEntity be) {
        return AllBlocks.SHAFT.getDefaultState().setValue(ShaftBlock.AXIS, getRotationAxisOf(be));
    }
}
