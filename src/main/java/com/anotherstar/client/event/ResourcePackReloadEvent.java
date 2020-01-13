package com.anotherstar.client.event;

import com.anotherstar.client.util.LoliCardUtil;
import com.anotherstar.client.util.obj.ObjModelManager;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ResourcePackReloadEvent {

	@SubscribeEvent
	public void ReloadResourcePackEvent(TextureStitchEvent.Post event) {
		LoliCardUtil.updateCustomArtDatas();
		ObjModelManager.reload();
	}

}
