package com.anotherstar.common.entity.ai;

import com.anotherstar.common.entity.EntityLoli;

import net.minecraft.entity.ai.EntityAISwimming;

public class EntityAILoliSwimming extends EntityAISwimming {

	private EntityLoli loli;
	private boolean obstructed;

	public EntityAILoliSwimming(EntityLoli loli) {
		super(loli);
		this.loli = loli;
	}

	public boolean shouldContinueExecuting() {
		return this.shouldExecute() && !this.obstructed;
	}

	@Override
	public void updateTask() {
		if (loli.getNavigator().noPath() && loli.getAttackTarget() == null) {
			super.updateTask();
		}
	}

	@Override
	public void startExecuting() {
		obstructed = false;
	}

}
