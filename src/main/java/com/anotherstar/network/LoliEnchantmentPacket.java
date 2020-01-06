package com.anotherstar.network;

import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.common.item.tool.ILoli;

import io.netty.buffer.ByteBuf;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class LoliEnchantmentPacket implements IMessage {

	NBTTagCompound ench;

	public LoliEnchantmentPacket() {
	}

	public LoliEnchantmentPacket(NBTTagCompound ench) {
		this.ench = ench;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		ench = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, ench);
	}

	public static class MessageHandler implements IMessageHandler<LoliEnchantmentPacket, IMessage> {

		@Override
		public IMessage onMessage(LoliEnchantmentPacket message, MessageContext ctx) {
			ItemStack stack = ctx.getServerHandler().player.getHeldItemMainhand();
			if (!stack.isEmpty() && stack.getItem() instanceof ILoli) {
				if (message.ench.hasKey("ench")) {
					NBTTagCompound nbt;
					if (stack.hasTagCompound()) {
						nbt = stack.getTagCompound();
					} else {
						nbt = new NBTTagCompound();
						stack.setTagCompound(nbt);
					}
					NBTTagList list = message.ench.getTagList("ench", 10);
					for (int i = 0; i < list.tagCount(); i++) {
						NBTTagCompound element = list.getCompoundTagAt(i);
						short lvl = element.getShort("lvl");
						String name = Enchantment.getEnchantmentByID(element.getShort("id")).getRegistryName().toString();
						if (ConfigLoader.loliPickaxeEnchantmentLimit.containsKey(name)) {
							if (element.getShort("lvl") > ConfigLoader.loliPickaxeEnchantmentLimit.get(name)) {
								element.setShort("lvl", (short) (int) ConfigLoader.loliPickaxeEnchantmentLimit.get(name));
							}
						} else if (element.getShort("lvl") > ConfigLoader.loliPickaxeEnchantmentDefaultLimit) {
							element.setShort("lvl", (short) ConfigLoader.loliPickaxeEnchantmentDefaultLimit);
						}
					}
					nbt.setTag("ench", message.ench.getTagList("ench", 10));
				} else if (stack.hasTagCompound() && stack.getTagCompound().hasKey("ench")) {
					stack.getTagCompound().removeTag("ench");
				}
			}
			return null;
		}

	}

}
