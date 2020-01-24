package com.anotherstar.client.gui;

import java.io.IOException;
import java.util.Map.Entry;

import com.anotherstar.common.LoliPickaxe;
import com.anotherstar.network.LoliSpaceFoldingPacket;
import com.anotherstar.network.NetworkHandler;

import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

public class GUILoliSpaceFolding extends GuiScreen {

	private static final ResourceLocation LOLI_PICKAXE_SPACE_FOLDING_GUI_TEXTURE = new ResourceLocation(LoliPickaxe.MODID, "textures/gui/loli_pickaxe_space_folding.png");

	private int id;
	private int count;
	private int row;
	private int col;
	private int dx;
	private int dy;

	public GUILoliSpaceFolding(int id) {
		this.id = id;
	}

	@Override
	public void initGui() {
		IntRBTreeSet ids = new IntRBTreeSet();
		for (Entry<DimensionType, IntSortedSet> dim : DimensionManager.getRegisteredDimensions().entrySet()) {
			for (int id : dim.getValue()) {
				ids.add(id);
			}
		}
		ids.remove(id);
		count = ids.size();
		row = (count - 1) / 4 + 1;
		col = count > 4 ? 4 : count;
		dx = 5 - col * 35;
		dy = -10 - row * 15;
		int index = 0;
		for (int id : ids) {
			addButton(new GuiButton(id, width / 2 + dx + (index % col) * 70, height / 2 + dy + index / 4 * 30, 60, 20, DimensionManager.getProviderType(id).getName() + "(" + id + ")"));
			index++;
		}
		addButton(new GuiButton(Integer.MIN_VALUE, width / 2 + dx, height / 2 + dy + row * 30, -10 + col * 70, 20, I18n.format("gui.back")));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.enabled) {
			if (button.id == Integer.MIN_VALUE) {
				mc.displayGuiScreen(null);
			} else {
				NetworkHandler.INSTANCE.sendMessageToServer(new LoliSpaceFoldingPacket(button.id));
			}
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		mc.getTextureManager().bindTexture(LOLI_PICKAXE_SPACE_FOLDING_GUI_TEXTURE);
		drawTexturedModalRect(width / 2 + dx - 10, height / 2 + dy - 10, 0, 0, 10, 10);
		drawTexturedModalRect(width / 2 + dx + 70 * col - 10, height / 2 + dy - 10, 80, 0, 10, 10);
		drawTexturedModalRect(width / 2 + dx - 10, height / 2 + dy + 30 * row + 20, 0, 40, 10, 10);
		drawTexturedModalRect(width / 2 + dx + 70 * col - 10, height / 2 + dy + 30 * row + 20, 80, 40, 10, 10);
		for (int i = 0; i < col - 1; i++) {
			drawTexturedModalRect(width / 2 + dx + 70 * i, height / 2 + dy - 10, 10, 0, 70, 10);
			drawTexturedModalRect(width / 2 + dx + 70 * i, height / 2 + dy + 30 * row + 20, 10, 40, 70, 10);
		}
		drawTexturedModalRect(width / 2 + dx + 70 * col - 70, height / 2 + dy - 10, 10, 0, 60, 10);
		drawTexturedModalRect(width / 2 + dx + 70 * col - 70, height / 2 + dy + 30 * row + 20, 10, 40, 60, 10);
		for (int i = 0; i < row; i++) {
			drawTexturedModalRect(width / 2 + dx - 10, height / 2 + dy + 30 * i, 0, 10, 10, 30);
			drawTexturedModalRect(width / 2 + dx + 70 * col - 10, height / 2 + dy + 30 * i, 80, 10, 10, 30);
		}
		drawTexturedModalRect(width / 2 + dx - 10, height / 2 + dy + 30 * row, 0, 10, 10, 20);
		drawTexturedModalRect(width / 2 + dx + 70 * col - 10, height / 2 + dy + 30 * row, 80, 10, 10, 20);
		for (int i = 0; i < row + 1; i++) {
			for (int j = 0; j < col; j++) {
				drawTexturedModalRect(width / 2 + dx + 70 * j, height / 2 + dy + 30 * i, 10, 10, j == col - 1 ? 60 : 70, i == row ? 20 : 30);
			}
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

}
