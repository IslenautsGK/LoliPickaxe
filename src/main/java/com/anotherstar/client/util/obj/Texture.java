package com.anotherstar.client.util.obj;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class Texture {

	public String name;
	public ResourceLocation texture;
	public FloatBuffer ka;
	public FloatBuffer kd;
	public FloatBuffer ks;
	public FloatBuffer ns;
	private boolean inited = false;

	public Texture(String name) {
		this(name, null, null, null, null);
	}

	public Texture(String name, ResourceLocation texture) {
		this(name, texture, null, null, null);
	}

	public Texture(String name, ResourceLocation texture, float[] ka, float[] kd, float[] ks) {
		this.name = name;
		this.texture = texture;
		this.ka = ka == null ? null : FloatBuffer.wrap(ka);
		this.kd = kd == null ? null : FloatBuffer.wrap(kd);
		this.ks = ks == null ? null : FloatBuffer.wrap(ks);
		this.ns = null;
	}

	public Texture(String name, ResourceLocation texture, float[] ka, float[] kd, float[] ks, float ns) {
		this(name, texture, ka, kd, ks);
		this.ns = FloatBuffer.wrap(new float[] { ns });
	}

	public void bindTexture(RenderManager manager) {
		manager.renderEngine.bindTexture(texture);
		if (!inited) {
			init();
		}
	}

	public void init() {
		if (ka != null) {
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT, ka);
		}
		if (kd != null) {
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, kd);
		}
		if (ks != null) {
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, ks);
		}
		if (ns != null) {
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SHININESS, ns);
		}
		inited = true;
	}

}
