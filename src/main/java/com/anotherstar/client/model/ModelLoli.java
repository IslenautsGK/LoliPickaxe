package com.anotherstar.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelLoli extends ModelBase {

	public ModelRenderer shape1;
	public ModelRenderer leg1;
	public ModelRenderer leg2;
	public ModelRenderer arm1;
	public ModelRenderer arm2;
	public ModelRenderer shape6;
	public ModelRenderer shape7;
	public ModelRenderer shape8;
	public ModelRenderer shape9;
	public ModelRenderer shape10;
	public ModelRenderer shape12;
	public ModelRenderer shape13;

	public ModelLoli() {
		this.textureWidth = 64;
		this.textureHeight = 32;
		(this.arm2 = new ModelRenderer((ModelBase) this, 10, 19)).setRotationPoint(-2.0f, 0.5f, 0.0f);
		this.arm2.addBox(-2.0f, -0.5f, -1.0f, 2, 6, 2, 0.0f);
		(this.shape7 = new ModelRenderer((ModelBase) this, 32, 0)).setRotationPoint(0.0f, 5.8f, -1.1f);
		this.shape7.addBox(0.0f, 0.0f, 0.0f, 2, 4, 1, 0.0f);
		this.setRotateAngle(this.shape7, -0.045553092f, 0.0f, 0.0f);
		(this.leg2 = new ModelRenderer((ModelBase) this, 0, 19)).setRotationPoint(-1.0f, 6.0f, 0.0f);
		this.leg2.addBox(-1.0f, 0.0f, -1.0f, 2, 6, 2, 0.0f);
		(this.shape9 = new ModelRenderer((ModelBase) this, 48, 0)).setRotationPoint(1.1f, 5.8f, -1.0f);
		this.shape9.addBox(0.0f, 0.0f, 0.0f, 1, 4, 2, 0.0f);
		this.setRotateAngle(this.shape9, 0.0f, 0.0f, -0.045553092f);
		(this.shape13 = new ModelRenderer((ModelBase) this, 40, 8)).setRotationPoint(0.0f, 5.8f, 1.1f);
		this.shape13.addBox(-2.0f, 0.0f, -1.0f, 2, 4, 1, 0.0f);
		this.setRotateAngle(this.shape13, 0.045553092f, 0.0f, 0.0f);
		(this.shape6 = new ModelRenderer((ModelBase) this, 14, 0)).setRotationPoint(0.0f, 0.0f, 0.0f);
		this.shape6.addBox(-2.0f, -4.0f, -2.0f, 4, 4, 4, 0.0f);
		(this.arm1 = new ModelRenderer((ModelBase) this, 10, 9)).setRotationPoint(2.0f, 0.5f, 0.0f);
		this.arm1.addBox(0.0f, -0.5f, -1.0f, 2, 6, 2, 0.0f);
		(this.shape1 = new ModelRenderer((ModelBase) this, 0, 0)).setRotationPoint(0.0f, 12.0f, 0.0f);
		this.shape1.addBox(-2.0f, 0.0f, -1.0f, 4, 6, 2, 0.0f);
		this.setRotateAngle(this.shape1, 0.0f, -3.1415927f, 0.0f);
		(this.leg1 = new ModelRenderer((ModelBase) this, 0, 9)).setRotationPoint(1.0f, 6.0f, 0.0f);
		this.leg1.addBox(-1.0f, 0.0f, -1.0f, 2, 6, 2, 0.0f);
		(this.shape12 = new ModelRenderer((ModelBase) this, 31, 8)).setRotationPoint(0.0f, 5.8f, 1.1f);
		this.shape12.addBox(0.0f, 0.0f, -1.0f, 2, 4, 1, 0.0f);
		this.setRotateAngle(this.shape12, 0.045553092f, 0.0f, 0.0f);
		(this.shape8 = new ModelRenderer((ModelBase) this, 40, 0)).setRotationPoint(0.0f, 5.8f, -1.1f);
		this.shape8.addBox(-2.0f, 0.0f, 0.0f, 2, 4, 1, 0.0f);
		this.setRotateAngle(this.shape8, -0.045553092f, 0.0f, 0.0f);
		(this.shape10 = new ModelRenderer((ModelBase) this, 21, 9)).setRotationPoint(-2.1f, 5.8f, -1.0f);
		this.shape10.addBox(0.0f, 0.0f, 0.0f, 1, 4, 2, 0.0f);
		this.setRotateAngle(this.shape10, 0.0f, 0.0f, 0.045553092f);
		this.shape1.addChild(this.arm2);
		this.shape1.addChild(this.shape7);
		this.shape1.addChild(this.leg2);
		this.shape1.addChild(this.shape9);
		this.shape1.addChild(this.shape13);
		this.shape1.addChild(this.shape6);
		this.shape1.addChild(this.arm1);
		this.shape1.addChild(this.leg1);
		this.shape1.addChild(this.shape12);
		this.shape1.addChild(this.shape8);
		this.shape1.addChild(this.shape10);
	}

	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scale) {
		this.arm1.rotateAngleX = MathHelper.cos((float) (limbSwing + Math.PI)) * limbSwingAmount * 1.5F;
		this.arm2.rotateAngleX = MathHelper.cos((float) (limbSwing)) * limbSwingAmount * 1.5F;
		this.leg1.rotateAngleX = MathHelper.cos((float) (limbSwing)) * limbSwingAmount * 1.5F;
		this.leg2.rotateAngleX = MathHelper.cos((float) (limbSwing + Math.PI)) * limbSwingAmount * 1.5F;
		this.shape6.rotateAngleY = netHeadYaw * 0.017453292F;
		this.shape6.rotateAngleX = -headPitch * 0.017453292F;
		this.shape1.render(scale);
	}

	public void setRotateAngle(final ModelRenderer modelRenderer, final float x, final float y, final float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

}
