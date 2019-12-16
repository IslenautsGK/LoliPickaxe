package com.anotherstar.client.event;

import com.anotherstar.client.gui.LoliGUIHandler;
import com.anotherstar.client.key.KeyLoader;
import com.anotherstar.common.LoliPickaxe;
import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.common.item.tool.ILoli;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class LoliKeyEvent {

	@SubscribeEvent
	public void onKeyPressed(KeyInputEvent event) {
		if (KeyLoader.LOLI_CONFIG.isPressed()) {
			EntityPlayer player = Minecraft.getMinecraft().player;
			ItemStack stack = Minecraft.getMinecraft().player.getHeldItemMainhand();
			if (!stack.isEmpty() && stack.getItem() instanceof ILoli
					&& !ConfigLoader.loliPickaxeGuiChangeList.isEmpty()) {
				player.openGui(LoliPickaxe.instance, LoliGUIHandler.GUI_LOLI_CONFIG, player.world, 0, 0, 0);
			}
		}
	}

}
