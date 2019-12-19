package com.anotherstar.common.entity;

import com.anotherstar.common.entity.ai.EntityAILoliAttack;
import com.anotherstar.common.entity.ai.EntityAILoliNearestAttackableEntity;
import com.anotherstar.common.entity.ai.EntityAILoliNearestAttackablePlayer;
import com.anotherstar.common.entity.ai.EntityAILoliSwimming;
import com.anotherstar.common.entity.ai.EntityLoliMoveHelper;
import com.anotherstar.common.item.ItemLoader;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;

public class EntityLoli extends EntityCreature implements IEntityLoli {

	private boolean dispersal;
	public boolean dimChangeing;

	public EntityLoli(World worldIn) {
		super(worldIn);
		setSize(0.5F, 0.9F);
		moveHelper = new EntityLoliMoveHelper(this);
		setPathPriority(PathNodeType.WATER, 0);
		dispersal = false;
		dimChangeing = false;
	}

	@Override
	protected void initEntityAI() {
		tasks.addTask(0, new EntityAILoliAttack(this));
		tasks.addTask(4, new EntityAIWanderAvoidWater(this, 1, 4));
		tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 16.0f));
		tasks.addTask(6, new EntityAILoliSwimming(this));
		tasks.addTask(7, new EntityAILookIdle(this));
		targetTasks.addTask(2, new EntityAILoliNearestAttackablePlayer(this));
		targetTasks.addTask(2, new EntityAILoliNearestAttackableEntity(this));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1);
		getEntityAttribute(SWIM_SPEED).setBaseValue(15);
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20);
		getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64);
	}

	@Override
	public boolean attackEntityAsMob(Entity entity) {
		ItemLoader.loliPickaxe.leftClickEntity(this, entity);
		return true;
	}

	@Override
	public void travel(float strafe, float vertical, float forward) {
		if (isServerWorld() && isInWater() && getAttackTarget() != null && getAttackTarget().isInWater()) {
			moveRelative(strafe, vertical, forward, 0.01F);
			move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
			motionX *= (double) 0.9F;
			motionY *= (double) 0.9F;
			motionZ *= (double) 0.9F;
		} else {
			super.travel(strafe, vertical, forward);
		}
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	protected int getExperiencePoints(EntityPlayer player) {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean canBeLeashedTo(EntityPlayer player) {
		return false;
	}

	@Override
	protected void handleJumpWater() {
		this.motionY += 0.04;
	}

	@Override
	protected void handleJumpLava() {
		this.motionY += 0.04;
	}

	@Override
	public boolean isChild() {
		return true;
	}

	@Override
	protected void outOfWorld() {
		dismountRidingEntity();
		setLocationAndAngles(posX, 256, posZ, rotationYaw, rotationPitch);
	}

	@Override
	public Entity changeDimension(int dimensionIn, ITeleporter teleporter) {
		dispersal = true;
		return super.changeDimension(dimensionIn, teleporter);
	}

	@Override
	public void onRemovedFromWorld() {
		if (!dispersal && !world.isRemote) {
			EntityLoli loli = new EntityLoli(world);
			loli.copyLocationAndAnglesFrom(this);
			world.spawnEntity(loli);
			dispersal = true;
		}
		super.onRemovedFromWorld();
	}

	@Override
	public boolean isDispersal() {
		return dispersal;
	}

	@Override
	public void setDispersal(boolean value) {
		dispersal = value;
	}

}
