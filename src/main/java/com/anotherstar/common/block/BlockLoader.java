package com.anotherstar.common.block;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;

public class BlockLoader {

	public final static Block blockLoli = new BlockLoli();

	public static void init(FMLPreInitializationEvent event) {
		GameRegistry.registerBlock(blockLoli, "blockLoli");
	}

	@SideOnly(Side.CLIENT)
	public static void initRenders(FMLPreInitializationEvent event) {
		blockLoli.setBlockTextureName("anotherstar:blockLoli");
	}

}
