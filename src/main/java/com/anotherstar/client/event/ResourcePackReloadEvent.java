package com.anotherstar.client.event;

import com.anotherstar.util.LoliCardUtil;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;

public class ResourcePackReloadEvent {

	@SubscribeEvent
	public void ReloadResourcePackEvent(TextureStitchEvent.Post event) {
		LoliCardUtil.updateCustomArtDatas();
	}

}
