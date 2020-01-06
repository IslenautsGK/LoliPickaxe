package com.anotherstar.common.entity;

import java.util.List;

import com.anotherstar.common.block.BlockBuffAttackTNT;
import com.anotherstar.common.block.BlockLoader;
import com.anotherstar.common.config.ConfigLoader;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityLoliBuffAttackTNT extends EntityTNTPrimed {

	private static final DataParameter<Integer> BLOCK_DATA = EntityDataManager.createKey(EntityLoliBuffAttackTNT.class, DataSerializers.VARINT);

	public EntityLoliBuffAttackTNT(World worldIn) {
		super(worldIn);
	}

	public EntityLoliBuffAttackTNT(World worldIn, double x, double y, double z, EntityLivingBase igniter, BlockBuffAttackTNT block) {
		super(worldIn, x, y, z, igniter);
		setBlock(block);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(BLOCK_DATA, Integer.valueOf(0));
	}

	private BlockBuffAttackTNT getBlock() {
		Block block = Block.getBlockById(dataManager.get(BLOCK_DATA));
		if (block instanceof BlockBuffAttackTNT) {
			return (BlockBuffAttackTNT) block;
		} else {
			return BlockLoader.loliBlueScreenTNT;
		}
	}

	private void setBlock(BlockBuffAttackTNT block) {
		dataManager.set(BLOCK_DATA, Block.getIdFromBlock(block));
	}

	public void onUpdate() {
		boolean dead = isDead;
		super.onUpdate();
		if (!dead && isDead && !world.isRemote && ConfigLoader.loliEnableBuffAttackTNT) {
			List<EntityPlayerMP> playerList = world.getEntitiesWithinAABB(EntityPlayerMP.class, new AxisAlignedBB(posX - 5, posY - 5, posZ - 5, posX + 5, posY + 5, posZ + 5));
			for (EntityPlayerMP player : playerList) {
				if (getDistance(player) < 5) {
					getBlock().buffAttack(player);
				}
			}
		}
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		ResourceLocation id = new ResourceLocation(compound.getString("block"));
		if (Block.REGISTRY.containsKey(id)) {
			Block block = Block.REGISTRY.getObject(id);
			if (block instanceof BlockBuffAttackTNT) {
				setBlock((BlockBuffAttackTNT) block);
			}
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setString("block", getBlock().getRegistryName().toString());
	}

	public IBlockState getDefaultState() {
		return getBlock().getDefaultState();
	}

}
