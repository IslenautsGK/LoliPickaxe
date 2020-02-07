package com.anotherstar.client.util.obj;

import java.util.ArrayList;
import java.util.Map;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GroupObject {

	public String name;
	public ArrayList<Face> faces = new ArrayList<Face>();
	public float rotationPointX;
	public float rotationPointY;
	public float rotationPointZ;
	public float rotateAngleX;
	public float rotateAngleY;
	public float rotateAngleZ;

	public GroupObject() {
		this("");
	}

	public GroupObject(String name) {
		this.name = name;
	}

	public void setRotationPoint(float rotationPointXIn, float rotationPointYIn, float rotationPointZIn) {
		rotationPointX = rotationPointXIn;
		rotationPointY = rotationPointYIn;
		rotationPointZ = rotationPointZIn;
	}

	@SideOnly(Side.CLIENT)
	public void render() {
		if (faces.size() > 0) {
			Tessellator tessellator = Tessellator.getInstance();
			render(tessellator);
		}
	}

	@SideOnly(Side.CLIENT)
	public void render(Tessellator tessellator) {
		render(null, tessellator, null);
	}

	@SideOnly(Side.CLIENT)
	public void render(RenderManager manager, Tessellator tessellator, Map<String, Texture> mtl) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(rotationPointX, rotationPointY, rotationPointZ);
		if (rotateAngleZ != 0.0F) {
			GlStateManager.rotate(rotateAngleZ * (180F / (float) Math.PI), 0.0F, 0.0F, 1.0F);
		}
		if (rotateAngleY != 0.0F) {
			GlStateManager.rotate(rotateAngleY * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
		}
		if (rotateAngleX != 0.0F) {
			GlStateManager.rotate(rotateAngleX * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);
		}
		GlStateManager.translate(-rotationPointX, -rotationPointY, -rotationPointZ);
		for (int i = 0; i < faces.size(); i++) {
			Face face = faces.get(i);
			if (manager != null && mtl != null && mtl.containsKey(face.usemtl)) {
				mtl.get(face.usemtl).bindTexture(manager);
			}
			face.render(tessellator);
		}
		GlStateManager.popMatrix();
	}

}
