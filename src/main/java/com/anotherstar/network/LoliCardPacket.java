package com.anotherstar.network;

import com.anotherstar.common.item.ItemLoliCard;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class LoliCardPacket implements IMessage {

	private int slot;
	private String name;

	public LoliCardPacket() {
	}

	public LoliCardPacket(int slot, String name) {
		this.slot = slot;
		this.name = name;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		slot = buf.readInt();
		name = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(slot);
		ByteBufUtils.writeUTF8String(buf, name);
	}

	public int getSlot() {
		return slot;
	}

	public String getName() {
		return name;
	}

	public static class MessageHandler implements IMessageHandler<LoliCardPacket, IMessage> {

		@Override
		public IMessage onMessage(LoliCardPacket message, MessageContext ctx) {
			ItemStack stack = ctx.getServerHandler().player.inventory.getStackInSlot(message.getSlot());
			if (stack.getItem() instanceof ItemLoliCard) {
				NBTTagCompound nbt;
				if (!stack.hasTagCompound()) {
					nbt = new NBTTagCompound();
					stack.setTagCompound(nbt);
				} else {
					nbt = stack.getTagCompound();
				}
				if (!nbt.hasKey("picture")) {
					nbt.setString("picture", message.getName());
				}
			}
			return null;
		}
	}

}
