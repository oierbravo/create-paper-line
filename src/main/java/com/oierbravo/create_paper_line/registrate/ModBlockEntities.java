package com.oierbravo.create_paper_line.registrate;

import com.oierbravo.create_paper_line.CreatePaperLine;
import com.oierbravo.create_paper_line.content.machines.dryer.DryerBlockEntity;
import com.oierbravo.create_paper_line.content.machines.dryer.DryerRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
public class ModBlockEntities {
    public static final BlockEntityEntry<DryerBlockEntity> DRYER = CreatePaperLine.registrate()
            .blockEntity("dryer", DryerBlockEntity::new)
            .validBlocks(ModBlocks.DRYER)
            .renderer(() -> DryerRenderer::new)
            .register();

    public static void register() {}
}