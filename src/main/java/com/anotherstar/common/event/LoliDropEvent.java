package com.anotherstar.common.event;

import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.common.item.ItemLoader;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LoliDropEvent {

	@SubscribeEvent
	public void onLivingDrop(LivingDropsEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if (entity.world.rand.nextDouble() < ConfigLoader.loliCardDropProbability) {
			event.getDrops().add(new EntityItem(entity.world, entity.posX, entity.posY, entity.posZ, new ItemStack(ItemLoader.loliCard)));
		}
		if (!ItemLoader.loliRecords.isEmpty() && entity instanceof EntityCreeper && entity.world.rand.nextDouble() < ConfigLoader.loliRecordDropProbability) {
			event.getDrops().add(new EntityItem(entity.world, entity.posX, entity.posY, entity.posZ, new ItemStack(ItemLoader.loliRecords.get(entity.world.rand.nextInt(ItemLoader.loliRecords.size())))));
		}
		if (entity.world.rand.nextDouble() < ConfigLoader.entitySoulDropProbability) {
			event.getDrops().add(new EntityItem(entity.world, entity.posX, entity.posY, entity.posZ, new ItemStack(ItemLoader.entitySoul)));
		}
	}

}
