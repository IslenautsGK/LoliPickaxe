package com.anotherstar.network;

import com.anotherstar.common.LoliPickaxe;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public enum NetworkHandler {

	INSTANCE;

	private final SimpleNetworkWrapper channel = NetworkRegistry.INSTANCE.newSimpleChannel(LoliPickaxe.MODID);

	private NetworkHandler() {
		int index = 0;
		this.channel.registerMessage(LoliKillFacingPacket.MessageHandler.class, LoliKillFacingPacket.class, index++,
				Side.SERVER);
		this.channel.registerMessage(LoliDeadPacket.MessageHandler.class, LoliDeadPacket.class, index++, Side.CLIENT);
		this.channel.registerMessage(LoliConfigPacket.MessageHandler.class, LoliConfigPacket.class, index++,
				Side.CLIENT);
		this.channel.registerMessage(LoliItemConfigPacket.MessageHandler.class, LoliItemConfigPacket.class, index++,
				Side.SERVER);
		this.channel.registerMessage(LoliKillEntityPacket.MessageHandler.class, LoliKillEntityPacket.class, index++,
				Side.CLIENT);
		this.channel.registerMessage(LoliCardPacket.MessageHandler.class, LoliCardPacket.class, index++, Side.SERVER);
		this.channel.registerMessage(LoliPickaxeContainerPackte.MessageHandler.class, LoliPickaxeContainerPackte.class,
				index++, Side.SERVER);
		this.channel.registerMessage(LoliPickaxeContainerOpenPackte.MessageHandler.class,
				LoliPickaxeContainerOpenPackte.class, index++, Side.SERVER);
		this.channel.registerMessage(LoliPickaxeDropAll.MessageHandler.class, LoliPickaxeDropAll.class, index++,
				Side.SERVER);
	}

	public void sendMessageToDim(IMessage msg, int dim) {
		channel.sendToDimension(msg, dim);
	}

	public void sendMessageAroundPos(IMessage msg, int dim, BlockPos pos) {
		channel.sendToAllAround(msg, new NetworkRegistry.TargetPoint(dim, pos.getX(), pos.getY(), pos.getZ(), 2.0D));
	}

	public void sendMessageToPlayer(IMessage msg, EntityPlayerMP player) {
		channel.sendTo(msg, player);
	}

	public void sendMessageToAll(IMessage msg) {
		channel.sendToAll(msg);
	}

	public void sendMessageToServer(IMessage msg) {
		channel.sendToServer(msg);
	}

}
