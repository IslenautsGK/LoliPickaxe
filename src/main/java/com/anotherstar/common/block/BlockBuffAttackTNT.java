package com.anotherstar.common.block;

import java.util.List;

import com.anotherstar.client.creative.CreativeTabLoader;
import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.common.entity.EntityLoliBuffAttackTNT;
import com.anotherstar.network.LoliDeadPacket;
import com.anotherstar.network.NetworkHandler;

import net.minecraft.block.BlockTNT;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBuffAttackTNT extends BlockTNT {

	private boolean blueScreen;
	private boolean exit;
	private boolean failRespond;

	public BlockBuffAttackTNT(String name, boolean blueScreen, boolean exit, boolean failRespond) {
		this.blueScreen = blueScreen;
		this.exit = exit;
		this.failRespond = failRespond;
		this.setUnlocalizedName(name);
		this.setCreativeTab(CreativeTabLoader.loliTabs);
	}

	public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
		if (!worldIn.isRemote) {
			EntityLoliBuffAttackTNT entitytntprimed = new EntityLoliBuffAttackTNT(worldIn, (double) ((float) pos.getX() + 0.5F), (double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), explosionIn.getExplosivePlacedBy(), this);
			entitytntprimed.setFuse((short) (worldIn.rand.nextInt(entitytntprimed.getFuse() / 4) + entitytntprimed.getFuse() / 8));
			worldIn.spawnEntity(entitytntprimed);
		}
	}

	public void explode(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase igniter) {
		if (!worldIn.isRemote) {
			if (((Boolean) state.getValue(EXPLODE)).booleanValue()) {
				EntityLoliBuffAttackTNT entitytntprimed = new EntityLoliBuffAttackTNT(worldIn, (double) ((float) pos.getX() + 0.5F), (double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), igniter, this);
				worldIn.spawnEntity(entitytntprimed);
				worldIn.playSound((EntityPlayer) null, entitytntprimed.posX, entitytntprimed.posY, entitytntprimed.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
			}
		}
	}

	public void buffAttack(EntityPlayerMP player) {
		NetworkHandler.INSTANCE.sendMessageToPlayer(new LoliDeadPacket(false, blueScreen, exit, failRespond), player);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		if (ConfigLoader.loliEnableBuffAttackTNT) {
			tooltip.add(I18n.format("buffAttackTNT.enable"));
		} else {
			tooltip.add(I18n.format("buffAttackTNT.disable"));
		}
	}

}
