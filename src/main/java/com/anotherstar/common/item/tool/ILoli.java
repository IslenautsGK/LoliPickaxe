package com.anotherstar.common.item.tool;

import net.minecraft.item.ItemStack;

public interface ILoli {
	
	static final String CONFIG = "LoliConfig";

	String getOwner(ItemStack stack);

	int getRange(ItemStack stack);
	
}
