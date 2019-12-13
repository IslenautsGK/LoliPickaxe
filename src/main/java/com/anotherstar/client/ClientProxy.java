package com.anotherstar.client;

import com.anotherstar.client.event.ResourcePackReloadEvent;
import com.anotherstar.common.CommonProxy;
import com.anotherstar.common.block.BlockLoader;
import com.anotherstar.common.item.ItemLoader;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

	public static ResourceLocation DEFLOLI;

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		BlockLoader.initRenders(event);
		ItemLoader.initRenders(event);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		MinecraftForge.EVENT_BUS.register(new ResourcePackReloadEvent());
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
		DEFLOLI = new ResourceLocation("lolipickaxe", "textures/gui/defloli.png");
	}

	@Override
	public void onServerStarting(FMLServerStartingEvent event) {
		super.onServerStarting(event);
	}

}
