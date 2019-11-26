package com.anotherstar.common.block;

import java.util.Random;

import com.anotherstar.common.AnotherStar;
import com.anotherstar.common.item.ItemLoader;
import com.anotherstar.common.item.ItemModRecord;
import com.anotherstar.util.LoliCardUtil;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockLoli extends Block {

	private Random rand;
	private static final IIcon[] Icons = new IIcon[3];

	public BlockLoli() {
		super(Material.iron);
		this.setBlockName("loliBlock");
		this.setHardness(1.0F);
		this.setHarvestLevel("net.minecraft.item.ItemPickaxe", 3);
		this.setLightLevel(1.0F);
		this.setResistance(6000F);
		this.setCreativeTab(CreativeTabs.tabDecorations);
		this.setStepSound(soundTypeStone);
		rand = new Random();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int look, float fx, float fy,
			float fz) {
		ItemStack itemStack = player.getCurrentEquippedItem();
		if (world.isRemote) {
			if (itemStack != null && itemStack.getItem() == Items.iron_ingot) {
				ByteBuf buffer = Unpooled.buffer();
				if (LoliCardUtil.customArtNames != null) {
					ByteBufUtils.writeUTF8String(buffer,
							LoliCardUtil.customArtNames[rand.nextInt(LoliCardUtil.customArtNames.length)]);
				} else {
					ByteBufUtils.writeUTF8String(buffer, "youdonthavecustonpictures");
				}
				AnotherStar.loliCardNetwork.sendToServer(new FMLProxyPacket(buffer, "loliCard"));
			}
		} else {
			if (itemStack != null) {
				if (itemStack.getItem() == Items.iron_ingot) {
					world.playSoundAtEntity(player, "anotherstar:block.lolisuccess", 1.0F, 1.0F);
				} else if (itemStack.getItem() instanceof ItemRecord
						&& !(itemStack.getItem() instanceof ItemModRecord)) {
					world.playSoundAtEntity(player, "anotherstar:block.lolisuccess", 1.0F, 1.0F);
					if (!player.capabilities.isCreativeMode) {
						--itemStack.stackSize;
					}
					player.inventory.addItemStackToInventory(new ItemStack(ItemLoader.loliRecord));
					player.inventoryContainer.detectAndSendChanges();
				} else {
					world.playSoundAtEntity(player, "anotherstar:block.lolisound", 1.0F, 1.0F);
				}
			} else {
				world.playSoundAtEntity(player, "anotherstar:block.lolisound", 1.0F, 1.0F);
			}
		}
		return true;
	}

}
