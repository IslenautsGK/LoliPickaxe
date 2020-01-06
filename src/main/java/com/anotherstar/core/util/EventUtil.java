package com.anotherstar.core.util;

import javax.annotation.Nullable;

import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.common.entity.IEntityLoli;
import com.anotherstar.common.gui.ContainerLoliPickaxe;
import com.anotherstar.network.LoliSlotChangePacket;
import com.anotherstar.network.LoliSlotsInitPacket;
import com.anotherstar.network.NetworkHandler;
import com.anotherstar.util.LoliPickaxeUtil;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventUtil {

	public static boolean onLivingDeath(EntityLivingBase entity, DamageSource src) {
		if (LoliPickaxeUtil.invHaveLoliPickaxe(entity)) {
			entity.setHealth(entity.getMaxHealth());
			entity.isDead = false;
			entity.loliDead = false;
			entity.deathTime = 0;
			if (ConfigLoader.getBoolean(LoliPickaxeUtil.getLoliPickaxe(entity), "loliPickaxeThorns")) {
				Entity source = src.getTrueSource();
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
							LoliPickaxeUtil.killPlayer((EntityPlayer) el, entity);
						} else {
							LoliPickaxeUtil.killEntityLiving(el, entity);
						}
					}
				}
			}
			return true;
		}
		return !src.getDamageType().equals("loli") && MinecraftForge.EVENT_BUS.post(new LivingDeathEvent(entity, src));
	}

	public static boolean onLivingUpdate(EntityLivingBase entity) {
		boolean isLoli = LoliPickaxeUtil.invHaveLoliPickaxe(entity);
		if (!isLoli && entity.loliCool) {
			entity.isDead = true;
			entity.deathTime = entity.loliDeathTime;
			return true;
		}
		if (ConfigLoader.loliPickaxeForbidOnLivingUpdate && !isLoli && (entity.loliDead || entity.isDead || entity.getHealth() == 0)) {
			if (++entity.loliDeathTime >= 20) {
				entity.isDead = true;
			}
			entity.deathTime = entity.loliDeathTime;
			return true;
		}
		boolean flying = false;
		if (isLoli && entity instanceof EntityPlayer) {
			flying = ((EntityPlayer) entity).capabilities.isFlying;
		}
		boolean result = MinecraftForge.EVENT_BUS.post(new LivingUpdateEvent(entity));
		if (isLoli && entity instanceof EntityPlayer) {
			((EntityPlayer) entity).capabilities.allowFlying = true;
			((EntityPlayer) entity).capabilities.isFlying = flying;
		} else if (entity.loliDead) {
			entity.deathTime = entity.loliDeathTime;
		}
		return result;
	}

	public static void onUpdate(EntityLivingBase entity) {
		boolean isLoli = LoliPickaxeUtil.invHaveLoliPickaxe(entity);
		if (!isLoli && entity.loliDead) {
			entity.deathTime = ++entity.loliDeathTime;
		}
		if (!isLoli && entity.loliCool) {
			entity.isDead = true;
		}
	}

	public static void updateEntities(World world) {
		for (Entity entity : world.loadedEntityList) {
			if (entity instanceof IEntityLoli) {
				entity.isDead = ((IEntityLoli) entity).isDispersal();
			}
		}
	}

	public static float getHealth(EntityLivingBase entity) {
		if (LoliPickaxeUtil.invHaveLoliPickaxe(entity)) {
			return 20;
		} else if (entity.loliDead || ConfigLoader.loliPickaxeBeyondRedemptionPlayerList.contains(entity.getUniqueID().toString())) {
			return 0;
		}
		return entity.getHealth2();
	}

	public static float getMaxHealth(EntityLivingBase entity) {
		if (LoliPickaxeUtil.invHaveLoliPickaxe(entity)) {
			entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20);
			return 20;
		} else if (entity.loliDead || ConfigLoader.loliPickaxeBeyondRedemptionPlayerList.contains(entity.getUniqueID().toString())) {
			return 0;
		}
		return entity.getMaxHealth2();
	}

	public static void dropAllItems(InventoryPlayer inventory) {
		if (!LoliPickaxeUtil.invHaveLoliPickaxe(inventory.player)) {
			inventory.dropAllItems2();
		}
	}

	public static int clearMatchingItems(InventoryPlayer inventory, @Nullable Item item, int meta, int removeCount, @Nullable NBTTagCompound itemNBT) {
		if (LoliPickaxeUtil.invHaveLoliPickaxe(inventory.player)) {
			return 0;
		} else {
			return inventory.clearMatchingItems2(item, meta, removeCount, itemNBT);
		}
	}

	public static void disconnect(NetHandlerPlayServer playerNetServerHandler, ITextComponent textComponent) {
		if (!LoliPickaxeUtil.invHaveLoliPickaxe(playerNetServerHandler.player)) {
			playerNetServerHandler.disconnect2(textComponent);
		}
	}

	public static NBTTagCompound readPlayerData(SaveHandler handler, EntityPlayer player) {
		if (ConfigLoader.loliPickaxeReincarnationPlayerList.contains(player.getUniqueID().toString())) {
			return null;
		} else {
			return handler.readPlayerData2(player);
		}
	}

	public static void writePlayerData(SaveHandler handler, EntityPlayer player) {
		if (!(ConfigLoader.loliPickaxeReincarnationPlayerList.contains(player.getUniqueID().toString()))) {
			handler.writePlayerData2(player);
		}
	}

	@SideOnly(Side.CLIENT)
	public static void removeEntityFromWorld(WorldClient client, int entityID) {
		Entity entity = client.getEntityByID(entityID);
		if (entity instanceof IEntityLoli) {
			((IEntityLoli) entity).setDispersal(true);
		}
	}

	public static void sendAllContents(EntityPlayerMP player, Container container, NonNullList<ItemStack> stackList) {
		if (container instanceof ContainerLoliPickaxe) {
			NetworkHandler.INSTANCE.sendMessageToPlayer(new LoliSlotsInitPacket(container.windowId, container.getInventory()), player);
			NetworkHandler.INSTANCE.sendMessageToPlayer(new LoliSlotChangePacket(-1, -1, player.inventory.getItemStack()), player);
		} else {
			player.sendAllContents2(container, stackList);
		}
	}

	public static RayTraceResult rayTrace(Entity entity, double blockReachDistance, float partialTicks) {
		Vec3d vec3d = entity.getPositionEyes(partialTicks);
		Vec3d vec3d1 = entity.getLook(partialTicks);
		Vec3d vec3d2 = vec3d.addVector(vec3d1.x * blockReachDistance, vec3d1.y * blockReachDistance, vec3d1.z * blockReachDistance);
		if (entity instanceof EntityPlayer) {
			ItemStack loli = LoliPickaxeUtil.getLoliPickaxe((EntityPlayer) entity);
			if (!loli.isEmpty() && ConfigLoader.getBoolean(loli, "loliPickaxeStopOnLiquid")) {
				return entity.world.rayTraceBlocks(vec3d, vec3d2, true, false, true);
			}
		}
		return entity.world.rayTraceBlocks(vec3d, vec3d2, false, false, true);
	}

}
