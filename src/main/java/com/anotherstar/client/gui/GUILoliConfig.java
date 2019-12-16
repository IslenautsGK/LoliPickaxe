package com.anotherstar.client.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.common.config.annotation.ConfigField;
import com.anotherstar.common.config.annotation.ConfigField.ValurType;
import com.anotherstar.network.LoliItemConfigPacket;
import com.anotherstar.network.NetworkHandler;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public class GUILoliConfig extends GuiScreen {

	private ItemStack stack;
	private GuiButton done;
	private GuiButton pre;
	private GuiButton next;
	private GuiButton booleanValue;
	private GuiTextField otherValue;
	private int curPage;

	public GUILoliConfig(ItemStack stack) {
		this.stack = stack.copy();
		curPage = 0;
	}

	public void initGui() {
		buttonList.clear();
		Keyboard.enableRepeatEvents(true);
		done = addButton(new GuiButton(0, width / 2 - 100, height / 2 + 60, I18n.format("gui.done")));
		pre = addButton(new GuiButton(1, width / 2 - 100, height / 2 - 80, 20, 20, "<"));
		next = addButton(new GuiButton(2, width / 2 + 80, height / 2 - 80, 20, 20, ">"));
		booleanValue = addButton(new GuiButton(3, width / 2 - 40, height / 2 + 20, 80, 20, "false"));
		otherValue = new GuiTextField(4, this.fontRenderer, width / 2 - 80, height / 2 + 20, 160, 20);
		otherValue.setMaxStringLength(100);
		changePage();
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	public void updateScreen() {
		this.otherValue.updateCursorCounter();
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.enabled) {
			switch (button.id) {
			case 0:
				if (stack.hasTagCompound()) {
					NetworkHandler.INSTANCE.sendMessageToServer(new LoliItemConfigPacket(stack.getTagCompound()));
				}
				mc.displayGuiScreen((GuiScreen) null);
				break;
			case 1:
				if (--curPage < 0) {
					curPage = ConfigLoader.loliPickaxeGuiChangeList.size() - 1;
				}
				changePage();
				break;
			case 2:
				if (++curPage >= ConfigLoader.loliPickaxeGuiChangeList.size()) {
					curPage = 0;
				}
				changePage();
				break;
			case 3:
				button.displayString = button.displayString.equals("false") ? "true" : "false";
				if (curPage >= 0 && curPage < ConfigLoader.loliPickaxeGuiChangeList.size()) {
					String flag = ConfigLoader.loliPickaxeGuiChangeList.get(curPage);
					ConfigLoader.setBoolean(stack, flag, button.displayString.equals("true"));
				}
				break;
			default:
				break;
			}
		}
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		otherValue.textboxKeyTyped(typedChar, keyCode);
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		otherValue.mouseClicked(mouseX, mouseY, mouseButton);
		if (!otherValue.isFocused() && curPage >= 0 && curPage < ConfigLoader.loliPickaxeGuiChangeList.size()) {
			String flag = ConfigLoader.loliPickaxeGuiChangeList.get(curPage);
			ConfigField annotations = ConfigLoader.flagAnnotations.get(flag);
			try {
				switch (annotations.valueType()) {
				case INT:
					ConfigLoader.setInt(stack, flag, Integer.parseInt(otherValue.getText()));
					break;
				case DOUBLE:
					ConfigLoader.setDouble(stack, flag, Double.parseDouble(otherValue.getText()));
					break;
				case STRING:
					ConfigLoader.setString(stack, flag, otherValue.getText());
					break;
				default:
					break;
				}
			} catch (NumberFormatException e) {
			}
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (curPage >= 0 && curPage < ConfigLoader.loliPickaxeGuiChangeList.size()) {
			String flag = ConfigLoader.loliPickaxeGuiChangeList.get(curPage);
			String comment = ConfigLoader.flagAnnotations.get(flag).comment();
			drawCenteredString(this.fontRenderer, comment, this.width / 2, 20, 16777215);
		}
		otherValue.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	private void changePage() {
		if (curPage >= 0 && curPage < ConfigLoader.loliPickaxeGuiChangeList.size()) {
			String flag = ConfigLoader.loliPickaxeGuiChangeList.get(curPage);
			ConfigField annotations = ConfigLoader.flagAnnotations.get(flag);
			if (annotations.valueType() == ValurType.BOOLEAN) {
				booleanValue.enabled = true;
				booleanValue.visible = true;
				otherValue.setEnabled(false);
				otherValue.setVisible(false);
			} else {
				booleanValue.enabled = false;
				booleanValue.visible = false;
				otherValue.setEnabled(true);
				otherValue.setVisible(true);
			}
			switch (annotations.valueType()) {
			case INT:
				otherValue.setText(String.valueOf(ConfigLoader.getInt(stack, flag)));
				break;
			case DOUBLE:
				otherValue.setText(String.valueOf(ConfigLoader.getDouble(stack, flag)));
				break;
			case STRING:
				otherValue.setText(String.valueOf(ConfigLoader.getString(stack, flag)));
				break;
			case BOOLEAN:
				booleanValue.displayString = ConfigLoader.getBoolean(stack, flag) ? "true" : "false";
				break;
			default:
				break;
			}
		}
	}

}
