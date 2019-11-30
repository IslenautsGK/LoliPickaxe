package com.anotherstar.common.event;

import com.anotherstar.common.config.ConfigLoader;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

public class PlayerJoinEvent {

	@SubscribeEvent
	public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.player instanceof EntityPlayerMP) {
			ConfigLoader.sandChange((EntityPlayerMP) event.player);
			event.player.addChatComponentMessage(new ChatComponentText("LoliPickaxe开源地址:"));
			event.player.addChatComponentMessage(new ChatComponentText("https://github.com/IslenautsGK/LoliPickaxe"));
		}
	}

}
