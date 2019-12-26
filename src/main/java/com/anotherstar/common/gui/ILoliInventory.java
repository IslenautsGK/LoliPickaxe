package com.anotherstar.common.gui;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public interface ILoliInventory extends IInventory {

	int getMaxPage();
	
	NonNullList<ItemStack> getPage(int index);

}
