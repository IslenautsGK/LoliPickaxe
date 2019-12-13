package com.anotherstar.common.item;

import java.util.List;

import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.common.item.tool.ItemLoliPickaxe;
import com.google.common.collect.Lists;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;

public class ItemLoader {

	public final static Item loliCard = new ItemLoliCard();
	public final static Item loliPickaxe = new ItemLoliPickaxe();
	public final static Item loliRecord = new ItemModRecord("Loli", "recordLoli");
	public final static List<Item> loliRecords = Lists.newArrayList();

	public static void init(FMLPreInitializationEvent event) {
		GameRegistry.registerItem(loliCard, "loli_card");
		GameRegistry.registerItem(loliPickaxe, "loli_pickaxe");
		GameRegistry.registerItem(loliRecord, "loli_record");
		for (String loliRecordName : ConfigLoader.loliRecodeNames) {
			String[] name = loliRecordName.split(":");
			Item record = new ItemModRecord(name[0], name[1]);
			loliRecords.add(record);
			GameRegistry.registerItem(record, name[2]);
		}
	}

	@SideOnly(Side.CLIENT)
	public static void initRenders(FMLPreInitializationEvent event) {
		loliCard.setTextureName("lolipickaxe:loliCard");
		loliPickaxe.setTextureName("lolipickaxe:loliPickaxe");
	}

}
