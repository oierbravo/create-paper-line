package com.oierbravo.create_paper_line.registrate;


import com.oierbravo.create_paper_line.CreatePaperLine;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;


public class ModCreativeTab extends CreativeModeTab {
	public static ModCreativeTab MAIN;

	public ModCreativeTab(String name) {
		super(CreatePaperLine.MODID+":"+name);
		MAIN = this;
	}

	@Override
	public ItemStack makeIcon() {
		return new ItemStack(ModBlocks.DRYER.get());
	}
}
