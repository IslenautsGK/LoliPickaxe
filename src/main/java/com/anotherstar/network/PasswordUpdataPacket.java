package com.anotherstar.network;

import com.anotherstar.common.gui.ContainerPasswordWorkbench;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PasswordUpdataPacket implements IMessage {

	private String password;

	public PasswordUpdataPacket() {
	}

	public PasswordUpdataPacket(String password) {
		this.password = password;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		password = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, password);
	}

	public String getPassword() {
		return password;
	}

	public static class MessageHandler implements IMessageHandler<PasswordUpdataPacket, IMessage> {

		@Override
		public IMessage onMessage(PasswordUpdataPacket message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			if (player.openContainer != null && player.openContainer instanceof ContainerPasswordWorkbench) {
				((ContainerPasswordWorkbench) player.openContainer).setPassword(message.getPassword());
			}
			return null;
		}
	}

}
