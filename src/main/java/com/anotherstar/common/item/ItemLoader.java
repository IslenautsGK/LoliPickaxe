package com.anotherstar.common.item;

import java.util.List;

import com.anotherstar.common.LoliPickaxe;
import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.common.item.tool.ItemLoliPickaxe;
import com.google.common.collect.Lists;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemLoader {

	public final static ItemLoliPickaxe loliPickaxe = new ItemLoliPickaxe();
	public final static ItemLoliPickaxeCore loliPickaxeCore = new ItemLoliPickaxeCore();
	public final static ItemLoliPickaxeStick loliPickaxeStick = new ItemLoliPickaxeStick();
	public final static ItemLoliDispersal loliDispersal = new ItemLoliDispersal();
	public final static ItemLoliCard loliCard = new ItemLoliCard();
	public final static List<ItemLoliRecord> loliRecords = Lists.newArrayList();
	public final static ItemBugEntityClear bugEntityClear = new ItemBugEntityClear();

	@SubscribeEvent
	public void registerItem(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(loliPickaxe.setRegistryName(LoliPickaxe.MODID, "loli_pickaxe"));
		event.getRegistry().register(loliPickaxeCore.setRegistryName(LoliPickaxe.MODID, "loli_pickaxe_core"));
		event.getRegistry().register(loliPickaxeStick.setRegistryName(LoliPickaxe.MODID, "loli_pickaxe_stick"));
		event.getRegistry().register(loliDispersal.setRegistryName(LoliPickaxe.MODID, "loli_dispersal"));
		event.getRegistry().register(loliCard.setRegistryName(LoliPickaxe.MODID, "loli_card"));
		for (String loliRecordName : ConfigLoader.loliRecodeNames) {
			String[] name = loliRecordName.split(":");
			ItemLoliRecord record = new ItemLoliRecord(name[1], name[0]);
			loliRecords.add(record);
			event.getRegistry().register(record.setRegistryName(LoliPickaxe.MODID, name[2]));
		}
		event.getRegistry().register(bugEntityClear.setRegistryName(LoliPickaxe.MODID, "bug_entity_clear"));
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void registerModel(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(loliPickaxe, 0,
				new ModelResourceLocation(loliPickaxe.getRegistryName(), "inventory"));
		ModelLoader.setCustomMeshDefinition(loliPickaxeCore,
				stack -> new ModelResourceLocation(loliPickaxeCore.getRegistryName(), "inventory"));
		ModelLoader.setCustomMeshDefinition(loliPickaxeStick,
				stack -> new ModelResourceLocation(loliPickaxeStick.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(loliDispersal, 0,
				new ModelResourceLocation(loliDispersal.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(loliCard, 0,
				new ModelResourceLocation(loliCard.getRegistryName(), "inventory"));
		for (ItemLoliRecord item : loliRecords) {
			ModelLoader.setCustomModelResourceLocation(item, 0,
					new ModelResourceLocation(item.getRegistryName(), "inventory"));
		}
		ModelLoader.setCustomModelResourceLocation(bugEntityClear, 0,
				new ModelResourceLocation(bugEntityClear.getRegistryName(), "inventory"));
	}

}
