package com.anotherstar.util;

import java.lang.reflect.Field;
import java.util.List;

import com.anotherstar.common.LoliPickaxe;
import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.common.event.LoliTickEvent;
import com.anotherstar.common.item.tool.ItemLoliPickaxe;

import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.ReflectionHelper;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.world.World;

public class LoliPickaxeUtil {

	public static Field stupidMojangProtectedVariable;

	static {
		stupidMojangProtectedVariable = ReflectionHelper.findField(EntityLivingBase.class,
				new String[] { "recentlyHit", "field_70718_bc" });
	}

	public static void kill(Entity entity, EntityLivingBase source) {
		if (entity instanceof EntityPlayer) {
			killPlayer((EntityPlayer) entity, source);
		} else if (entity instanceof EntityLivingBase) {
			killEntityLiving((EntityLivingBase) entity, source);
		} else if (ConfigLoader.loliPickaxeValidToAllEntity) {
			killEntity(entity);
		}
	}

	public static void killPlayer(EntityPlayer player, EntityLivingBase source) {
		if (ItemLoliPickaxe.invHaveLoliPickaxe(player) || player.loliDead) {
			return;
		}
		if (ConfigLoader.loliPickaxeClearInventory) {
			player.inventory.clearInventory(null, -1);
			InventoryEnderChest ec = player.getInventoryEnderChest();
			for (int i = 0; i < ec.getSizeInventory(); i++) {
				ec.setInventorySlotContents(i, null);
			}
		}
		if (ConfigLoader.loliPickaxeDropItems) {
			player.inventory.dropAllItems();
		}
		DamageSource ds = source == null ? new DamageSource("loli") : new EntityDamageSource("loli", source);
		player.func_110142_aN().func_94547_a(ds, Float.MAX_VALUE, Float.MAX_VALUE);
		player.setHealth(0.0F);
		player.onDeath(ds);
		if (ConfigLoader.loliPickaxeCompulsoryRemove) {
			player.loliDead = true;
			delayKill(player, 21);
		}
		if (player instanceof EntityPlayerMP) {
			EntityPlayerMP playerMP = (EntityPlayerMP) player;
			if (ConfigLoader.loliPickaxeCompulsoryRemove) {
				LoliPickaxe.loliDeadNetwork.sendTo(new FMLProxyPacket(Unpooled.buffer(), "loliDead"), playerMP);
			}
			if (ConfigLoader.loliPickaxeBeyondRedemption) {
				ConfigLoader.addPlayerToBeyondRedemption(playerMP);
			}
			if (ConfigLoader.loliPickaxeKickPlayer) {
				playerMP.playerNetServerHandler.kickPlayerFromServer(ConfigLoader.loliPickaxeKickMessage);
			}
			if (ConfigLoader.loliPickaxeReincarnation) {
				ConfigLoader.addPlayerToReincarnation(playerMP);
			}
		}
	}

	public static void killEntityLiving(EntityLivingBase entity, EntityLivingBase source) {
		if (!(entity.worldObj.isRemote || entity.loliDead || entity.isDead || entity.getHealth() == 0.0F)) {
			try {
				stupidMojangProtectedVariable.setInt(entity, 60);
			} catch (Exception e) {
				e.printStackTrace();
			}
			DamageSource ds = source == null ? new DamageSource("loli") : new EntityDamageSource("loli", source);
			entity.func_110142_aN().func_94547_a(ds, Float.MAX_VALUE, Float.MAX_VALUE);
			entity.setHealth(0.0F);
			entity.onDeath(ds);
			if (ConfigLoader.loliPickaxeCompulsoryRemove) {
				entity.loliDead = true;
				delayKill(entity, 21);
			}
		}
	}

	public static void killEntity(Entity entity) {
		entity.setDead();
		if (ConfigLoader.loliPickaxeCompulsoryRemove) {
			entity.isDead = true;
		}
	}

	public static int killRangeEntity(World world, EntityPlayer player, int range) {
		List<Entity> el = world.getEntitiesWithinAABB(
				ConfigLoader.loliPickaxeValidToAllEntity ? Entity.class : EntityLivingBase.class,
				AxisAlignedBB.getBoundingBox(player.posX - range, player.posY - range, player.posZ - range,
						player.posX + range, player.posY + range, player.posZ + range));
		el.remove(player);
		for (Entity en : el) {
			if (en instanceof EntityPlayer) {
				killPlayer((EntityPlayer) en, player);
			} else if (en instanceof EntityLivingBase) {
				killEntityLiving((EntityLivingBase) en, player);
			} else {
				killEntity((Entity) en);
			}
		}
		return el.size();
	}

	private static void delayKill(EntityLivingBase entity, int tick) {
		LoliTickEvent.addTask(new LoliTickEvent.TickStartTask(tick, () -> {
			entity.loliCool = true;
			entity.isDead = true;
		}), Phase.START);
	}

}
