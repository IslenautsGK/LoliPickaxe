package com.anotherstar.common.item.tool;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.anotherstar.client.creative.CreativeTabLoader;
import com.anotherstar.common.gui.ILoliInventory;
import com.anotherstar.common.gui.InventorySmallLoliPickaxe;
import com.anotherstar.common.item.ItemLoader;
import com.anotherstar.common.item.ItemLoliPickaxeMaterial;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class ItemSmallLoliPickaxe extends ItemTool implements IContainer {

	public static Map<ItemLoliPickaxeMaterial, String> nbtMap = Maps.newHashMap();
	public static boolean isharvesting = false;
	public static boolean autoFurnace = false;
	public static int fortuneLevel = 0;
	public static float exp = 0;
	public static ILoliInventory inventory = null;
	public static NonNullList<ItemStack> blacklist = null;
	private static ItemStack full = null;

	private static void init() {
		nbtMap.put(ItemLoader.coalAddon, "LoliDodge");
		nbtMap.put(ItemLoader.ironAddon, "LoliDiggingSpeed");
		nbtMap.put(ItemLoader.goldAddon, "LoliAttackDamage");
		nbtMap.put(ItemLoader.redstoneAddon, "LoliAttackSpeed");
		nbtMap.put(ItemLoader.lapisAddon, "LoliFortuneLevel");
		nbtMap.put(ItemLoader.diamondAddon, "LoliDiggingLevel");
		nbtMap.put(ItemLoader.emeraldAddon, "LoliDiggingRange");
		nbtMap.put(ItemLoader.obsidianAddon, "LoliAntiInjury");
		nbtMap.put(ItemLoader.glowAddon, "LoliBuff");
		nbtMap.put(ItemLoader.quartzAddon, "LoliHitRange");
		nbtMap.put(ItemLoader.netherStarAddon, "LoliBackpackPage");
		nbtMap.put(ItemLoader.autoFurnaceAddon, "LoliAutoFurnace");
		nbtMap.put(ItemLoader.flyAddon, "LoliFly");
		full = new ItemStack(ItemLoader.smallLoliPickaxe);
		NBTTagCompound nbt = new NBTTagCompound();
		for (Entry<ItemLoliPickaxeMaterial, String> entry : ItemSmallLoliPickaxe.nbtMap.entrySet()) {
			nbt.setInteger(entry.getValue(), entry.getKey().getSubCount() - 1);
		}
		full.setTagCompound(nbt);
		ItemLoader.smallLoliPickaxe.updateEnchantment(full);
	}

	public static ItemStack getFull() {
		if (full == null) {
			init();
		}
		return full;
	}

	public ItemSmallLoliPickaxe() {
		super(ItemLoliPickaxe.LOLI, Sets.newHashSet());
		this.setUnlocalizedName("smallLoliPickaxe");
		this.setCreativeTab(CreativeTabLoader.loliTabs);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		if (canHarvestBlock(state, stack) && stack.hasTagCompound() && stack.getTagCompound().hasKey("LoliDiggingSpeed")) {
			return getTransformValue("LoliDiggingSpeed", stack.getTagCompound().getInteger("LoliDiggingSpeed"));
		}
		return 1;
	}

	@Override
	public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("LoliDiggingLevel")) {
			return state.getBlock().getHarvestLevel(state) <= getHarvestLevel(stack);
		}
		return false;
	}

	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass, EntityPlayer player, IBlockState blockState) {
		return getHarvestLevel(stack);
	}

	public int getHarvestLevel(ItemStack stack) {
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("LoliDiggingLevel")) {
			return getTransformValue("LoliDiggingLevel", stack.getTagCompound().getInteger("LoliDiggingLevel"));
		}
		return -1;
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
		Multimap<String, AttributeModifier> multimap = HashMultimap.create();
		if (slot == EntityEquipmentSlot.MAINHAND) {
			double damage = 0;
			double speed = 0;
			if (stack.hasTagCompound()) {
				if (stack.getTagCompound().hasKey("LoliAttackDamage")) {
					damage = getDoubleTransformValue("LoliAttackDamage", stack.getTagCompound().getInteger("LoliAttackDamage"));
				}
				if (stack.getTagCompound().hasKey("LoliAttackSpeed")) {
					speed = getTransformValue("LoliAttackSpeed", stack.getTagCompound().getInteger("LoliAttackSpeed"));
				}
			}
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", damage, 0));
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", speed, 0));
		}
		return multimap;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entityIn, int itemSlot, boolean isSelected) {
		updateEnchantment(stack);
	}

	public void updateEnchantment(ItemStack stack) {
		if (stack.hasTagCompound()) {
			if (stack.getTagCompound().hasKey("LoliFortuneLevel")) {
				int level = getTransformValue("LoliFortuneLevel", stack.getTagCompound().getInteger("LoliFortuneLevel"));
				Map<Enchantment, Integer> enchMap = Maps.newHashMap();
				enchMap.put(Enchantments.FORTUNE, level);
				enchMap.put(Enchantments.LOOTING, level);
				EnchantmentHelper.setEnchantments(enchMap, stack);
			}
		} else {
			stack.setTagCompound(new NBTTagCompound());
		}
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entity) {
		if (!world.isRemote && entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			int range = getRange(stack);
			isharvesting = true;
			autoFurnace = stack.getTagCompound().hasKey("LoliAutoFurnace") ? getTransformValue("LoliAutoFurnace", stack.getTagCompound().getInteger("LoliAutoFurnace")) == 0 : false;
			fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
			exp = 0;
			blacklist = NonNullList.create();
			if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Blacklist")) {
				NBTTagList blackList = stack.getTagCompound().getTagList("Blacklist", 10);
				for (int i = 0; i < blackList.tagCount(); i++) {
					NBTTagCompound black = blackList.getCompoundTagAt(i);
					if (black.hasKey("Name") && black.hasKey("Damage")) {
						ItemStack blackStack = new ItemStack(Item.getByNameOrId(black.getString("Name")), 1, black.getInteger("Damage"));
						blacklist.add(blackStack);
					}
				}
			}
			if (hasInventory(stack)) {
				inventory = getInventory(stack);
				inventory.openInventory(player);
			}
			int bonusLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
			for (int i = -range; i <= range; i++) {
				for (int j = -range; j <= range; j++) {
					for (int k = -range; k <= range; k++) {
						BlockPos curPos = pos.add(i, j, k);
						IBlockState curState = world.getBlockState(curPos);
						Block curBlock = curState.getBlock();
						if (!curBlock.isAir(curState, world, curPos) && curBlock.canCollideCheck(curState, false)) {
							TileEntity tile = world.getTileEntity(curPos);
							boolean canHarvest = curBlock.canHarvestBlock(world, curPos, player) && curBlock.getBlockHardness(curState, world, curPos) > 0;
							if (canHarvest) {
								if (curBlock.removedByPlayer(curState, world, curPos, player, canHarvest)) {
									curBlock.onBlockDestroyedByPlayer(world, curPos, curState);
									curBlock.harvestBlock(world, player, curPos, curState, tile, stack.copy());
									curBlock.dropXpOnBlockBreak(world, curPos, curBlock.getExpDrop(curState, world, curPos, bonusLevel));
								}
							}
						}
					}
				}
			}
			if (inventory != null) {
				inventory.closeInventory(player);
				inventory = null;
			}
			blacklist = null;
			if ((int) exp > 0) {
				player.world.spawnEntity(new EntityXPOrb(player.world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, (int) exp));
			}
			fortuneLevel = 0;
			autoFurnace = false;
			isharvesting = false;
		}
		return false;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (stack.hasTagCompound()) {
			NBTTagCompound nbt = stack.getTagCompound();
			if (player.isSneaking()) {
				if (nbt.hasKey("LoliHitRange")) {
					if (!world.isRemote) {
						int cooldown = player.ticksSinceLastSwing;
						int range = getTransformValue("LoliHitRange", nbt.getInteger("LoliHitRange")) / 2;
						List<Entity> list = world.getEntitiesInAABBexcluding(player, player.getEntityBoundingBox().grow(range + 0.7, range + 0.1, range + 0.7), entity -> !(entity instanceof EntityPlayer || entity instanceof EntityArmorStand || entity instanceof EntityAmbientCreature || (entity instanceof EntityCreature && !(entity instanceof EntityMob))));
						for (Entity entity : list) {
							player.attackTargetEntityWithCurrentItem(entity);
							player.ticksSinceLastSwing = cooldown;
						}
						if (player instanceof EntityPlayerMP) {
							BlockPos pos = player.getPosition();
							((EntityPlayerMP) player).connection.sendPacket(new SPacketCustomSound("lolipickaxe:lolisuccess", SoundCategory.BLOCKS, pos.getX(), pos.getY(), pos.getZ(), 1.0F, 1.0F));
						}
					}
					player.resetCooldown();
					return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
				}
			} else {
				if (!world.isRemote) {
					if (nbt.hasKey("LoliDiggingRange")) {
						int maxRange = (getTransformValue("LoliDiggingRange", stack.getTagCompound().getInteger("LoliDiggingRange")) - 1) / 2 + 1;
						if (nbt.hasKey("LoliCurrentDiggingRange")) {
							nbt.setInteger("LoliCurrentDiggingRange", (nbt.getInteger("LoliCurrentDiggingRange") + 1) % maxRange);
						} else {
							nbt.setInteger("LoliCurrentDiggingRange", 1);
						}
						player.sendMessage(new TextComponentTranslation("loliPickaxe.range", 1 + 2 * nbt.getInteger("LoliCurrentDiggingRange")));
						if (player instanceof EntityPlayerMP) {
							BlockPos pos = player.getPosition();
							((EntityPlayerMP) player).connection.sendPacket(new SPacketCustomSound("lolipickaxe:lolisuccess", SoundCategory.BLOCKS, pos.getX(), pos.getY(), pos.getZ(), 1.0F, 1.0F));
						}
						return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
					}
				}
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
	}

	public int getRange(ItemStack stack) {
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("LoliCurrentDiggingRange")) {
			return stack.getTagCompound().getInteger("LoliCurrentDiggingRange");
		}
		return 0;
	}

	@Override
	public int getItemEnchantability(ItemStack stack) {
		return 0;
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return false;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (isInCreativeTab(tab)) {
			ItemStack stack = new ItemStack(this);
			items.add(stack);
			items.add(getFull());
		}
	}

	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		if (stack.hasTagCompound()) {
			NBTTagCompound nbt = stack.getTagCompound();
			for (String levelKey : nbtMap.values()) {
				if (nbt.hasKey(levelKey)) {
					int level = nbt.getInteger(levelKey);
					int value = getTransformValue(levelKey, level);
					if (value == Integer.MIN_VALUE) {
						tooltip.add(I18n.format("smallLoliPickaxe." + levelKey, getDoubleTransformValue(levelKey, level)));
					} else {
						tooltip.add(I18n.format("smallLoliPickaxe." + levelKey, value));
					}
				}
			}
		}
	}

	public int getTransformValue(String key, int level) {
		switch (key) {
		case "LoliDiggingSpeed":
			return 4 << level;
		case "LoliDiggingLevel":
			switch (level) {
			case 0:
				return 1;
			case 1:
				return 3;
			case 2:
				return 7;
			case 3:
				return 13;
			case 4:
				return 21;
			case 5:
				return 32;
			default:
				return -1;
			}
		case "LoliDiggingRange":
			return level * 2 + 3;
		case "LoliAttackSpeed":
			return 2 << level;
		case "LoliFortuneLevel":
			return 1 << level;
		case "LoliBackpackPage":
			return 2 << level;
		case "LoliBuff":
			return level + 1;
		case "LoliHitRange":
			return level * 10 + 6;
		case "LoliAutoFurnace":
			return level;
		case "LoliFly":
			return level;
		default:
			return Integer.MIN_VALUE;
		}
	}

	public double getDoubleTransformValue(String key, int level) {
		switch (key) {
		case "LoliDodge":
			return (level + 1) / 10.0;
		case "LoliAttackDamage":
			return 4 + Math.pow(2, Math.pow(2, level));
		case "LoliAntiInjury":
			return (level + 1) / 10.0;
		default:
			return Integer.MIN_VALUE;
		}
	}

	@Override
	public boolean hasInventory(ItemStack stack) {
		return stack.hasTagCompound() && stack.getTagCompound().hasKey("LoliBackpackPage");
	}

	@Override
	public ILoliInventory getInventory(ItemStack stack) {
		return new InventorySmallLoliPickaxe(stack);
	}

	public int getMaxPage(ItemStack stack) {
		return getTransformValue("LoliBackpackPage", stack.getTagCompound().getInteger("LoliBackpackPage"));
	}

	public boolean canFly(ItemStack stack) {
		return stack.hasTagCompound() && stack.getTagCompound().hasKey("LoliFly") && getTransformValue("LoliFly", stack.getTagCompound().getInteger("LoliFly")) == 0;
	}

	public double getDodge(ItemStack stack) {
		return stack.hasTagCompound() && stack.getTagCompound().hasKey("LoliDodge") ? getDoubleTransformValue("LoliDodge", stack.getTagCompound().getInteger("LoliDodge")) : 0;
	}

	public double getAntiInjury(ItemStack stack) {
		return stack.hasTagCompound() && stack.getTagCompound().hasKey("LoliAntiInjury") ? getDoubleTransformValue("LoliAntiInjury", stack.getTagCompound().getInteger("LoliAntiInjury")) : 0;
	}

	public int buffLevel(ItemStack stack) {
		return stack.hasTagCompound() && stack.getTagCompound().hasKey("LoliBuff") ? getTransformValue("LoliBuff", stack.getTagCompound().getInteger("LoliBuff")) : -1;
	}

}
