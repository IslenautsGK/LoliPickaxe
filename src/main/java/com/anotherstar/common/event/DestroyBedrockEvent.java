package com.anotherstar.common.event;

import java.util.ArrayList;

import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.common.item.tool.ItemLoliPickaxe;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class DestroyBedrockEvent {

	@SubscribeEvent
	public void onPlayerMine(PlayerInteractEvent event) {
		if (event.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK
				&& event.entityPlayer.getCurrentEquippedItem() != null
				&& event.entityPlayer.getCurrentEquippedItem().getItem() instanceof ItemLoliPickaxe) {
			breakBlock(event.entityPlayer.getCurrentEquippedItem(), event.x, event.y, event.z, event.entityPlayer);
		}
	}

	private void breakBlock(ItemStack itemstack, int X, int Y, int Z, EntityPlayer player) {
		if (!player.worldObj.isRemote && !player.capabilities.isCreativeMode
				&& itemstack.getItem() instanceof ItemLoliPickaxe) {
			int range = 1;
			NBTTagCompound nbt = itemstack.stackTagCompound;
			if (nbt != null && nbt.hasKey("range")) {
				range = nbt.getInteger("range");
			}
			for (int i = -range; i <= range; i++) {
				for (int j = -range; j <= range; j++) {
					for (int k = -range; k <= range; k++) {
						Block block = player.worldObj.getBlock(X + i, Y + j, Z + k);
						if (block == Blocks.air) {
							continue;
						}
						int meta = player.worldObj.getBlockMetadata(X + i, Y + j, Z + k);
						ArrayList<ItemStack> dropStacks = block.getDrops(player.worldObj, X + i, Y + j, Z + k, meta, 5);
						if (dropStacks.isEmpty() && ConfigLoader.loliPickaxeMandatoryDrop) {
							ItemStack dropStack = new ItemStack(block, 1, meta);
							EntityItem item = new EntityItem(player.worldObj, X + 0.5, Y + 0.5, Z + 0.5, dropStack);
							player.worldObj.spawnEntityInWorld(item);
						} else {
							for (ItemStack dropStack : dropStacks) {
								EntityItem item = new EntityItem(player.worldObj, X + 0.5, Y + 0.5, Z + 0.5, dropStack);
								player.worldObj.spawnEntityInWorld(item);
							}
						}
						player.worldObj.setBlock(X + i, Y + j, Z + k, Blocks.air);
					}
				}
			}
			player.worldObj.playSoundEffect((double) X, (double) Y, (double) Z, "anotherstar:block.lolisuccess", 1.0F,
					1.0F);
		}
	}

}
