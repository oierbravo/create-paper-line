package com.oierbravo.create_paper_line.registrate;


import com.oierbravo.create_paper_line.CreatePaperLine;
import com.simibubi.create.AllCreativeModeTabs;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;


public class ModCreativeTab{

	private static final DeferredRegister<CreativeModeTab> TAB_REGISTER =
			DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreatePaperLine.MODID);

	public static final RegistryObject<CreativeModeTab> MAIN_TAB = TAB_REGISTER.register("main",
			() -> CreativeModeTab.builder()
					.title(Component.translatable("itemGroup.create_paper_line:main"))
					.withTabsBefore(AllCreativeModeTabs.PALETTES_CREATIVE_TAB.getId())
					.icon(ModBlocks.DRYER::asStack)
					.displayItems((pParameters, pOutput) -> {
						for (RegistryEntry<Item> entry : CreatePaperLine.REGISTRATE.getAll(Registries.ITEM)) {
							pOutput.accept(entry.get());
						}
					})
					.build());

	public static void register(IEventBus modEventBus) {
		TAB_REGISTER.register(modEventBus);
	}
}
