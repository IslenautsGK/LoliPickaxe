package com.anotherstar.network;

import com.anotherstar.common.gui.ILoliInventory;
import com.anotherstar.common.item.tool.IContainer;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class LoliPickaxeDropAllPacket implements IMessage {

	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

	public static class MessageHandler implements IMessageHandler<LoliPickaxeDropAllPacket, IMessage> {

		@Override
		public IMessage onMessage(LoliPickaxeDropAllPacket message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			if (!player.getServer().isCallingFromMinecraftThread()) {
				player.getServer().addScheduledTask(() -> {
					this.onMessage(message, ctx);
				});
			} else {
				ItemStack loli = player.getHeldItemMainhand();
				if (loli.isEmpty() || !(loli.getItem() instanceof IContainer)) {
					loli = ctx.getServerHandler().player.getHeldItemOffhand();
				}
				if (!loli.isEmpty() && loli.getItem() instanceof IContainer && ((IContainer) loli.getItem()).hasInventory(loli)) {
					ILoliInventory inventory = ((IContainer) loli.getItem()).getInventory(loli);
					inventory.openInventory(player);
					NonNullList<ItemStack> stacks = NonNullList.create();
					for (int i = 0; i < inventory.getMaxPage(); i++) {
						for (ItemStack stack : inventory.getPage(i)) {
							if (!stack.isEmpty()) {
								stacks.add(stack);
							}
						}
					}
					inventory.clear();
					inventory.closeInventory(player);
					for (ItemStack stack : stacks) {
						player.dropItem(stack, true, false);
					}
				}
			}
			return null;
		}

	}

}
