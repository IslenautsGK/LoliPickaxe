package com.anotherstar.common.recipe;

import com.anotherstar.common.LoliPickaxe;
import com.anotherstar.common.item.ItemLoader;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RecipeLoader {

	public final static SuperpositionRecipe superpositionRecipe = new SuperpositionRecipe();
	public final static SmallLoliPickaxeRecipe smallLoliPickaxeRecipe = new SmallLoliPickaxeRecipe();
	public final static LoliPickaxeRecipe loliPickaxeRecipe = new LoliPickaxeRecipe();

	@SubscribeEvent
	public void registerItem(RegistryEvent.Register<IRecipe> event) {
		event.getRegistry().register(superpositionRecipe.setRegistryName(LoliPickaxe.MODID, "loli_superposition"));
		superpositionRecipe.registItem(ItemLoader.coalAddon);
		superpositionRecipe.registItem(ItemLoader.ironAddon);
		superpositionRecipe.registItem(ItemLoader.goldAddon);
		superpositionRecipe.registItem(ItemLoader.redstoneAddon);
		superpositionRecipe.registItem(ItemLoader.lapisAddon);
		superpositionRecipe.registItem(ItemLoader.diamondAddon);
		superpositionRecipe.registItem(ItemLoader.emeraldAddon);
		superpositionRecipe.registItem(ItemLoader.obsidianAddon);
		superpositionRecipe.registItem(ItemLoader.glowAddon);
		superpositionRecipe.registItem(ItemLoader.quartzAddon);
		superpositionRecipe.registItem(ItemLoader.netherStarAddon);
		superpositionRecipe.registItem(ItemLoader.entitySoul);
		event.getRegistry().register(smallLoliPickaxeRecipe.setRegistryName(LoliPickaxe.MODID, "small_loli_pickaxe_up"));
		event.getRegistry().register(loliPickaxeRecipe.setRegistryName(LoliPickaxe.MODID, "loli_pickaxe"));
	}

}
