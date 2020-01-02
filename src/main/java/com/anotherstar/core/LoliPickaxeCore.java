package com.anotherstar.core;

import java.io.File;
import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

public class LoliPickaxeCore implements IFMLLoadingPlugin {

	public static boolean debug;
	public static File coremodLocation;
	public static File blueScreenExe;

	@Override
	public String[] getASMTransformerClass() {
		return new String[] { "com.anotherstar.core.transformer.LoliPickaxeTransformer" };
	}

	@Override
	public String getModContainerClass() {
		return "com.anotherstar.core.container.LoliPickaxeContainer";
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		debug = !(Boolean) data.get("runtimeDeobfuscationEnabled");
		coremodLocation = (File) data.get("coremodLocation");
		if (coremodLocation == null || coremodLocation.isDirectory()) {
			blueScreenExe = new File("BlueScreen.exe");
		} else {
			blueScreenExe = new File(coremodLocation.getParentFile().getParentFile(), "BlueScreen.exe");
		}
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

}
