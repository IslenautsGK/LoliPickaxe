package com.anotherstar.util;

import java.util.Collection;
import java.util.List;

import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.common.entity.IEntityLoli;
import com.anotherstar.common.event.LoliPickaxeEvent;
import com.anotherstar.common.event.LoliTickEvent;
import com.anotherstar.common.item.tool.ILoli;
import com.anotherstar.network.LoliDeadPacket;
import com.anotherstar.network.LoliKillEntityPacket;
import com.anotherstar.network.NetworkHandler;
import com.google.common.collect.Lists;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class LoliPickaxeUtil {

	public static void kill(Collection<Entity> entitys, EntityLivingBase source) {
		for (Entity entity : entitys) {
			kill(entity, source);
		}
	}

	public static void kill(Entity entity, EntityLivingBase source) {
		if (entity instanceof EntityPlayer) {
			killPlayer((EntityPlayer) entity, source);
		} else if (entity instanceof EntityLivingBase) {
			killEntityLiving((EntityLivingBase) entity, source);
		} else if (ConfigLoader.getBoolean(getLoliPickaxe(source), "loliPickaxeValidToAllEntity")) {
			killEntity(entity);
		}
	}

	public static void killFacing(EntityLivingBase source) {
		World world = source.world;
		ItemStack stack = getLoliPickaxe(source);
		List<Entity> entitys = Lists.newArrayList();
		int range = ConfigLoader.getInt(stack, "loliPickaxeKillFacingRange");
		double slope = ConfigLoader.getDouble(stack, "loliPickaxeKillFacingSlope");
		boolean all = ConfigLoader.getBoolean(stack, "loliPickaxeValidToAllEntity");
		for (int dist = 0; dist <= range; dist += 2) {
			AxisAlignedBB bb = source.getEntityBoundingBox();
			Vec3d vec = source.getLookVec();
			vec = vec.normalize();
			bb = bb.grow(slope * dist + 2.0, slope * dist + 0.25, slope * dist + 2.0);
			bb = bb.offset(vec.x * dist, vec.y * dist, vec.z * dist);
			List<Entity> list = world.getEntitiesWithinAABB(all ? Entity.class : EntityLivingBase.class, bb);
			list.removeAll(entitys);
			list.removeIf(entity -> entity.getDistance(source) > range);
			entitys.addAll(list);
		}
		entitys.remove(source);
		if (!ConfigLoader.getBoolean(stack, "loliPickaxeValidToAmityEntity")) {
			entitys.removeIf(en -> en instanceof EntityPlayer || en instanceof EntityArmorStand
					|| en instanceof EntityAmbientCreature
					|| (en instanceof EntityCreature && !(en instanceof EntityMob)));
		}
		LoliPickaxeUtil.kill(entitys, source);
	}

	public static void killPlayer(EntityPlayer player, EntityLivingBase source) {
		if (invHaveLoliPickaxe(player) || player.loliDead) {
			return;
		}
		ItemStack stack = getLoliPickaxe(source);
		if (ConfigLoader.getBoolean(stack, "loliPickaxeClearInventory")) {
			player.inventory.clearMatchingItems(null, -1, -1, null);
			InventoryEnderChest ec = player.getInventoryEnderChest();
			for (int i = 0; i < ec.getSizeInventory(); i++) {
				ec.removeStackFromSlot(i);
			}
		}
		if (ConfigLoader.getBoolean(stack, "loliPickaxeDropItems")) {
			player.inventory.dropAllItems();
		}
		DamageSource ds = source == null ? new DamageSource("loli") : new EntityDamageSource("loli", source);
		player.getCombatTracker().trackDamage(ds, Float.MAX_VALUE, Float.MAX_VALUE);
		player.setHealth(0.0F);
		player.onDeath(ds);
		boolean remove = ConfigLoader.getBoolean(stack, "loliPickaxeCompulsoryRemove");
		if (remove) {
			player.loliDead = true;
			delayKill(player);
		}
		if (player instanceof EntityPlayerMP) {
			EntityPlayerMP playerMP = (EntityPlayerMP) player;
			NetworkHandler.INSTANCE.sendMessageToPlayer(
					new LoliDeadPacket(remove, ConfigLoader.getBoolean(stack, "loliPickaxeBlueScreenAttack"),
							ConfigLoader.getBoolean(stack, "loliPickaxeExitAttack"),
							ConfigLoader.getBoolean(stack, "loliPickaxeFailRespondAttack")),
					playerMP);
			if (ConfigLoader.getBoolean(stack, "loliPickaxeBeyondRedemption")) {
				ConfigLoader.addPlayerToBeyondRedemption(playerMP);
			}
			if (ConfigLoader.getBoolean(stack, "loliPickaxeKickPlayer")) {
				playerMP.connection
						.disconnect(new TextComponentString(ConfigLoader.getString(stack, "loliPickaxeKickMessage")));
			}
			if (ConfigLoader.getBoolean(stack, "loliPickaxeReincarnation")) {
				ConfigLoader.addPlayerToReincarnation(playerMP);
			}
		}
	}

	public static void killEntityLiving(EntityLivingBase entity, EntityLivingBase source) {
		if (invHaveLoliPickaxe(entity)) {
			return;
		}
		if (!(entity.world.isRemote || entity.loliDead || entity.isDead || entity.getHealth() == 0.0F)) {
			entity.recentlyHit = 60;
			DamageSource ds = source == null ? new DamageSource("loli") : new EntityDamageSource("loli", source);
			entity.getCombatTracker().trackDamage(ds, Float.MAX_VALUE, Float.MAX_VALUE);
			entity.setHealth(0.0F);
			Class<? extends EntityLivingBase> clazz = entity.getClass();
			LoliPickaxeEvent.antiEntity.add(clazz);
			entity.onDeath(ds);
			LoliPickaxeEvent.antiEntity.remove(clazz);
			if (ConfigLoader.getBoolean(getLoliPickaxe(source), "loliPickaxeCompulsoryRemove")) {
				entity.loliDead = true;
				delayKill(entity);
			}
		}
	}

	public static void killEntity(Entity entity) {
		entity.setDead();
	}

	public static int killRangeEntity(World world, EntityLivingBase entity, int range) {
		ItemStack stack = entity.getHeldItemMainhand();
		if (stack.isEmpty() || !(stack.getItem() instanceof ILoli)) {
			stack = getLoliPickaxe(entity);
		}
		List<Entity> list = world.getEntitiesWithinAABB(
				ConfigLoader.getBoolean(stack, "loliPickaxeValidToAllEntity") ? Entity.class : EntityLivingBase.class,
				new AxisAlignedBB(entity.posX - range, entity.posY - range, entity.posZ - range, entity.posX + range,
						entity.posY + range, entity.posZ + range));
		if (!ConfigLoader.getBoolean(stack, "loliPickaxeValidToAmityEntity")) {
			list.removeIf(en -> en instanceof EntityPlayer || en instanceof EntityArmorStand
					|| en instanceof EntityAmbientCreature
					|| (en instanceof EntityCreature && !(en instanceof EntityMob)));
		}
		list.remove(entity);
		for (Entity en : list) {
			if (en instanceof EntityPlayer) {
				killPlayer((EntityPlayer) en, entity);
			} else if (en instanceof EntityLivingBase) {
				killEntityLiving((EntityLivingBase) en, entity);
			} else {
				killEntity((Entity) en);
			}
		}
		return list.size();
	}

	private static void delayKill(EntityLivingBase entity) {
		int tick = 21;
		if (!(entity instanceof EntityPlayer)) {
			ResourceLocation id = EntityList.getKey(entity);
			if (ConfigLoader.loliPickaxeDelayRemoveList.containsKey(id.toString())) {
				tick = ConfigLoader.loliPickaxeDelayRemoveList.get(id.toString());
			} else if (ConfigLoader.loliPickaxeDelayRemoveList.containsKey(id.getResourcePath())) {
				tick = ConfigLoader.loliPickaxeDelayRemoveList.get(id.getResourcePath());
			}
		}
		LoliTickEvent.addTask(new LoliTickEvent.TickStartTask(tick, () -> {
			entity.loliCool = true;
			entity.isDead = true;
			NetworkHandler.INSTANCE.sendMessageToAll(new LoliKillEntityPacket(entity.dimension, entity.getEntityId()));
		}), Phase.START);
	}

	public static boolean invHaveLoliPickaxe(EntityLivingBase entity) {
		if (entity instanceof IEntityLoli) {
			return true;
		} else if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			if (player.inventory != null) {
				boolean hasLoli = false;
				for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
					ItemStack stack = player.inventory.getStackInSlot(i);
					if (!stack.isEmpty() && stack.getItem() instanceof ILoli) {
						ILoli loli = (ILoli) stack.getItem();
						String owner = loli.getOwner(stack);
						if (!owner.isEmpty()) {
							if (owner.equals(player.getName())) {
								int time = ConfigLoader.loliPickaxeDuration;
								if (time > 0 && time > player.hodeLoli) {
									player.hodeLoli = time;
								}
								hasLoli = true;
							} else {
								player.dropItem(stack, true, false);
								player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
							}
						}
					}
				}
				return hasLoli || player.hodeLoli > 0;
			}
			return player.hodeLoli > 0;
		}
		return false;
	}

	public static ItemStack getLoliPickaxe(EntityLivingBase entity) {
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			if (player.inventory != null) {
				ItemStack iloli = ItemStack.EMPTY;
				for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
					ItemStack stack = player.inventory.getStackInSlot(i);
					if (!stack.isEmpty() && stack.getItem() instanceof ILoli) {
						ILoli loli = (ILoli) stack.getItem();
						String owner = loli.getOwner(stack);
						if (!owner.isEmpty()) {
							if (owner.equals(player.getName())) {
								int time = ConfigLoader.loliPickaxeDuration;
								if (time > 0 && time > player.hodeLoli) {
									player.hodeLoli = time;
								}
								iloli = stack;
							} else {
								player.dropItem(stack, true, false);
								player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
							}
						}
					}
				}
				return iloli;
			}
		}
		return ItemStack.EMPTY;
	}

}
