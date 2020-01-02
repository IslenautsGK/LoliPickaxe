package com.anotherstar.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GUILoliCard extends GuiScreen {

	private final ResourceLocation resource;
	private final int imageWidth;
	private final int imageHeight;
	private final double ratio;

	public GUILoliCard(ResourceLocation resource, int imageWidth, int imageHeight) {
		this.resource = resource;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.ratio = (double) imageWidth / (double) imageHeight;
	}

	@Override
	public void initGui() {
		addButton(new GuiButton(0, (this.width - 200) / 2, this.height - 20, 200, 20, I18n.format("gui.back")));
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.enabled) {
			if (button.id == 0) {
				mc.displayGuiScreen(null);
			}
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		mc.getTextureManager().bindTexture(resource);
		int cx = width / 2;
		int cy = height / 2;
		double proportion;
		if (ratio < (double) width / (double) height) {
			proportion = (double) height / (double) imageHeight;
		} else {
			proportion = (double) width / (double) imageWidth;
		}
		int x = (int) (imageWidth * proportion / 2);
		int y = (int) (imageHeight * proportion / 2);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(cx - x, cy + y, zLevel).tex(0, 1).endVertex();
		bufferbuilder.pos(cx + x, cy + y, zLevel).tex(1, 1).endVertex();
		bufferbuilder.pos(cx + x, cy - y, zLevel).tex(1, 0).endVertex();
		bufferbuilder.pos(cx - x, cy - y, zLevel).tex(0, 0).endVertex();
		tessellator.draw();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

}
