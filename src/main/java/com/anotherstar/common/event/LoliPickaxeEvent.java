package com.anotherstar.common.event;

import java.util.List;
import java.util.Set;

import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.common.item.tool.ItemLoliPickaxe;
import com.anotherstar.util.LoliPickaxeUtil;
import com.google.common.collect.Sets;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

public class LoliPickaxeEvent {

	/*
	 * @SubscribeEvent public void onPlayerDeath(LivingDeathEvent event) { if
	 * ((event.entity instanceof EntityPlayer)) { EntityPlayer player =
	 * (EntityPlayer) event.entity; if (ItemLoliPickaxe.invHaveLoliPickaxe(player))
	 * { event.setCanceled(true); player.setHealth(player.getMaxHealth()); } } }
	 */

	@SubscribeEvent
	public void onGetHurt(LivingHurtEvent event) {
		if (event.entityLiving.worldObj.isRemote) {
			return;
		}
		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			if (ItemLoliPickaxe.invHaveLoliPickaxe(player)) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onAttack(LivingAttackEvent event) {
		if (event.entityLiving.worldObj.isRemote) {
			return;
		}
		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			if (ItemLoliPickaxe.invHaveLoliPickaxe(player)) {
				Entity source = event.source.getSourceOfDamage();
				if (source != null) {
					EntityLivingBase el = null;
					if (source instanceof EntityArrow) {
						Entity se = ((EntityArrow) source).shootingEntity;
						if (se instanceof EntityLivingBase) {
							el = (EntityLivingBase) se;
						}
					} else if (source instanceof EntityLivingBase) {
						el = (EntityLivingBase) source;
					}
					if (el != null) {
						if (el instanceof EntityPlayer) {
							LoliPickaxeUtil.killPlayer((EntityPlayer) el, player);
						} else {
							LoliPickaxeUtil.killEntityLiving(el, player);
						}
					}
				}
				event.setCanceled(true);
			}
		}
	}

	private Set<String> flyingPlayer = Sets.newHashSet();

	@SubscribeEvent
	public void onPlayerUpdate(LivingEvent.LivingUpdateEvent event) {
		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			if (player.hodeLoli > 0) {
				player.hodeLoli--;
			}
			if (ItemLoliPickaxe.invHaveLoliPickaxe(player)) {
				if (!flyingPlayer.contains(player.getDisplayName())) {
					flyingPlayer.add(player.getDisplayName());
				}
				player.isDead = false;
				player.deathTime = 0;
				player.capabilities.flySpeed = 0.05F;
				player.capabilities.walkSpeed = 0.1F;
				if (!player.worldObj.isRemote) {
					player.clearActivePotions();
					player.extinguish();
					player.getFoodStats().addStats(20, 1);
					if (ConfigLoader.loliPickaxeAutoKillRangeEntity) {
						LoliPickaxeUtil.killRangeEntity(player.worldObj, player, ConfigLoader.loliPickaxeAutoKillRange);
					}
				}
			} else {
				if (!player.capabilities.isCreativeMode && flyingPlayer.contains(player.getDisplayName())) {
					flyingPlayer.remove(player.getDisplayName());
					player.capabilities.allowFlying = false;
					player.capabilities.isFlying = false;
				}
			}
			if (ConfigLoader.loliPickaxeFindOwner && !player.worldObj.isRemote) {
				List<EntityItem> entityItems = player.worldObj.getEntitiesWithinAABB(EntityItem.class,
						AxisAlignedBB.getBoundingBox(player.posX - ConfigLoader.loliPickaxeFindOwnerRange,
								player.posY - ConfigLoader.loliPickaxeFindOwnerRange,
								player.posZ - ConfigLoader.loliPickaxeFindOwnerRange,
								player.posX + ConfigLoader.loliPickaxeFindOwnerRange,
								player.posY + ConfigLoader.loliPickaxeFindOwnerRange,
								player.posZ + ConfigLoader.loliPickaxeFindOwnerRange));
				for (EntityItem entityItem : entityItems) {
					if (entityItem.getEntityItem() != null
							&& entityItem.getEntityItem().getItem() instanceof ItemLoliPickaxe
							&& entityItem.getEntityItem().hasTagCompound()) {
						NBTTagCompound nbt = entityItem.getEntityItem().getTagCompound();
						if (nbt.hasKey("Owner") && nbt.getString("Owner").equals(player.getDisplayName())) {
							entityItem.onCollideWithPlayer(player);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onEntityItemJoinWorld(EntityJoinWorldEvent event) {
		if (event.entity instanceof EntityItem) {
			EntityItem entityItem = (EntityItem) event.entity;
			if (entityItem.getEntityItem() != null && entityItem.getEntityItem().getItem() instanceof ItemLoliPickaxe) {
				entityItem.invulnerable = true;
				if (ConfigLoader.loliPickaxeFindOwner) {
					entityItem.delayBeforeCanPickup = 0;
				}
			}
		}
	}

	@SubscribeEvent
	public void onEntityItemPickup(EntityItemPickupEvent event) {
		if (event.item.getEntityItem() != null && event.item.getEntityItem().getItem() instanceof ItemLoliPickaxe
				&& event.item.getEntityItem().hasTagCompound()) {
			NBTTagCompound nbt = event.item.getEntityItem().getTagCompound();
			if (nbt.hasKey("Owner") && !nbt.getString("Owner").equals(event.entityPlayer.getDisplayName())) {
				event.setCanceled(true);
			}
		}
	}

}
