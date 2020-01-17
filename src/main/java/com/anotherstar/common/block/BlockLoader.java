package com.anotherstar.common.block;

import com.anotherstar.common.LoliPickaxe;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLoader {

	public static final BlockBuffAttackTNT loliBlueScreenTNT = new BlockBuffAttackTNT("LoliBlueScreenTNT", true, false, false);
	public static final ItemBlock itemLoliBlueScreenTNT = new ItemBlock(loliBlueScreenTNT);
	public static final BlockBuffAttackTNT loliExitTNT = new BlockBuffAttackTNT("LoliExitTNT", false, true, false);
	public static final ItemBlock itemLoliExitTNT = new ItemBlock(loliExitTNT);
	public static final BlockBuffAttackTNT loliFailRespondTNT = new BlockBuffAttackTNT("LoliFailRespondTNT", false, false, true);
	public static final ItemBlock itemLoliFailRespondTNT = new ItemBlock(loliFailRespondTNT);
	public static final BlockLoliAltar loliAltar = new BlockLoliAltar();
	public static final ItemBlock itemLoliAltar = new ItemBlock(loliAltar);

	@SubscribeEvent
	public void registerBlock(RegistryEvent.Register<Block> event) {
		event.getRegistry().register(loliBlueScreenTNT.setRegistryName(LoliPickaxe.MODID, "loli_blue_screen_tnt"));
		event.getRegistry().register(loliExitTNT.setRegistryName(LoliPickaxe.MODID, "loli_exit_tnt"));
		event.getRegistry().register(loliFailRespondTNT.setRegistryName(LoliPickaxe.MODID, "loli_fail_respond_tnt"));
		event.getRegistry().register(loliAltar.setRegistryName(LoliPickaxe.MODID, "loli_altar"));
	}

	@SubscribeEvent
	public void registerItem(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(itemLoliBlueScreenTNT.setRegistryName(loliBlueScreenTNT.getRegistryName()));
		event.getRegistry().register(itemLoliExitTNT.setRegistryName(loliExitTNT.getRegistryName()));
		event.getRegistry().register(itemLoliFailRespondTNT.setRegistryName(loliFailRespondTNT.getRegistryName()));
		event.getRegistry().register(itemLoliAltar.setRegistryName(loliAltar.getRegistryName()));
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void registerModel(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(itemLoliBlueScreenTNT, 0, new ModelResourceLocation(itemLoliBlueScreenTNT.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(itemLoliExitTNT, 0, new ModelResourceLocation(itemLoliExitTNT.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(itemLoliFailRespondTNT, 0, new ModelResourceLocation(itemLoliFailRespondTNT.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(itemLoliAltar, 0, new ModelResourceLocation(itemLoliAltar.getRegistryName(), "inventory"));
	}

}
