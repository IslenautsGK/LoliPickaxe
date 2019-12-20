package com.anotherstar.common.gui;

import com.anotherstar.client.gui.GUIContainerBlaceListLoliPickaxe;
import com.anotherstar.client.gui.GUIContainerLoliPickaxe;
import com.anotherstar.client.gui.GUILoliCard;
import com.anotherstar.client.gui.GUILoliConfig;
import com.anotherstar.client.util.LoliCardUtil;
import com.anotherstar.common.LoliPickaxe;
import com.anotherstar.common.item.tool.ILoli;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public enum LoliGUIHandler implements IGuiHandler {

	INSTANCE;

	public static final int GUI_LOLI_CONFIG = 1;
	public static final int GUI_LOLI_CARD = 2;
	public static final int GUI_LOLI_PICKAXE_CONTAINER = 3;
	public static final int GUI_LOLI_PICKAXE_CONTAINER_BLACKLIST = 4;

	private LoliGUIHandler() {
		NetworkRegistry.INSTANCE.registerGuiHandler(LoliPickaxe.instance, this);
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case GUI_LOLI_PICKAXE_CONTAINER:
			return new ContainerLoliPickaxe(player, player.getHeldItem(y == 0 ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND),
					x);
		case GUI_LOLI_PICKAXE_CONTAINER_BLACKLIST:
			return new ContainerBlaceListLoliPickaxe(player,
					player.getHeldItem(y == 0 ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND), x);
		default:
			break;
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case GUI_LOLI_CONFIG: {
			ItemStack stack = player.getHeldItemMainhand();
			if (!stack.isEmpty() && stack.getItem() instanceof ILoli) {
				return new GUILoliConfig(stack);
			}
			break;
		}
		case GUI_LOLI_CARD: {
			ItemStack stack = player.getHeldItem(y == 0 ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND);
			String name = stack.hasTagCompound() ? stack.getTagCompound().getString("picture") : "";
			if (!name.isEmpty() && LoliCardUtil.customArtNames != null) {
				for (int i = 0; i < LoliCardUtil.customArtNames.length; i++) {
					if (LoliCardUtil.customArtNames[i].equals(name)) {
						return new GUILoliCard(LoliCardUtil.customArtResources[i]);
					}
				}
			}
			break;
		}
		case GUI_LOLI_PICKAXE_CONTAINER:
			return new GUIContainerLoliPickaxe(new ContainerLoliPickaxe(player,
					player.getHeldItem(y == 0 ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND), x));
		case GUI_LOLI_PICKAXE_CONTAINER_BLACKLIST:
			return new GUIContainerBlaceListLoliPickaxe(new ContainerBlaceListLoliPickaxe(player,
					player.getHeldItem(y == 0 ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND), x));
		default:
			break;
		}
		return null;
	}

}
