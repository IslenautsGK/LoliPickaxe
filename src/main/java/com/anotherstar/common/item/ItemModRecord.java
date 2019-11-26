package com.anotherstar.common.item;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemModRecord extends ItemRecord {

	private final String file;

	public ItemModRecord(String record, String name) {
		super(record);
		setCreativeTab(CreativeTabs.tabMisc);
		setUnlocalizedName(name);
		this.file = ("anotherstar:record." + record);
	}

	public Item setUnlocalizedName(String par1Str) {
		GameRegistry.registerItem(this, par1Str);
		return super.setUnlocalizedName(par1Str);
	}

	public String getUnlocalizedNameInefficiently(ItemStack par1ItemStack) {
		return super.getUnlocalizedNameInefficiently(par1ItemStack);
	}

	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		this.itemIcon = par1IconRegister.registerIcon(this.getUnlocalizedName().replaceAll("item\\.", "anotherstar:"));
	}

	public ResourceLocation getRecordResource(String name) {
		return new ResourceLocation(this.file);
	}
}