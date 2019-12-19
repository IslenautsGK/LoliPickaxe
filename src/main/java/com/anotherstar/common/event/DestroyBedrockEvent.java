package com.anotherstar.common.event;

import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.common.item.tool.ILoli;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DestroyBedrockEvent {

	@SubscribeEvent
	public void onPlayerMine(PlayerInteractEvent.LeftClickBlock event) {
		if (!event.getItemStack().isEmpty() && event.getItemStack().getItem() instanceof ILoli) {
			breakBlock(event.getItemStack(), event.getPos(), event.getEntityPlayer());
		}
	}

	private void breakBlock(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
		if (!player.world.isRemote && !player.capabilities.isCreativeMode && itemstack.getItem() instanceof ILoli) {
			int range = Math.min(((ILoli) itemstack.getItem()).getRange(itemstack), ConfigLoader.loliPickaxeMaxRange);
			for (int i = -range; i <= range; i++) {
				for (int j = -range; j <= range; j++) {
					for (int k = -range; k <= range; k++) {
						BlockPos curPos = pos.add(i, j, k);
						IBlockState state = player.world.getBlockState(curPos);
						Block block = state.getBlock();
						int meta = block.getMetaFromState(state);
						if (block == Blocks.AIR) {
							continue;
						}
						NonNullList<ItemStack> dropStacks = NonNullList.create();
						block.getDrops(dropStacks, player.world, curPos, state, 5);
						if (dropStacks.isEmpty() && ConfigLoader.getBoolean(itemstack, "loliPickaxeMandatoryDrop")) {
							ItemStack dropStack = new ItemStack(block, 1, meta);
							EntityItem item = new EntityItem(player.world, pos.getX() + 0.5, pos.getY() + 0.5,
									pos.getZ() + 0.5, dropStack);
							player.world.spawnEntity(item);
						} else {
							for (ItemStack dropStack : dropStacks) {
								EntityItem item = new EntityItem(player.world, pos.getX() + 0.5, pos.getY() + 0.5,
										pos.getZ() + 0.5, dropStack);
								player.world.spawnEntity(item);
							}
						}
						player.world.setBlockToAir(curPos);
					}
				}
			}
			if (player instanceof EntityPlayerMP) {
				BlockPos playerPos = player.getPosition();
				((EntityPlayerMP) player).connection.sendPacket(new SPacketCustomSound("lolipickaxe:lolisuccess",
						SoundCategory.BLOCKS, playerPos.getX(), playerPos.getY(), playerPos.getZ(), 1.0F, 1.0F));
			}
		}
	}

}
