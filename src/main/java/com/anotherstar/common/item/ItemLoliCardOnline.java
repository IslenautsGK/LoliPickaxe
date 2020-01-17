package com.anotherstar.common.item;

import com.anotherstar.client.creative.CreativeTabLoader;
import com.anotherstar.common.LoliPickaxe;
import com.anotherstar.common.gui.LoliGUIHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemLoliCardOnline extends Item {

	public ItemLoliCardOnline() {
		this.setUnlocalizedName("loliCardOnline");
		this.setCreativeTab(CreativeTabLoader.loliTabs);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if (world.isRemote) {
			if (player.isSneaking()) {
				player.openGui(LoliPickaxe.instance, LoliGUIHandler.GUI_LOLI_CARD_ONLINE_CONFIG, world, 0, hand == EnumHand.MAIN_HAND ? 0 : 1, 0);
			} else {
				player.openGui(LoliPickaxe.instance, LoliGUIHandler.GUI_LOLI_CARD_ONLINE, world, 0, hand == EnumHand.MAIN_HAND ? 0 : 1, 0);
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

}
