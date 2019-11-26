package com.anotherstar.core;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

public class AnotherStarCore implements IFMLLoadingPlugin {

	public static boolean debug;

	@Override
	public String[] getASMTransformerClass() {
		return new String[] { "com.anotherstar.core.transformer.LoliPickaxeTransformer" };
	}

	@Override
	public String getModContainerClass() {
		return "com.anotherstar.core.container.AnotherStarContainer";
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		debug = !(Boolean) data.get("runtimeDeobfuscationEnabled");
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

}
