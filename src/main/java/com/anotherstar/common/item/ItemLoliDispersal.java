package com.anotherstar.common.item;

import com.anotherstar.client.creative.CreativeTabLoader;
import com.anotherstar.common.entity.IEntityLoli;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class ItemLoliDispersal extends Item {

	public ItemLoliDispersal() {
		this.setUnlocalizedName("loliDispersal");
		this.setCreativeTab(CreativeTabLoader.loliTabs);
		this.setMaxStackSize(1);
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target,
			EnumHand hand) {
		if (target instanceof IEntityLoli) {
			if (player.world.isRemote) {
				player.swingArm(hand);
			} else {
				((IEntityLoli) target).setDispersal(true);
			}
			return true;
		}
		return false;
	}

}
