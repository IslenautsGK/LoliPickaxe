package com.anotherstar.network;

import java.util.List;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LoliSlotsInitPacket implements IMessage {

	private int windowId;
	private List<ItemStack> stacks;

	public LoliSlotsInitPacket() {
	}

	public LoliSlotsInitPacket(int windowId, List<ItemStack> stacks) {
		this.windowId = windowId;
		this.stacks = NonNullList.<ItemStack>withSize(stacks.size(), ItemStack.EMPTY);
		for (int i = 0; i < this.stacks.size(); ++i) {
			ItemStack stack = stacks.get(i);
			this.stacks.set(i, stack.copy());
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		windowId = buf.readUnsignedByte();
		int size = buf.readShort();
		stacks = NonNullList.<ItemStack>withSize(size, ItemStack.EMPTY);
		for (int j = 0; j < size; ++j) {
			ItemStack stack;
			int itemId = buf.readShort();
			if (itemId < 0) {
				stack = ItemStack.EMPTY;
			} else {
				int count = buf.readInt();
				int meta = buf.readShort();
				stack = new ItemStack(Item.getItemById(itemId), count, meta);
				stack.getItem().readNBTShareTag(stack, ByteBufUtils.readTag(buf));
			}
			stacks.set(j, stack);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(this.windowId);
		buf.writeShort(stacks.size());
		for (ItemStack stack : stacks) {
			if (stack.isEmpty()) {
				buf.writeShort(-1);
			} else {
				buf.writeShort(Item.getIdFromItem(stack.getItem()));
				buf.writeInt(stack.getCount());
				buf.writeShort(stack.getMetadata());
				NBTTagCompound nbt = null;
				if (stack.getItem().isDamageable() || stack.getItem().getShareTag()) {
					nbt = stack.getItem().getNBTShareTag(stack);
				}
				ByteBufUtils.writeTag(buf, nbt);
			}
		}
	}

	public int getWindowId() {
		return windowId;
	}

	public List<ItemStack> getStacks() {
		return stacks;
	}

	public static class MessageHandler implements IMessageHandler<LoliSlotsInitPacket, IMessage> {

		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(LoliSlotsInitPacket message, MessageContext ctx) {
			Minecraft mc = Minecraft.getMinecraft();
			EntityPlayer player = mc.player;
			if (message.getWindowId() == 0) {
				player.inventoryContainer.setAll(message.getStacks());
			} else if (message.getWindowId() == player.openContainer.windowId) {
				player.openContainer.setAll(message.getStacks());
			}
			return null;
		}
	}

}
