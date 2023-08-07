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
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.api.ui.IProgressStyle;

public class ProgressComponentProvider  implements IBlockComponentProvider, IServerDataProvider<BlockEntity> {

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        //CompoundTag serverData = accessor.getServerData();
        if (accessor.getServerData().contains("dryer.progress")) {
            int progress = accessor.getServerData().getInt("dryer.progress");
            IElementHelper elementHelper = tooltip.getElementHelper();
            IProgressStyle progressStyle = elementHelper.progressStyle();
            if(progress > 0)
                tooltip.add(elementHelper.progress((float)progress / 100, ModLang.translate("dryer.tooltip.progress", progress).component(), progressStyle,elementHelper.borderStyle()));
        }

    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if(blockEntity instanceof DryerBlockEntity){
            DryerBlockEntity melter = (DryerBlockEntity) blockEntity;
            compoundTag.putInt("melter.progress",melter.getProgressPercent());
        }
    }

    @Override
    public ResourceLocation getUid() {
        return CreatePaperLinePlugin.MOD_DATA;
    }
}
