package com.anotherstar.common.gui;

import com.anotherstar.common.item.tool.IContainer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerLoliPickaxe extends Container {

	public ILoliInventory inventory;
	private EntityPlayer player;
	private ItemStack stack;
	private int slotIndex;

	public ContainerLoliPickaxe(EntityPlayer player, ItemStack stack, int slotIndex) {
		if (!stack.isEmpty() && stack.getItem() instanceof IContainer) {
			this.stack = stack;
			this.inventory = ((IContainer) stack.getItem()).getInventory(stack);
			this.inventory.openInventory(player);
			this.player = player;
			this.slotIndex = slotIndex;
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					this.addSlotToContainer(new Slot(this.inventory, i * 9 + j, j * 18 + 8, i * 18 + 8));
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
		}
		return super.slotClick(slotId, dragType, clickTypeIn, player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack stackResult = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack stack = slot.getStack();
			stackResult = stack.copy();
			if (index < inventory.getSizeInventory()) {
				if (!mergeItemStack(stack, inventory.getSizeInventory(), inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!mergeItemStack(stack, 0, inventory.getSizeInventory(), false)) {
				return ItemStack.EMPTY;
			}
			if (stack.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
		}
		return stackResult;
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		inventory.closeInventory(player);
	}

	public void prePage() {
		inventory.setField(0, inventory.getField(0) - 1);
		updateSlot();
	}

	public void nextPage() {
		inventory.setField(0, inventory.getField(0) + 1);
		updateSlot();
	}

	private void updateSlot() {
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			inventorySlots.get(i).onSlotChanged();
		}
	}

}
