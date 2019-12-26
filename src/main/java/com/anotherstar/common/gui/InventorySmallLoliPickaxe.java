package com.anotherstar.common.gui;

import com.anotherstar.common.item.tool.ItemSmallLoliPickaxe;

import net.minecraft.item.ItemStack;

public class InventorySmallLoliPickaxe extends InventoryLoliBase {

	private ItemStack stack;

	public InventorySmallLoliPickaxe(ItemStack stack) {
		super(stack);
		this.stack = stack;
	}

	@Override
	public String getName() {
		return "container.smallLoliPickaxe";
	}

	@Override
	public int getMaxPage() {
		return ((ItemSmallLoliPickaxe) stack.getItem()).getMaxPage(stack);
	}

}
