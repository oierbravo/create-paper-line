package com.oierbravo.create_paper_line.content.machines.dryer;

import com.oierbravo.create_paper_line.CreatePaperLine;
import com.oierbravo.create_paper_line.registrate.ModBlocks;
import com.simibubi.create.content.kinetics.mechanicalArm.AllArmInteractionPointTypes;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

public class DryerArmInteraction {

    public static final DryerType DRYER_TYPE = register("dryer", DryerType::new);

    public static void register() {}

    private static <T extends ArmInteractionPointType> T register(String id, Function<ResourceLocation, T> factory) {
        T type = factory.apply(CreatePaperLine.asResource(id));
        ArmInteractionPointType.register(type);
        return type;
    }

    public static class DryerType extends ArmInteractionPointType {

        public DryerType(ResourceLocation id) {
            super(id);
        }

        @Override
        public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
            return state.is(ModBlocks.DRYER.get());
        }

        @Override
        public ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
            return new DryerPoint(this, level, pos, state);
        }
    }

    public static class DryerPoint extends AllArmInteractionPointTypes.DepotPoint {

        public DryerPoint(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
            super(type, level, pos, state);
        }

        @Override
        public int getSlotCount() {
            return 1;
        }

        /*@Override
        public ItemStack insert(ItemStack stack, boolean simulate) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (!(blockEntity instanceof DryerBlockEntity dryerTileEntity)) {
                return stack;
            }

            ItemStack remainder = stack.copy();
            ItemStack toInsert = remainder.split(1);
            if (!simulate) {
                dryerTileEntity.inventory.insertItem(0,toInsert,false);
                dryerTileEntity.setChanged();
                dryerTileEntity.sendData();
            }
            return remainder;
        }*/
    }
}