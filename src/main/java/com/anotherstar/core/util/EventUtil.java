package com.anotherstar.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.common.item.tool.ILoli;
import com.anotherstar.common.item.tool.ItemLoliPickaxe;
import com.anotherstar.util.LoliPickaxeUtil;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class EventUtil {

	public static boolean onLivingDeath(EntityLivingBase entity, DamageSource src) {
		if (checkEntity(entity)) {
			entity.setHealth(entity.getMaxHealth());
			entity.isDead = false;
			entity.loliDead = false;
			entity.deathTime = 0;
			if (ConfigLoader.loliPickaxeThorns) {
				Entity source = src.getSourceOfDamage();
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
		boolean isLoli = checkEntity(entity);
		if (ConfigLoader.loliPickaxeForbidOnLivingUpdate && !isLoli
				&& (entity.loliDead || entity.isDead || entity.getHealth() == 0)) {
			// entity.isDead = true;
			if (++entity.loliDeathTime >= 20) {
				entity.isDead = true;
			}
			entity.deathTime = entity.loliDeathTime;
			return true;
		}
		if (ConfigLoader.loliPickaxeForbidOnLivingUpdateChangeHealth) {
			float health = entity.getHealth();
			boolean flying = false;
			if (isLoli) {
				flying = ((EntityPlayer) entity).capabilities.isFlying;
			}
			boolean result = MinecraftForge.EVENT_BUS.post(new LivingUpdateEvent(entity));
			entity.setHealth(health);
			/*
			 * if (health == 0 || entity.loliDead) { entity.isDead = true; }
			 */
			if (isLoli) {
				((EntityPlayer) entity).capabilities.allowFlying = true;
				((EntityPlayer) entity).capabilities.isFlying = flying;
			}
			return result;
		} else {
			boolean flying = false;
			if (isLoli) {
				flying = ((EntityPlayer) entity).capabilities.isFlying;
			}
			boolean result = MinecraftForge.EVENT_BUS.post(new LivingUpdateEvent(entity));
			/*
			 * if (entity.loliDead) { entity.isDead = true; }
			 */
			if (isLoli) {
				((EntityPlayer) entity).capabilities.allowFlying = true;
				((EntityPlayer) entity).capabilities.isFlying = flying;
			}
			return result;
		}
	}

	public static float getHealth(EntityLivingBase entity) {
		if (checkEntity(entity)) {
			// entity.isDead = false;
			return 20;
		} else if (entity.loliDead || ConfigLoader.loliPickaxeBeyondRedemption
				&& ConfigLoader.loliPickaxeBeyondRedemptionPlayerList.contains(entity.getUniqueID().toString())) {
			// entity.isDead = true;
			return 0;
		}
		return entity.getHealth2();
	}

	public static float getMaxHealth(EntityLivingBase entity) {
		if (checkEntity(entity)) {
			entity.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20);
			// entity.isDead = false;
			return 20;
		} else if (entity.loliDead || ConfigLoader.loliPickaxeBeyondRedemption
				&& ConfigLoader.loliPickaxeBeyondRedemptionPlayerList.contains(entity.getUniqueID().toString())) {
			// entity.isDead = true;
			return 0;
		}
		return entity.getMaxHealth2();
	}

	public static void setHealth(EntityLivingBase entity, float health) {
		/*
		 * if (health > 0) { entity.loliDead = false; }
		 */
		entity.setHealth2(health);
	}

	public static void setDead(Entity entity) {
		/*
		 * if (checkEntity(entity)) { entity.isDead = false; }
		 */
	}

	public static void dropAllItems(InventoryPlayer inventory) {
		if (checkEntity(inventory.player)) {
			return;
		}
		for (int i = 0; i < inventory.mainInventory.length; ++i) {
			if (inventory.mainInventory[i] != null && !(inventory.mainInventory[i].getItem() instanceof ILoli)) {
				inventory.player.func_146097_a(inventory.mainInventory[i], true, false);
				inventory.mainInventory[i] = null;
			}
		}

		for (int i = 0; i < inventory.armorInventory.length; ++i) {
			if (inventory.armorInventory[i] != null && !(inventory.armorInventory[i].getItem() instanceof ILoli)) {
				inventory.player.func_146097_a(inventory.armorInventory[i], true, false);
				inventory.armorInventory[i] = null;
			}
		}
	}

	public static int clearInventory(InventoryPlayer inventory, Item item, int damage) {
		if (checkEntity(inventory.player)) {
			return 0;
		}
		int count = 0;
		ItemStack itemstack;
		for (int i = 0; i < inventory.mainInventory.length; ++i) {
			itemstack = inventory.mainInventory[i];
			if (itemstack != null && (item == null || itemstack.getItem() == item)
					&& (damage <= -1 || itemstack.getItemDamage() == damage)
					&& !(itemstack.getItem() instanceof ILoli)) {
				count += itemstack.stackSize;
				inventory.mainInventory[i] = null;
			}
		}
		for (int i = 0; i < inventory.armorInventory.length; ++i) {
			itemstack = inventory.armorInventory[i];

			if (itemstack != null && (item == null || itemstack.getItem() == item)
					&& (damage <= -1 || itemstack.getItemDamage() == damage)
					&& !(itemstack.getItem() instanceof ItemLoliPickaxe)) {
				count += itemstack.stackSize;
				inventory.armorInventory[i] = null;
			}
		}
		if (inventory.getItemStack() != null) {
			if (item != null && inventory.getItemStack().getItem() != item) {
				return count;
			}
			if (damage > -1 && inventory.getItemStack().getItemDamage() != damage) {
				return count;
			}
			if (inventory.getItemStack().getItem() instanceof ItemLoliPickaxe) {
				return count;
			}
			count += inventory.getItemStack().stackSize;
			inventory.setItemStack((ItemStack) null);
		}
		return count;
	}

	public static void kickPlayerFromServer(NetHandlerPlayServer playerNetServerHandler, String message) {
		if (checkEntity(playerNetServerHandler.playerEntity)) {
			return;
		}
		final ChatComponentText chatcomponenttext = new ChatComponentText(message);
		playerNetServerHandler.netManager.scheduleOutboundPacket(new S40PacketDisconnect(chatcomponenttext),
				new GenericFutureListener[] { new GenericFutureListener() {
					private static final String __OBFID = "CL_00001453";

					public void operationComplete(Future p_operationComplete_1_) {
						playerNetServerHandler.netManager.closeChannel(chatcomponenttext);
					}
				} });
		playerNetServerHandler.netManager.disableAutoRead();
	}

	public static NBTTagCompound readPlayerData(SaveHandler handler, EntityPlayer player) {
		if (ConfigLoader.loliPickaxeReincarnation
				&& ConfigLoader.loliPickaxeReincarnationPlayerList.contains(player.getUniqueID().toString())) {
			return null;
		}
		NBTTagCompound nbttagcompound = null;
		try {
			File file1 = new File(handler.playersDirectory, player.getUniqueID().toString() + ".dat");
			if (file1.exists() && file1.isFile()) {
				nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(file1));
			}
		} catch (Exception exception) {
			handler.logger.warn("Failed to load player data for " + player.getCommandSenderName());
		}

		if (nbttagcompound != null) {
			player.readFromNBT(nbttagcompound);
		}
		net.minecraftforge.event.ForgeEventFactory.firePlayerLoadingEvent(player, handler.playersDirectory,
				player.getUniqueID().toString());
		return nbttagcompound;
	}

	public static void writePlayerData(SaveHandler handler, EntityPlayer player) {
		if (ConfigLoader.loliPickaxeReincarnation
				&& ConfigLoader.loliPickaxeReincarnationPlayerList.contains(player.getUniqueID().toString())) {
			return;
		}
		try {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			player.writeToNBT(nbttagcompound);
			File file1 = new File(handler.playersDirectory, player.getUniqueID().toString() + ".dat.tmp");
			File file2 = new File(handler.playersDirectory, player.getUniqueID().toString() + ".dat");
			CompressedStreamTools.writeCompressed(nbttagcompound, new FileOutputStream(file1));
			if (file2.exists()) {
				file2.delete();
			}
			file1.renameTo(file2);
			net.minecraftforge.event.ForgeEventFactory.firePlayerSavingEvent(player, handler.playersDirectory,
					player.getUniqueID().toString());
		} catch (Exception exception) {
			handler.logger.warn("Failed to save player data for " + player.getCommandSenderName());
		}
	}

	private static boolean checkEntity(Entity entity) {
		if ((entity instanceof EntityPlayer)) {
			EntityPlayer player = (EntityPlayer) entity;
			return ItemLoliPickaxe.invHaveLoliPickaxe(player);
		}
		return false;
	}

}
