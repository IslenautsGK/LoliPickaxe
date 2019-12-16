package com.anotherstar.common.item;

import com.anotherstar.common.LoliPickaxe;
import com.anotherstar.common.item.tool.ItemLoliPickaxe;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemLoader {

	public final static ItemLoliPickaxe loliPickaxe = new ItemLoliPickaxe();

	@SubscribeEvent
	public void registerItem(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(loliPickaxe.setRegistryName(LoliPickaxe.MODID, "loli_pickaxe"));
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void registerModel(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(loliPickaxe, 0,
				new ModelResourceLocation(loliPickaxe.getRegistryName(), "inventory"));
	}

}
