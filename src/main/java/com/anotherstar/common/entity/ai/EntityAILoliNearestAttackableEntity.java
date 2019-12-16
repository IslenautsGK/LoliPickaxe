package com.anotherstar.common.entity.ai;

import java.util.Collections;
import java.util.List;

import com.anotherstar.common.entity.EntityLoli;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;

public class EntityAILoliNearestAttackableEntity extends EntityAINearestAttackableTarget<EntityLivingBase> {

	public EntityAILoliNearestAttackableEntity(EntityCreature creature) {
		super(creature, EntityLivingBase.class, false);
	}

	public boolean shouldExecute() {
		List<EntityLivingBase> list = taskOwner.world.getEntitiesWithinAABB(this.targetClass,
				getTargetableArea(getTargetDistance()), targetEntitySelector);
		list.removeIf(entity -> entity instanceof EntityLoli);
		if (list.isEmpty()) {
			return false;
		} else {
			Collections.sort(list, this.sorter);
			targetEntity = list.get(0);
			return true;
		}

	}

}
