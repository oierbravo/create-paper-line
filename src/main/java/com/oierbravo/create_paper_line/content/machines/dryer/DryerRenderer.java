package com.oierbravo.create_paper_line.content.machines.dryer;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import com.simibubi.create.foundation.utility.AngleHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;

public class DryerRenderer extends SmartBlockEntityRenderer<DryerBlockEntity> {

    public DryerRenderer(BlockEntityRendererProvider.Context context) {
        super(context);

    }
    @Override
    protected void renderSafe(DryerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
        ItemStack itemStack = be.getItemStack();
        if (itemStack.isEmpty())
            return;

        BlockState blockState = be.getBlockState();
        Direction facing = blockState.getValue(DryerBlock.FACING);
        AttachFace face = blockState.getValue(DryerBlock.FACE);

        ItemRenderer itemRenderer = Minecraft.getInstance()
                .getItemRenderer();
        boolean blockItem = itemRenderer.getModel(itemStack, null, null, 0)
                .isGui3d();

        ms.pushPose();
        TransformStack.cast(ms)
                .centre()
                .rotate(Direction.UP,
                        (face == AttachFace.CEILING ? Mth.PI : 0) + AngleHelper.rad(180 + AngleHelper.horizontalAngle(facing)))
                .rotate(Direction.EAST,
                        face == AttachFace.CEILING ? -Mth.PI / 2 : face == AttachFace.FLOOR ? Mth.PI / 2 : 0)
                .translate(0, 0, blockItem ? 3 / 16f : 5 / 16f)
                .scale(blockItem ? .75f : .7f);

        itemRenderer.renderStatic(itemStack, ItemTransforms.TransformType.FIXED, light, overlay, ms, buffer, 0);
        ms.popPose();
    }
}
