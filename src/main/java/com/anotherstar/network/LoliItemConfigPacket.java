package com.anotherstar.network;

import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.common.item.tool.ILoli;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class LoliItemConfigPacket implements IMessage {

	private NBTTagCompound data;

	public LoliItemConfigPacket() {
	}

	public LoliItemConfigPacket(NBTTagCompound data) {
		this.data = data;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		data = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, data);
	}

	public NBTTagCompound getData() {
		return data;
	}

	public static class MessageHandler implements IMessageHandler<LoliItemConfigPacket, IMessage> {

		@Override
		public IMessage onMessage(LoliItemConfigPacket message, MessageContext ctx) {
			ItemStack stack = ctx.getServerHandler().player.getHeldItemMainhand();
			if (!stack.isEmpty() && stack.getItem() instanceof ILoli) {
				ConfigLoader.setItemConfigs(stack, message.getData());
			}
			return null;
		}

	}

}
