package com.anotherstar.client.key;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.client.settings.KeyBinding;

public class KeyLoader {

	public static KeyBinding showTime;

	public static void init() {
		KeyLoader.showTime = new KeyBinding("key.fmltutor.showTime", Keyboard.KEY_GRAVE, "key.categories.fmltutor");
		ClientRegistry.registerKeyBinding(KeyLoader.showTime);
	}

}
