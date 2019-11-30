package com.anotherstar.util;

import java.lang.reflect.Field;
import java.util.List;

import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.common.item.tool.ItemLoliPickaxe;

import cpw.mods.fml.relauncher.ReflectionHelper;
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
		player.func_110142_aN().func_94547_a(new EntityDamageSource("loli", source), Float.MAX_VALUE, Float.MAX_VALUE);
		player.setHealth(0.0F);
		player.onDeath(new DamageSource("loli"));
		if (ConfigLoader.loliPickaxeCompulsoryRemove) {
			// player.setDead();
			// player.isDead = true;
			player.loliDead = true;
		}
		if (player instanceof EntityPlayerMP) {
			EntityPlayerMP playerMP = (EntityPlayerMP) player;
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

	public static void killEntityLiving(EntityLivingBase entity, EntityLivingBase player) {
		if (!(entity.worldObj.isRemote || entity.loliDead || entity.isDead || entity.getHealth() == 0.0F)) {
			try {
				stupidMojangProtectedVariable.setInt(entity, 60);
			} catch (Exception e) {
				e.printStackTrace();
			}
			entity.func_110142_aN().func_94547_a(new EntityDamageSource("loli", player), Float.MAX_VALUE,
					Float.MAX_VALUE);
			entity.setHealth(0.0F);
			entity.onDeath(new EntityDamageSource("loli", player));
			if (ConfigLoader.loliPickaxeCompulsoryRemove) {
				// entity.setDead();
				// entity.isDead = true;
				entity.loliDead = true;
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
		for (Entity en : el) {
			if (en instanceof EntityPlayer) {
				if (en != player) {
					killPlayer((EntityPlayer) en, player);
				}
			} else if (en instanceof EntityLivingBase) {
				killEntityLiving((EntityLivingBase) en, player);
			} else {
				killEntity((Entity) en);
			}
		}
		return el.size();
	}

}
