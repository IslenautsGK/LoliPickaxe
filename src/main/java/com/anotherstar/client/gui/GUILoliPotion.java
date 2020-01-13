package com.anotherstar.client.gui;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.input.Keyboard;

import com.anotherstar.client.gui.assembly.GUILoliList;
import com.anotherstar.network.LoliPotionPacket;
import com.anotherstar.network.NetworkHandler;
import com.anotherstar.util.LoliRomeDigitalUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class GUILoliPotion extends GuiScreen {

	private ItemStack stack;
	private GuiButton done;
	private GuiButton add;
	private GuiButton remove;
	private GuiTextField level;
	private GUILoliList potionList;
	private GUILoliList selectPotionList;
	private ResourceLocation[] potions;
	private List<LoliEntry> selectPotions;

	public GUILoliPotion(ItemStack stack) {
		this.stack = stack.copy();
	}

	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		done = addButton(new GuiButton(0, width / 2 - 160, height / 2 + 95, 320, 20, I18n.format("gui.done")));
		add = addButton(new GuiButton(1, width / 2 + 60, height / 2 - 95, 100, 20, I18n.format("gui.loliAdd")));
		remove = addButton(new GuiButton(2, width / 2 + 60, height / 2 - 65, 100, 20, I18n.format("gui.loliRemove")));
		level = new GuiTextField(4, fontRenderer, width / 2 + 60, height / 2 - 35, 100, 20);
		level.setText("0");
		potions = Potion.REGISTRY.getKeys().toArray(new ResourceLocation[0]);
		potionList = new GUILoliList(width / 2 - 160, height / 2 - 115, 100, 200, potions.length, 75, 15) {

			@Override
			public void moveElement(int from, int to) {
			}

			@Override
			public String getElementName(int index) {
				return I18n.format(Potion.REGISTRY.getObject(potions[index]).getName());
			}

			@Override
			public int getElementColor(int index) {
				return 16777215;
			}

		};
		potionList.selected = 0;
		selectPotions = Lists.newArrayList();
		Map<Potion, Integer> potionMap = getPotions(stack);
		for (Entry<Potion, Integer> entry : potionMap.entrySet()) {
			selectPotions.add(new LoliEntry(entry.getKey().getRegistryName(), entry.getValue()));
		}
		selectPotionList = new GUILoliList(width / 2 - 50, height / 2 - 115, 100, 200, selectPotions.size(), 75, 15) {

			@Override
			public void moveElement(int from, int to) {
				selectPotions.add(to, selectPotions.remove(from));
			}

			@Override
			public String getElementName(int index) {
				Potion potion = Potion.REGISTRY.getObject(selectPotions.get(index).potion);
				int level = selectPotions.get(index).level;
				String name = I18n.format(potion.getName());
				name += " " + LoliRomeDigitalUtil.intToRoman(level + 1);
				return name;
			}

			@Override
			public int getElementColor(int index) {
				return 16777215;
			}

		};
		selectPotionList.selected = 0;
	}

	private Map<Potion, Integer> getPotions(ItemStack stack) {
		Map<Potion, Integer> map = Maps.newLinkedHashMap();
		NBTTagList list = stack.hasTagCompound() ? stack.getTagCompound().getTagList("LoliPotion", 10) : new NBTTagList();
		for (int i = 0; i < list.tagCount(); ++i) {
			NBTTagCompound element = list.getCompoundTagAt(i);
			Potion potion = Potion.getPotionById(element.getShort("id"));
			int level = element.getByte("lvl");
			map.put(potion, level);
		}
		return map;
	}

	private void setPotions(Map<Potion, Integer> potionMap, ItemStack stack) {
		NBTTagList list = new NBTTagList();
		for (Entry<Potion, Integer> entry : potionMap.entrySet()) {
			Potion potion = entry.getKey();
			if (potion != null) {
				int i = entry.getValue();
				NBTTagCompound element = new NBTTagCompound();
				element.setShort("id", (short) Potion.getIdFromPotion(potion));
				element.setByte("lvl", (byte) i);
				list.appendTag(element);
			}
		}
		if (list.hasNoTags()) {
			if (stack.hasTagCompound()) {
				stack.getTagCompound().removeTag("LoliPotion");
			}
		} else {
			stack.setTagInfo("LoliPotion", list);
		}
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
				Map<Potion, Integer> potionMap = Maps.newLinkedHashMap();
				if (!selectPotions.isEmpty()) {
					for (LoliEntry entry : selectPotions) {
						potionMap.put(Potion.REGISTRY.getObject(entry.potion), entry.level);
					}
				}
				setPotions(potionMap, stack);
				NBTTagCompound send = new NBTTagCompound();
				if (stack.hasTagCompound() && stack.getTagCompound().hasKey("LoliPotion")) {
					NBTTagList list = stack.getTagCompound().getTagList("LoliPotion", 10);
					send.setTag("LoliPotion", list);
				}
				NetworkHandler.INSTANCE.sendMessageToServer(new LoliPotionPacket(send));
				mc.displayGuiScreen((GuiScreen) null);
				break;
			}
			case 1: {
				int numberLevel;
				try {
					numberLevel = Integer.parseInt(level.getText());
				} catch (Exception e) {
					numberLevel = 0;
				}
				selectPotions.add(new LoliEntry(potions[potionList.selected], numberLevel));
				selectPotionList.add();
				remove.enabled = true;
				break;
			}
			case 2:
				selectPotions.remove(selectPotionList.selected);
				if (selectPotionList.selected >= selectPotions.size()) {
					selectPotionList.selected = selectPotions.size() - 1;
				}
				if (selectPotions.size() == 0) {
					remove.enabled = false;
				}
				selectPotionList.remove();
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
		potionList.mouseClick(mouseX, mouseY);
		selectPotionList.mouseClick(mouseX, mouseY);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		drawString(this.fontRenderer, I18n.format("gui.loliPotion"), this.width / 2 + 60, height / 2 - 110, 16777215);
		level.drawTextBox();
		potionList.draw(mouseX, mouseY);
		potionList.update(mouseX, mouseY);
		selectPotionList.draw(mouseX, mouseY);
		selectPotionList.update(mouseX, mouseY);
	}

	private class LoliEntry {

		public ResourceLocation potion;
		public int level;

		public LoliEntry(ResourceLocation potion, int level) {
			this.potion = potion;
			this.level = level;
		}

	}

}
