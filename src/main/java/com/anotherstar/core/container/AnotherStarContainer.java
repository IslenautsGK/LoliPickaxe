package com.anotherstar.core.container;

import java.util.Arrays;

import org.apache.logging.log4j.Logger;

import com.anotherstar.core.AnotherStarCore;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class AnotherStarContainer extends DummyModContainer {

	private Logger log;

	public AnotherStarContainer() {
		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId = "AnotherStarCore";
		meta.name = "AnotherStarCore";
		meta.version = "1.0.4";
		meta.authorList = Arrays.asList("Junior_Mo");
		meta.description = "AnotherStarCore";
		meta.url = "www.Junior_Mo.com";
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
		log.info("debug is " + AnotherStarCore.debug);
		log.info("AnotherStarCore Fix Loaded");
	}

}
