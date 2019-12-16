package com.anotherstar.common.entity.ai;

import com.anotherstar.util.LoliPickaxeUtil;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

public class EntityAILoliAttack extends EntityAIAttackMelee {

	public EntityAILoliAttack(EntityCreature creature) {
		super(creature, 1.0, false);
	}

	public boolean shouldExecute() {
		EntityLivingBase target = attacker.getAttackTarget();
		if (target == null) {
			return false;
		} else if (LoliPickaxeUtil.invHaveLoliPickaxe(target)) {
			attacker.setAttackTarget(null);
			return false;
		} else {
			attacker.dismountRidingEntity();
			attacker.setLocationAndAngles(target.posX, target.posY, target.posZ, target.rotationYaw,
					target.rotationPitch);
			return true;
		}
	}

	public boolean shouldContinueExecuting() {
		EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
		if (entitylivingbase == null) {
			return false;
		} else if (!entitylivingbase.isEntityAlive()) {
			return false;
		} else if (entitylivingbase instanceof EntityPlayer && ((EntityPlayer) entitylivingbase).isSpectator()
				|| LoliPickaxeUtil.invHaveLoliPickaxe(entitylivingbase)) {
			return false;
		} else {
			return !this.attacker.getNavigator().noPath();
		}
	}

	@Override
	public void resetTask() {
		EntityLivingBase entity = this.attacker.getAttackTarget();
		if (entity instanceof EntityPlayer && ((EntityPlayer) entity).isSpectator()
				|| LoliPickaxeUtil.invHaveLoliPickaxe(entity)) {
			this.attacker.setAttackTarget((EntityLivingBase) null);
		}
		this.attacker.getNavigator().clearPath();
	}

	@Override
	protected void checkAndPerformAttack(EntityLivingBase entity, double distance) {
		double d = this.getAttackReachSqr(entity);
		if (distance <= d && this.attackTick <= 0) {
			this.attackTick = 5;
			this.attacker.swingArm(EnumHand.MAIN_HAND);
			this.attacker.attackEntityAsMob(entity);
		}
	}

}
