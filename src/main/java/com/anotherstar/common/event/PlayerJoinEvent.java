package com.anotherstar.common.event;

import com.anotherstar.common.config.ConfigLoader;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class PlayerJoinEvent {

	@SubscribeEvent
	public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.player instanceof EntityPlayerMP) {
			ConfigLoader.sandChange((EntityPlayerMP) event.player);
			TextComponentString message = new TextComponentString("§2LoliPickaxe§f开源地址: ");
			TextComponentTranslation submsg = new TextComponentTranslation(
					"§9§nhttps://github.com/IslenautsGK/LoliPickaxe");
			submsg.getStyle()
					.setClickEvent(
							new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/IslenautsGK/LoliPickaxe"))
					.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("点击前往链接")));
			message.appendSibling(submsg);
			event.player.sendMessage(message);
		}
	}

}
