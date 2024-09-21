package com.oierbravo.create_paper_line.compat.jade;

import com.oierbravo.create_paper_line.content.machines.dryer.DryerBlockEntity;
import com.oierbravo.create_paper_line.foundatation.utility.ModLang;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.api.ui.IProgressStyle;
import snownee.jade.util.Color;

public class ProgressComponentProvider  implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        //CompoundTag serverData = accessor.getServerData();
        if(!isWorking(accessor))
            return;
        if (accessor.getServerData().contains("dryer.progress")) {
            int progress = accessor.getServerData().getInt("dryer.progress");
            IElementHelper elementHelper = tooltip.getElementHelper();
            if(progress > 0)
                tooltip.add(elementHelper.progress((float)progress / 100, ModLang.translate("dryer.tooltip.progress", progress).component(),elementHelper.progressStyle().color(Color.hex("#FFFF00").toInt()), BoxStyle.DEFAULT,true));
        }

    }

    private boolean isWorking(BlockAccessor accessor){
        if (accessor.getServerData().contains("dryer.is_working")) {
            return accessor.getServerData().getBoolean("dryer.is_working");
        }
        return false;
    }


    @Override
    public ResourceLocation getUid() {
        return CreatePaperLinePlugin.MOD_DATA;
    }


    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        if(blockAccessor.getBlockEntity() instanceof DryerBlockEntity){
            DryerBlockEntity dryerBlockEntity = (DryerBlockEntity) blockAccessor.getBlockEntity();
            compoundTag.putBoolean("dryer.is_working",dryerBlockEntity.isWorking());
            compoundTag.putInt("dryer.progress",dryerBlockEntity.getProgressPercent());
        }
    }
}
