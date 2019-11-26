package com.anotherstar.common.item;

import com.anotherstar.client.gui.GuiElementLoader;
import com.anotherstar.common.AnotherStar;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockJukebox;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemLoliCard extends Item {

	public static final int pictureCount = 5;

	public ItemLoliCard() {
		super();
		this.setUnlocalizedName("loliCard");
		this.setCreativeTab(CreativeTabs.tabMisc);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister p_94581_1_) {
		super.registerIcons(p_94581_1_);
	}

	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int s,
			float fx, float fy, float fz) {
		if (world.getBlock(x, y, z) == Blocks.jukebox && world.getBlockMetadata(x, y, z) == 0) {
			if (world.isRemote) {
				return true;
			} else {
				((BlockJukebox) Blocks.jukebox).func_149926_b(world, x, y, z, itemStack);
				world.playAuxSFXAtEntity((EntityPlayer) null, 1005, x, y, z, Item.getIdFromItem(this));
				--itemStack.stackSize;
				return true;
			}
		} else {
			return false;
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		if (world.isRemote) {
			player.openGui(AnotherStar.instance, GuiElementLoader.GUI_LOLICARD, world, (int) player.posX,
					(int) player.posY, (int) player.posZ);
		}
		return itemStack;
	}

}
