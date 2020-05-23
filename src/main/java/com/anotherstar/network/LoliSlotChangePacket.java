package com.anotherstar.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LoliSlotChangePacket implements IMessage {

	private int windowId;
	private int slotIndex;
	private ItemStack stack;

	public LoliSlotChangePacket() {
	}

	public LoliSlotChangePacket(int windowId, int slotIndex, ItemStack stack) {
		this.windowId = windowId;
		this.slotIndex = slotIndex;
		this.stack = stack.copy();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		windowId = buf.readByte();
		slotIndex = buf.readShort();
		int itemId = buf.readShort();
		if (itemId < 0) {
			stack = ItemStack.EMPTY;
		} else {
			int count = buf.readInt();
			int meta = buf.readShort();
			stack = new ItemStack(Item.getItemById(itemId), count, meta);
			// stack.getItem().readNBTShareTag(stack, ByteBufUtils.readTag(buf));
			stack.setTagCompound(ByteBufUtils.readTag(buf));
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(windowId);
		buf.writeShort(slotIndex);
		if (stack.isEmpty()) {
			buf.writeShort(-1);
		} else {
			buf.writeShort(Item.getIdFromItem(stack.getItem()));
			buf.writeInt(stack.getCount());
			buf.writeShort(stack.getMetadata());
			NBTTagCompound nbt = null;
			if (stack.getItem().isDamageable() || stack.getItem().getShareTag()) {
				// nbt = stack.getItem().getNBTShareTag(stack);
				nbt = stack.getTagCompound();
			}
			ByteBufUtils.writeTag(buf, nbt);
		}
	}

	public int getWindowId() {
		return windowId;
	}

	public int getSlotIndex() {
		return slotIndex;
	}

	public ItemStack getStack() {
		return stack;
	}

	public static class MessageHandler implements IMessageHandler<LoliSlotChangePacket, IMessage> {

		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(LoliSlotChangePacket message, MessageContext ctx) {
			Minecraft mc = Minecraft.getMinecraft();
			EntityPlayer player = mc.player;
			ItemStack itemstack = message.getStack();
			int i = message.getSlotIndex();
			mc.getTutorial().handleSetSlot(itemstack);
			if (message.getWindowId() == -1) {
				player.inventory.setItemStack(itemstack);
			} else if (message.getWindowId() == -2) {
				player.inventory.setInventorySlotContents(i, itemstack);
			} else {
				boolean flag = false;
				if (mc.currentScreen instanceof GuiContainerCreative) {
					GuiContainerCreative guicontainercreative = (GuiContainerCreative) mc.currentScreen;
					flag = guicontainercreative.getSelectedTabIndex() != CreativeTabs.INVENTORY.getTabIndex();
				}
				if (message.getWindowId() == 0 && message.getSlotIndex() >= 36 && i < 45) {
					if (!itemstack.isEmpty()) {
						ItemStack itemstack1 = player.inventoryContainer.getSlot(i).getStack();
						if (itemstack1.isEmpty() || itemstack1.getCount() < itemstack.getCount()) {
							itemstack.setAnimationsToGo(5);
						}
					}
					player.inventoryContainer.putStackInSlot(i, itemstack);
				} else if (message.getWindowId() == player.openContainer.windowId && (message.getWindowId() != 0 || !flag)) {
					player.openContainer.putStackInSlot(i, itemstack);
				}
			}
			return null;
		}

	}

}
