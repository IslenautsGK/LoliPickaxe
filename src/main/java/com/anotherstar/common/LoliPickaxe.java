package com.anotherstar.common;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = LoliPickaxe.MODID, name = LoliPickaxe.NAME, version = LoliPickaxe.VERSION, acceptedMinecraftVersions = "1.7.10")
public class LoliPickaxe {

	public static final String MODID = "lolipickaxe";
	public static final String NAME = "LoliPickaxe Mod";
	public static final String VERSION = "1.1.1";

	@SidedProxy(clientSide = "com.anotherstar.client.ClientProxy", serverSide = "com.anotherstar.common.CommonProxy")
	public static CommonProxy proxy;

	@Instance(LoliPickaxe.MODID)
	public static LoliPickaxe instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {
		proxy.onServerStarting(event);
	}

}