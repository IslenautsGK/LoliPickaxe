package com.anotherstar.client.gui;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.anotherstar.common.config.ConfigLoader;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GUILoliCardAlbum extends GuiScreen {

	private final String groupName;
	private final int count;
	private final ResourceLocation[] resources;
	private final int[] imageWidths;
	private final int[] imageHeights;
	private final double[] ratios;

	private int dcx;
	private int dcy;
	private double ds;
	private boolean clicked;
	private boolean moved;
	private int clickX;
	private int clickY;
	private int odcx;
	private int odcy;
	private int page;
	private URI clickedLinkURI;

	public GUILoliCardAlbum(String groupName, List<ResourceLocation> resourceList, List<Integer> imageWidthList, List<Integer> imageHeightList) {
		this.groupName = groupName;
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
		this.dcx = 0;
		this.dcy = 0;
		this.ds = 1;
		this.clicked = false;
		this.clickX = 0;
		this.clickY = 0;
		this.page = 0;
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
				if (ConfigLoader.loliCardURL.containsKey(groupName)) {
					try {
						String url = ConfigLoader.loliCardURL.get(groupName);
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
		mc.getTextureManager().bindTexture(resources[page]);
		int cx = width / 2 + dcx;
		int cy = height / 2 + dcy;
		double proportion;
		if (ratios[page] < (double) width / (double) height) {
			proportion = (double) height / (double) imageHeights[page];
		} else {
			proportion = (double) width / (double) imageWidths[page];
		}
		int x = (int) (imageWidths[page] * proportion / 2 * ds);
		int y = (int) (imageHeights[page] * proportion / 2 * ds);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(cx - x, cy + y, zLevel).tex(0, 1).endVertex();
		bufferbuilder.pos(cx + x, cy + y, zLevel).tex(1, 1).endVertex();
		bufferbuilder.pos(cx + x, cy - y, zLevel).tex(1, 0).endVertex();
		bufferbuilder.pos(cx - x, cy - y, zLevel).tex(0, 0).endVertex();
		tessellator.draw();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

}
