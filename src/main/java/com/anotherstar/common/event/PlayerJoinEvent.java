package com.anotherstar.common.event;

import com.anotherstar.common.config.ConfigLoader;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;

public class PlayerJoinEvent {

	@SubscribeEvent
	public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.player instanceof EntityPlayerMP) {
			ConfigLoader.sandChange((EntityPlayerMP) event.player);
			ChatComponentText message = new ChatComponentText("§2LoliPickaxe§f开源地址: ");
			ChatComponentTranslation submsg = new ChatComponentTranslation(
					"§9§nhttps://github.com/IslenautsGK/LoliPickaxe");
			submsg.getChatStyle()
					.setChatClickEvent(
							new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/IslenautsGK/LoliPickaxe"))
					.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("点击前往链接")));
			message.appendSibling(submsg);
			event.player.addChatComponentMessage(message);
		}
	}

}
