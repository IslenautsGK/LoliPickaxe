package com.anotherstar.common.block;

import com.anotherstar.client.creative.CreativeTabLoader;
import com.anotherstar.common.LoliPickaxe;
import com.anotherstar.common.gui.LoliGUIHandler;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPasswordWorkBench extends Block {

	public BlockPasswordWorkBench() {
		super(Material.WOOD);
		this.setUnlocalizedName("PasswordWorkBench");
		this.setCreativeTab(CreativeTabLoader.loliTabs);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			playerIn.openGui(LoliPickaxe.instance, LoliGUIHandler.GUI_PASSWORD_WORK_BENCH, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

}
