package com.anotherstar.network;

import com.anotherstar.util.LoliPickaxeUtil;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class LoliKillFacingPacket implements IMessage {

	public LoliKillFacingPacket() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

	public static class MessageHandler implements IMessageHandler<LoliKillFacingPacket, IMessage> {

		@Override
		public IMessage onMessage(LoliKillFacingPacket message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			if (!player.getServer().isCallingFromMinecraftThread()) {
				player.getServer().addScheduledTask(() -> {
					this.onMessage(message, ctx);
				});
			} else {
				LoliPickaxeUtil.killFacing(player);
				BlockPos pos = player.getPosition();
				((EntityPlayerMP) player).connection.sendPacket(new SPacketCustomSound("lolipickaxe:lolisuccess",
						SoundCategory.BLOCKS, pos.getX(), pos.getY(), pos.getZ(), 1.0F, 1.0F));

			}
			return null;
		}

	}

}
