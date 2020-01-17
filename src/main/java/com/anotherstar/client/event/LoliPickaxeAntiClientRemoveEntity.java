package com.anotherstar.client.event;

import com.anotherstar.util.LoliPickaxeUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class LoliPickaxeAntiClientRemoveEntity {

	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		if (LoliPickaxeUtil.invHaveLoliPickaxe(player)) {
			if (player.isDead) {
				player.isDead = false;
			}
			if (!player.world.playerEntities.contains(player)) {
				player.world.playerEntities.add(player);
				player.world.onEntityAdded(player);
			}
		}
	}

}
