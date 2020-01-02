package com.anotherstar.client.gui;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GUILoliCardAlbum extends GuiScreen {

	private final int count;
	private final ResourceLocation[] resources;
	private final int[] imageWidths;
	private final int[] imageHeights;
	private final double[] ratios;

	private int page;

	public GUILoliCardAlbum(List<ResourceLocation> resourceList, List<Integer> imageWidthList, List<Integer> imageHeightList) {
		this.count = resourceList.size();
		this.resources = new ResourceLocation[this.count];
		this.imageWidths = new int[this.count];
		this.imageHeights = new int[this.count];
		this.ratios = new double[this.count];
		for (int i = 0; i < this.count; i++) {
			this.resources[i] = resourceList.get(i);
			this.imageWidths[i] = imageWidthList.get(i);
			this.imageHeights[i] = imageHeightList.get(i);
			this.ratios[i] = (double) this.imageWidths[i] / (double) this.imageHeights[i];
		}
		page = 0;
	}

	@Override
	public void initGui() {
		addButton(new GuiButton(0, (this.width - 200) / 2, this.height - 20, 200, 20, I18n.format("gui.back")));
		addButton(new GuiButton(1, (this.width - 220) / 2, this.height - 20, 20, 20, "<"));
		addButton(new GuiButton(2, (this.width + 200) / 2, this.height - 20, 20, 20, ">"));
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.enabled) {
			switch (button.id) {
			case 0:
				mc.displayGuiScreen(null);
				break;
			case 1:
				if (--page < 0) {
					page = count - 1;
				}
				break;
			case 2:
				if (++page >= count) {
					page = 0;
				}
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		mc.getTextureManager().bindTexture(resources[page]);
		int cx = width / 2;
		int cy = height / 2;
		double proportion;
		if (ratios[page] < (double) width / (double) height) {
			proportion = (double) height / (double) imageHeights[page];
		} else {
			proportion = (double) width / (double) imageWidths[page];
		}
		int x = (int) (imageWidths[page] * proportion / 2);
		int y = (int) (imageHeights[page] * proportion / 2);
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
