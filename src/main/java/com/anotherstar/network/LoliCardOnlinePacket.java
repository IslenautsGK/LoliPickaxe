package com.anotherstar.network;

import com.anotherstar.common.item.ItemLoliCardOnline;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class LoliCardOnlinePacket implements IMessage {

	private String name;

	public LoliCardOnlinePacket() {
	}

	public LoliCardOnlinePacket(String name) {
		this.name = name;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		name = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, name);
	}

	public String getName() {
		return name;
	}

	public static class MessageHandler implements IMessageHandler<LoliCardOnlinePacket, IMessage> {

		@Override
		public IMessage onMessage(LoliCardOnlinePacket message, MessageContext ctx) {
			ItemStack stack = ctx.getServerHandler().player.getHeldItemMainhand();
			if (!stack.isEmpty() && stack.getItem() instanceof ItemLoliCardOnline) {
				NBTTagCompound nbt;
				if (stack.hasTagCompound()) {
					nbt = stack.getTagCompound();
				} else {
					nbt = new NBTTagCompound();
					stack.setTagCompound(nbt);
				}
				nbt.setString("ImageUrl", message.getName());
			}
			return null;
		}
	}

}
