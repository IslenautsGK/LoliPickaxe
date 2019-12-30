package com.anotherstar.common.event;

import java.util.Map;
import java.util.Set;

import com.anotherstar.common.item.ItemLoader;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SmallLoliFlyEvent {

	private Set<String> flyingPlayer = Sets.newHashSet();
	private Map<String, Double> dodgeMap = Maps.newHashMap();
	private Map<String, Double> antiInjury = Maps.newHashMap();

	@SubscribeEvent
	public void onPlayerUpdate(LivingEvent.LivingUpdateEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
				ItemStack stack = player.inventory.getStackInSlot(i);
				if (stack.getItem() == ItemLoader.smallLoliPickaxe) {
					if (ItemLoader.smallLoliPickaxe.canFly(stack)) {
						if (!flyingPlayer.contains(player.getName())) {
							flyingPlayer.add(player.getName());
						}
						player.capabilities.allowFlying = true;
					}
					if (!player.world.isRemote) {
						switch (ItemLoader.smallLoliPickaxe.buffLevel(stack)) {
						case 3:
							player.getFoodStats().addStats(20, 1);
						case 2:
							player.addPotionEffect(new PotionEffect(Potion.getPotionById(13), 410, 0, false, false));
						case 1:
							player.addPotionEffect(new PotionEffect(Potion.getPotionById(16), 410, 0, false, false));
						}
					}
					dodgeMap.put(player.getName(), ItemLoader.smallLoliPickaxe.getDodge(stack));
					antiInjury.put(player.getName(), ItemLoader.smallLoliPickaxe.getAntiInjury(stack));
					return;
				}
			}
			if (!player.capabilities.isCreativeMode && flyingPlayer.contains(player.getName())) {
				flyingPlayer.remove(player.getName());
				player.capabilities.allowFlying = false;
				player.capabilities.isFlying = false;
			}
			if (dodgeMap.containsKey(player.getName())) {
				dodgeMap.remove(player.getName());
			}
			if (antiInjury.containsKey(player.getName())) {
				antiInjury.remove(player.getName());
			}
		}
	}

	@SubscribeEvent
	public void onFallDown(LivingHurtEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if (!entity.world.isRemote && entity instanceof EntityPlayer) {
			if (flyingPlayer.contains(entity.getName()) && event.getSource().getDamageType().equals("fall")) {
				event.setCanceled(true);
			} else if (dodgeMap.containsKey(entity.getName()) && entity.world.rand.nextDouble() < dodgeMap.get(entity.getName())) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onAttack(LivingAttackEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if (!entity.world.isRemote && entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			if (antiInjury.containsKey(player.getName()) && entity.world.rand.nextDouble() < antiInjury.get(entity.getName())) {
				Entity source = event.getSource().getTrueSource();
				if (source != null) {
					EntityLivingBase attacker = null;
					if (source instanceof EntityArrow) {
						Entity owner = ((EntityArrow) source).shootingEntity;
						if (owner instanceof EntityLivingBase) {
							attacker = (EntityLivingBase) owner;
						}
					} else if (source instanceof EntityLivingBase) {
						attacker = (EntityLivingBase) source;
					}
					if (attacker != null) {
						int cooldown = player.ticksSinceLastSwing;
						player.attackTargetEntityWithCurrentItem(attacker);
						player.ticksSinceLastSwing = cooldown;
						float damage = (float) player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue() * 0.5F;
						player.setHealth(Math.min(player.getHealth() + damage, player.getMaxHealth()));
					}
				}
			}
		}
	}

}
