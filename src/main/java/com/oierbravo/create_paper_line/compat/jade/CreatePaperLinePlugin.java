package com.oierbravo.create_paper_line.compat.jade;

import com.oierbravo.create_paper_line.content.machines.dryer.DryerBlock;
import com.oierbravo.create_paper_line.content.machines.dryer.DryerBlockEntity;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class CreatePaperLinePlugin implements IWailaPlugin {
    public static final ResourceLocation MOD_DATA = new ResourceLocation("create_paper_line:data");

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(new ProgressComponentProvider(), DryerBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(new ProgressComponentProvider(), DryerBlock.class);
    }
}
