package com.anotherstar.common.item;

import com.anotherstar.common.LoliPickaxe;

import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemLoliPickaxeMaterial extends Item {

	private final int subCount;

	public ItemLoliPickaxeMaterial(String name, int subCount, boolean differentEnd) {
		this.setUnlocalizedName(name);
		this.subCount = subCount;
		this.setCreativeTab(LoliPickaxe.instance.loliRecipeTabs);
		this.setHasSubtypes(subCount != 1);
		if (differentEnd) {
			this.addPropertyOverride(new ResourceLocation(LoliPickaxe.MODID, "end"), (stack, world, entity) -> stack.getItemDamage() == this.subCount - 1 ? 1 : 0);
		}
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (isInCreativeTab(tab)) {
			for (int i = 0; i < subCount; i++) {
				ItemStack stack = new ItemStack(this, 1, i);
				items.add(stack);
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getItemStackDisplayName(ItemStack stack) {
		if (getHasSubtypes()) {
			if (stack.getItemDamage() == getSubCount() - 1) {
				return I18n.format("item.loliMaterialFormat", I18n.format(super.getUnlocalizedName(stack) + ".name"), I18n.format("item.loliMaterial.end"));
			} else {
				return I18n.format("item.loliMaterialFormat", I18n.format(super.getUnlocalizedName(stack) + ".name"), I18n.format("item.loliMaterial." + stack.getItemDamage()));
			}
		} else {
			return super.getItemStackDisplayName(stack);
		}
	}

	public int getSubCount() {
		return subCount;
	}

}
