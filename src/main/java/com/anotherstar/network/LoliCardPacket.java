package com.anotherstar.network;

import java.util.Map;

import com.anotherstar.common.item.ItemLoliCard;
import com.anotherstar.common.item.ItemLoliCardAlbum;
import com.google.common.collect.Maps;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class LoliCardPacket implements IMessage {

	private int slot;
	private ItemType type;
	private String name;

	public LoliCardPacket() {
	}

	public LoliCardPacket(int slot, ItemType type, String name) {
		this.slot = slot;
		this.type = type;
		this.name = name;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		slot = buf.readInt();
		type = ItemType.idToElement.get(buf.readInt());
		name = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(slot);
		buf.writeInt(type.getId());
		ByteBufUtils.writeUTF8String(buf, name);
	}

	public int getSlot() {
		return slot;
	}

	public ItemType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public static enum ItemType {

		LOLICARD(0), LOLICARDALBUM(1);

		public static Map<Integer, ItemType> idToElement = Maps.newHashMap();

		private final int id;

		private ItemType(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}

		static {
			for (ItemType element : values()) {
				idToElement.put(element.id, element);
			}
		}

	}

	public static class MessageHandler implements IMessageHandler<LoliCardPacket, IMessage> {

		@Override
		public IMessage onMessage(LoliCardPacket message, MessageContext ctx) {
			ItemStack stack = ctx.getServerHandler().player.inventory.getStackInSlot(message.getSlot());
			switch (message.getType()) {
			case LOLICARD:
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
				break;
			case LOLICARDALBUM:
				if (stack.getItem() instanceof ItemLoliCardAlbum) {
					NBTTagCompound nbt;
					if (!stack.hasTagCompound()) {
						nbt = new NBTTagCompound();
						stack.setTagCompound(nbt);
					} else {
						nbt = stack.getTagCompound();
					}
					if (!nbt.hasKey("PictureGroup")) {
						nbt.setString("PictureGroup", message.getName());
					}
				}
				break;
			}
			return null;
		}
	}

}
