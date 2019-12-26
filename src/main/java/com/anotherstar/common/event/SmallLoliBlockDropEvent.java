package com.anotherstar.common.event;

import java.util.List;

import com.anotherstar.common.item.tool.ItemSmallLoliPickaxe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SmallLoliBlockDropEvent {

	@SubscribeEvent
	public void onDrop(BlockEvent.HarvestDropsEvent event) {
		if (ItemSmallLoliPickaxe.isharvesting) {
			float chance = event.getDropChance();
			List<ItemStack> drops = event.getDrops();
			World world = event.getWorld();
			BlockPos pos = event.getPos();
			NonNullList<ItemStack> furnaceed = NonNullList.create();
			if (ItemSmallLoliPickaxe.autoFurnace) {
				for (ItemStack drop : drops) {
					ItemStack result = FurnaceRecipes.instance().getSmeltingResult(drop);
					if (!result.isEmpty()) {
						float furnaceExp = FurnaceRecipes.instance().getSmeltingExperience(result) * drop.getCount();
						int resultCount = result.getCount() * drop.getCount();
						if (ItemSmallLoliPickaxe.fortuneLevel > 0) {
							int power = world.rand.nextInt(ItemSmallLoliPickaxe.fortuneLevel + 2);
							if (power == 0) {
								power = 1;
							}
							resultCount *= power;
							furnaceExp *= power;
						}
						ItemSmallLoliPickaxe.exp += furnaceExp;
						while (resultCount > 64) {
							furnaceed.add(new ItemStack(result.getItem(), 64, result.getItemDamage()));
							resultCount -= 64;
						}
						furnaceed.add(new ItemStack(result.getItem(), resultCount, result.getItemDamage()));
						drop.setCount(0);
					}
				}
				drops.removeIf(stack -> stack.isEmpty());
				drops.addAll(furnaceed);
			}
			if (!ItemSmallLoliPickaxe.blacklist.isEmpty()) {
				drops.removeIf(stack -> {
					for (ItemStack black : ItemSmallLoliPickaxe.blacklist) {
						if (black.isItemEqual(stack)) {
							return true;
						}
					}
					return false;
				});
			}
			for (ItemStack drop : drops) {
				if (world.rand.nextFloat() <= chance) {
					for (int i = 0; i < ItemSmallLoliPickaxe.inventory.getMaxPage(); i++) {
						NonNullList<ItemStack> stacks = ItemSmallLoliPickaxe.inventory.getPage(i);
						for (int j = 0; j < stacks.size(); j++) {
							ItemStack slotStack = stacks.get(j);
							if (slotStack.isEmpty()) {
								stacks.set(j, drop.copy());
								drop.setCount(0);
								break;
							} else {
								int count = Math.min(slotStack.getMaxStackSize() - slotStack.getCount(), drop.getCount());
								if (count > 0 && slotStack.isItemEqual(drop) && ItemStack.areItemStackTagsEqual(slotStack, drop)) {
									slotStack.grow(count);
									drop.shrink(count);
									if (drop.isEmpty()) {
										break;
									}
								}
							}
						}
						if (drop.isEmpty()) {
							break;
						}
					}
				}
			}
			drops.removeIf(stack -> stack.isEmpty());
		}
	}

}
