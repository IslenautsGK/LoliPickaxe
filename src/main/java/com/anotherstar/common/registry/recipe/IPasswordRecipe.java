package com.anotherstar.common.registry.recipe;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface IPasswordRecipe extends IForgeRegistryEntry<IPasswordRecipe> {

	ItemStack getResult(InventoryCrafting inv, EntityPlayer player, String password);

}
