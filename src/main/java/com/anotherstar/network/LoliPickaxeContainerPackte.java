package com.anotherstar.network;

import com.anotherstar.common.gui.ContainerLoliPickaxe;

import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class LoliPickaxeContainerPackte implements IMessage {

	private boolean next;

	public LoliPickaxeContainerPackte() {
	}

	public LoliPickaxeContainerPackte(boolean next) {
		this.next = next;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		next = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(next);
	}

	public boolean isNext() {
		return next;
	}

	public static class MessageHandler implements IMessageHandler<LoliPickaxeContainerPackte, IMessage> {

		@Override
		public IMessage onMessage(LoliPickaxeContainerPackte message, MessageContext ctx) {
			Container container = ctx.getServerHandler().player.openContainer;
			if (container instanceof ContainerLoliPickaxe) {
				if (message.isNext()) {
					((ContainerLoliPickaxe) container).nextPage();
				} else {
					((ContainerLoliPickaxe) container).prePage();
				}
			}
			return null;
		}

	}

}
