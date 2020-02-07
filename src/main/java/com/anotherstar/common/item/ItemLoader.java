package com.anotherstar.common.item;

import java.util.List;

import com.anotherstar.common.LoliPickaxe;
import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.common.item.tool.ItemLoliPickaxe;
import com.anotherstar.common.item.tool.ItemSmallLoliPickaxe;
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
	public final static ItemSmallLoliPickaxe smallLoliPickaxe = new ItemSmallLoliPickaxe();
	public final static ItemLoliPickaxeMaterial coalAddon = new ItemLoliPickaxeMaterial("loliCoalAddon", 10, false);
	public final static ItemLoliPickaxeMaterial ironAddon = new ItemLoliPickaxeMaterial("loliIronAddon", 10, false);
	public final static ItemLoliPickaxeMaterial goldAddon = new ItemLoliPickaxeMaterial("loliGoldAddon", 7, false);
	public final static ItemLoliPickaxeMaterial redstoneAddon = new ItemLoliPickaxeMaterial("loliRedstoneAddon", 4, false);
	public final static ItemLoliPickaxeMaterial lapisAddon = new ItemLoliPickaxeMaterial("loliLapisAddon", 6, false);
	public final static ItemLoliPickaxeMaterial diamondAddon = new ItemLoliPickaxeMaterial("loliDiamondAddon", 6, false);
	public final static ItemLoliPickaxeMaterial emeraldAddon = new ItemLoliPickaxeMaterial("loliEmeraldAddon", 5, false);
	public final static ItemLoliPickaxeMaterial obsidianAddon = new ItemLoliPickaxeMaterial("loliObsidianAddon", 10, false);
	public final static ItemLoliPickaxeMaterial glowAddon = new ItemLoliPickaxeMaterial("loliGlowAddon", 3, false);
	public final static ItemLoliPickaxeMaterial quartzAddon = new ItemLoliPickaxeMaterial("loliQuartzAddon", 3, false);
	public final static ItemLoliPickaxeMaterial netherStarAddon = new ItemLoliPickaxeMaterial("loliNetherStarAddon", 5, false);
	public final static ItemLoliPickaxeMaterial autoFurnaceAddon = new ItemLoliPickaxeMaterial("loliAutoFurnaceAddon", 1, false);
	public final static ItemLoliPickaxeMaterial flyAddon = new ItemLoliPickaxeMaterial("loliFlyAddon", 1, false);
	public final static ItemLoliPickaxeMaterial entitySoul = new ItemLoliPickaxeMaterial("loliEntitySoulAddon", 7, true);
	public final static ItemLoliDispersal loliDispersal = new ItemLoliDispersal();
	public final static ItemBugEntityClear bugEntityClear = new ItemBugEntityClear();
	public final static ItemLoliCard loliCard = new ItemLoliCard();
	public final static ItemLoliCardAlbum loliCardAlbum = new ItemLoliCardAlbum();
	public final static ItemLoliCardOnline loliCardOnline = new ItemLoliCardOnline();
	public final static List<ItemLoliRecord> loliRecords = Lists.newArrayList();

	@SubscribeEvent
	public void registerItem(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(loliPickaxe.setRegistryName(LoliPickaxe.MODID, "loli_pickaxe"));
		event.getRegistry().register(smallLoliPickaxe.setRegistryName(LoliPickaxe.MODID, "small_loli_pickaxe"));
		event.getRegistry().register(coalAddon.setRegistryName(LoliPickaxe.MODID, "loli_coal_addon"));
		event.getRegistry().register(ironAddon.setRegistryName(LoliPickaxe.MODID, "loli_iron_addon"));
		event.getRegistry().register(goldAddon.setRegistryName(LoliPickaxe.MODID, "loli_gold_addon"));
		event.getRegistry().register(redstoneAddon.setRegistryName(LoliPickaxe.MODID, "loli_redstone_addon"));
		event.getRegistry().register(lapisAddon.setRegistryName(LoliPickaxe.MODID, "loli_lapis_addon"));
		event.getRegistry().register(diamondAddon.setRegistryName(LoliPickaxe.MODID, "loli_diamond_addon"));
		event.getRegistry().register(emeraldAddon.setRegistryName(LoliPickaxe.MODID, "loli_emerald_addon"));
		event.getRegistry().register(obsidianAddon.setRegistryName(LoliPickaxe.MODID, "loli_obsidian_addon"));
		event.getRegistry().register(glowAddon.setRegistryName(LoliPickaxe.MODID, "loli_glow_addon"));
		event.getRegistry().register(quartzAddon.setRegistryName(LoliPickaxe.MODID, "loli_quartz_addon"));
		event.getRegistry().register(netherStarAddon.setRegistryName(LoliPickaxe.MODID, "loli_nether_star_addon"));
		event.getRegistry().register(autoFurnaceAddon.setRegistryName(LoliPickaxe.MODID, "loli_auto_furnace_addon"));
		event.getRegistry().register(flyAddon.setRegistryName(LoliPickaxe.MODID, "loli_fly_addon"));
		event.getRegistry().register(entitySoul.setRegistryName(LoliPickaxe.MODID, "loli_entity_soul_addon"));
		event.getRegistry().register(loliDispersal.setRegistryName(LoliPickaxe.MODID, "loli_dispersal"));
		event.getRegistry().register(bugEntityClear.setRegistryName(LoliPickaxe.MODID, "bug_entity_clear"));
		event.getRegistry().register(loliCard.setRegistryName(LoliPickaxe.MODID, "loli_card"));
		event.getRegistry().register(loliCardAlbum.setRegistryName(LoliPickaxe.MODID, "loli_card_album"));
		event.getRegistry().register(loliCardOnline.setRegistryName(LoliPickaxe.MODID, "loli_card_online"));
		for (String loliRecordName : ConfigLoader.loliRecodeNames) {
			String[] name = loliRecordName.split(":");
			ItemLoliRecord record = new ItemLoliRecord(name[1], name[0]);
			loliRecords.add(record);
			event.getRegistry().register(record.setRegistryName(LoliPickaxe.MODID, name[2]));
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void registerModel(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(loliPickaxe, 0, new ModelResourceLocation(loliPickaxe.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(smallLoliPickaxe, 0, new ModelResourceLocation(loliPickaxe.getRegistryName(), "inventory"));
		ModelLoader.setCustomMeshDefinition(coalAddon, stack -> new ModelResourceLocation(coalAddon.getRegistryName(), "inventory"));
		ModelLoader.setCustomMeshDefinition(ironAddon, stack -> new ModelResourceLocation(ironAddon.getRegistryName(), "inventory"));
		ModelLoader.setCustomMeshDefinition(goldAddon, stack -> new ModelResourceLocation(goldAddon.getRegistryName(), "inventory"));
		ModelLoader.setCustomMeshDefinition(redstoneAddon, stack -> new ModelResourceLocation(redstoneAddon.getRegistryName(), "inventory"));
		ModelLoader.setCustomMeshDefinition(lapisAddon, stack -> new ModelResourceLocation(lapisAddon.getRegistryName(), "inventory"));
		ModelLoader.setCustomMeshDefinition(diamondAddon, stack -> new ModelResourceLocation(diamondAddon.getRegistryName(), "inventory"));
		ModelLoader.setCustomMeshDefinition(emeraldAddon, stack -> new ModelResourceLocation(emeraldAddon.getRegistryName(), "inventory"));
		ModelLoader.setCustomMeshDefinition(obsidianAddon, stack -> new ModelResourceLocation(obsidianAddon.getRegistryName(), "inventory"));
		ModelLoader.setCustomMeshDefinition(glowAddon, stack -> new ModelResourceLocation(glowAddon.getRegistryName(), "inventory"));
		ModelLoader.setCustomMeshDefinition(quartzAddon, stack -> new ModelResourceLocation(quartzAddon.getRegistryName(), "inventory"));
		ModelLoader.setCustomMeshDefinition(netherStarAddon, stack -> new ModelResourceLocation(netherStarAddon.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(autoFurnaceAddon, 0, new ModelResourceLocation(autoFurnaceAddon.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(flyAddon, 0, new ModelResourceLocation(flyAddon.getRegistryName(), "inventory"));
		ModelLoader.setCustomMeshDefinition(entitySoul, stack -> new ModelResourceLocation(entitySoul.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(loliDispersal, 0, new ModelResourceLocation(loliDispersal.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(bugEntityClear, 0, new ModelResourceLocation(bugEntityClear.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(loliCard, 0, new ModelResourceLocation(loliCard.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(loliCardAlbum, 0, new ModelResourceLocation(loliCardAlbum.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(loliCardOnline, 0, new ModelResourceLocation(loliCardOnline.getRegistryName(), "inventory"));
		for (ItemLoliRecord item : loliRecords) {
			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
		}
	}

}
