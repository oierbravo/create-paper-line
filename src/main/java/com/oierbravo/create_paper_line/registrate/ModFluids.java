package com.oierbravo.create_paper_line.registrate;

import com.oierbravo.create_paper_line.CreatePaperLine;
import com.simibubi.create.content.fluids.VirtualFluid;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.minecraft.resources.ResourceLocation;

public class ModFluids {
    public static final CreateRegistrate REGISTRATE = CreatePaperLine.registrate()
            .creativeModeTab(() -> ModCreativeTab.MAIN);

    public static final FluidEntry<VirtualFluid> WOOD_PULP = createVirtual( "wood_pulp")
            .lang("Wood pulp")
            .bucket()
            .build()
            .register();

    public static final FluidEntry<VirtualFluid> WHITENED_WOOD_PULP = createVirtual( "whitened_wood_pulp")
            .lang("Whitened Wood pulp")
            .bucket()
            .build()
            .register();
    private static FluidBuilder<VirtualFluid, CreateRegistrate> createVirtual(String target){
        ResourceLocation flow = new ResourceLocation(CreatePaperLine.MODID,"fluid/" + target + "_flow");
        ResourceLocation still = new ResourceLocation(CreatePaperLine.MODID,"fluid/" + target + "_still");
        return REGISTRATE.virtualFluid(target,still, flow);

    }

    public static void register() {
    }
}
