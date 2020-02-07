package com.anotherstar.common.gui;

import java.util.List;

import com.anotherstar.client.gui.GUIContainerBlaceListLoliPickaxe;
import com.anotherstar.client.gui.GUIContainerLoliPickaxe;
import com.anotherstar.client.gui.GUILoliCard;
import com.anotherstar.client.gui.GUILoliCardAlbum;
import com.anotherstar.client.gui.GUILoliCardOnline;
import com.anotherstar.client.gui.GUILoliCardOnlineConfig;
import com.anotherstar.client.gui.GUILoliConfig;
import com.anotherstar.client.gui.GUILoliEnchantment;
import com.anotherstar.client.gui.GUILoliPotion;
import com.anotherstar.client.gui.GUILoliSpaceFolding;
import com.anotherstar.client.gui.GUIPasswordCrafting;
import com.anotherstar.client.util.LoliCardUtil;
import com.anotherstar.common.LoliPickaxe;
import com.anotherstar.common.item.ItemLoliCardOnline;
import com.anotherstar.common.item.tool.ILoli;
import com.google.common.collect.Lists;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum LoliGUIHandler implements IGuiHandler {

	INSTANCE;

	public static final int GUI_LOLI_CONFIG = 1;
	public static final int GUI_LOLI_CARD = 2;
	public static final int GUI_LOLI_PICKAXE_CONTAINER = 3;
	public static final int GUI_LOLI_PICKAXE_CONTAINER_BLACKLIST = 4;
	public static final int GUI_LOLI_CARD_ALBUM = 5;
	public static final int GUI_LOLI_CARD_ONLINE = 6;
	public static final int GUI_LOLI_CARD_ONLINE_CONFIG = 7;
	public static final int GUI_LOLI_ENCHANTMENT = 8;
	public static final int GUI_LOLI_POTION = 9;
	public static final int GUI_LOLI_SPACEF_OLDING = 10;
	public static final int GUI_PASSWORD_WORK_BENCH = 11;

	private LoliGUIHandler() {
		NetworkRegistry.INSTANCE.registerGuiHandler(LoliPickaxe.instance, this);
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case GUI_LOLI_PICKAXE_CONTAINER:
			return new ContainerLoliPickaxe(player, player.getHeldItem(y == 0 ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND), x);
		case GUI_LOLI_PICKAXE_CONTAINER_BLACKLIST:
			return new ContainerBlaceListLoliPickaxe(player, player.getHeldItem(y == 0 ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND), x);
		case GUI_PASSWORD_WORK_BENCH:
			return new ContainerPasswordWorkbench(player.inventory, world, new BlockPos(x, y, z));
		}
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
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
						return new GUILoliCard(name, LoliCardUtil.customArtResources[i], LoliCardUtil.customArtWidths[i], LoliCardUtil.customArtHeights[i]);
					}
				}
			}
			break;
		}
		case GUI_LOLI_PICKAXE_CONTAINER:
			return new GUIContainerLoliPickaxe(new ContainerLoliPickaxe(player, player.getHeldItem(y == 0 ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND), x));
		case GUI_LOLI_PICKAXE_CONTAINER_BLACKLIST:
			return new GUIContainerBlaceListLoliPickaxe(new ContainerBlaceListLoliPickaxe(player, player.getHeldItem(y == 0 ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND), x));
		case GUI_LOLI_CARD_ALBUM: {
			ItemStack stack = player.getHeldItem(y == 0 ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND);
			String groupName = stack.hasTagCompound() ? stack.getTagCompound().getString("PictureGroup") : "";
			if (!groupName.isEmpty() && LoliCardUtil.customArtNames != null) {
				String name = groupName + "'";
				List<ResourceLocation> resources = Lists.newArrayList();
				List<Integer> widths = Lists.newArrayList();
				List<Integer> heights = Lists.newArrayList();
				for (int i = 0; i < LoliCardUtil.customArtNames.length; i++) {
					if (LoliCardUtil.customArtNames[i].startsWith(name)) {
						resources.add(LoliCardUtil.customArtResources[i]);
						widths.add(LoliCardUtil.customArtWidths[i]);
						heights.add(LoliCardUtil.customArtHeights[i]);
					}
				}
				if (!resources.isEmpty()) {
					return new GUILoliCardAlbum(groupName, resources, widths, heights);
				}
			}
			break;
		}
		case GUI_LOLI_CARD_ONLINE: {
			ItemStack stack = player.getHeldItem(y == 0 ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND);
			String url = stack.hasTagCompound() ? stack.getTagCompound().getString("ImageUrl") : "";
			if (!url.isEmpty()) {
				return new GUILoliCardOnline(url);
			}
			break;
		}
		case GUI_LOLI_CARD_ONLINE_CONFIG: {
			ItemStack stack = player.getHeldItemMainhand();
			if (!stack.isEmpty() && stack.getItem() instanceof ItemLoliCardOnline) {
				String url = stack.hasTagCompound() ? stack.getTagCompound().getString("ImageUrl") : "";
				return new GUILoliCardOnlineConfig(url);
			}
			break;
		}
		case GUI_LOLI_ENCHANTMENT: {
			ItemStack stack = player.getHeldItemMainhand();
			if (!stack.isEmpty() && stack.getItem() instanceof ILoli) {
				return new GUILoliEnchantment(stack);
			}
			break;
		}
		case GUI_LOLI_POTION: {
			ItemStack stack = player.getHeldItemMainhand();
			if (!stack.isEmpty() && stack.getItem() instanceof ILoli) {
				return new GUILoliPotion(stack);
			}
			break;
		}
		case GUI_LOLI_SPACEF_OLDING: {
			ItemStack stack = player.getHeldItemMainhand();
			if (!stack.isEmpty() && stack.getItem() instanceof ILoli) {
				return new GUILoliSpaceFolding(player);
			}
			break;
		}
		case GUI_PASSWORD_WORK_BENCH:
			return new GUIPasswordCrafting(new ContainerPasswordWorkbench(player.inventory, world, new BlockPos(x, y, z)));
		}
		return null;
	}

}
