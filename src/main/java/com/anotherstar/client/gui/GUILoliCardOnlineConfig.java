package com.anotherstar.client.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import com.anotherstar.network.LoliCardOnlinePacket;
import com.anotherstar.network.NetworkHandler;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

public class GUILoliCardOnlineConfig extends GuiScreen {

	private String url;
	private GuiTextField urlField;
	private GuiButton done;

	public GUILoliCardOnlineConfig(String url) {
		this.url = url;
	}

	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		urlField = new GuiTextField(1, this.fontRenderer, width / 2 - 80, height / 2 + 20, 160, 20);
		urlField.setMaxStringLength(500);
		if (this.url != null) {
			urlField.setText(url);
		}
		done = addButton(new GuiButton(0, width / 2 - 100, height / 2 + 60, I18n.format("gui.done")));
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
		super.drawScreen(mouseX, mouseY, partialTicks);
		drawCenteredString(this.fontRenderer, I18n.format("gui.loliCardOnline"), this.width / 2, 20, 16777215);
		urlField.drawTextBox();
	}

}
