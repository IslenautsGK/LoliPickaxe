package com.anotherstar.client.util.obj;

import java.util.ArrayList;
import java.util.Map;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GroupObject {

	public String name;
	public ResourceLocation texture;
	public ArrayList<Face> faces = new ArrayList<Face>();

	public GroupObject() {
		this("");
	}

	public GroupObject(String name) {
		this(name, null);
	}

	public GroupObject(String name, ResourceLocation texture) {
		this.name = name;
		this.texture = texture;
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
	public void render(RenderManager manager, Tessellator tessellator, Map<String, ResourceLocation> mtl) {
		Face face;
		if (faces.size() > 0) {
			for (int i = 0, j = faces.size(); i < j; i++) {
				face = faces.get(i);
				if (manager != null && mtl != null && mtl.containsKey(face.usemtl)) {
					manager.renderEngine.bindTexture(mtl.get(face.usemtl));
				}
				face.render(tessellator);
			}
		}
	}

}
