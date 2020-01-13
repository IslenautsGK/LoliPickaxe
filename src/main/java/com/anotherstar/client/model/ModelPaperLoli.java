package com.anotherstar.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelPaperLoli extends ModelBase {

	private final ModelRenderer leftLeg;
	private final ModelRenderer rightLeg;
	private final ModelRenderer body;
	private final ModelRenderer leftArm;
	private final ModelRenderer rightArm;
	private final ModelRenderer head;

	public ModelPaperLoli() {
		textureWidth = 32;
		textureHeight = 32;
		leftLeg = new ModelRenderer(this);
		leftLeg.setRotationPoint(1.5F, 17.0F, 0.0F);
		leftLeg.cubeList.add(new ModelBox(leftLeg, 18, 0, -1.5F, 0.0F, 0.0F, 3, 7, 0, 0.0F, false));
		rightLeg = new ModelRenderer(this);
		rightLeg.setRotationPoint(-1.5F, 17.0F, 0.0F);
		rightLeg.cubeList.add(new ModelBox(rightLeg, 12, 0, -1.5F, 0.0F, 0.0F, 3, 7, 0, 0.0F, false));
		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 14.0F, 0.0F);
		body.cubeList.add(new ModelBox(body, 0, 0, -3.0F, -5.0F, 0.0F, 6, 8, 0, 0.0F, false));
		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, 9.0F, 0.0F);
		head.cubeList.add(new ModelBox(head, 0, 8, -3.0F, -6.0F, 0.0F, 6, 6, 0, 0.0F, false));
		leftArm = new ModelRenderer(this);
		leftArm.setRotationPoint(3.0F, 9.0F, 0.0F);
		leftArm.cubeList.add(new ModelBox(leftArm, 28, 0, 0.0F, 0.0F, 0.0F, 2, 8, 0, 0.0F, false));
		rightArm = new ModelRenderer(this);
		rightArm.setRotationPoint(-3.0F, 9.0F, 0.0F);
		rightArm.cubeList.add(new ModelBox(rightArm, 24, 0, -2.0F, 0.0F, 0.0F, 2, 8, 0, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		leftArm.rotateAngleX = MathHelper.cos((float) (limbSwing + Math.PI)) * limbSwingAmount * 1.5F;
		rightArm.rotateAngleX = MathHelper.cos((float) (limbSwing)) * limbSwingAmount * 1.5F;
		leftLeg.rotateAngleX = MathHelper.cos((float) (limbSwing)) * limbSwingAmount * 1.5F;
		rightLeg.rotateAngleX = MathHelper.cos((float) (limbSwing + Math.PI)) * limbSwingAmount * 1.5F;
		head.rotateAngleY = netHeadYaw * 0.017453292F;
		head.rotateAngleX = headPitch * 0.017453292F;
		leftLeg.render(scale);
		rightLeg.render(scale);
		body.render(scale);
		head.render(scale);
		leftArm.render(scale);
		rightArm.render(scale);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

}