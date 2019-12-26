package com.anotherstar.common.recipe;

import com.anotherstar.common.LoliPickaxe;
import com.anotherstar.common.item.ItemLoader;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RecipeLoader {

	@SubscribeEvent
	public void registerItem(RegistryEvent.Register<IRecipe> event) {
		event.getRegistry().register(new SuperpositionRecipe().setRegistryName(LoliPickaxe.MODID, "loli_superposition"));
		SuperpositionRecipe.registItem(ItemLoader.ironAddon);
		SuperpositionRecipe.registItem(ItemLoader.goldAddon);
		SuperpositionRecipe.registItem(ItemLoader.diamondAddon);
		SuperpositionRecipe.registItem(ItemLoader.emeraldAddon);
		SuperpositionRecipe.registItem(ItemLoader.redstoneAddon);
		SuperpositionRecipe.registItem(ItemLoader.lapisAddon);
		SuperpositionRecipe.registItem(ItemLoader.netherStarAddon);
		SuperpositionRecipe.registItem(ItemLoader.entitySoul);
		event.getRegistry().register(new SmallLoliPickaxeRecipe().setRegistryName(LoliPickaxe.MODID, "small_loli_pickaxe_up"));
		event.getRegistry().register(new LoliPickaxeRecipe().setRegistryName(LoliPickaxe.MODID, "loli_pickaxe"));
	}

}
