package com.anotherstar.common.item;

import java.util.List;

import com.anotherstar.client.creative.CreativeTabLoader;
import com.anotherstar.common.LoliPickaxe;
import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.common.gui.LoliGUIHandler;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		super.getSubItems(tab, items);
		if (isInCreativeTab(tab)) {
			for (String url : ConfigLoader.loliCardOnlineDefURL) {
				ItemStack stack = new ItemStack(this);
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setString("ImageUrl", url);
				stack.setTagCompound(nbt);
				items.add(stack);
			}
		}
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (stack.hasTagCompound()) {
			NBTTagCompound nbt = stack.getTagCompound();
			if (nbt.hasKey("ImageUrl")) {
				tooltip.add(nbt.getString("ImageUrl"));
			}
		}
	}

}
