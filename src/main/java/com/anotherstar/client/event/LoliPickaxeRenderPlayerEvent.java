package com.anotherstar.client.event;

import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.util.LoliPickaxeUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LoliPickaxeRenderPlayerEvent {

	@SubscribeEvent
	public void onPlayerRender(RenderPlayerEvent.Pre event) {
		EntityPlayer player = event.getEntityPlayer();
		ItemStack loli = LoliPickaxeUtil.getLoliPickaxe(player);
		if (!loli.isEmpty() && ConfigLoader.getBoolean(loli, "loliPickaxeInvisible")) {
			event.setCanceled(true);
		}
	}

}
