package com.anotherstar.network;

import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.util.LoliPickaxeUtil;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class LoliSpaceFoldingPacket implements IMessage {

	private int id;

	public LoliSpaceFoldingPacket() {
	}

	public LoliSpaceFoldingPacket(int id) {
		this.id = id;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		id = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(id);
	}

	public int getId() {
		return id;
	}

	public static class MessageHandler implements IMessageHandler<LoliSpaceFoldingPacket, IMessage> {

		@Override
		public IMessage onMessage(LoliSpaceFoldingPacket message, MessageContext ctx) {
			if (ConfigLoader.loliPickaxeSpaceFolding) {
				EntityPlayer player = ctx.getServerHandler().player;
				if (LoliPickaxeUtil.invHaveLoliPickaxe(player) && player.dimension != message.getId()) {
					player.getServer().addScheduledTask(() -> {
						player.changeDimension(message.getId(), new LoliTeleporter(player.world));
					});
				}
			}
			return null;
		}

	}

	private static class LoliTeleporter implements ITeleporter {

		private World world;

		public LoliTeleporter(World world) {
			this.world = world;
		}

		@Override
		public void placeEntity(World world, Entity entity, float yaw) {
			double x = entity.posX;
			double y = entity.posY;
			double z = entity.posZ;
			if (this.world.provider.getDimensionType().getId() == -1) {
				x *= 8;
				y *= 8;
				z *= 2;
			}
			if (world.provider.getDimensionType().getId() == -1) {
				x /= 8;
				y /= 8;
				z /= 2;
			}
			entity.setLocationAndAngles(x, y, z, yaw, 0.0F);
		}

	}

}
