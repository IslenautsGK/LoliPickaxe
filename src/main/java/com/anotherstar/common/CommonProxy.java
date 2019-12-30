package com.anotherstar.common;

import com.anotherstar.common.command.ConfigCommand;
import com.anotherstar.common.command.LoliBuffAttackCommand;
import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.common.entity.EntityLoader;
import com.anotherstar.common.event.DestroyBedrockEvent;
import com.anotherstar.common.event.LoliDropEvent;
import com.anotherstar.common.event.LoliPickaxeEvent;
import com.anotherstar.common.event.LoliTickEvent;
import com.anotherstar.common.event.PlayerJoinEvent;
import com.anotherstar.common.event.SmallLoliBlockDropEvent;
import com.anotherstar.common.event.SmallLoliFlyEvent;
import com.anotherstar.common.gui.LoliGUIHandler;
import com.anotherstar.common.item.ItemLoader;
import com.anotherstar.common.recipe.RecipeLoader;
import com.anotherstar.network.NetworkHandler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		ConfigLoader.init(event);
		MinecraftForge.EVENT_BUS.register(new ItemLoader());
		MinecraftForge.EVENT_BUS.register(new EntityLoader());
		MinecraftForge.EVENT_BUS.register(new RecipeLoader());
	}

	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new DestroyBedrockEvent());
		MinecraftForge.EVENT_BUS.register(new LoliPickaxeEvent());
		MinecraftForge.EVENT_BUS.register(new LoliTickEvent());
		MinecraftForge.EVENT_BUS.register(new PlayerJoinEvent());
		MinecraftForge.EVENT_BUS.register(new LoliDropEvent());
		MinecraftForge.EVENT_BUS.register(new SmallLoliBlockDropEvent());
		MinecraftForge.EVENT_BUS.register(new SmallLoliFlyEvent());
		NetworkHandler.INSTANCE.name();
		LoliGUIHandler.INSTANCE.name();
	}

	public void postInit(FMLPostInitializationEvent event) {
	}

	public void onServerStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new ConfigCommand());
		event.registerServerCommand(new LoliBuffAttackCommand());
	}

}
