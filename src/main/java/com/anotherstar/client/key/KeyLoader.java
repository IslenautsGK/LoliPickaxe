package com.anotherstar.client.key;

import org.lwjgl.input.Keyboard;

import com.anotherstar.common.LoliPickaxe;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyLoader {

	public static final KeyBinding LOLI_CONFIG = new KeyBinding("key." + LoliPickaxe.MODID + ".loli_config",
			KeyConflictContext.IN_GAME, KeyModifier.NONE, Keyboard.KEY_N, "key.category." + LoliPickaxe.MODID);

	public static void init() {
		ClientRegistry.registerKeyBinding(LOLI_CONFIG);
	}

}
