package com.anotherstar.core.container;

import java.util.Arrays;

import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.ModMetadata;

public class LoliPickaxeContainer extends DummyModContainer {

	public LoliPickaxeContainer() {
		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId = "lolipickaxecore";
		meta.name = "LoliPickaxeCore";
		meta.version = "@VERSION@";
		meta.authorList = Arrays.asList("Is_GK");
		meta.description = "LoliPickaxeCore";
		meta.url = "www.Is_GK.com";
	}

}
