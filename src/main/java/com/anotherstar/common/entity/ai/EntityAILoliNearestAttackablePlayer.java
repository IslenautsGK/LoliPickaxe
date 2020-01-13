package com.anotherstar.common.entity.ai;

import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.util.LoliPickaxeUtil;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAILoliNearestAttackablePlayer extends EntityAINearestAttackableTarget<EntityPlayer> {

	public EntityAILoliNearestAttackablePlayer(EntityCreature creature) {
		super(creature, EntityPlayer.class, false);
	}

	public boolean shouldExecute() {
		if (ConfigLoader.loliAttack) {
			targetEntity = null;
			double min = Double.MAX_VALUE;
			for (EntityPlayer player : taskOwner.world.playerEntities) {
				if (!player.isSpectator() && !LoliPickaxeUtil.invHaveLoliPickaxe(player)) {
					double d = player.getDistanceSq(taskOwner);
					if (d < min && d < getTargetDistance()) {
						min = d;
						targetEntity = player;
					}
				}
			}
			return targetEntity != null;
		} else {
			return false;
		}
	}

}
