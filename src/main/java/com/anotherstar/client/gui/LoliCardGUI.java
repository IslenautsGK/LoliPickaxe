package com.anotherstar.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class LoliCardGUI extends GuiScreen {

	private final EntityPlayer player;
	private final ResourceLocation pictureResource;
	private GuiButton exitButton;

	public LoliCardGUI(EntityPlayer player, ResourceLocation pictureResource) {
		this.player = player;
		this.pictureResource = pictureResource;
	}

	@Override
	public void initGui() {
		buttonList.add(exitButton = new GuiButton(0, (this.width - 200) / 2, this.height - 20, 200, 20,
				I18n.format("gui.back", new Object[0])));
		updateButtons();
	}

	private void updateButtons() {
		exitButton.visible = true;
		exitButton.enabled = true;
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.enabled) {
			if (button.id == 0) {
				this.mc.displayGuiScreen(null);
			}
		}
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		this.mc.getTextureManager().bindTexture(pictureResource);
		drawTexturedModalRect((this.width - 256) / 2, (this.height - 256) / 2, 0, 0, 256, 256);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

}
