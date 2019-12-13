package com.anotherstar.client.gui;

import com.anotherstar.client.ClientProxy;
import com.anotherstar.common.LoliPickaxe;
import com.anotherstar.util.LoliCardUtil;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class GuiElementLoader implements IGuiHandler {

	public static final int GUI_LOLICARD = 1;

	public GuiElementLoader() {
	}

	public void init() {
		NetworkRegistry.INSTANCE.registerGuiHandler(LoliPickaxe.instance, this);
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case GUI_LOLICARD:
			ItemStack itemStack = player.getCurrentEquippedItem();
			NBTTagCompound nbt = itemStack.stackTagCompound;
			String pictureName = nbt == null ? null : nbt.getString("picturename");
			if (LoliCardUtil.customArtNames == null) {
				return new LoliCardGUI(player, ClientProxy.DEFLOLI);
			}
			for (int i = 0; i < LoliCardUtil.customArtNames.length; i++) {
				if (LoliCardUtil.customArtNames[i].equals(pictureName)) {
					return new LoliCardGUI(player, LoliCardUtil.customArtResources[i]);
				}
			}
			return new LoliCardGUI(player, ClientProxy.DEFLOLI);
		default:
			return null;
		}
	}

}
