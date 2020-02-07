package com.anotherstar.client.model;

import com.anotherstar.client.util.obj.GroupObject;
import com.anotherstar.client.util.obj.ObjModelManager;
import com.anotherstar.client.util.obj.ResourceLocationRaw;
import com.anotherstar.client.util.obj.WavefrontObject;
import com.anotherstar.common.LoliPickaxe;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelNevermore extends ModelBase {

	private final RenderManager manager;
	private final WavefrontObject nevermore;
	private GroupObject head;
	private GroupObject la;
	private GroupObject ra;

	public ModelNevermore(RenderManager manager) {
		this.manager = manager;
		this.nevermore = ObjModelManager.getModel(new ResourceLocationRaw(LoliPickaxe.MODID, "models/entity/loli/loli.obj"));
		for (GroupObject group : this.nevermore.groupObjects) {
			if (group.name.equals("head")) {
				this.head = group;
			} else if (group.name.equals("la")) {
				this.la = group;
			} else if (group.name.equals("ra")) {
				this.ra = group;
			}
		}
	}

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		GlStateManager.pushMatrix();
		GlStateManager.rotate(180, 1, 0, 0);
		GlStateManager.translate(0, -1.5, 0);
		la.rotateAngleX = MathHelper.cos((float) (limbSwing + Math.PI)) * limbSwingAmount * 1.5F;
		ra.rotateAngleX = MathHelper.cos((float) (limbSwing)) * limbSwingAmount * 1.5F;
		head.rotateAngleY = -netHeadYaw * 0.017453292F;
		head.rotateAngleX = headPitch * 0.017453292F;
		nevermore.renderAll(manager);
		GlStateManager.popMatrix();
	}

}
