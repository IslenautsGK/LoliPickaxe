package com.anotherstar.network;

import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.common.item.tool.ILoli;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class LoliPotionPacket implements IMessage {

	NBTTagCompound potion;

	public LoliPotionPacket() {
	}

	public LoliPotionPacket(NBTTagCompound ench) {
		this.potion = ench;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		potion = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, potion);
	}

	public NBTTagCompound getPotion() {
		return potion;
	}

	public static class MessageHandler implements IMessageHandler<LoliPotionPacket, IMessage> {

		@Override
		public IMessage onMessage(LoliPotionPacket message, MessageContext ctx) {
			ItemStack stack = ctx.getServerHandler().player.getHeldItemMainhand();
			if (!stack.isEmpty() && stack.getItem() instanceof ILoli) {
				if (message.getPotion().hasKey("LoliPotion")) {
					NBTTagCompound nbt;
					if (stack.hasTagCompound()) {
						nbt = stack.getTagCompound();
					} else {
						nbt = new NBTTagCompound();
						stack.setTagCompound(nbt);
					}
					NBTTagList list = message.getPotion().getTagList("LoliPotion", 10);
					for (int i = 0; i < list.tagCount(); i++) {
						NBTTagCompound element = list.getCompoundTagAt(i);
						int lvl = element.getByte("lvl");
						String name = Potion.getPotionById(element.getShort("id")).getRegistryName().toString();
						if (ConfigLoader.loliPickaxePotionLimit.containsKey(name)) {
							if (element.getByte("lvl") > ConfigLoader.loliPickaxePotionLimit.get(name)) {
								element.setByte("lvl", (byte) (int) ConfigLoader.loliPickaxePotionLimit.get(name));
							}
						} else if (element.getByte("lvl") > ConfigLoader.loliPickaxePotionDefaultLimit) {
							element.setByte("lvl", (byte) ConfigLoader.loliPickaxePotionDefaultLimit);
						}
					}
					nbt.setTag("LoliPotion", message.getPotion().getTagList("LoliPotion", 10));
				} else if (stack.hasTagCompound() && stack.getTagCompound().hasKey("LoliPotion")) {
					stack.getTagCompound().removeTag("LoliPotion");
				}
			}
			return null;
		}

	}

}
