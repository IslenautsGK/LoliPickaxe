package com.anotherstar.common.item;

import com.anotherstar.common.LoliPickaxe;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class ItemLoliPickaxeCore extends Item {

	public ItemLoliPickaxeCore() {
		this.setUnlocalizedName("loliPickaxeCore");
		this.setCreativeTab(LoliPickaxe.instance.loliRecipeTabs);
		this.setHasSubtypes(true);
		this.addPropertyOverride(new ResourceLocation(LoliPickaxe.MODID, "end"),
				(stack, world, entity) -> stack.getItemDamage() == 9 ? 1 : 0);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)) {
			for (int i = 0; i < 10; i++) {
				ItemStack stack = new ItemStack(this, 1, i);
				items.add(stack);
			}
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName(stack) + "_" + stack.getItemDamage();
	}

}
