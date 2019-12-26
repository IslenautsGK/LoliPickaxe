package com.anotherstar.common.item.tool;

import com.anotherstar.common.gui.ILoliInventory;

import net.minecraft.item.ItemStack;

public interface IContainer {
	
	boolean hasInventory(ItemStack stack);

	ILoliInventory getInventory(ItemStack stack);

}
