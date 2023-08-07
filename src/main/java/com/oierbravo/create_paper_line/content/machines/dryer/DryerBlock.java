package com.oierbravo.create_paper_line.content.machines.dryer;

import com.oierbravo.create_paper_line.registrate.ModBlockEntities;
import com.oierbravo.create_paper_line.registrate.ModShapes;
import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class DryerBlock extends FaceAttachedHorizontalDirectionalBlock implements IBE<DryerBlockEntity>, IWrenchable {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public DryerBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState().setValue(POWERED, false));
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(FACE, FACING, POWERED));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState stateForPlacement = super.getStateForPlacement(pContext);
        if (stateForPlacement == null)
            return null;
        if (stateForPlacement.getValue(FACE) == AttachFace.FLOOR)
            stateForPlacement = stateForPlacement.setValue(FACING, stateForPlacement.getValue(FACING)
                    .getOpposite());
        return stateForPlacement;
    }
    @Override
    public Class<DryerBlockEntity> getBlockEntityClass() {
        return DryerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends DryerBlockEntity> getBlockEntityType() {
        return ModBlockEntities.DRYER.get();
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.hasBlockEntity() && state.getBlock() != newState.getBlock()) {
            withBlockEntityDo(worldIn, pos, DryerBlockEntity::setRemoved);

            worldIn.removeBlockEntity(pos);
        }
    }
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return ModShapes.DRYER.get(getConnectedDirection(pState));
    }
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player player, InteractionHand pHand,
                                 BlockHitResult pHit) {
      //  if (player.isShiftKeyDown())
       //     return InteractionResult.PASS;
        if (pLevel.isClientSide)
            return InteractionResult.SUCCESS;
        ItemStack heldItem = player.getItemInHand(pHand);

        return onBlockEntityUse(pLevel, pPos, be -> {

            IItemHandlerModifiable inv = be.itemCapability.orElse(new ItemStackHandler(1));
            boolean success = false;
                ItemStack stackInSlot = inv.getStackInSlot(0);
                if(!player.isShiftKeyDown() && heldItem.isEmpty()){
                    return InteractionResult.PASS;
                }
                if (!player.isShiftKeyDown() && stackInSlot.isEmpty()){

                   // ItemStack insertItem = ItemHandlerHelper.insertItem(be.inventory, heldItem.getItem()
                    //        .copy(), false);
                    ItemStack itemToInsert = new ItemStack(heldItem.getItem(),1);
                    ItemStack insertedItemStack = be.inventory.insertItem(0, itemToInsert, false);
                    //ItemStack insertedItemStack = ItemHandlerHelper.insertItem(be.inventory, new ItemStack(heldItem.getItem(),1), false);
                    heldItem.shrink(1);

                }
                if(player.isShiftKeyDown() && !stackInSlot.isEmpty()){
                    player.getInventory()
                            .placeItemBackInInventory(stackInSlot);
                    inv.setStackInSlot(0, ItemStack.EMPTY);
                }


                pLevel.playSound(null, pPos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, .2f,
                        1f + Create.RANDOM.nextFloat());
            return InteractionResult.SUCCESS;
        });
    }
/*
    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, worldIn, pos, oldState, isMoving);
    }
*/
}
