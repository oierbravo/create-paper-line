package com.oierbravo.create_paper_line;

import com.mojang.logging.LogUtils;
import com.oierbravo.create_paper_line.content.machines.dryer.DryerArmInteraction;
import com.oierbravo.create_paper_line.registrate.*;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.minecraft.resources.ResourceLocation;
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

        ModCreativeTab.register(modEventBus);
        ModBlocks.register();
        ModBlockEntities.register();
        ModItems.register();
        ModFluids.register();

        ModRecipes.register(modEventBus);


        DryerArmInteraction.register();
        generateLangEntries();


    }
    private void generateLangEntries(){

        registrate().addRawLang("itemGroup.create_paper_line:main", "Create Paper Line");
        registrate().addRawLang("config.jade.plugin_create_paper_line.data", "Dryer data");

        registrate().addRawLang("create_paper_line.recipe.drying", "Drying recipe");

        registrate().addRawLang("create_paper_line.dryer.tooltip.progress", "Progress: %d%%");

        registrate().addRawLang("block.create_paper_line.dryer.tooltip", "DRYER");
        registrate().addRawLang("block.create_paper_line.dryer.tooltip.summary", "Dry items over time");
    }
    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MODID, path);
    }

}
