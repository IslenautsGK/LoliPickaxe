package com.anotherstar.common.item.tool;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface ILoli extends IContainer {

	static final String CONFIG = "LoliConfig";

	boolean hasOwner(ItemStack stack);

	boolean isOwner(ItemStack stack, EntityPlayer player);

	int getRange(ItemStack stack);

}
