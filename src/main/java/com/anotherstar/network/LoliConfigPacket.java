package com.anotherstar.network;

import com.anotherstar.common.config.ConfigLoader;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LoliConfigPacket implements IMessage {

	private NBTTagCompound data;

	public LoliConfigPacket() {
	}

	public LoliConfigPacket(NBTTagCompound data) {
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

	public static class MessageHandler implements IMessageHandler<LoliConfigPacket, IMessage> {

		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(LoliConfigPacket message, MessageContext ctx) {
			ConfigLoader.receptionChange(message.getData());
			return null;
		}

	}

}
