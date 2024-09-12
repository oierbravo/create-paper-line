package com.oierbravo.create_paper_line.registrate;

import com.oierbravo.create_paper_line.CreatePaperLine;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import static com.simibubi.create.Create.REGISTRATE;

public class ModItems {


    private static final CreateRegistrate REGISTRATE = CreatePaperLine.registrate()
            .setCreativeTab(ModCreativeTab.MAIN_TAB);

    public static final ItemEntry<Item> CARD_BOARD_SHEET = REGISTRATE.item("cardboard_sheet", Item::new).register();
    public static final ItemEntry<Item> FRAME = REGISTRATE.item("frame", Item::new)
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get())
                    .define('S', Items.STICK)
                    .pattern(" S ")
                    .pattern("S S")
                    .pattern(" S ")
                    .unlockedBy("has_stick", RegistrateRecipeProvider.has(Items.STICK))
                    .save(p, CreatePaperLine.asResource("crafting/" + c.getName())))
            .register();

    public static final ItemEntry<Item> SAW_DUST = REGISTRATE.item("saw_dust", Item::new).register();
    public static final ItemEntry<Item> WET_CARDBOARD_SHEET = REGISTRATE.item("wet_cardboard_sheet", Item::new).register();
    public static final ItemEntry<Item> WET_PAPER_SHEET = REGISTRATE.item("wet_paper_sheet", Item::new).register();
    public static final ItemEntry<Item> WOOD_CHIPS = REGISTRATE.item("wood_chips", Item::new).register();

    public static final ItemEntry<SequencedAssemblyItem> INCOMPLETE_WET_CARDBOARD_SHEET = REGISTRATE.item("incomplete_wet_cardboard_sheet", SequencedAssemblyItem::new)
            .register();
    public static final ItemEntry<SequencedAssemblyItem> INCOMPLETE_WET_PAPER_SHEET = REGISTRATE.item("incomplete_wet_paper_sheet", SequencedAssemblyItem::new)
            .register();

    public static final ItemEntry<SequencedAssemblyItem> INCOMPLETE_BOOK = REGISTRATE.item("incomplete_book", SequencedAssemblyItem::new)
            .register();

    public static void register() {}

}
