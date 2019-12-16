package com.anotherstar.common.entity.ai;

import com.anotherstar.common.entity.EntityLoli;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.math.MathHelper;

public class EntityLoliMoveHelper extends EntityMoveHelper {

	private EntityLoli loli;

	public EntityLoliMoveHelper(EntityLoli loli) {
		super(loli);
		this.loli = loli;
	}

	@Override
	public void onUpdateMoveHelper() {
		EntityLivingBase target = loli.getAttackTarget();
		if (target != null && target.isInWater() && loli.isInWater()) {
			if (action != EntityMoveHelper.Action.MOVE_TO || loli.getNavigator().noPath()) {
				loli.setAIMoveSpeed(0.0F);
				return;
			}
			double dx = posX - loli.posX;
			double dy = posY - loli.posY;
			double dz = posZ - loli.posZ;
			double d = (double) MathHelper.sqrt(dx * dx + dy * dy + dz * dz);
			dy = dy / d;
			float f = (float) (MathHelper.atan2(dz, dx) * 180 / Math.PI) - 90.0F;
			loli.rotationYaw = limitAngle(loli.rotationYaw, f, 90.0F);
			loli.renderYawOffset = loli.rotationYaw;
			float f1 = (float) (speed * loli.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue());
			loli.setAIMoveSpeed(loli.getAIMoveSpeed() + (f1 - loli.getAIMoveSpeed()) * 0.125F);
			loli.motionY += loli.getAIMoveSpeed() * dy * 0.4;
			loli.motionX += loli.getAIMoveSpeed() * dx * 0.02;
			loli.motionZ += loli.getAIMoveSpeed() * dz * 0.02;
		} else {
			super.onUpdateMoveHelper();
		}
	}

}
