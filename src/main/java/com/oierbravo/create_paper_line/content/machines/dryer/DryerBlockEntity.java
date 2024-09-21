package com.oierbravo.create_paper_line.content.machines.dryer;

import com.oierbravo.create_paper_line.foundatation.utility.ModLang;
import com.oierbravo.create_paper_line.registrate.ModRecipes;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.equipment.goggles.IHaveHoveringInformation;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.content.kinetics.fan.EncasedFanBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public class DryerBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation, IHaveHoveringInformation {
    protected SmartInventory inventory;
    protected LazyOptional<IItemHandlerModifiable> itemCapability;

    private boolean contentsChanged;

    private boolean working;

    private int progress;

    private int processingTime = 0;

    private DryingRecipe lastRecipe;

    public DryerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        inventory = new SmartInventory(1, this,1, false);
        itemCapability = LazyOptional.of(() -> new CombinedInvWrapper(inventory));
        inventory.whenContentsChanged($ -> {
            contentsChanged = true;
            if(inventory.isEmpty())
                inventory.allowInsertion();

        });

        contentsChanged = false;
        working = false;
        inventory.forbidExtraction();

    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(new DirectBeltInputBehaviour(this));

    }
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        inventory.deserializeNBT(compound.getCompound("Inventory"));
        progress = compound.getInt("Progress");
        processingTime = compound.getInt("ProcessingTime");
    }
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("Inventory", inventory.serializeNBT());
        compound.putInt("Progress",progress);
        compound.putInt("ProcessingTime", processingTime);
    }
    @Override
    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(level, worldPosition, inventory);
    }
    @Override
    public void invalidate() {
        super.invalidate();
        itemCapability.invalidate();

    }
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return itemCapability.cast();
        return super.getCapability(cap, side);
    }
    public boolean isEmpty() {
        return inventory.isEmpty();
    }
    public ItemStack getItemStack(){
        return inventory.getStackInSlot(0);
    }
    protected SimpleContainer getSimpleContainer() {
        return new SimpleContainer(inventory.getStackInSlot(0));
    }
    protected void setRecipe(DryingRecipe recipe){
        lastRecipe = recipe;
        progress = 0;
        processingTime = recipe.getProcessingTime();
    }

    public int getProgressPercent(){
        if(!isWorking())
            return 0;
        return progress * 100 / processingTime;
    }
    @Override
    public void tick() {
        super.tick();

        if (progress < processingTime) {
            if(progress > 0){
                setWorking(true);
            }
            float progressMultiplier = getFanSpeedMultiplier();
            progress += (int) (1 * progressMultiplier);

            if (level.isClientSide) {
                spawnParticles();
                return;
            }
            if (progress >= processingTime)
                processRecipe();
            return;
        }

        if (inventory
                .getStackInSlot(0)
                .isEmpty())
            return;

        if( lastRecipe == null || !lastRecipe.matches(getSimpleContainer(),this.getLevel())){
            Optional<DryingRecipe> recipe = ModRecipes.findDrying(inventory.getStackInSlot(0), level);
            if (!recipe.isPresent()) {
                resetProgress();
            } else {
                setRecipe(recipe.get());
            }
            sendData();
            return;
        }
        processingTime = lastRecipe.getProcessingTime();
        sendData();
    }

    private float getFanSpeedMultiplier() {
        BlockPos below = getBlockPos().below();
        BlockEntity belowBlockEntity =  level.getBlockEntity(below);
        if(belowBlockEntity instanceof EncasedFanBlockEntity){
            float fanSpeed = ((EncasedFanBlockEntity) belowBlockEntity).getSpeed();
            if(fanSpeed > 0)
                return 1 + fanSpeed/64;
        }

        return 1;
    }

    private void processRecipe(){
        if(lastRecipe != null) {
            inventory.setStackInSlot(0, lastRecipe.assemble(getSimpleContainer(), level.registryAccess()));
            resetProgress();
            sendData();
            setChanged();
        }
    }
    public void spawnParticles() {
        if(!isWorking())
            return;

        Vec3 offset = new Vec3(0f, 0f, 0f);

        Vec3 center = offset.add(VecHelper.getCenterOf(worldPosition));

        assert level != null;
        level.addParticle(ParticleTypes.POOF, center.x, center.y , center.z, 0, 0.01, 0);

    }
    public boolean isWorking(){
        return true;
    }
    protected void setWorking(boolean working){
        this.working = working;
        if(!working){
            inventory.allowExtraction();
        }
        if(working)
            inventory.forbidExtraction();

    }

    public void resetProgress() {
        progress = 0;
        processingTime = 0;
        //lastRecipe = null;
        setWorking(false);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean added = IHaveGoggleInformation.super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        if(this.progress < this.processingTime && progress > 0) {
            ModLang.translate("dryer.tooltip.progress", this.getProgressPercent()).style(ChatFormatting.YELLOW).forGoggles(tooltip);
            return true;
        }
        return added;
    }






}
