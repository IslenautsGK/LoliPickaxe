package com.anotherstar.client.gui.assembly;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public abstract class GUILoliList extends Gui {

	protected int xWidth;
	protected int yHeight;
	public int xPosition;
	public int yPosition;
	public double scroll = 0.0D;
	public int numElements = 0;
	public int elementWidth;
	public int elementHeight;
	public int elementsPerLine;
	public int selected = -1;
	public boolean scrolling;
	public int dragged = -1;
	public int dragYOffset = 0;
	public int dragXOffset = 0;
	public int dragDelay = 0;

	public GUILoliList(int xPosition, int yPosition, int width, int height, int numElements, int elementWidth, int elementHeight) {
		this.xWidth = width;
		this.yHeight = height;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.numElements = numElements;
		this.elementWidth = elementWidth;
		this.elementHeight = elementHeight;
		this.elementsPerLine = ((this.xWidth - 20) / elementWidth);
		if (this.elementsPerLine < 1) {
			this.elementsPerLine = 1;
		}
	}

	public void draw(int x, int y) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/gui/widgets.png"));
		drawGradientRect(xPosition, yPosition, xPosition + xWidth, yPosition + yHeight, -1072689136, -804253680);
		drawTexturedModalRect(xPosition + xWidth - 20, yPosition + (int) (scroll * (yHeight - 20)), 0, 66, 10, 20);
		drawTexturedModalRect(xPosition + xWidth - 10, yPosition + (int) (scroll * (yHeight - 20)), 190, 66, 10, 20);
		if ((dragged > -1) && (dragDelay == 0) && (dragged < numElements) && (x >= xPosition) && (y >= yPosition) && (x < xPosition + xWidth - 25) && (y < yPosition + yHeight)) {
			int scrollOffset = (int) (scroll * elementHeight * (numElements / elementsPerLine));
			int moused = (int) (y - yPosition + scrollOffset) / elementHeight * elementsPerLine + (x - xPosition) / elementWidth;
			if (moused < numElements) {
				int yOffset = y - yPosition - (y - yPosition + scrollOffset) % elementHeight;
				int xOffset = x - xPosition - (x - xPosition) % elementWidth;
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
				bufferbuilder.pos(xPosition + xOffset, yPosition + yOffset + elementHeight + 2, 0.0).color(255, 0, 0, 255).endVertex();
				bufferbuilder.pos(xPosition + xOffset + elementWidth + 5, yPosition + yOffset + elementHeight + 2, 0.0).color(255, 0, 0, 255).endVertex();
				bufferbuilder.pos(xPosition + xOffset + elementWidth + 5, yPosition + yOffset - 2, 0.0).color(255, 0, 0, 255).endVertex();
				bufferbuilder.pos(xPosition + xOffset, yPosition + yOffset - 2, 0.0).color(255, 0, 0, 255).endVertex();
				bufferbuilder.pos(xPosition + xOffset + 1, yPosition + yOffset + elementHeight + 1, 0.0).color(0, 0, 0, 255).endVertex();
				bufferbuilder.pos(xPosition + xOffset + elementWidth + 4, yPosition + yOffset + elementHeight + 1, 0.0).color(0, 0, 0, 255).endVertex();
				bufferbuilder.pos(xPosition + xOffset + elementWidth + 4, yPosition + yOffset - 1, 0.0).color(0, 0, 0, 255).endVertex();
				bufferbuilder.pos(xPosition + xOffset + 1, yPosition + yOffset - 1, 0.0).color(0, 0, 0, 255).endVertex();
				tessellator.draw();
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}
		}
		for (int i = 0; i < numElements; i++) {
			if ((i / elementsPerLine * elementHeight <= scroll * elementHeight * (numElements / elementsPerLine) + yHeight - elementHeight) && (i / elementsPerLine * elementHeight >= scroll * elementHeight * (numElements / elementsPerLine))) {
				int yOffset = (int) (-scroll * elementHeight * (numElements / elementsPerLine)) + i / elementsPerLine * elementHeight;
				int xOffset = i % elementsPerLine * elementWidth;
				if (i != selected) {
					drawElement(i, xOffset, yOffset);
				}
			}
		}
		if ((!Mouse.isButtonDown(0)) && (x > xPosition) && (x < xPosition + xWidth) && (y > yPosition) && (y < yPosition + yHeight)) {
			while (Mouse.next()) {
				int var16 = Mouse.getEventDWheel();
				if (var16 != 0) {
					if (var16 > 0) {
						var16 = -1;
					} else if (var16 < 0) {
						var16 = 1;
					}
					scroll += yHeight / (numElements / elementsPerLine * elementHeight + yHeight) * var16 * 0.1D;
					if (scroll > 1.0D) {
						scroll = 1.0D;
					}
					if (scroll < 0.0D) {
						scroll = 0.0D;
					}
				}
			}
		}
		if ((selected != -1) && (selected / elementsPerLine * elementHeight <= scroll * elementHeight * (numElements / elementsPerLine) + yHeight - elementHeight) && (selected / elementsPerLine * elementHeight >= scroll * elementHeight * (numElements / elementsPerLine)) && (numElements != 0)) {
			int yOffset = (int) (-scroll * elementHeight * (numElements / elementsPerLine)) + selected / elementsPerLine * elementHeight;
			int xOffset = selected % elementsPerLine * elementWidth;
			int var14 = xWidth / 2 - 110;
			int var15 = xWidth / 2 + 110;
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
			bufferbuilder.pos(xPosition + xOffset, yPosition + yOffset + elementHeight + 2, 0.0).color(127, 127, 127, 255).endVertex();
			bufferbuilder.pos(xPosition + xOffset + elementWidth + 5, yPosition + yOffset + elementHeight + 2, 0.0).color(127, 127, 127, 255).endVertex();
			bufferbuilder.pos(xPosition + xOffset + elementWidth + 5, yPosition + yOffset - 2, 0.0).color(127, 127, 127, 255).endVertex();
			bufferbuilder.pos(xPosition + xOffset, yPosition + yOffset - 2, 0.0).color(127, 127, 127, 255).endVertex();
			bufferbuilder.pos(xPosition + xOffset + 1, yPosition + yOffset + elementHeight + 1, 0.0).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(xPosition + xOffset + elementWidth + 4, yPosition + yOffset + elementHeight + 1, 0.0).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(xPosition + xOffset + elementWidth + 4, yPosition + yOffset - 1, 0.0).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos(xPosition + xOffset + 1, yPosition + yOffset - 1, 0.0).color(0, 0, 0, 255).endVertex();
			tessellator.draw();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			drawElement(selected, xOffset, yOffset);
		}
		if ((dragged > -1) && (dragDelay == 0)) {
			if (dragged < numElements) {
				int yOffset = (int) (-scroll * elementHeight * (numElements / elementsPerLine)) + dragged / elementsPerLine * elementHeight;
				int xOffset = dragged % elementsPerLine * elementWidth;
				drawElement(dragged, x - xPosition + dragXOffset, y - yPosition + dragYOffset);
			} else {
				dragged = -1;
			}
		}
	}

	public void update(int x, int y) {
		if (dragDelay > 0) {
			dragDelay += 1;
		}
		if (dragDelay > 10) {
			dragDelay = 0;
		}
		if (!Mouse.isButtonDown(0)) {
			if ((dragged > -1) && (dragDelay == 0) && (dragged < numElements) && (x >= xPosition) && (y >= yPosition) && (x < xPosition + xWidth - 25) && (y < yPosition + yHeight)) {
				int yOffset = (int) (scroll * elementHeight * (numElements / elementsPerLine));
				int moused = (int) (y - yPosition + yOffset) / elementHeight * elementsPerLine + (x - xPosition) / elementWidth;
				if (moused < numElements) {
					moveElement(dragged, moused);
				}
			}
			dragged = -1;
		}
		if ((dragged != -1) && (dragDelay == 0)) {
			if (y < yPosition) {
				scroll -= yHeight / (numElements / elementsPerLine * elementHeight + yHeight) * 0.05D;
			}
			if (y > yPosition + yHeight) {
				scroll += yHeight / (numElements / elementsPerLine * elementHeight + yHeight) * 0.05D;
			}
		}
		if ((scrolling) && (Mouse.isButtonDown(0))) {
			scroll = ((y - (yPosition + 10.0)) / (yHeight - 20.0));
		} else {
			scrolling = false;
		}
		if (scroll < 0.0D) {
			scroll = 0.0D;
		}
		if (scroll > 1.0D) {
			scroll = 1.0D;
		}
	}

	public void mouseClick(int x, int y) {
		if ((x >= xPosition) && (y >= yPosition) && (x < xPosition + xWidth - 20) && (y < yPosition + yHeight)) {
			int yOffset = (int) (scroll * elementHeight * (numElements / elementsPerLine));
			int tempSelected = (int) (y - yPosition + yOffset) / elementHeight * elementsPerLine + (x - xPosition) / elementWidth;
			if (tempSelected >= numElements) {
				tempSelected = numElements - 1;
			}
			dragged = tempSelected;
			dragDelay = 1;
			dragYOffset = (-(y - yPosition + yOffset) % elementHeight);
			dragXOffset = (-(x - xPosition) % elementWidth);
			if (tempSelected != selected) {
				selected = tempSelected;
				selectElement();
			}
			selected = tempSelected;
		}
		if ((x >= xPosition + xWidth - 20) && (y >= yPosition) && (x < xPosition + xWidth) && (y < yPosition + yHeight) && (Mouse.isButtonDown(0))) {
			scrolling = true;
		} else {
			scrolling = false;
		}
	}

	public void selectElement() {
	}

	public void drawElement(int index, int xOffset, int yOffset) {
		drawString(Minecraft.getMinecraft().fontRenderer, getElementName(index), xPosition + 2, yPosition + yOffset + 2, getElementColor(index));
	}

	public abstract String getElementName(int index);

	public abstract int getElementColor(int index);

	public abstract void moveElement(int from, int to);

	public void add() {
		numElements += 1;
		if (selected == -1) {
			selected = 0;
		}
	}

	public void remove() {
		numElements -= 1;
		if (selected >= numElements) {
			selected = (numElements - 1);
		}
	}

}
