package com.anotherstar.common.recipe;

import java.util.Map;

import com.anotherstar.common.LoliPickaxe;
import com.anotherstar.common.item.ItemLoliPickaxeMaterial;
import com.google.common.collect.Maps;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class SuperpositionRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	private Map<Item, Integer> superpositionAble = Maps.newHashMap();

	private ItemStack resultItem = ItemStack.EMPTY;

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		resultItem = ItemStack.EMPTY;
		Item item = null;
		int damage = -1;
		int count = 0;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty()) {
				if (superpositionAble.containsKey(stack.getItem())) {
					count++;
					if (item == null) {
						item = stack.getItem();
					} else if (stack.getItem() != item) {
						return false;
					}
					if (damage == -1) {
						damage = stack.getItemDamage();
					} else if (stack.getItemDamage() != damage) {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		if (count == 9) {
			if (damage >= superpositionAble.get(item)) {
				return false;
			}
			resultItem = new ItemStack(item, 1, damage + 1);
			return true;
		} else if (count == 1) {
			if (damage <= 0) {
				return false;
			}
			resultItem = new ItemStack(item, 9, damage - 1);
			return true;
		}
		return false;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		return resultItem.copy();
	}

	@Override
	public ItemStack getRecipeOutput() {
		return resultItem;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width == 3 && height == 3 || width == 1 && height == 1;
	}

	@Override
	public boolean isDynamic() {
		return true;
	}

	@Override
	public String getGroup() {
		return LoliPickaxe.MODID + ":loli_pickaxe_core";
	}

	public void registItem(Item item, int maxDamage) {
		if (maxDamage > 0) {
			superpositionAble.put(item, maxDamage);
		}
	}

	public void registItem(ItemLoliPickaxeMaterial item) {
		superpositionAble.put(item, item.getSubCount() - 1);
	}

	public Map<Item, Integer> getSuperpositionAble() {
		return superpositionAble;
	}

}
