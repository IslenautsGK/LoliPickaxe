package com.anotherstar.client.gui;

import java.io.IOException;
import java.util.Map.Entry;

import org.lwjgl.input.Keyboard;

import com.anotherstar.common.LoliPickaxe;
import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.network.LoliSpaceFoldingPacket;
import com.anotherstar.network.NetworkHandler;

import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

public class GUILoliSpaceFolding extends GuiScreen {

	private static final ResourceLocation LOLI_PICKAXE_SPACE_FOLDING_GUI_TEXTURE = new ResourceLocation(LoliPickaxe.MODID, "textures/gui/loli_pickaxe_space_folding.png");

	private EntityPlayer player;
	private GuiButton selectedWorld;
	private GuiButton selectedLocation;
	private GuiTextField xField;
	private GuiTextField yField;
	private GuiTextField zField;
	private int count;
	private int row;
	private int col;
	private int dx;
	private int dy;

	public GUILoliSpaceFolding(EntityPlayer player) {
		this.player = player;
	}

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		IntRBTreeSet ids = new IntRBTreeSet();
		for (Entry<DimensionType, IntSortedSet> dim : DimensionManager.getRegisteredDimensions().entrySet()) {
			for (int id : dim.getValue()) {
				if (!ConfigLoader.loliPickaxeWorldBlacklist.contains(id)) {
					ids.add(id);
				}
			}
		}
		count = ids.size();
		row = (count - 1) / 4 + 1;
		col = count > 4 ? 4 : count;
		dx = 5 - col * 35;
		dy = -25 - row * 15;
		int index = 0;
		for (int id : ids) {
			GuiButton button = addButton(new GuiButton(id, width / 2 + dx + (index % col) * 70, height / 2 + dy + index / 4 * 30, 60, 20, DimensionManager.getProviderType(id).getName() + "(" + id + ")"));
			index++;
			if (id == player.dimension) {
				button.enabled = false;
				selectedWorld = button;
			}
		}
		row += 2;
		col = 4;
		dx = -135;
		xField = new GuiTextField(0, fontRenderer, width / 2 + dx, height / 2 + dy + row * 30 - 60, 60, 20);
		xField.setMaxStringLength(100);
		xField.setText(String.valueOf(player.posX));
		xField.setFocused(true);
		yField = new GuiTextField(1, fontRenderer, width / 2 + dx + 70, height / 2 + dy + row * 30 - 60, 60, 20);
		yField.setMaxStringLength(100);
		yField.setText(String.valueOf(player.posY));
		yField.setFocused(false);
		zField = new GuiTextField(2, fontRenderer, width / 2 + dx + 140, height / 2 + dy + row * 30 - 60, 60, 20);
		zField.setMaxStringLength(100);
		zField.setText(String.valueOf(player.posZ));
		zField.setFocused(false);
		selectedLocation = addButton(new GuiButton(Integer.MIN_VALUE + 1, width / 2 + dx, height / 2 + dy + row * 30 - 30, 60, 20, I18n.format("gui.loliAbsolute")));
		selectedLocation.enabled = false;
		addButton(new GuiButton(Integer.MAX_VALUE - 1, width / 2 + dx + 70, height / 2 + dy + row * 30 - 30, 60, 20, I18n.format("gui.loliRelative")));
		addButton(new GuiButton(Integer.MAX_VALUE, width / 2 + dx + 210, height / 2 + dy + row * 30 - 60, 60, 20, I18n.format("gui.done")));
		addButton(new GuiButton(Integer.MIN_VALUE, width / 2 + dx + 140, height / 2 + dy + row * 30 - 30, 130, 20, I18n.format("gui.back")));
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.enabled) {
			if (button.id == Integer.MIN_VALUE) {
				mc.displayGuiScreen(null);
			} else if (button.id == Integer.MAX_VALUE) {
				try {
					double x = Double.parseDouble(xField.getText());
					double y = Double.parseDouble(yField.getText());
					double z = Double.parseDouble(zField.getText());
					if (selectedLocation.id == Integer.MIN_VALUE + 1) {
						x -= player.posX;
						y -= player.posY;
						z -= player.posZ;
					}
					NetworkHandler.INSTANCE.sendMessageToServer(new LoliSpaceFoldingPacket(selectedWorld.id, x, y, z));
				} catch (NumberFormatException e) {
				}
				mc.displayGuiScreen(null);
			} else if (button.id == Integer.MAX_VALUE - 1 || button.id == Integer.MIN_VALUE + 1) {
				button.enabled = false;
				selectedLocation.enabled = true;
				selectedLocation = button;
				try {
					if (button.id == Integer.MAX_VALUE - 1) {
						xField.setText(String.valueOf(Double.parseDouble(xField.getText()) - player.posX));
						yField.setText(String.valueOf(Double.parseDouble(yField.getText()) - player.posY));
						zField.setText(String.valueOf(Double.parseDouble(zField.getText()) - player.posZ));
					} else {
						xField.setText(String.valueOf(Double.parseDouble(xField.getText()) + player.posX));
						yField.setText(String.valueOf(Double.parseDouble(yField.getText()) + player.posY));
						zField.setText(String.valueOf(Double.parseDouble(zField.getText()) + player.posZ));
					}
				} catch (NumberFormatException e) {
					xField.setText("0.0");
					yField.setText("0.0");
					zField.setText("0.0");
				}
			} else {
				button.enabled = false;
				selectedWorld.enabled = true;
				selectedWorld = button;
			}
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		xField.textboxKeyTyped(typedChar, keyCode);
		yField.textboxKeyTyped(typedChar, keyCode);
		zField.textboxKeyTyped(typedChar, keyCode);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		xField.mouseClicked(mouseX, mouseY, mouseButton);
		yField.mouseClicked(mouseX, mouseY, mouseButton);
		zField.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		mc.getTextureManager().bindTexture(LOLI_PICKAXE_SPACE_FOLDING_GUI_TEXTURE);
		drawTexturedModalRect(width / 2 + dx - 10, height / 2 + dy - 10, 0, 0, 10, 10);
		drawTexturedModalRect(width / 2 + dx + 70 * col - 10, height / 2 + dy - 10, 80, 0, 10, 10);
		drawTexturedModalRect(width / 2 + dx - 10, height / 2 + dy + 30 * row - 10, 0, 40, 10, 10);
		drawTexturedModalRect(width / 2 + dx + 70 * col - 10, height / 2 + dy + 30 * row - 10, 80, 40, 10, 10);
		for (int i = 0; i < col - 1; i++) {
			drawTexturedModalRect(width / 2 + dx + 70 * i, height / 2 + dy - 10, 10, 0, 70, 10);
			drawTexturedModalRect(width / 2 + dx + 70 * i, height / 2 + dy + 30 * row - 10, 10, 40, 70, 10);
		}
		drawTexturedModalRect(width / 2 + dx + 70 * col - 70, height / 2 + dy - 10, 10, 0, 60, 10);
		drawTexturedModalRect(width / 2 + dx + 70 * col - 70, height / 2 + dy + 30 * row - 10, 10, 40, 60, 10);
		for (int i = 0; i < row - 1; i++) {
			drawTexturedModalRect(width / 2 + dx - 10, height / 2 + dy + 30 * i, 0, 10, 10, 30);
			drawTexturedModalRect(width / 2 + dx + 70 * col - 10, height / 2 + dy + 30 * i, 80, 10, 10, 30);
		}
		drawTexturedModalRect(width / 2 + dx - 10, height / 2 + dy + 30 * row - 30, 0, 10, 10, 20);
		drawTexturedModalRect(width / 2 + dx + 70 * col - 10, height / 2 + dy + 30 * row - 30, 80, 10, 10, 20);
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				drawTexturedModalRect(width / 2 + dx + 70 * j, height / 2 + dy + 30 * i, 10, 10, j == col - 1 ? 60 : 70, i == row - 1 ? 20 : 30);
			}
		}
		xField.drawTextBox();
		yField.drawTextBox();
		zField.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

}
