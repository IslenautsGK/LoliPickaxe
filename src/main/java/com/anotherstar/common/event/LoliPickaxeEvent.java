package com.anotherstar.common.event;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.common.item.tool.ILoli;
import com.anotherstar.network.LoliKillFacingPacket;
import com.anotherstar.network.NetworkHandler;
import com.anotherstar.util.LoliPickaxeUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LoliPickaxeEvent {

	public static Set<Class<? extends Entity>> antiEntity = Sets.newHashSet();

	private Set<String> flyingPlayer = Sets.newHashSet();
	private Set<EntityPlayer> loliPlayer = Sets.newHashSet();

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onLiftClick(PlayerInteractEvent.LeftClickEmpty event) {
		EntityPlayer player = event.getEntityPlayer();
		if (ConfigLoader.getBoolean(player.getHeldItemMainhand(), "loliPickaxeKillFacing") && !player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() instanceof ILoli) {
			if (player.world.isRemote) {
				NetworkHandler.INSTANCE.sendMessageToServer(new LoliKillFacingPacket());
			}
		}
	}

	@SubscribeEvent
	public void onGetHurt(LivingHurtEvent event) {
		if (event.getEntityLiving().world.isRemote) {
			return;
		}
		if (LoliPickaxeUtil.invHaveLoliPickaxe(event.getEntityLiving())) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onAttack(LivingAttackEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if (!entity.world.isRemote) {
			if (LoliPickaxeUtil.invHaveLoliPickaxe(event.getEntityLiving())) {
				if (ConfigLoader.getBoolean(LoliPickaxeUtil.getLoliPickaxe(entity), "loliPickaxeThorns")) {
					Entity source = event.getSource().getTrueSource();
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
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onPlayerUpdate(LivingEvent.LivingUpdateEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		boolean isLoli = LoliPickaxeUtil.invHaveLoliPickaxe(entity);
		if (isLoli) {
			entity.isDead = false;
			entity.deathTime = 0;
			if (!entity.world.isRemote) {
				entity.extinguish();
				if (ConfigLoader.getBoolean(entity.getHeldItemMainhand(), "loliPickaxeAutoKillRangeEntity")) {
					int range = ConfigLoader.getInt(entity.getHeldItemMainhand(), "loliPickaxeAutoKillRange");
					LoliPickaxeUtil.killRangeEntity(entity.world, entity, range);
				}
			}
		}
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			ItemStack stack = LoliPickaxeUtil.getLoliPickaxe(player);
			if (player.hodeLoli > 0) {
				player.hodeLoli--;
			}
			if (isLoli) {
				if (!flyingPlayer.contains(player.getName())) {
					flyingPlayer.add(player.getName());
				}
				if (!loliPlayer.contains(player)) {
					loliPlayer.add(player);
				}
				player.capabilities.flySpeed = 0.05F;
				player.capabilities.walkSpeed = 0.1F;
				double distance = ConfigLoader.getDouble(stack, "loliPickaxeBlockReachDistance");
				if (distance > 0) {
					player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).setBaseValue(distance);
				} else {
					player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).setBaseValue(5.0);
				}
				if (!player.world.isRemote) {
					List<Potion> potions = Lists.newArrayList();
					if (stack.hasTagCompound() && stack.getTagCompound().hasKey("LoliPotion")) {
						NBTTagList list = stack.getTagCompound().getTagList("LoliPotion", 10);
						for (int i = 0; i < list.tagCount(); i++) {
							NBTTagCompound element = list.getCompoundTagAt(i);
							Potion potion = Potion.getPotionById(element.getShort("id"));
							byte level = element.getByte("lvl");
							potions.add(potion);
							player.addPotionEffect(new PotionEffect(potion, 410, level, false, false));
						}
					}
					Iterator<Entry<Potion, PotionEffect>> iterator = player.getActivePotionMap().entrySet().iterator();
					while (iterator.hasNext()) {
						Entry<Potion, PotionEffect> entry = iterator.next();
						if (!potions.contains(entry.getKey())) {
							player.onLoliFinishedPotionEffect(entry.getValue());
							iterator.remove();
						}
					}
					player.getFoodStats().addStats(20, 1);
				}
			} else {
				if (!player.capabilities.isCreativeMode && flyingPlayer.contains(player.getName())) {
					flyingPlayer.remove(player.getName());
					player.capabilities.allowFlying = false;
					player.capabilities.isFlying = false;
				}
				if (loliPlayer.contains(player)) {
					loliPlayer.remove(player);
				}
			}
			if (ConfigLoader.loliPickaxeFindOwner && !player.world.isRemote) {
				List<EntityItem> entityItems = player.world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(player.posX - ConfigLoader.loliPickaxeFindOwnerRange, player.posY - ConfigLoader.loliPickaxeFindOwnerRange, player.posZ - ConfigLoader.loliPickaxeFindOwnerRange, player.posX + ConfigLoader.loliPickaxeFindOwnerRange, player.posY + ConfigLoader.loliPickaxeFindOwnerRange, player.posZ + ConfigLoader.loliPickaxeFindOwnerRange));
				for (EntityItem entityItem : entityItems) {
					ItemStack estack = entityItem.getItem();
					if (!estack.isEmpty() && estack.getItem() instanceof ILoli) {
						ILoli loli = (ILoli) estack.getItem();
						String owner = loli.getOwner(estack);
						if (!owner.isEmpty() && owner.equals(player.getName())) {
							entityItem.onCollideWithPlayer(player);
						}
					}
				}
			}
		} else if (isLoli) {
			entity.clearActivePotions();
		}
	}

	@SubscribeEvent
	public void onEntityItemJoinWorld(EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();
		for (Class<? extends Entity> clazz : antiEntity) {
			if (clazz.isInstance(entity)) {
				event.setCanceled(true);
				return;
			}
		}
		if (entity instanceof EntityItem) {
			EntityItem entityItem = (EntityItem) event.getEntity();
			if (!entityItem.getItem().isEmpty() && entityItem.getItem().getItem() instanceof ILoli) {
				entityItem.invulnerable = true;
				if (ConfigLoader.loliPickaxeFindOwner) {
					entityItem.setNoPickupDelay();
				}
			}
		}
	}

	@SubscribeEvent
	public void onEntityItemPickup(EntityItemPickupEvent event) {
		ItemStack stack = event.getItem().getItem();
		if (!stack.isEmpty() && stack.getItem() instanceof ILoli) {
			ILoli loli = (ILoli) stack.getItem();
			String owner = loli.getOwner(stack);
			if (!(owner.isEmpty() || owner.equals(event.getEntityPlayer().getName()))) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onServerTick(ServerTickEvent event) {
		if (event.phase == Phase.END) {
			for (EntityPlayer player : loliPlayer) {
				if (player.isDead) {
					player.isDead = false;
				}
				if (!player.world.playerEntities.contains(player)) {
					player.world.playerEntities.add(player);
					player.world.onEntityAdded(player);
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerOut(PlayerEvent.PlayerLoggedOutEvent event) {
		if (loliPlayer.contains(event.player)) {
			loliPlayer.remove(event.player);
		}
	}

}
