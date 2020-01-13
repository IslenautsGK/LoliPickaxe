package com.anotherstar.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelLoli extends ModelBase {

	private final ModelRenderer leftLeg;
	private final ModelRenderer rightLeg;
	private final ModelRenderer body;
	private final ModelRenderer head;
	private final ModelRenderer hair;
	private final ModelRenderer leftArm;
	private final ModelRenderer rightArm;

	public ModelLoli() {
		textureWidth = 128;
		textureHeight = 128;
		leftLeg = new ModelRenderer(this);
		leftLeg.setRotationPoint(2.0F, 17.0F, 0.0F);
		leftLeg.cubeList.add(new ModelBox(leftLeg, 0, 0, -1.5F, -1.0F, -1.5F, 3, 8, 3, 0.0F, false));
		rightLeg = new ModelRenderer(this);
		rightLeg.setRotationPoint(-2.0F, 17.0F, 0.0F);
		rightLeg.cubeList.add(new ModelBox(rightLeg, 12, 0, -1.5F, -1.0F, -1.5F, 3, 8, 3, 0.0F, false));
		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 14.0F, 0.0F);
		body.cubeList.add(new ModelBox(body, 0, 41, -5.0F, 2.0F, -5.0F, 10, 2, 10, 0.0F, false));
		body.cubeList.add(new ModelBox(body, 4, 31, -4.0F, 0.0F, -4.0F, 8, 2, 8, 0.0F, false));
		body.cubeList.add(new ModelBox(body, 8, 20, -3.0F, -5.0F, -3.0F, 6, 5, 6, 0.0F, false));
		body.cubeList.add(new ModelBox(body, 12, 13, -2.0F, -8.0F, -2.0F, 4, 3, 4, 0.0F, false));
		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, 5.0F, 0.0F);
		head.cubeList.add(new ModelBox(head, 48, 0, -4.0F, -4.5F, -4.0F, 8, 8, 8, -0.5F, false));
		hair = new ModelRenderer(this);
		hair.setRotationPoint(0.0F, 0.0F, 0.0F);
		head.addChild(hair);
		hair.cubeList.add(new ModelBox(hair, 80, 0, -4.0F, -4.5F, -4.0F, 8, 1, 8, 0.0F, false));
		hair.cubeList.add(new ModelBox(hair, 80, 9, -4.0F, -3.5F, 3.0F, 8, 9, 1, 0.0F, false));
		hair.cubeList.add(new ModelBox(hair, 112, 24, -3.0F, 5.5F, 3.0F, 6, 2, 1, 0.0F, false));
		hair.cubeList.add(new ModelBox(hair, 80, 19, -2.0F, 7.5F, 3.5F, 4, 1, 1, 0.0F, false));
		hair.cubeList.add(new ModelBox(hair, 90, 19, -2.0F, 8.5F, 4.0F, 4, 1, 1, 0.0F, false));
		hair.cubeList.add(new ModelBox(hair, 80, 21, -1.0F, 9.5F, 4.5F, 2, 1, 1, 0.0F, false));
		hair.cubeList.add(new ModelBox(hair, 86, 21, -1.0F, 10.5F, 5.0F, 2, 1, 1, 0.0F, false));
		hair.cubeList.add(new ModelBox(hair, 104, 14, 0.0F, -1.5F, -4.0F, 1, 1, 1, 0.0F, false));
		hair.cubeList.add(new ModelBox(hair, 98, 15, 1.0F, -3.5F, -4.0F, 1, 1, 1, 0.0F, false));
		hair.cubeList.add(new ModelBox(hair, 104, 12, -2.0F, -3.5F, -4.0F, 1, 1, 1, 0.0F, false));
		hair.cubeList.add(new ModelBox(hair, 98, 17, -4.0F, -1.5F, -4.0F, 1, 1, 1, 0.0F, false));
		hair.cubeList.add(new ModelBox(hair, 104, 16, 3.0F, -1.5F, -4.0F, 1, 1, 1, 0.0F, false));
		hair.cubeList.add(new ModelBox(hair, 104, 9, -1.0F, -3.5F, -4.0F, 2, 2, 1, 0.0F, false));
		hair.cubeList.add(new ModelBox(hair, 98, 9, 2.0F, -3.5F, -4.0F, 2, 2, 1, 0.0F, false));
		hair.cubeList.add(new ModelBox(hair, 98, 12, -4.0F, -3.5F, -4.0F, 2, 2, 1, 0.0F, false));
		hair.cubeList.add(new ModelBox(hair, 112, 0, -4.0F, -3.5F, -3.0F, 1, 4, 6, 0.0F, false));
		hair.cubeList.add(new ModelBox(hair, 102, 18, -4.0F, 0.5F, -1.0F, 1, 1, 4, 0.0F, false));
		hair.cubeList.add(new ModelBox(hair, 112, 20, -4.0F, 1.5F, 1.0F, 1, 1, 2, 0.0F, false));
		hair.cubeList.add(new ModelBox(hair, 124, 20, -4.0F, 2.5F, 2.0F, 1, 1, 1, 0.0F, false));
		hair.cubeList.add(new ModelBox(hair, 102, 23, 3.0F, 0.5F, -1.0F, 1, 1, 4, 0.0F, false));
		hair.cubeList.add(new ModelBox(hair, 118, 20, 3.0F, 1.5F, 1.0F, 1, 1, 2, 0.0F, false));
		hair.cubeList.add(new ModelBox(hair, 124, 22, 3.0F, 2.5F, 2.0F, 1, 1, 1, 0.0F, false));
		hair.cubeList.add(new ModelBox(hair, 112, 10, 3.0F, -3.5F, -3.0F, 1, 4, 6, 0.0F, false));
		leftArm = new ModelRenderer(this);
		leftArm.setRotationPoint(3.0F, 10.0F, 0.0F);
		setRotationAngle(leftArm, 0.0F, 0.0F, -0.3491F);
		leftArm.cubeList.add(new ModelBox(leftArm, 26, 0, 0.0F, 2.0F, -1.0F, 2, 5, 2, 0.0F, false));
		leftArm.cubeList.add(new ModelBox(leftArm, 24, 7, -0.5F, -1.0F, -1.5F, 3, 3, 3, 0.0F, false));
		rightArm = new ModelRenderer(this);
		rightArm.setRotationPoint(-3.0F, 10.0F, 0.0F);
		setRotationAngle(rightArm, 0.0F, 0.0F, 0.3491F);
		rightArm.cubeList.add(new ModelBox(rightArm, 38, 0, -2.0F, 2.0F, -1.0F, 2, 5, 2, 0.0F, false));
		rightArm.cubeList.add(new ModelBox(rightArm, 36, 7, -2.5F, -1.0F, -1.5F, 3, 3, 3, 0.0F, false));
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
