package com.anotherstar.client.creative;

import com.anotherstar.common.item.ItemLoader;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTabLoader {

	public static CreativeTabs loliTabs;

	public static CreativeTabs loliRecipeTabs;

	public static void init() {
		loliTabs = new CreativeTabs("loli") {

			@Override
			public ItemStack getTabIconItem() {
				return new ItemStack(ItemLoader.loliPickaxe);
			}

		};

		loliRecipeTabs = new CreativeTabs("loliRecipe") {

			@Override
			public ItemStack getTabIconItem() {
				return new ItemStack(ItemLoader.entitySoul, 1, ItemLoader.entitySoul.getSubCount() - 1);
			}

		};
	}

}
