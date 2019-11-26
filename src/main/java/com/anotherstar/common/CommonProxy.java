package com.anotherstar.common;

import com.anotherstar.client.gui.GuiElementLoader;
import com.anotherstar.common.block.BlockLoader;
import com.anotherstar.common.command.ConfigCommand;
import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.common.crafting.CraftingLoader;
import com.anotherstar.common.event.DestroyBedrockEvent;
import com.anotherstar.common.event.LoliPickaxeEvent;
import com.anotherstar.common.event.PlayerJoinEvent;
import com.anotherstar.common.item.ItemLoader;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		ConfigLoader.init(event);
		ItemLoader.init(event);
		BlockLoader.init(event);
	}

	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new DestroyBedrockEvent());
		MinecraftForge.EVENT_BUS.register(new LoliPickaxeEvent());
		FMLCommonHandler.instance().bus().register(new PlayerJoinEvent());
		CraftingLoader.init();
		new GuiElementLoader().init();
	}

	public void postInit(FMLPostInitializationEvent event) {
	}

	public void onServerStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new ConfigCommand());
	}

}
