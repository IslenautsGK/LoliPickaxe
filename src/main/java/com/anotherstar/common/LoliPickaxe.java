package com.anotherstar.common;

import com.anotherstar.common.item.ItemLoader;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = LoliPickaxe.MODID, name = LoliPickaxe.NAME, version = LoliPickaxe.VERSION)
public class LoliPickaxe {

	public static final String MODID = "lolipickaxe";
	public static final String NAME = "LoliPickaxe Mod";
	public static final String VERSION = "@VERSION@";

	public CreativeTabs loliTabs = new CreativeTabs("loli") {

		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(ItemLoader.entitySoul, 1, ItemLoader.entitySoul.getSubCount() - 1);
		}

	};

	public CreativeTabs loliRecipeTabs = new CreativeTabs("loliRecipe") {

		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(ItemLoader.loliPickaxe);
		}

	};

	@SidedProxy(clientSide = "com.anotherstar.client.ClientProxy", serverSide = "com.anotherstar.common.CommonProxy")
	public static CommonProxy proxy;

	@Instance(LoliPickaxe.MODID)
	public static LoliPickaxe instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {
		proxy.onServerStarting(event);
	}

}