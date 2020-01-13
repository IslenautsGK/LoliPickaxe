package com.anotherstar.client.model;

import com.anotherstar.client.util.obj.ObjModelManager;
import com.anotherstar.client.util.obj.ResourceLocationRaw;
import com.anotherstar.common.LoliPickaxe;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;

public class ModelNevermore extends ModelBase {

	private final RenderManager manager;
	private final ResourceLocationRaw nevermore;

	public ModelNevermore(RenderManager manager) {
		this.manager = manager;
		this.nevermore = new ResourceLocationRaw(LoliPickaxe.MODID, "models/entity/loli/loli.obj");
	}

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		GlStateManager.pushMatrix();
		GlStateManager.rotate(180, 1, 0, 0);
		GlStateManager.translate(0, -1.5, 0);
		ObjModelManager.getModel(nevermore).renderAll(manager);
		GlStateManager.popMatrix();
	}

}
