package com.anotherstar.common.item.tool;

import java.util.List;

import javax.annotation.Nullable;

import com.anotherstar.common.LoliPickaxe;
import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.common.entity.IEntityLoli;
import com.anotherstar.common.gui.ILoliInventory;
import com.anotherstar.common.gui.InventoryLoliPickaxe;
import com.anotherstar.util.LoliPickaxeUtil;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemLoliPickaxe extends ItemPickaxe implements ILoli {

	public static final Item.ToolMaterial LOLI = EnumHelper.addToolMaterial("LOLI", 32, 0, 0, 0, 0);

	public ItemLoliPickaxe() {
		super(LOLI);
		this.setUnlocalizedName("loliPickaxe");
		this.setCreativeTab(LoliPickaxe.instance.loliTabs);
	}

	@Override
	public void setDamage(ItemStack stack, int damage) {
		super.setDamage(stack, 0);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		return 0.0F;
	}

	@Override
	public int getEntityLifespan(ItemStack itemStack, World world) {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean canHarvestBlock(IBlockState blockIn) {
		return false;
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return false;
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		return leftClickEntity(player, entity);
	}

	public boolean leftClickEntity(EntityLivingBase loli, Entity entity) {
		if (!entity.world.isRemote && (loli instanceof EntityPlayer || loli instanceof IEntityLoli)) {
			ItemStack stack = loli.getHeldItemMainhand();
			boolean success = false;
			if (entity instanceof EntityPlayer) {
				LoliPickaxeUtil.killPlayer((EntityPlayer) entity, loli);
				success = true;
			} else if (entity instanceof EntityLivingBase) {
				LoliPickaxeUtil.killEntityLiving((EntityLivingBase) entity, loli);
				success = true;
			} else if (ConfigLoader.getBoolean(stack, "loliPickaxeValidToAllEntity") && !(entity instanceof EntityLivingBase)) {
				LoliPickaxeUtil.killEntity(entity);
				success = true;
			}
			if (ConfigLoader.getBoolean(stack, "loliPickaxeKillFacing")) {
				LoliPickaxeUtil.killFacing(loli);
				success = true;
			}
			if (success && loli instanceof EntityPlayerMP) {
				BlockPos pos = loli.getPosition();
				((EntityPlayerMP) loli).connection.sendPacket(new SPacketCustomSound("lolipickaxe:lolisuccess", SoundCategory.BLOCKS, pos.getX(), pos.getY(), pos.getZ(), 1.0F, 1.0F));
			}
			return success;
		}
		return false;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (!world.isRemote) {
			if (player.isSneaking()) {
				if (ConfigLoader.getBoolean(stack, "loliPickaxeKillRangeEntity")) {
					int range = ConfigLoader.getInt(stack, "loliPickaxeKillRange");
					int count = LoliPickaxeUtil.killRangeEntity(world, player, range);
					player.sendMessage(new TextComponentTranslation("loliPickaxe.killrangeentity", range * 2, count));
					if (player instanceof EntityPlayerMP) {
						BlockPos pos = player.getPosition();
						((EntityPlayerMP) player).connection.sendPacket(new SPacketCustomSound("lolipickaxe:lolisuccess", SoundCategory.BLOCKS, pos.getX(), pos.getY(), pos.getZ(), 1.0F, 1.0F));
					}
				}
			} else {
				NBTTagCompound nbt = stack.getTagCompound();
				if (nbt == null) {
					nbt = new NBTTagCompound();
					nbt.setInteger("range", 0);
					stack.setTagCompound(nbt);
				} else {
					if (nbt.hasKey("range")) {
						nbt.setInteger("range", nbt.getInteger("range") >= ConfigLoader.loliPickaxeMaxRange ? 0 : nbt.getInteger("range") + 1);
					} else {
						nbt.setInteger("range", 1);
					}
				}
				ITextComponent message = new TextComponentTranslation("loliPickaxe.range", 1 + 2 * nbt.getInteger("range"));
				player.sendMessage(message);
				if (player instanceof EntityPlayerMP) {
					BlockPos pos = player.getPosition();
					((EntityPlayerMP) player).connection.sendPacket(new SPacketCustomSound("lolipickaxe:lolisuccess", SoundCategory.BLOCKS, pos.getX(), pos.getY(), pos.getZ(), 1.0F, 1.0F));
				}
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public int getDamage(ItemStack stack) {
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		super.addInformation(stack, world, tooltip, flag);
		tooltip.add("已在GitHub上开源");
		tooltip.add(I18n.format("loliPickaxe.curRange", 1 + 2 * getRange(stack)));
		if (ConfigLoader.getBoolean(stack, "loliPickaxeMandatoryDrop")) {
			tooltip.add(I18n.format("loliPickaxe.mandatoryDrop"));
		}
		if (ConfigLoader.getBoolean(stack, "loliPickaxeAutoAccept")) {
			tooltip.add(I18n.format("loliPickaxe.autoAccept"));
		}
		if (ConfigLoader.getBoolean(stack, "loliPickaxeThorns")) {
			tooltip.add(I18n.format("loliPickaxe.thorns"));
		}
		if (ConfigLoader.getBoolean(stack, "loliPickaxeKillRangeEntity")) {
			tooltip.add(I18n.format("loliPickaxe.killRange", 2 * ConfigLoader.getInt(stack, "loliPickaxeKillRange")));
		}
		if (ConfigLoader.getBoolean(stack, "loliPickaxeAutoKillRangeEntity")) {
			tooltip.add(I18n.format("loliPickaxe.autoKillRange", 2 * ConfigLoader.getInt(stack, "loliPickaxeAutoKillRange")));
		}
		if (ConfigLoader.getBoolean(stack, "loliPickaxeCompulsoryRemove")) {
			tooltip.add(I18n.format("loliPickaxe.compulsoryRemove"));
		}
		if (ConfigLoader.getBoolean(stack, "loliPickaxeValidToAmityEntity")) {
			tooltip.add(I18n.format("loliPickaxe.validToAmityEntity"));
		}
		if (ConfigLoader.getBoolean(stack, "loliPickaxeValidToAllEntity")) {
			tooltip.add(I18n.format("loliPickaxe.validToAllEntity"));
		}
		if (ConfigLoader.getBoolean(stack, "loliPickaxeClearInventory")) {
			tooltip.add(I18n.format("loliPickaxe.clearInventory"));
		}
		if (ConfigLoader.getBoolean(stack, "loliPickaxeDropItems")) {
			tooltip.add(I18n.format("loliPickaxe.dropItems"));
		}
		if (ConfigLoader.getBoolean(stack, "loliPickaxeKickPlayer")) {
			tooltip.add(I18n.format("loliPickaxe.kickPlayer"));
		}
		if (ConfigLoader.getBoolean(stack, "loliPickaxeReincarnation")) {
			tooltip.add(I18n.format("loliPickaxe.reincarnation"));
		}
		if (ConfigLoader.getBoolean(stack, "loliPickaxeBeyondRedemption")) {
			tooltip.add(I18n.format("loliPickaxe.beyondRedemption"));
		}
		if (ConfigLoader.getBoolean(stack, "loliPickaxeBlueScreenAttack")) {
			tooltip.add(I18n.format("loliPickaxe.blueScreenAttack"));
		}
		if (ConfigLoader.getBoolean(stack, "loliPickaxeExitAttack")) {
			tooltip.add(I18n.format("loliPickaxe.exitAttack"));
		}
		if (ConfigLoader.getBoolean(stack, "loliPickaxeFailRespondAttack")) {
			tooltip.add(I18n.format("loliPickaxe.failRespondAttack"));
		}
		if (ConfigLoader.getBoolean(stack, "loliPickaxeKillFacing")) {
			tooltip.add(I18n.format("loliPickaxe.killFacing", ConfigLoader.getInt(stack, "loliPickaxeKillFacingRange"), ConfigLoader.getDouble(stack, "loliPickaxeKillFacingSlope")));
		}
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack stack, EntityPlayer player) {
		int time = ConfigLoader.loliPickaxeDropProtectTime;
		if (time <= 0) {
			return true;
		}
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null) {
			nbt = new NBTTagCompound();
			nbt.setLong("preDropTime", System.currentTimeMillis());
			stack.setTagCompound(nbt);
			return false;
		} else {
			if (nbt.hasKey("preDropTime")) {
				long preDropTime = nbt.getLong("preDropTime");
				long curDropTime = System.currentTimeMillis();
				nbt.setLong("preDropTime", curDropTime);
				return curDropTime - preDropTime < time;
			} else {
				nbt.setLong("preDropTime", System.currentTimeMillis());
				return false;
			}
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		super.onUpdate(stack, world, entity, itemSlot, isSelected);
		if (entity instanceof EntityPlayer) {
			NBTTagCompound nbt;
			if (stack.hasTagCompound()) {
				nbt = stack.getTagCompound();
			} else {
				nbt = new NBTTagCompound();
				stack.setTagCompound(nbt);
			}
			if (!nbt.hasKey("Owner")) {
				nbt.setString("Owner", ((EntityPlayer) entity).getName());
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

	@Override
	public int getRange(ItemStack stack) {
		int range = 1;
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt != null && nbt.hasKey("range")) {
			range = nbt.getInteger("range");
		}
		return range;
	}

	@Override
	public boolean hasInventory(ItemStack stack) {
		return true;
	}

	@Override
	public ILoliInventory getInventory(ItemStack stack) {
		return new InventoryLoliPickaxe(stack);
	}

}
