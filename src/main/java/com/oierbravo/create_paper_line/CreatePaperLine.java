package com.oierbravo.create_paper_line;

import com.mojang.logging.LogUtils;
import com.oierbravo.create_paper_line.content.machines.dryer.DryerArmInteraction;
import com.oierbravo.create_paper_line.foundatation.data.ModLangPartials;
import com.oierbravo.create_paper_line.registrate.*;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.LangMerger;
import com.simibubi.create.foundation.data.TagGen;
import com.simibubi.create.foundation.data.recipe.MechanicalCraftingRecipeGen;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.data.recipe.SequencedAssemblyRecipeGen;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CreatePaperLine.MODID)
public class CreatePaperLine
{
    public static final String MODID = "create_paper_line";
    public static final String DISPLAY_NAME = "Create Paper Line";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);

    static {
        REGISTRATE.setTooltipModifierFactory(item -> {
            return new ItemDescription.Modifier(item, TooltipHelper.Palette.STANDARD_CREATE)
                    .andThen(TooltipModifier.mapNull(KineticStats.create(item)));
        });
    }
    public CreatePaperLine()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        REGISTRATE.registerEventListeners(modEventBus);


        new ModCreativeTab("main");
        ModBlocks.register();
        ModBlockEntities.register();
        ModItems.register();
        ModFluids.register();

        ModRecipes.register(modEventBus);


        DryerArmInteraction.register();

        modEventBus.addListener(EventPriority.LOWEST, CreatePaperLine::gatherData);
    }
    public static void gatherData(GatherDataEvent event) {
        //TagGen.datagen();
        DataGenerator gen = event.getGenerator();
        if (event.includeClient()) {
            gen.addProvider(true, new LangMerger(gen, MODID, DISPLAY_NAME, ModLangPartials.values()));
        }
        if (event.includeServer()) {
//            gen.addProvider(true, new AllAdvancements(gen));
           //  ProcessingRecipeGen.registerAll(gen);
//			AllOreFeatureConfigEntries.gatherData(event);
        }
    }
    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MODID, path);
    }

}
