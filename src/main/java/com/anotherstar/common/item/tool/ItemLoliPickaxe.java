package com.anotherstar.common.item.tool;

import java.lang.reflect.Field;
import java.util.List;

import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.util.LoliPickaxeUtil;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;

public class ItemLoliPickaxe extends ItemPickaxe implements ILoli {

	public static Field stupidMojangProtectedVariable = ReflectionHelper.findField(EntityLivingBase.class,
			"recentlyHit", "field_70718_bc");

	public static final Item.ToolMaterial LOLI = EnumHelper.addToolMaterial("LOLI", 32, Short.MAX_VALUE,
			Float.MAX_VALUE, -2.0F, 200);

	public ItemLoliPickaxe() {
		super(LOLI);
		this.setUnlocalizedName("loliPickaxe");
		this.setCreativeTab(CreativeTabs.tabTools);
	}

	@Override
	public void setDamage(ItemStack stack, int damage) {
		super.setDamage(stack, 0);
	}

	@Override
	public float getDigSpeed(ItemStack stack, Block block, int meta) {
		return 0.0F;
	}

	@Override
	public int getEntityLifespan(ItemStack itemStack, World world) {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean canHarvestBlock(Block par1Block, ItemStack itemStack) {
		return false;
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		if (!entity.worldObj.isRemote) {
			if (entity instanceof EntityPlayer) {
				LoliPickaxeUtil.killPlayer((EntityPlayer) entity, player);
				return true;
			} else if (entity instanceof EntityLivingBase) {
				LoliPickaxeUtil.killEntityLiving((EntityLivingBase) entity, player);
				return true;
			} else if (ConfigLoader.loliPickaxeValidToAllEntity && !(entity instanceof EntityLivingBase)) {
				LoliPickaxeUtil.killEntity(entity);
				return true;
			}
		}
		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		if (!world.isRemote) {
			if (player.isSneaking()) {
				if (ConfigLoader.loliPickaxeKillRangeEntity) {
					int count = LoliPickaxeUtil.killRangeEntity(world, player, ConfigLoader.loliPickaxeKillRange);
					IChatComponent ic = new ChatComponentTranslation("loliPickaxe.killrangeentity", new Object[] {
							String.valueOf(ConfigLoader.loliPickaxeKillRange * 2), String.valueOf(count) });
					player.addChatMessage(ic);
					player.worldObj.playSoundAtEntity(player, "anotherstar:block.lolisuccess", 1.0F, 1.0F);
				}
			} else {
				NBTTagCompound nbt = itemStack.stackTagCompound;
				if (nbt == null) {
					nbt = new NBTTagCompound();
					nbt.setInteger("range", 0);
					itemStack.setTagCompound(nbt);
				} else {
					if (nbt.hasKey("range")) {
						nbt.setInteger("range", nbt.getInteger("range") >= ConfigLoader.loliPickaxeMaxRange ? 0
								: nbt.getInteger("range") + 1);
					} else {
						nbt.setInteger("range", 1);
					}
				}
				IChatComponent ic = new ChatComponentTranslation("loliPickaxe.range",
						new Object[] { String.valueOf(1 + 2 * nbt.getInteger("range")) });
				player.addChatMessage(ic);
				player.worldObj.playSoundAtEntity(player, "anotherstar:block.lolisuccess", 1.0F, 1.0F);
			}
		}
		return itemStack;
	}

	@Override
	public int getDamage(ItemStack stack) {
		return 0;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
		super.addInformation(stack, player, list, flag);
		list.add("AnotherStar专属");
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
		if (ConfigLoader.loliPickaxeDropProtectTime <= 0) {
			return true;
		}
		NBTTagCompound nbt = item.stackTagCompound;
		if (nbt == null) {
			nbt = new NBTTagCompound();
			nbt.setLong("preDropTime", System.currentTimeMillis());
			item.setTagCompound(nbt);
			return false;
		} else {
			if (nbt.hasKey("preDropTime")) {
				long preDropTime = nbt.getLong("preDropTime");
				long curDropTime = System.currentTimeMillis();
				nbt.setLong("preDropTime", curDropTime);
				return curDropTime - preDropTime < ConfigLoader.loliPickaxeDropProtectTime;
			} else {
				nbt.setLong("preDropTime", System.currentTimeMillis());
				return false;
			}
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int p_77663_4_, boolean p_77663_5_) {
		super.onUpdate(stack, world, entity, p_77663_4_, p_77663_5_);
		if (entity instanceof EntityPlayer) {
			if (stack.hasTagCompound()) {
				NBTTagCompound nbt = stack.getTagCompound();
				if (!nbt.hasKey("Owner")) {
					nbt.setString("Owner", ((EntityPlayer) entity).getDisplayName());
				}
			} else {
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setString("Owner", ((EntityPlayer) entity).getDisplayName());
				stack.setTagCompound(nbt);
			}
		}
	}

	@Override
	public String getOwner(ItemStack stack) {
		if (stack.hasTagCompound()) {
			NBTTagCompound nbt = stack.getTagCompound();
			if (nbt.hasKey("Owner")) {
				return nbt.getString("Owner");
			}
		}
		return "";
	}

	public static boolean invHaveLoliPickaxe(EntityPlayer player) {
		if (player.inventory != null) {
			boolean hasLoli = false;
			for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
				ItemStack stack = player.inventory.getStackInSlot(i);
				if (stack != null && stack.getItem() instanceof ILoli) {
					ILoli loli = (ILoli) stack.getItem();
					String owner = loli.getOwner(stack);
					if (!owner.isEmpty()) {
						if (owner.equals(player.getDisplayName())) {
							if (ConfigLoader.loliPickaxeDuration > 0) {
								player.hodeLoli = ConfigLoader.loliPickaxeDuration;
							}
							hasLoli = true;
						} else {
							player.func_146097_a(stack, true, false);
							player.inventory.setInventorySlotContents(i, null);
						}
					}
				}
			}
			return hasLoli || player.hodeLoli > 0;
		}
		return player.hodeLoli > 0;
	}

}
