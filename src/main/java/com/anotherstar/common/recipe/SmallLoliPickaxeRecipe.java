package com.anotherstar.common.recipe;

import java.util.Map;
import java.util.Map.Entry;

import com.anotherstar.common.item.ItemLoader;
import com.anotherstar.common.item.ItemLoliPickaxeMaterial;
import com.anotherstar.common.item.tool.ItemSmallLoliPickaxe;
import com.google.common.collect.Maps;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class SmallLoliPickaxeRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	private ItemStack resultItem = ItemStack.EMPTY;

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		resultItem = ItemStack.EMPTY;
		ItemStack loli = ItemStack.EMPTY;
		Map<ItemLoliPickaxeMaterial, Integer> levels = Maps.newHashMap();
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() == ItemLoader.smallLoliPickaxe && loli.isEmpty()) {
					loli = stack.copy();
				} else if (ItemSmallLoliPickaxe.nbtMap.containsKey(stack.getItem()) && !levels.containsKey(stack.getItem())) {
					levels.put((ItemLoliPickaxeMaterial) stack.getItem(), stack.getItemDamage());
				} else {
					return false;
				}
			}
		}
		if (loli.isEmpty() || levels.isEmpty()) {
			return false;
		}
		NBTTagCompound nbt;
		if (loli.hasTagCompound()) {
			nbt = loli.getTagCompound();
		} else {
			nbt = new NBTTagCompound();
			loli.setTagCompound(nbt);
		}
		for (Entry<ItemLoliPickaxeMaterial, Integer> entry : levels.entrySet()) {
			String levelKey = ItemSmallLoliPickaxe.nbtMap.get(entry.getKey());
			int loliLevel;
			if (nbt.hasKey(levelKey)) {
				loliLevel = nbt.getInteger(levelKey);
			} else {
				loliLevel = -1;
			}
			if (loliLevel != entry.getValue() - 1) {
				return false;
			}
			nbt.setInteger(levelKey, loliLevel + 1);
		}
		((ItemSmallLoliPickaxe) loli.getItem()).updateEnchantment(loli);
		resultItem = loli;
		return true;
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
		return width * height >= 1;
	}

	@Override
	public boolean isDynamic() {
		return true;
	}

}
