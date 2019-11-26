package com.anotherstar.common;

import com.anotherstar.network.ServerPacketHandler;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid = AnotherStar.MODID, name = AnotherStar.NAME, version = AnotherStar.VERSION, acceptedMinecraftVersions = "1.7.10")
public class AnotherStar {

	public static final String MODID = "AnotherStar";
	public static final String NAME = "AnotherStar Mod";
	public static final String VERSION = "1.0.0";

	@SidedProxy(clientSide = "com.anotherstar.client.ClientProxy", serverSide = "com.anotherstar.common.CommonProxy")
	public static CommonProxy proxy;

	@Instance(AnotherStar.MODID)
	public static AnotherStar instance;

	public static FMLEventChannel loliCardNetwork;
	public static FMLEventChannel loliConfigNetwork;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		loliCardNetwork = NetworkRegistry.INSTANCE.newEventDrivenChannel("loliCard");
		loliCardNetwork.register(new ServerPacketHandler());
		loliConfigNetwork = NetworkRegistry.INSTANCE.newEventDrivenChannel("loliConfig");
		loliConfigNetwork.register(new ServerPacketHandler());
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