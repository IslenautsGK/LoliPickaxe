package com.anotherstar.common.event;

import com.anotherstar.common.config.ConfigLoader;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayerMP;

public class PlayerJoinEvent {

	@SubscribeEvent
	public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.player instanceof EntityPlayerMP) {
			ConfigLoader.sandChange((EntityPlayerMP) event.player);
		}
	}

}
