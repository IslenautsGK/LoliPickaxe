package com.anotherstar.network;

import java.util.EnumSet;
import java.util.Set;

import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.util.LoliPickaxeUtil;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class LoliSpaceFoldingPacket implements IMessage {

	private int id;
	private double x;
	private double y;
	private double z;

	public LoliSpaceFoldingPacket() {
	}

	public LoliSpaceFoldingPacket(int id, double x, double y, double z) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		id = buf.readInt();
		x = buf.readDouble();
		y = buf.readDouble();
		z = buf.readDouble();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(id);
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
	}

	public int getId() {
		return id;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public static class MessageHandler implements IMessageHandler<LoliSpaceFoldingPacket, IMessage> {

		@Override
		public IMessage onMessage(LoliSpaceFoldingPacket message, MessageContext ctx) {
			if (ConfigLoader.loliPickaxeSpaceFolding) {
				EntityPlayerMP player = ctx.getServerHandler().player;
				if (LoliPickaxeUtil.invHaveLoliPickaxe(player)) {
					if (player.dimension == message.getId()) {
						player.getServer().addScheduledTask(() -> {
							Set<SPacketPlayerPosLook.EnumFlags> set = EnumSet.<SPacketPlayerPosLook.EnumFlags>noneOf(SPacketPlayerPosLook.EnumFlags.class);
							set.add(SPacketPlayerPosLook.EnumFlags.X);
							set.add(SPacketPlayerPosLook.EnumFlags.Y);
							set.add(SPacketPlayerPosLook.EnumFlags.Z);
							double x = message.getX();
							double y = message.getY();
							double z = message.getZ();
							double proportion = Math.sqrt(x * x + y * y + z * z) / ConfigLoader.loliPickaxeMaxTeleportDistance;
							if (proportion > 1) {
								x /= proportion;
								y /= proportion;
								z /= proportion;
							}
							player.dismountRidingEntity();
							player.connection.setPlayerLocation(x, y, z, player.rotationYaw, player.rotationPitch, set);
						});
					} else if (!ConfigLoader.loliPickaxeWorldBlacklist.contains(message.getId())) {
						player.getServer().addScheduledTask(() -> {
							player.changeDimension(message.getId(), new LoliTeleporter(message.getX(), message.getY(), message.getZ()));
						});
					}
				}
			}
			return null;
		}

	}

	private static class LoliTeleporter implements ITeleporter {

		private double x;
		private double y;
		private double z;

		public LoliTeleporter(double x, double y, double z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		@Override
		public void placeEntity(World world, Entity entity, float yaw) {
			double proportion = Math.sqrt(x * x + y * y + z * z) / ConfigLoader.loliPickaxeMaxTeleportDistance;
			if (proportion > 1) {
				x /= proportion;
				y /= proportion;
				z /= proportion;
			}
			entity.setLocationAndAngles(x + entity.posX, y + entity.posY, z + entity.posZ, yaw, 0.0F);
		}

	}

}
