package com.anotherstar.client.render;

import com.anotherstar.common.LoliPickaxe;
import com.anotherstar.common.entity.EntityLoli;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderLoli extends RenderLiving<EntityLoli> {

	private static final ResourceLocation TEXTURE_LOLI = new ResourceLocation(LoliPickaxe.MODID,
			"textures/entities/loli.png");;

	public RenderLoli(RenderManager rendermanager, ModelBase modelbase, float shadowsize) {
		super(rendermanager, modelbase, shadowsize);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityLoli entity) {
		return TEXTURE_LOLI;
	}

}
