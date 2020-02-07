package com.anotherstar.common.item;

import java.util.List;

import com.anotherstar.client.creative.CreativeTabLoader;
import com.anotherstar.common.LoliPickaxe;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemLoliPickaxeMaterial extends Item {

	private final int subCount;

	public ItemLoliPickaxeMaterial(String name, int subCount, boolean differentEnd) {
		this.setUnlocalizedName(name);
		this.subCount = subCount;
		this.setCreativeTab(CreativeTabLoader.loliRecipeTabs);
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

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		if (stack.getItemDamage() < ((ItemLoliPickaxeMaterial) stack.getItem()).getSubCount() - 1) {
			tooltip.add(I18n.format("item.loliMaterial.recipe", I18n.format("item.loliMaterial." + stack.getItemDamage()), stack.getItemDamage() == getSubCount() - 2 ? I18n.format("item.loliMaterial.end") : I18n.format("item.loliMaterial." + (stack.getItemDamage() + 1)), I18n.format("item.loliMaterial." + (getSubCount() - 1))));
		}
	}

	public int getSubCount() {
		return subCount;
	}

}
