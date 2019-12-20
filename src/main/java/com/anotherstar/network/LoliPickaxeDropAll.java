package com.anotherstar.network;

import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.common.gui.InventoryLoliPickaxe;
import com.anotherstar.common.item.tool.ILoli;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class LoliPickaxeDropAll implements IMessage {

	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

	public static class MessageHandler implements IMessageHandler<LoliPickaxeDropAll, IMessage> {

		@Override
		public IMessage onMessage(LoliPickaxeDropAll message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			ItemStack loli = player.getHeldItemMainhand();
			if (loli.isEmpty() || !(loli.getItem() instanceof ILoli)) {
				loli = ctx.getServerHandler().player.getHeldItemOffhand();
			}
			if (!loli.isEmpty() && loli.getItem() instanceof ILoli) {
				InventoryLoliPickaxe inventory = new InventoryLoliPickaxe(loli);
				inventory.openInventory(player);
				NonNullList<ItemStack> stacks = NonNullList.create();
				for (int i = 0; i < ConfigLoader.loliPickaxeMaxPage; i++) {
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
			return null;
		}

	}

}
