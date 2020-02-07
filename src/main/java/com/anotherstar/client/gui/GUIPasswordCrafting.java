package com.anotherstar.client.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import com.anotherstar.common.LoliPickaxe;
import com.anotherstar.common.gui.ContainerPasswordWorkbench;
import com.anotherstar.network.NetworkHandler;
import com.anotherstar.network.PasswordUpdataPacket;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GUIPasswordCrafting extends GuiContainer {

	private static final ResourceLocation PASSWORD_CRAFTING_TABLE_GUI_TEXTURES = new ResourceLocation(LoliPickaxe.MODID, "textures/gui/container/password_crafting_table.png");

	private GuiTextField password;
	private GuiButton done;

	public GUIPasswordCrafting(ContainerPasswordWorkbench inventorySlotsIn) {
		super(inventorySlotsIn);
		ySize = 196;
	}

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		super.initGui();
		password = new GuiTextField(0, fontRenderer, guiLeft + 29, guiTop + 18, 75, 16);
		password.setTextColor(16777215);
		done = addButton(new GuiButton(0, guiLeft + 114, guiTop + 16, 30, 20, I18n.format("gui.done")));
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		password.updateCursorCounter();
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.enabled && button.id == 0) {
			NetworkHandler.INSTANCE.sendMessageToServer(new PasswordUpdataPacket(password.getText()));
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (!password.textboxKeyTyped(typedChar, keyCode)) {
			super.keyTyped(typedChar, keyCode);
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		password.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		RenderHelper.disableStandardItemLighting();
		password.drawTextBox();
		RenderHelper.enableGUIStandardItemLighting();
		renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		fontRenderer.drawString(I18n.format("gui.password"), 28, 6, 4210752);
		fontRenderer.drawString(I18n.format("container.crafting"), 28, 36, 4210752);
		fontRenderer.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(PASSWORD_CRAFTING_TABLE_GUI_TEXTURES);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

}
