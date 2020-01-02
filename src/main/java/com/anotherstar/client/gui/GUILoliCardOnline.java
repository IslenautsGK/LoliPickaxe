package com.anotherstar.client.gui;

import com.anotherstar.client.util.LoliCardOnlineUtil;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;

public class GUILoliCardOnline extends GuiScreen {

	private final String url;

	public GUILoliCardOnline(String url) {
		this.url = url;
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
		if (LoliCardOnlineUtil.isLoad(url)) {
			LoliCardOnlineUtil.bind(url);
			int imageWidth = LoliCardOnlineUtil.getWidth(url);
			int imageHeight = LoliCardOnlineUtil.getHeight(url);
			double ratio = (double) imageWidth / (double) imageHeight;
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
		} else {
			LoliCardOnlineUtil.load(url);
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

}
