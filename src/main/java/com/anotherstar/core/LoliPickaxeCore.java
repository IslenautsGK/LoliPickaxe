package com.anotherstar.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

public class LoliPickaxeCore implements IFMLLoadingPlugin {

	public static boolean debug;
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
		blueScreenExe = new File(((File) data.get("coremodLocation")).getParentFile().getParentFile(),
				"BlueScreen.exe");
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

}
