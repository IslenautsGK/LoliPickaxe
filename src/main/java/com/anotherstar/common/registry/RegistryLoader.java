package com.anotherstar.common.registry;

import com.anotherstar.common.LoliPickaxe;
import com.anotherstar.common.registry.recipe.IPasswordRecipe;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

@Mod.EventBusSubscriber(modid = LoliPickaxe.MODID)
public class RegistryLoader {

	public static IForgeRegistry<IPasswordRecipe> PASSWORD_RECIPE_REGISTRY;

	@SubscribeEvent
	public static void registerRegistry(RegistryEvent.NewRegistry event) {
		PASSWORD_RECIPE_REGISTRY = new RegistryBuilder<IPasswordRecipe>().setName(new ResourceLocation(LoliPickaxe.MODID, "password_recipe")).setType(IPasswordRecipe.class).disableSaving().allowModification().create();
	}

}
