package com.oierbravo.create_paper_line.content.machines.dryer;

import com.oierbravo.create_paper_line.foundatation.utility.ModLang;
import com.oierbravo.create_paper_line.registrate.ModRecipes;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.equipment.goggles.IHaveHoveringInformation;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
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

    private int processingTime = 100;

    private DryingRecipe lastRecipe;

    public DryerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        inventory = new SmartInventory(1, this,1, false);
        itemCapability = LazyOptional.of(() -> new CombinedInvWrapper(inventory));
        inventory.whenContentsChanged($ -> contentsChanged = true);

        contentsChanged = true;
        working = true;
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
        return progress * 100 / processingTime;
    }
    @Override
    public void tick() {
        super.tick();
        assert level != null;

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

        if (progress < processingTime) {
            if(progress > 0){
                setWorking(true);
            }
            progress += 1;

            if (level.isClientSide) {
                spawnParticles();
            }
            return;
        }
        processRecipe();
        resetProgress();
        sendData();
    }
    private void processRecipe(){
        inventory.setStackInSlot(0,lastRecipe.assemble(getSimpleContainer()));
    }
    public void spawnParticles() {
        ItemStack stackInSlot = inventory.getStackInSlot(0);
        if (stackInSlot.isEmpty())
            return;

        //SimpleParticleType data = new SimpleParticleType(false);
        Vec3 offset = new Vec3(0.1f, 0, 8 /16f);

        Vec3 center = offset.add(VecHelper.getCenterOf(worldPosition));
        Vec3 target = VecHelper.rotate(offset, 90, Direction.Axis.Y);


        assert level != null;
        //level.addParticle(ParticleTypes.WHITE_ASH,  center.x, center.y, center.z, .05f, .05f, .05f);
        level.addParticle(ParticleTypes.POOF, center.x, center.y + .25f, center.z, 0, 1 / 16f, 0);

        //level.addParticle(ParticleTypes.COMPOSTER, (double)pPos.getX() + (double)0.13125F + (double)0.7375F * (double)randomsource.nextFloat(), (double)pPos.getY() + d0 + (double)randomsource.nextFloat() * (1.0D - d0), (double)pPos.getZ() + (double)0.13125F + (double)0.7375F * (double)randomsource.nextFloat(), d3, d4, d5);

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
        processingTime = 100;
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


    /*@Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);

        createTeleport();
        behaviours.add(teleport);
    }*/

    /*protected void createTeleport() {
         teleport = new TeleportLinkBehaviour(this);
    }
*/





}
