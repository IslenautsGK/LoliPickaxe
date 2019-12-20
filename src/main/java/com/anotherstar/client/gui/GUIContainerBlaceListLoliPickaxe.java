package com.anotherstar.client.gui;

import com.anotherstar.common.LoliPickaxe;
import com.anotherstar.common.gui.ContainerBlaceListLoliPickaxe;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GUIContainerBlaceListLoliPickaxe extends GuiContainer {

	private static final ResourceLocation LOLI_PICKAXE_CONTAINER_BLACELIST_GUI_TEXTURE = new ResourceLocation(
			LoliPickaxe.MODID, "textures/gui/container/loli_pickaxe_container_blacklist.png");

	public GUIContainerBlaceListLoliPickaxe(ContainerBlaceListLoliPickaxe inventorySlots) {
		super(inventorySlots);
		this.ySize = 256;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(LOLI_PICKAXE_CONTAINER_BLACELIST_GUI_TEXTURE);
		this.drawTexturedModalRect((width - xSize) / 2, (height - ySize) / 2, 0, 0, xSize, ySize);
	}

}
