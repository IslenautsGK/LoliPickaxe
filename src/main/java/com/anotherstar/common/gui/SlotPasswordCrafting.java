package com.anotherstar.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class SlotPasswordCrafting extends Slot {

	private final InventoryCrafting craftMatrix;
	private final EntityPlayer player;
	private int amountCrafted;

	public SlotPasswordCrafting(EntityPlayer player, InventoryCrafting craftingInventory, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
		super(inventoryIn, slotIndex, xPosition, yPosition);
		this.player = player;
		this.craftMatrix = craftingInventory;
	}

	public boolean isItemValid(ItemStack stack) {
		return false;
	}

	public ItemStack decrStackSize(int amount) {
		if (getHasStack()) {
			amountCrafted += Math.min(amount, getStack().getCount());
		}

		return super.decrStackSize(amount);
	}

	protected void onCrafting(ItemStack stack, int amount) {
		amountCrafted += amount;
		onCrafting(stack);
	}

	protected void onSwapCraft(int amount) {
		amountCrafted += amount;
	}

	protected void onCrafting(ItemStack stack) {
		if (amountCrafted > 0) {
			stack.onCrafting(player.world, player, amountCrafted);
			FMLCommonHandler.instance().firePlayerCraftingEvent(player, stack, craftMatrix);
		}
		amountCrafted = 0;
	}

	public ItemStack onTake(EntityPlayer thePlayer, ItemStack stackIn) {
		onCrafting(stackIn);
		for (int i = 0; i < craftMatrix.getSizeInventory(); ++i) {
			ItemStack stack = craftMatrix.getStackInSlot(i);
			if (!stack.isEmpty()) {
				craftMatrix.decrStackSize(i, 1);
			}
		}
		return stackIn;
	}
}
