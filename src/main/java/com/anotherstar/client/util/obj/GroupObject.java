package com.anotherstar.client.util.obj;

import java.util.ArrayList;
import java.util.Map;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GroupObject {

	public String name;
	public ArrayList<Face> faces = new ArrayList<Face>();

	public GroupObject() {
		this("");
	}

	public GroupObject(String name) {
		this.name = name;
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
		Face face;
		if (faces.size() > 0) {
			for (int i = 0, j = faces.size(); i < j; i++) {
				face = faces.get(i);
				if (manager != null && mtl != null && mtl.containsKey(face.usemtl)) {
					mtl.get(face.usemtl).bindTexture(manager);
				}
				face.render(tessellator);
			}
		}
	}

}
