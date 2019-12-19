package com.anotherstar.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LoliKillEntityPacket implements IMessage {

	private int worldID;
	private int entityID;

	public LoliKillEntityPacket() {
	}

	public LoliKillEntityPacket(int worldID, int entityID) {
		this.worldID = worldID;
		this.entityID = entityID;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		worldID = buf.readInt();
		entityID = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(worldID);
		buf.writeInt(entityID);
	}

	public int getWorldID() {
		return worldID;
	}

	public int getEntityID() {
		return entityID;
	}

	public static class MessageHandler implements IMessageHandler<LoliKillEntityPacket, IMessage> {

		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(LoliKillEntityPacket message, MessageContext ctx) {
			if (Minecraft.getMinecraft().player.dimension == message.getWorldID()) {
				Entity entity = Minecraft.getMinecraft().player.world.getEntityByID(message.getEntityID());
				if (entity instanceof EntityLivingBase) {
					entity.isDead = true;
					((EntityLivingBase) entity).loliDead = true;
					((EntityLivingBase) entity).loliCool = true;
				}
			}
			return null;
		}

	}

}
