package com.anotherstar.common.enchantment;

import com.anotherstar.common.LoliPickaxe;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantmentLoader {

	public static final EnchantmentAutoFurnace loliAutoFurnace = new EnchantmentAutoFurnace();

	@SubscribeEvent
	public void registerEnchantment(RegistryEvent.Register<Enchantment> event) {
		event.getRegistry().register(loliAutoFurnace.setRegistryName(LoliPickaxe.MODID, "loli_auto_furnace"));
	}

}
