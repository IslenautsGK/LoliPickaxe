package com.anotherstar.client.event;

import com.anotherstar.client.render.RenderLoliCardFrame;
import com.anotherstar.common.config.ConfigLoader;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class LoliCardAlbumSwitchEvent {

	private int tick = 0;

	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
		if (event.phase == Phase.START) {
			if (++tick >= ConfigLoader.loliCardAlbumSwitchSpeed) {
				tick = 0;
				if (++RenderLoliCardFrame.step == Integer.MAX_VALUE) {
					RenderLoliCardFrame.step = 0;
				}
			}
		}
	}

}
