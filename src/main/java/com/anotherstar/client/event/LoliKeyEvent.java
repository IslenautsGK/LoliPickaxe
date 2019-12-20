package com.anotherstar.client.event;

import org.lwjgl.input.Keyboard;

import com.anotherstar.client.key.KeyLoader;
import com.anotherstar.common.LoliPickaxe;
import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.common.gui.LoliGUIHandler;
import com.anotherstar.common.item.tool.ILoli;
import com.anotherstar.network.LoliPickaxeContainerOpenPackte;
import com.anotherstar.network.LoliPickaxeDropAll;
import com.anotherstar.network.NetworkHandler;

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
		if (KeyLoader.LOLI_PICKAXE_CONTAINER.isPressed()) {
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				NetworkHandler.INSTANCE.sendMessageToServer(new LoliPickaxeDropAll());
			} else {
				NetworkHandler.INSTANCE.sendMessageToServer(
						new LoliPickaxeContainerOpenPackte(LoliGUIHandler.GUI_LOLI_PICKAXE_CONTAINER));
			}
		}
		if (KeyLoader.LOLI_PICKAXE_CONTAINER_BLACKLIST.isPressed()) {
			NetworkHandler.INSTANCE.sendMessageToServer(
					new LoliPickaxeContainerOpenPackte(LoliGUIHandler.GUI_LOLI_PICKAXE_CONTAINER_BLACKLIST));
		}
	}

}
