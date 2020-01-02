package com.anotherstar.client;

import com.anotherstar.client.event.LoliCardAlbumSwitchEvent;
import com.anotherstar.client.event.LoliKeyEvent;
import com.anotherstar.client.event.LoliPickaxeTooltipEvent;
import com.anotherstar.client.event.ResourcePackReloadEvent;
import com.anotherstar.client.key.KeyLoader;
import com.anotherstar.client.render.RenderLoliItem;
import com.anotherstar.client.util.LoliCardUtil;
import com.anotherstar.common.CommonProxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		KeyLoader.init();
		MinecraftForge.EVENT_BUS.register(new LoliKeyEvent());
		MinecraftForge.EVENT_BUS.register(new ResourcePackReloadEvent());
		MinecraftForge.EVENT_BUS.register(new LoliPickaxeTooltipEvent());
		MinecraftForge.EVENT_BUS.register(new LoliCardAlbumSwitchEvent());
		LoliCardUtil.updateCustomArtDatas();
		RenderLoliItem.init();
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
	}

	@Override
	public void onServerStarting(FMLServerStartingEvent event) {
		super.onServerStarting(event);
	}

}
