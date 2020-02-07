package com.anotherstar.common.recipe.password;

import com.anotherstar.common.registry.RegistryLoader;
import com.anotherstar.common.registry.recipe.IPasswordRecipe;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public class PasswordRecipeManager {

	public static ItemStack findMatchingResult(InventoryCrafting craftMatrix, EntityPlayer player, String password) {
		for (IPasswordRecipe recipe : RegistryLoader.PASSWORD_RECIPE_REGISTRY) {
			ItemStack result = recipe.getResult(craftMatrix, player, password);
			if (!result.isEmpty()) {
				return result;
			}
		}
		return ItemStack.EMPTY;
	}

}
