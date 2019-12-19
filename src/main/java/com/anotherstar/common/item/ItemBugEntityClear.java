package com.anotherstar.common.item;

import java.util.List;

import com.anotherstar.common.LoliPickaxe;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemBugEntityClear extends Item {

	public ItemBugEntityClear() {
		this.setUnlocalizedName("bugEntityClear");
		this.setCreativeTab(LoliPickaxe.instance.loliTabs);
		this.setMaxStackSize(1);
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target,
			EnumHand hand) {
		if (player.world.isRemote) {
			player.swingArm(hand);
			target.isDead = true;
			target.loliDead = true;
			target.loliCool = true;
		}
		return true;
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("§c不要对正常实体使用!");
	}

}
