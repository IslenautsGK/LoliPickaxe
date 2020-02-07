package com.anotherstar.common.gui;

import com.anotherstar.common.block.BlockLoader;
import com.anotherstar.common.recipe.password.PasswordRecipeManager;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ContainerPasswordWorkbench extends Container {

	public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
	public InventoryCraftResult craftResult = new InventoryCraftResult();
	private final World world;
	private final BlockPos pos;
	private final EntityPlayer player;
	private String password;

	public ContainerPasswordWorkbench(InventoryPlayer playerInventory, World worldIn, BlockPos posIn) {
		this.world = worldIn;
		this.pos = posIn;
		this.player = playerInventory.player;
		this.addSlotToContainer(new SlotPasswordCrafting(playerInventory.player, this.craftMatrix, this.craftResult, 0, 124, 65));
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				this.addSlotToContainer(new Slot(this.craftMatrix, j + i * 3, 30 + j * 18, 47 + i * 18));
			}
		}
		for (int k = 0; k < 3; ++k) {
			for (int i1 = 0; i1 < 9; ++i1) {
				this.addSlotToContainer(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 114 + k * 18));
			}
		}
		for (int l = 0; l < 9; ++l) {
			this.addSlotToContainer(new Slot(playerInventory, l, 8 + l * 18, 172));
		}
		password = "";
	}

	public void setPassword(String password) {
		this.password = password;
		onCraftMatrixChanged(craftMatrix);
	}

	@Override
	public void onCraftMatrixChanged(IInventory inventoryIn) {
		if (!world.isRemote) {
			ItemStack stack = PasswordRecipeManager.findMatchingResult(craftMatrix, player, password);
			craftResult.setInventorySlotContents(0, stack);
			((EntityPlayerMP) player).connection.sendPacket(new SPacketSetSlot(this.windowId, 0, stack));
		}
	}

	@Override
	public void onContainerClosed(EntityPlayer playerIn) {
		super.onContainerClosed(playerIn);
		if (!world.isRemote) {
			clearContainer(playerIn, world, craftMatrix);
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		if (world.getBlockState(pos).getBlock() != BlockLoader.passwordWorkBench) {
			return false;
		} else {
			return playerIn.getDistanceSq(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64.0;
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack resultStack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack slotStack = slot.getStack();
			resultStack = slotStack.copy();
			if (index == 0) {
				slotStack.getItem().onCreated(slotStack, this.world, playerIn);
				if (!mergeItemStack(slotStack, 10, 46, true)) {
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(slotStack, resultStack);
			} else if (index >= 10 && index < 37) {
				if (!mergeItemStack(slotStack, 37, 46, false)) {
					return ItemStack.EMPTY;
				}
			} else if (index >= 37 && index < 46) {
				if (!mergeItemStack(slotStack, 10, 37, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!mergeItemStack(slotStack, 10, 46, false)) {
				return ItemStack.EMPTY;
			}
			if (slotStack.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
			if (slotStack.getCount() == resultStack.getCount()) {
				return ItemStack.EMPTY;
			}
			ItemStack dropStack = slot.onTake(playerIn, slotStack);
			if (index == 0) {
				playerIn.dropItem(dropStack, false);
			}
		}
		return resultStack;
	}

	@Override
	public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
		return slotIn.inventory != craftResult && super.canMergeSlot(stack, slotIn);
	}

}
