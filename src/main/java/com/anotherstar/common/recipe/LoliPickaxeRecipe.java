package com.anotherstar.common.recipe;

import java.util.Map.Entry;

import com.anotherstar.common.item.ItemLoader;
import com.anotherstar.common.item.ItemLoliPickaxeMaterial;
import com.anotherstar.common.item.tool.ItemLoliPickaxe;
import com.anotherstar.common.item.tool.ItemSmallLoliPickaxe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class LoliPickaxeRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	private ItemStack resultItem = ItemStack.EMPTY;

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		ItemStack loli = ItemStack.EMPTY;
		ItemStack soul = ItemStack.EMPTY;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() == ItemLoader.smallLoliPickaxe && loli.isEmpty()) {
					loli = stack;
				} else if (stack.getItem() == ItemLoader.entitySoul && soul.isEmpty()) {
					soul = stack;
				} else {
					return false;
				}
			}
		}
		if (loli.isEmpty() || !loli.hasTagCompound() || soul.isEmpty() || soul.getItemDamage() != ItemLoader.entitySoul.getSubCount() - 1) {
			return false;
		}
		NBTTagCompound nbt = loli.getTagCompound();
		for (Entry<ItemLoliPickaxeMaterial, String> entry : ItemSmallLoliPickaxe.nbtMap.entrySet()) {
			if (!nbt.hasKey(entry.getValue()) || nbt.getInteger(entry.getValue()) != entry.getKey().getSubCount() - 1) {
				return false;
			}
		}
		resultItem = ItemLoliPickaxe.getDef().copy();
		if (loli.getTagCompound().hasKey("Pages")) {
			resultItem.getTagCompound().setTag("Pages", loli.getTagCompound().getCompoundTag("Pages"));
		}
		if (loli.getTagCompound().hasKey("Blacklist")) {
			resultItem.getTagCompound().setTag("Blacklist", loli.getTagCompound().getCompoundTag("Blacklist"));
		}
		return true;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		return resultItem.copy();
	}

	@Override
	public ItemStack getRecipeOutput() {
		return ItemLoliPickaxe.getDef().copy();
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 1;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> list = NonNullList.create();
		list.add(Ingredient.fromStacks(ItemSmallLoliPickaxe.getFull()));
		list.add(Ingredient.fromStacks(new ItemStack(ItemLoader.entitySoul, 1, ItemLoader.entitySoul.getSubCount() - 1)));
		return list;
	}

	@Override
	public String getGroup() {
		return "loli_pickaxe";
	}

}
