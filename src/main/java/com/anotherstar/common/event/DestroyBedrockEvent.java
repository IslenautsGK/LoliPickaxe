package com.anotherstar.common.event;

import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.common.gui.InventoryLoliPickaxe;
import com.anotherstar.common.item.tool.ILoli;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
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

	private void breakBlock(ItemStack loli, BlockPos pos, EntityPlayer player) {
		if (!player.world.isRemote && !player.capabilities.isCreativeMode && loli.getItem() instanceof ILoli) {
			int range = Math.min(((ILoli) loli.getItem()).getRange(loli), ConfigLoader.loliPickaxeMaxRange);
			boolean auto = ConfigLoader.getBoolean(loli, "loliPickaxeAutoAccept");
			InventoryLoliPickaxe inventory = null;
			if (auto) {
				inventory = new InventoryLoliPickaxe(loli);
				inventory.openInventory(player);
			}
			NonNullList<ItemStack> drops = NonNullList.create();
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
						if (dropStacks.isEmpty() && ConfigLoader.getBoolean(loli, "loliPickaxeMandatoryDrop")) {
							ItemStack dropStack = new ItemStack(block, 1, meta);
							dropStacks.add(dropStack);
						}
						drops.addAll(dropStacks);
						player.world.setBlockToAir(curPos);
					}
				}
			}
			NonNullList<ItemStack> blacklist = NonNullList.create();
			if (loli.hasTagCompound() && loli.getTagCompound().hasKey("Blacklist")) {
				NBTTagList blackList = loli.getTagCompound().getTagList("Blacklist", 10);
				for (int i = 0; i < blackList.tagCount(); i++) {
					NBTTagCompound black = blackList.getCompoundTagAt(i);
					if (black.hasKey("Name") && black.hasKey("Damage")) {
						ItemStack blackStack = new ItemStack(Item.getByNameOrId(black.getString("Name")), 1,
								black.getInteger("Damage"));
						blacklist.add(blackStack);
					}
				}
			}
			if (!blacklist.isEmpty()) {
				drops.removeIf(stack -> {
					for (ItemStack black : blacklist) {
						if (black.isItemEqual(stack)) {
							return true;
						}
					}
					return false;
				});
			}
			if (auto) {
				for (ItemStack dropStack : drops) {
					for (int m = 0; m < ConfigLoader.loliPickaxeMaxPage; m++) {
						NonNullList<ItemStack> stacks = inventory.getPage(m);
						for (int n = 0; n < stacks.size(); n++) {
							ItemStack slotStack = stacks.get(n);
							if (slotStack.isEmpty()) {
								stacks.set(n, dropStack.copy());
								dropStack.setCount(0);
								break;
							} else {
								int count = Math.min(slotStack.getMaxStackSize() - slotStack.getCount(),
										dropStack.getCount());
								if (count > 0 && slotStack.isItemEqual(dropStack)
										&& ItemStack.areItemStackTagsEqual(slotStack, dropStack)) {
									slotStack.grow(count);
									dropStack.shrink(count);
									if (dropStack.isEmpty()) {
										break;
									}
								}
							}
						}
						if (dropStack.isEmpty()) {
							break;
						}
					}
				}
			}
			drops.removeIf(stack -> stack.isEmpty());
			if (!drops.isEmpty()) {
				for (ItemStack dropStack : drops) {
					EntityItem item = new EntityItem(player.world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
							dropStack);
					player.world.spawnEntity(item);
				}
			}
			if (auto) {
				inventory.closeInventory(player);
			}
			if (player instanceof EntityPlayerMP) {
				BlockPos playerPos = player.getPosition();
				((EntityPlayerMP) player).connection.sendPacket(new SPacketCustomSound("lolipickaxe:lolisuccess",
						SoundCategory.BLOCKS, playerPos.getX(), playerPos.getY(), playerPos.getZ(), 1.0F, 1.0F));
			}
		}
	}

}
