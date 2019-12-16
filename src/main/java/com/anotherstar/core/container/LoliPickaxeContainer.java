package com.anotherstar.core.container;

import java.util.Arrays;

import org.apache.logging.log4j.Logger;

import com.anotherstar.core.LoliPickaxeCore;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class LoliPickaxeContainer extends DummyModContainer {

	private Logger log;

	public LoliPickaxeContainer() {
		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId = "lolipickaxecore";
		meta.name = "LoliPickaxeCore";
		meta.version = "1.1.1";
		meta.authorList = Arrays.asList("Is_GK");
		meta.description = "LoliPickaxeCore";
		meta.url = "www.Is_GK.com";
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		return true;
	}

	@Subscribe
	public void preInit(FMLPreInitializationEvent event) {
		log = event.getModLog();
	}

	@Subscribe
	public void init(FMLInitializationEvent event) {
		log.info("debug is " + LoliPickaxeCore.debug);
		log.info("LoliPickaxeCore Fix Loaded");
	}

}
