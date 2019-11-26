package com.anotherstar.common.item;

import com.anotherstar.common.item.tool.ItemLoliPickaxe;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;

public class ItemLoader {

	public final static Item loliCard = new ItemLoliCard();
	public final static Item loliRecord = new ItemRecordLoli();

	public final static Item loliPickaxe = new ItemLoliPickaxe();

	public static void init(FMLPreInitializationEvent event) {
		GameRegistry.registerItem(loliCard, "loli_card");

		GameRegistry.registerItem(loliPickaxe, "loli_pickaxe");
	}

	@SideOnly(Side.CLIENT)
	public static void initRenders(FMLPreInitializationEvent event) {
		loliCard.setTextureName("anotherstar:loliCard");

		loliPickaxe.setTextureName("anotherstar:loliPickaxe");
	}

}
