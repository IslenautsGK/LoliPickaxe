package com.anotherstar.client.gui;

import java.io.IOException;
import java.util.List;

import com.anotherstar.client.render.RenderLoliItem;
import com.anotherstar.common.LoliPickaxe;
import com.anotherstar.common.gui.ContainerLoliPickaxe;
import com.anotherstar.network.LoliPickaxeContainerPackte;
import com.anotherstar.network.NetworkHandler;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GUIContainerLoliPickaxe extends GuiContainer {

	private static final ResourceLocation LOLI_PICKAXE_CONTAINER_GUI_TEXTURE = new ResourceLocation(LoliPickaxe.MODID, "textures/gui/container/loli_pickaxe_container.png");

	private ContainerLoliPickaxe loliContainer;
	private GuiButton pre;
	private GuiButton next;

	public GUIContainerLoliPickaxe(ContainerLoliPickaxe inventorySlots) {
		super(inventorySlots);
		this.loliContainer = inventorySlots;
		this.xSize = 240;
		this.ySize = 256;
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonList.clear();
		pre = addButton(new GuiButton(0, (width - xSize) / 2 + 173, (height - ySize) / 2 + 22, 20, 20, "<"));
		next = addButton(new GuiButton(1, (width - xSize) / 2 + 213, (height - ySize) / 2 + 22, 20, 20, ">"));
		itemRender = RenderLoliItem.instance;
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.enabled) {
			switch (button.id) {
			case 0:
				loliContainer.prePage();
				NetworkHandler.INSTANCE.sendMessageToServer(new LoliPickaxeContainerPackte(false));
				break;
			case 1:
				loliContainer.nextPage();
				NetworkHandler.INSTANCE.sendMessageToServer(new LoliPickaxeContainerPackte(true));
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void renderToolTip(ItemStack stack, int x, int y) {
		FontRenderer font = stack.getItem().getFontRenderer(stack);
		net.minecraftforge.fml.client.config.GuiUtils.preItemToolTip(stack);
		List<String> tip = this.getItemToolTip(stack);
		if (stack.getCount() > 1000) {
			tip.add(I18n.format("container.loliPickaxe.stackCount", stack.getCount()));
		}
		this.drawHoveringText(tip, x, y, (font == null ? fontRenderer : font));
		net.minecraftforge.fml.client.config.GuiUtils.postItemToolTip();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		fontRenderer.drawString(loliContainer.inventory.getDisplayName().getUnformattedText(), 173, 8, 4210752);
		String page = String.valueOf(loliContainer.inventory.getField(0) + 1);
		int strSize = fontRenderer.getStringWidth(page);
		fontRenderer.drawString(page, 203 - strSize / 2, 27, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(LOLI_PICKAXE_CONTAINER_GUI_TEXTURE);
		this.drawTexturedModalRect((width - xSize) / 2, (height - ySize) / 2, 0, 0, xSize, ySize);
	}

}
