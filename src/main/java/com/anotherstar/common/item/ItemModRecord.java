package com.anotherstar.common.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemRecord;
import net.minecraft.util.ResourceLocation;

public class ItemModRecord extends ItemRecord {

	private final String file;

	public ItemModRecord(String record, String name) {
		super(record);
		setCreativeTab(CreativeTabs.tabMisc);
		setUnlocalizedName(name);
		this.file = ("lolipickaxe:record." + record);
	}

	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		this.itemIcon = par1IconRegister.registerIcon(this.getUnlocalizedName().replaceAll("item\\.", "lolipickaxe:"));
	}

	public ResourceLocation getRecordResource(String name) {
		return new ResourceLocation(this.file);
	}
}