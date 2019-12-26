package com.anotherstar.common.gui;

import com.anotherstar.common.config.ConfigLoader;

import net.minecraft.item.ItemStack;

public class InventoryLoliPickaxe extends InventoryLoliBase {

	public InventoryLoliPickaxe(ItemStack stack) {
		super(stack);
	}

	@Override
	public String getName() {
		return "container.loliPickaxe";
	}

	@Override
	public int getMaxPage() {
		return ConfigLoader.loliPickaxeMaxPage;
	}

}
