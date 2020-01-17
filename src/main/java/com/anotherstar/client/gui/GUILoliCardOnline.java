package com.anotherstar.client.gui;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.anotherstar.client.util.LoliCardOnlineUtil;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;

public class GUILoliCardOnline extends GuiScreen {

	private final String url;

	private int dcx;
	private int dcy;
	private double ds;
	private boolean clicked;
	private boolean moved;
	private int clickX;
	private int clickY;
	private int odcx;
	private int odcy;
	private URI clickedLinkURI;

	public GUILoliCardOnline(String url) {
		this.url = url;
		this.dcx = 0;
		this.dcy = 0;
		this.ds = 1;
		this.clicked = false;
		this.clickX = 0;
		this.clickY = 0;
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

	public void handleMouseInput() throws IOException {
		int d = Mouse.getEventDWheel();
		if (d == 0) {
			super.handleMouseInput();
		} else {
			int x = Mouse.getEventX() * width / mc.displayWidth;
			int y = height - Mouse.getEventY() * height / mc.displayHeight - 1;
			if (ds > 0.1 || d > 0) {
				ds *= d > 0 ? 1.28 : 0.78125;
				dcx += d > 0 ? (width / 2 + dcx - x) * 0.28 : (x - width / 2 - dcx) * 0.21875;
				dcy += d > 0 ? (height / 2 + dcy - y) * 0.28 : (y - height / 2 - dcy) * 0.21875;
			}
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if (mouseButton == 0) {
			clicked = true;
			clickX = mouseX;
			clickY = mouseY;
			odcx = dcx;
			odcy = dcy;
		}
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		if (clicked) {
			clicked = false;
			if (!moved) {
				try {
					URI uri = new URI(url);
					if (mc.gameSettings.chatLinksPrompt) {
						clickedLinkURI = uri;
						mc.displayGuiScreen(new GuiConfirmOpenLink(this, url, 75395975, false));
					} else {
						openWebLink(uri);
					}
				} catch (URISyntaxException e) {
				}
			}
			moved = false;
		}
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
		if (clicked) {
			dcx = odcx + mouseX - clickX;
			dcy = odcy + mouseY - clickY;
			moved = true;
		}
	}

	public void confirmClicked(boolean result, int id) {
		if (id == 75395975) {
			if (result) {
				openWebLink(clickedLinkURI);
			}
			clickedLinkURI = null;
			mc.displayGuiScreen(this);
		}
	}

	private void openWebLink(URI url) {
		try {
			Class<?> oclass = Class.forName("java.awt.Desktop");
			Object object = oclass.getMethod("getDesktop").invoke((Object) null);
			oclass.getMethod("browse", URI.class).invoke(object, url);
		} catch (Throwable e) {
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (LoliCardOnlineUtil.isLoad(url)) {
			LoliCardOnlineUtil.bind(url);
			int imageWidth = LoliCardOnlineUtil.getWidth(url);
			int imageHeight = LoliCardOnlineUtil.getHeight(url);
			double ratio = (double) imageWidth / (double) imageHeight;
			int cx = width / 2 + dcx;
			int cy = height / 2 + dcy;
			double proportion;
			if (ratio < (double) width / (double) height) {
				proportion = (double) height / (double) imageHeight;
			} else {
				proportion = (double) width / (double) imageWidth;
			}
			int x = (int) (imageWidth * proportion / 2 * ds);
			int y = (int) (imageHeight * proportion / 2 * ds);
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuffer();
			bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
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
