package com.anotherstar.common.event;

import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.common.enchantment.EnchantmentLoader;
import com.anotherstar.common.gui.ILoliInventory;
import com.anotherstar.common.item.tool.IContainer;
import com.anotherstar.common.item.tool.ILoli;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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
			int range = MathHelper.clamp(((ILoli) loli.getItem()).getRange(loli), 0, ConfigLoader.loliPickaxeMaxRange);
			boolean mandatoryDrop = ConfigLoader.getBoolean(loli, "loliPickaxeMandatoryDrop");
			int fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, loli);
			boolean silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, loli) > 0;
			boolean autoFurnace = EnchantmentHelper.getEnchantmentLevel(EnchantmentLoader.loliAutoFurnace, loli) > 0;
			boolean auto = ConfigLoader.getBoolean(loli, "loliPickaxeAutoAccept") && ((IContainer) loli.getItem()).hasInventory(loli);
			ILoliInventory inventory = null;
			if (auto) {
				inventory = ((IContainer) loli.getItem()).getInventory(loli);
				inventory.openInventory(player);
			}
			NonNullList<ItemStack> drops = NonNullList.create();
			float exp = 0;
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
						if (silkTouch && block.canSilkHarvest(player.world, pos, state, player)) {
							ItemStack stack = block.getLoliSilkTouchDrop(state);
							if (!stack.isEmpty()) {
								dropStacks.add(stack);
							}
						} else {
							block.getDrops(dropStacks, player.world, curPos, state, fortuneLevel);
							exp += block.getExpDrop(state, player.world, curPos, fortuneLevel);
							NonNullList<ItemStack> furnaceed = NonNullList.create();
							if (autoFurnace) {
								for (ItemStack stack : dropStacks) {
									ItemStack result = FurnaceRecipes.instance().getSmeltingResult(stack);
									if (!result.isEmpty()) {
										float furnaceExp = FurnaceRecipes.instance().getSmeltingExperience(result) * stack.getCount();
										int resultCount = result.getCount() * stack.getCount();
										if (fortuneLevel > 0) {
											int power = player.world.rand.nextInt(fortuneLevel + 2);
											if (power == 0) {
												power = 1;
											}
											resultCount *= power;
											furnaceExp *= power;
										}
										exp += furnaceExp;
										while (resultCount > 64) {
											furnaceed.add(new ItemStack(result.getItem(), 64, result.getItemDamage()));
											resultCount -= 64;
										}
										furnaceed.add(new ItemStack(result.getItem(), resultCount, result.getItemDamage()));
										stack.setCount(0);
									}
								}
								dropStacks.removeIf(stack -> stack.isEmpty());
								dropStacks.addAll(furnaceed);
							}
						}
						if (dropStacks.isEmpty() && mandatoryDrop) {
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
						ItemStack blackStack = new ItemStack(Item.getByNameOrId(black.getString("Name")), 1, black.getInteger("Damage"));
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
					for (int m = 0; m < inventory.getMaxPage(); m++) {
						NonNullList<ItemStack> stacks = inventory.getPage(m);
						for (int n = 0; n < stacks.size(); n++) {
							ItemStack slotStack = stacks.get(n);
							if (slotStack.isEmpty()) {
								stacks.set(n, dropStack.copy());
								dropStack.setCount(0);
								break;
							} else {
								int maxCount = inventory.cancelStackLimit() ? inventory.getInventoryStackLimit() : Math.min(inventory.getInventoryStackLimit(), slotStack.getMaxStackSize());
								int count = Math.min(maxCount - slotStack.getCount(), dropStack.getCount());
								if (count > 0 && slotStack.isItemEqual(dropStack) && ItemStack.areItemStackTagsEqual(slotStack, dropStack)) {
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
					EntityItem item = new EntityItem(player.world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, dropStack);
					player.world.spawnEntity(item);
				}
			}
			if (auto) {
				inventory.closeInventory(player);
			}
			if ((int) exp > 0) {
				player.world.spawnEntity(new EntityXPOrb(player.world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, (int) exp));
			}
			if (player instanceof EntityPlayerMP) {
				BlockPos playerPos = player.getPosition();
				((EntityPlayerMP) player).connection.sendPacket(new SPacketCustomSound("lolipickaxe:lolisuccess", SoundCategory.BLOCKS, playerPos.getX(), playerPos.getY(), playerPos.getZ(), 1.0F, 1.0F));
			}
		}
	}

}
