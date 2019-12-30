package com.anotherstar.common.gui;

import com.anotherstar.common.item.ItemLoader;

import net.minecraft.item.ItemStack;

public class InventorySmallLoliPickaxe extends InventoryLoliBase {

	private ItemStack stack;

	public InventorySmallLoliPickaxe(ItemStack stack) {
		super(stack);
		this.stack = stack;
	}

	@Override
	public int getInventoryStackLimit() {
		return getMaxPage() * 32;
	}

	@Override
	public String getName() {
		return "container.smallLoliPickaxe";
	}

	@Override
	public int getMaxPage() {
		return ItemLoader.smallLoliPickaxe.getMaxPage(stack);
	}

	@Override
	public boolean cancelStackLimit() {
		return true;
	}

}
