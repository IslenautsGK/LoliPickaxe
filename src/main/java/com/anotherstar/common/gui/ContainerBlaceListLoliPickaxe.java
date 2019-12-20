package com.anotherstar.common.gui;

import com.anotherstar.common.item.tool.ILoli;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerBlaceListLoliPickaxe extends Container {

	private ItemStackHandler items = new ItemStackHandler(81);
	private EntityPlayer player;
	private ItemStack stack;
	private int slotIndex;

	public ContainerBlaceListLoliPickaxe(EntityPlayer player, ItemStack stack, int slotIndex) {
		if (!stack.isEmpty() && stack.getItem() instanceof ILoli) {
			this.stack = stack;
			this.player = player;
			this.slotIndex = slotIndex;
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					this.addSlotToContainer(new SlotItemHandler(this.items, i * 9 + j, j * 18 + 8, i * 18 + 8));
				}
			}
			for (int i = 0; i < 3; ++i) {
				for (int j = 0; j < 9; ++j) {
					this.addSlotToContainer(new Slot(player.inventory, i * 9 + j + 9, j * 18 + 8, i * 18 + 174));
				}
			}
			for (int i = 0; i < 9; ++i) {
				this.addSlotToContainer(new Slot(player.inventory, i, i * 18 + 8, 232));
			}
			NBTTagCompound nbt;
			if (stack.hasTagCompound()) {
				nbt = stack.getTagCompound();
			} else {
				nbt = new NBTTagCompound();
				stack.setTagCompound(nbt);
			}
			if (nbt.hasKey("Blacklist")) {
				NBTTagList blackList = nbt.getTagList("Blacklist", 10);
				if (blackList.tagCount() <= items.getSlots()) {
					for (int i = 0; i < blackList.tagCount(); i++) {
						NBTTagCompound black = blackList.getCompoundTagAt(i);
						if (black.hasKey("Slot") && black.hasKey("Name") && black.hasKey("Damage")) {
							ItemStack blackStack = new ItemStack(Item.getByNameOrId(black.getString("Name")), 1,
									black.getInteger("Damage"));
							items.setStackInSlot(black.getInteger("Slot"), blackStack);

						}
					}
				}
			}
		} else {
			this.stack = ItemStack.EMPTY;
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return !stack.isEmpty() && (slotIndex == -1 || slotIndex == player.inventory.currentItem);
	}

	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
		if (slotIndex != -1 && slotId == 108 + slotIndex) {
			return ItemStack.EMPTY;
		} else if (slotId >= 0 && slotId < items.getSlots()) {
			if (clickTypeIn == ClickType.PICKUP) {
				ItemStack stack = player.inventory.getItemStack().copy();
				stack.setCount(1);
				items.setStackInSlot(slotId, stack);
			}
			return ItemStack.EMPTY;
		}
		return super.slotClick(slotId, dragType, clickTypeIn, player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		return ItemStack.EMPTY;
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		if (!stack.isEmpty()) {
			NBTTagCompound nbt;
			if (stack.hasTagCompound()) {
				nbt = stack.getTagCompound();
			} else {
				nbt = new NBTTagCompound();
				stack.setTagCompound(nbt);
			}
			NBTTagList blackList = new NBTTagList();
			for (int i = 0; i < items.getSlots(); i++) {
				ItemStack blackStack = items.getStackInSlot(i);
				if (!blackStack.isEmpty()) {
					NBTTagCompound black = new NBTTagCompound();
					black.setInteger("Slot", i);
					black.setString("Name", blackStack.getItem().getRegistryName().toString());
					black.setInteger("Damage", blackStack.getItemDamage());
					blackList.appendTag(black);
				}
			}
			nbt.setTag("Blacklist", blackList);
		}
	}

}
