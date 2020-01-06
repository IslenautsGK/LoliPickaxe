package com.anotherstar.client.gui;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.input.Keyboard;

import com.anotherstar.client.gui.assembly.GUILoliList;
import com.anotherstar.network.LoliEnchantmentPacket;
import com.anotherstar.network.NetworkHandler;
import com.anotherstar.util.LoliRomeDigitalUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class GUILoliEnchantment extends GuiScreen {

	private ItemStack stack;
	private GuiButton done;
	private GuiButton add;
	private GuiButton remove;
	private GuiTextField level;
	private GUILoliList enchantmentList;
	private GUILoliList selectEnchantmentList;
	private ResourceLocation[] enchantments;
	private List<LoliEntry> selectEnchantments;

	public GUILoliEnchantment(ItemStack stack) {
		this.stack = stack.copy();
	}

	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		done = addButton(new GuiButton(0, width / 2 - 160, height / 2 + 95, 320, 20, I18n.format("gui.done")));
		add = addButton(new GuiButton(1, width / 2 + 60, height / 2 - 95, 100, 20, I18n.format("gui.loliAdd")));
		remove = addButton(new GuiButton(2, width / 2 + 60, height / 2 - 65, 100, 20, I18n.format("gui.loliRemove")));
		level = new GuiTextField(4, fontRenderer, width / 2 + 60, height / 2 - 35, 100, 20);
		level.setText("1");
		enchantments = Enchantment.REGISTRY.getKeys().toArray(new ResourceLocation[0]);
		enchantmentList = new GUILoliList(width / 2 - 160, height / 2 - 115, 100, 200, enchantments.length, 75, 15) {

			@Override
			public void moveElement(int from, int to) {
			}

			@Override
			public String getElementName(int index) {
				return I18n.format(Enchantment.REGISTRY.getObject(enchantments[index]).getName());
			}

			@Override
			public int getElementColor(int index) {
				return 16777215;
			}

		};
		enchantmentList.selected = 0;
		selectEnchantments = Lists.newArrayList();
		Map<Enchantment, Integer> enchMap = EnchantmentHelper.getEnchantments(stack);
		for (Entry<Enchantment, Integer> entry : enchMap.entrySet()) {
			selectEnchantments.add(new LoliEntry(entry.getKey().getRegistryName(), entry.getValue()));
		}
		selectEnchantmentList = new GUILoliList(width / 2 - 50, height / 2 - 115, 100, 200, selectEnchantments.size(), 75, 15) {

			@Override
			public void moveElement(int from, int to) {
				selectEnchantments.add(to, selectEnchantments.remove(from));
			}

			@Override
			public String getElementName(int index) {
				Enchantment ench = Enchantment.REGISTRY.getObject(selectEnchantments.get(index).enchantment);
				int level = selectEnchantments.get(index).level;
				String name = I18n.format(ench.getName());
				if (ench.isCurse()) {
					name = TextFormatting.RED + name;
				}
				if (level != 1 || ench.getMaxLevel() != 1) {
					name += " " + LoliRomeDigitalUtil.intToRoman(level);
				}
				return name;
			}

			@Override
			public int getElementColor(int index) {
				return 16777215;
			}

		};
		selectEnchantmentList.selected = 0;
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	public void updateScreen() {
		level.updateCursorCounter();
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.enabled) {
			switch (button.id) {
			case 0: {
				Map<Enchantment, Integer> enchMap = Maps.newLinkedHashMap();
				if (!selectEnchantments.isEmpty()) {
					for (LoliEntry entry : selectEnchantments) {
						enchMap.put(Enchantment.REGISTRY.getObject(entry.enchantment), entry.level);
					}
				}
				EnchantmentHelper.setEnchantments(enchMap, stack);
				NBTTagCompound send = new NBTTagCompound();
				if (stack.hasTagCompound() && stack.getTagCompound().hasKey("ench")) {
					NBTTagList list = stack.getTagCompound().getTagList("ench", 10);
					send.setTag("ench", list);
				}
				NetworkHandler.INSTANCE.sendMessageToServer(new LoliEnchantmentPacket(send));
				mc.displayGuiScreen((GuiScreen) null);
				break;
			}
			case 1: {
				int numberLevel;
				try {
					numberLevel = Integer.parseInt(level.getText());
				} catch (Exception e) {
					numberLevel = 1;
				}
				selectEnchantments.add(new LoliEntry(enchantments[enchantmentList.selected], numberLevel));
				selectEnchantmentList.add();
				remove.enabled = true;
				break;
			}
			case 2:
				selectEnchantments.remove(selectEnchantmentList.selected);
				if (selectEnchantmentList.selected >= selectEnchantments.size()) {
					selectEnchantmentList.selected = selectEnchantments.size() - 1;
				}
				if (selectEnchantments.size() == 0) {
					remove.enabled = false;
				}
				selectEnchantmentList.remove();
				break;
			}
		}
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		level.textboxKeyTyped(typedChar, keyCode);
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		level.mouseClicked(mouseX, mouseY, mouseButton);
		enchantmentList.mouseClick(mouseX, mouseY);
		selectEnchantmentList.mouseClick(mouseX, mouseY);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		drawString(this.fontRenderer, I18n.format("gui.loliEnch"), this.width / 2 + 60, height / 2 - 110, 16777215);
		level.drawTextBox();
		enchantmentList.draw(mouseX, mouseY);
		enchantmentList.update(mouseX, mouseY);
		selectEnchantmentList.draw(mouseX, mouseY);
		selectEnchantmentList.update(mouseX, mouseY);
	}

	private class LoliEntry {

		public ResourceLocation enchantment;
		public int level;

		public LoliEntry(ResourceLocation enchantment, int level) {
			this.enchantment = enchantment;
			this.level = level;
		}

	}

}
