package com.anotherstar.common.crafting;

import com.anotherstar.common.block.BlockLoader;
import com.anotherstar.common.item.ItemLoader;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CraftingLoader {

	public static void init() {
		registerRecipe();
		registerSmelting();
		registerFuel();
	}

	private static void registerFuel() {
		GameRegistry.registerFuelHandler(
				fuel -> Item.getItemFromBlock(BlockLoader.blockLoli) != fuel.getItem() ? 0 : 12800);
		GameRegistry.registerFuelHandler(fuel -> ItemLoader.loliCard != fuel.getItem() ? 0 : 400);
		GameRegistry.registerFuelHandler(fuel -> ItemLoader.loliPickaxe != fuel.getItem() ? 0 : 102400);
	}

	private static void registerSmelting() {
		GameRegistry.addSmelting(Item.getItemFromBlock(BlockLoader.blockLoli), new ItemStack(ItemLoader.loliRecord),
				0.0F);
	}

	private static void registerRecipe() {
		GameRegistry.addShapedRecipe(new ItemStack(BlockLoader.blockLoli),
				new Object[] { "###", "#*#", "###", '#', Items.gold_ingot, '*', Items.nether_star });
		/*
		 * GameRegistry.addShapedRecipe(new ItemStack(ItemLoader.loliPickaxe), new
		 * Object[] { "###", " * ", " * ", '#', BlockLoader.blockLoli, '*',
		 * Items.nether_star });
		 */
	}

}
