package com.anotherstar.client.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import com.anotherstar.common.LoliPickaxe;
import com.anotherstar.network.LoliCardOnlinePacket;
import com.anotherstar.network.NetworkHandler;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GUILoliCardOnlineConfig extends GuiScreen {

	private static final ResourceLocation LOLI_CARD_ONLINE_CONFIG_GUI_TEXTURE = new ResourceLocation(LoliPickaxe.MODID, "textures/gui/loli_card_online_config.png");

	private String url;
	private GuiTextField urlField;
	private GuiButton done;

	public GUILoliCardOnlineConfig(String url) {
		this.url = url;
	}

	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		urlField = new GuiTextField(1, this.fontRenderer, width / 2 - 80, height / 2 - 20, 160, 20);
		urlField.setMaxStringLength(500);
		if (this.url != null) {
			urlField.setText(url);
		}
		urlField.setFocused(true);
		urlField.setCursorPositionEnd();
		done = addButton(new GuiButton(0, width / 2 - 100, height / 2 + 10, I18n.format("gui.done")));
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	public void updateScreen() {
		urlField.updateCursorCounter();
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.enabled && button.id == 0) {
			NetworkHandler.INSTANCE.sendMessageToServer(new LoliCardOnlinePacket(urlField.getText()));
			mc.displayGuiScreen((GuiScreen) null);
		}
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		urlField.textboxKeyTyped(typedChar, keyCode);
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		urlField.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		mc.getTextureManager().bindTexture(LOLI_CARD_ONLINE_CONFIG_GUI_TEXTURE);
		drawTexturedModalRect((width - 220) / 2, (height - 100) / 2, 0, 0, 220, 90);
		super.drawScreen(mouseX, mouseY, partialTicks);
		drawCenteredString(this.fontRenderer, I18n.format("gui.loliCardOnline"), this.width / 2, height / 2 - 40, 16777215);
		urlField.drawTextBox();
	}

}
