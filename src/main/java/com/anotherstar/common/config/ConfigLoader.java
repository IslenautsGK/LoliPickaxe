package com.anotherstar.common.config;

import java.util.Set;

import org.apache.logging.log4j.Logger;

import com.anotherstar.common.AnotherStar;
import com.google.common.collect.Sets;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.config.Configuration;

public class ConfigLoader {

	private static Configuration config;
	private static Logger logger;
	public static int loliPickaxeMaxRange;
	public static boolean loliPickaxeMandatoryDrop;
	public static boolean loliPickaxeThorns;
	public static boolean loliPickaxeKillRangeEntity;
	public static int loliPickaxeKillRange;
	public static boolean loliPickaxeAutoKillRangeEntity;
	public static int loliPickaxeAutoKillRange;
	public static int loliPickaxeDuration;
	public static int loliPickaxeDropProtectTime;
	public static boolean loliPickaxeCompulsoryRemove;
	public static boolean loliPickaxeValidToAllEntity;
	public static boolean loliPickaxeClearInventory;
	public static boolean loliPickaxeDropItems;
	public static boolean loliPickaxeKickPlayer;
	public static String loliPickaxeKickMessage;
	public static boolean loliPickaxeForbidOnLivingUpdateChangeHealth;
	public static boolean loliPickaxeForbidOnLivingUpdate;
	public static boolean loliPickaxeReincarnation;
	public static Set<String> loliPickaxeReincarnationPlayerList;
	public static boolean loliPickaxeBeyondRedemption;
	public static Set<String> loliPickaxeBeyondRedemptionPlayerList;
	public static boolean loliPickaxeFindOwner;
	public static int loliPickaxeFindOwnerRange;

	public static void init(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		config = new Configuration(event.getSuggestedConfigurationFile());
		load();
	}

	public static void load() {
		logger.info("Started loading config. ");
		config.load();
		loliPickaxeMaxRange = config.get(Configuration.CATEGORY_GENERAL, "loliPickaxeMaxRange", 2, "氪金萝莉的最大采掘范围")
				.getInt();
		loliPickaxeMandatoryDrop = config
				.get(Configuration.CATEGORY_GENERAL, "loliPickaxeMandatoryDrop", false, "氪金萝莉是否会强制掉落方块").getBoolean();
		loliPickaxeThorns = config.get(Configuration.CATEGORY_GENERAL, "loliPickaxeThorns", true, "氪金萝莉是否会反伤")
				.getBoolean();
		loliPickaxeKillRangeEntity = config
				.get(Configuration.CATEGORY_GENERAL, "loliPickaxeKillRangeEntity", true, "氪金萝莉是否可以潜行右键杀死周围实体")
				.getBoolean();
		loliPickaxeKillRange = config.get(Configuration.CATEGORY_GENERAL, "loliPickaxeKillRange", 5, "氪金萝莉杀死周围实体的范围")
				.getInt();
		loliPickaxeAutoKillRangeEntity = config
				.get(Configuration.CATEGORY_GENERAL, "loliPickaxeAutoKillRangeEntity", false, "氪金萝莉是否自动杀死周围实体")
				.getBoolean();
		loliPickaxeAutoKillRange = config
				.get(Configuration.CATEGORY_GENERAL, "loliPickaxeAutoKillRange", 5, "氪金萝莉自动杀死周围实体的范围").getInt();
		loliPickaxeDuration = config.get(Configuration.CATEGORY_GENERAL, "loliPickaxeDuration", 200, "氪金萝莉效果持续时间(Tick)")
				.getInt();
		loliPickaxeDropProtectTime = config
				.get(Configuration.CATEGORY_GENERAL, "loliPickaxeDropProtectTime", 200, "氪金萝莉丢弃保护时间(ms)").getInt();
		loliPickaxeCompulsoryRemove = config
				.get(Configuration.CATEGORY_GENERAL, "loliPickaxeCompulsoryRemove", false, "氪金萝莉强制清除生物").getBoolean();
		loliPickaxeValidToAllEntity = config
				.get(Configuration.CATEGORY_GENERAL, "loliPickaxeValidToAllEntity", false, "氪金萝莉对全部实体有效").getBoolean();
		loliPickaxeClearInventory = config
				.get(Configuration.CATEGORY_GENERAL, "loliPickaxeClearInventory", false, "氪金萝莉清背包").getBoolean();
		loliPickaxeDropItems = config.get(Configuration.CATEGORY_GENERAL, "loliPickaxeDropItems", false, "氪金萝莉缴械")
				.getBoolean();
		loliPickaxeKickPlayer = config.get(Configuration.CATEGORY_GENERAL, "loliPickaxeKickPlayer", false, "氪金萝莉踢出玩家")
				.getBoolean();
		loliPickaxeKickMessage = config
				.get(Configuration.CATEGORY_GENERAL, "loliPickaxeKickMessage", "你被氪金萝莉踢出了服务器", "氪金萝莉踢出玩家消息")
				.getString();
		loliPickaxeForbidOnLivingUpdateChangeHealth = config.get(Configuration.CATEGORY_GENERAL,
				"loliPickaxeForbidOnLivingUpdateChangeHealth", false, "禁止实体更新事件修改生命值").getBoolean();
		loliPickaxeForbidOnLivingUpdate = config
				.get(Configuration.CATEGORY_GENERAL, "loliPickaxeForbidOnLivingUpdate", false, "禁止死亡实体触发实体更新事件")
				.getBoolean();
		loliPickaxeReincarnation = config
				.get(Configuration.CATEGORY_GENERAL, "loliPickaxeReincarnation", false, "氪金萝莉对玩家发动伊邪那美(需同时开启踢出玩家)")
				.getBoolean();
		String[] strs = config
				.get(Configuration.CATEGORY_GENERAL, "loliPickaxeReincarnationPlayerList", new String[0], "伊邪那美玩家列表")
				.getStringList();
		loliPickaxeReincarnationPlayerList = Sets.newHashSet();
		for (String str : strs) {
			loliPickaxeReincarnationPlayerList.add(str);
		}
		loliPickaxeBeyondRedemption = config
				.get(Configuration.CATEGORY_GENERAL, "loliPickaxeBeyondRedemption", false, "氪金萝莉使玩家万劫不复").getBoolean();
		strs = config
				.get(Configuration.CATEGORY_GENERAL, "loliPickaxeBeyondRedemptionPlayerList", new String[0], "万劫不复玩家列表")
				.getStringList();
		loliPickaxeBeyondRedemptionPlayerList = Sets.newHashSet();
		for (String str : strs) {
			loliPickaxeBeyondRedemptionPlayerList.add(str);
		}
		loliPickaxeFindOwner = config.get(Configuration.CATEGORY_GENERAL, "loliPickaxeFindOwner", true, "氪金萝莉是否自动寻找所有者")
				.getBoolean();
		loliPickaxeFindOwnerRange = config
				.get(Configuration.CATEGORY_GENERAL, "loliPickaxeFindOwnerRange", 10, "氪金萝莉自动寻找所有者范围").getInt();
		config.save();
		logger.info("Finished loading config. ");
	}

	public static void save() {
		config.get(Configuration.CATEGORY_GENERAL, "loliPickaxeMaxRange", 2, "氪金萝莉的最大采掘范围")
				.setValue(loliPickaxeMaxRange);
		config.get(Configuration.CATEGORY_GENERAL, "loliPickaxeMandatoryDrop", false, "氪金萝莉是否会强制掉落方块")
				.setValue(loliPickaxeMandatoryDrop);
		config.get(Configuration.CATEGORY_GENERAL, "loliPickaxeThorns", true, "氪金萝莉是否会反伤").setValue(loliPickaxeThorns);
		config.get(Configuration.CATEGORY_GENERAL, "loliPickaxeKillRangeEntity", true, "氪金萝莉是否可以潜行右键杀死周围实体")
				.setValue(loliPickaxeKillRangeEntity);
		config.get(Configuration.CATEGORY_GENERAL, "loliPickaxeKillRange", 5, "氪金萝莉杀死周围实体的范围")
				.setValue(loliPickaxeKillRange);
		config.get(Configuration.CATEGORY_GENERAL, "loliPickaxeAutoKillRangeEntity", false, "氪金萝莉是否自动杀死周围实体")
				.setValue(loliPickaxeAutoKillRangeEntity);
		config.get(Configuration.CATEGORY_GENERAL, "loliPickaxeAutoKillRange", 5, "氪金萝莉自动杀死周围实体的范围")
				.setValue(loliPickaxeAutoKillRange);
		config.get(Configuration.CATEGORY_GENERAL, "loliPickaxeDuration", 200, "氪金萝莉效果持续时间(Tick)")
				.setValue(loliPickaxeDuration);
		config.get(Configuration.CATEGORY_GENERAL, "loliPickaxeDropProtectTime", 200, "氪金萝莉丢弃保护时间(ms)")
				.setValue(loliPickaxeDropProtectTime);
		config.get(Configuration.CATEGORY_GENERAL, "loliPickaxeCompulsoryRemove", false, "氪金萝莉强制清除生物")
				.setValue(loliPickaxeCompulsoryRemove);
		config.get(Configuration.CATEGORY_GENERAL, "loliPickaxeValidToAllEntity", false, "氪金萝莉对全部实体有效")
				.setValue(loliPickaxeValidToAllEntity);
		config.get(Configuration.CATEGORY_GENERAL, "loliPickaxeClearInventory", false, "氪金萝莉清背包")
				.setValue(loliPickaxeClearInventory);
		config.get(Configuration.CATEGORY_GENERAL, "loliPickaxeDropItems", false, "氪金萝莉缴械")
				.setValue(loliPickaxeClearInventory);
		config.get(Configuration.CATEGORY_GENERAL, "loliPickaxeKickPlayer", false, "氪金萝莉踢出玩家")
				.setValue(loliPickaxeKickPlayer);
		config.get(Configuration.CATEGORY_GENERAL, "loliPickaxeKickMessage", "你被氪金萝莉踢出了服务器", "氪金萝莉踢出玩家消息")
				.setValue(loliPickaxeKickMessage);
		config.get(Configuration.CATEGORY_GENERAL, "loliPickaxeForbidOnLivingUpdateChangeHealth", false,
				"禁止实体更新事件修改生命值").setValue(loliPickaxeForbidOnLivingUpdateChangeHealth);
		config.get(Configuration.CATEGORY_GENERAL, "loliPickaxeForbidOnLivingUpdate", false, "禁止死亡实体触发实体更新事件")
				.setValue(loliPickaxeForbidOnLivingUpdate);
		config.get(Configuration.CATEGORY_GENERAL, "loliPickaxeReincarnation", false, "氪金萝莉对玩家发动伊邪那美(需同时开启踢出玩家)")
				.setValue(loliPickaxeReincarnation);
		config.get(Configuration.CATEGORY_GENERAL, "loliPickaxeReincarnationPlayerList", new String[0], "伊邪那美玩家列表")
				.setValues(loliPickaxeReincarnationPlayerList.toArray(new String[0]));
		config.get(Configuration.CATEGORY_GENERAL, "loliPickaxeBeyondRedemption", false, "氪金萝莉使玩家万劫不复")
				.setValue(loliPickaxeBeyondRedemption);
		config.get(Configuration.CATEGORY_GENERAL, "loliPickaxeBeyondRedemptionPlayerList", new String[0], "万劫不复玩家列表")
				.setValues(loliPickaxeBeyondRedemptionPlayerList.toArray(new String[0]));
		config.get(Configuration.CATEGORY_GENERAL, "loliPickaxeFindOwner", true, "氪金萝莉是否自动寻找所有者")
				.setValue(loliPickaxeFindOwner);
		config.get(Configuration.CATEGORY_GENERAL, "loliPickaxeFindOwnerRange", 10, "氪金萝莉自动寻找所有者范围")
				.setValue(loliPickaxeFindOwnerRange);
		config.save();
	}

	public static void addPlayerToReincarnation(EntityPlayer player) {
		addPlayerToReincarnation(player.getUniqueID().toString());
	}

	public static void addPlayerToReincarnation(String uuid) {
		loliPickaxeReincarnationPlayerList.add(uuid);
		config.get(Configuration.CATEGORY_GENERAL, "loliPickaxeReincarnationPlayerList", new String[0], "伊邪那美玩家列表")
				.setValues(loliPickaxeReincarnationPlayerList.toArray(new String[0]));
		config.save();
	}

	public static void addPlayerToBeyondRedemption(EntityPlayer player) {
		addPlayerToBeyondRedemption(player.getUniqueID().toString());
	}

	public static void addPlayerToBeyondRedemption(String uuid) {
		loliPickaxeBeyondRedemptionPlayerList.add(uuid);
		config.get(Configuration.CATEGORY_GENERAL, "loliPickaxeBeyondRedemptionPlayerList", new String[0], "万劫不复玩家列表")
				.setValues(loliPickaxeBeyondRedemptionPlayerList.toArray(new String[0]));
		config.save();
	}

	public static void sandChange(EntityPlayerMP player) {
		ByteBuf buffer = Unpooled.buffer();
		buffer.writeInt(loliPickaxeMaxRange);
		buffer.writeBoolean(loliPickaxeMandatoryDrop);
		buffer.writeBoolean(loliPickaxeThorns);
		buffer.writeBoolean(loliPickaxeKillRangeEntity);
		buffer.writeInt(loliPickaxeKillRange);
		buffer.writeBoolean(loliPickaxeAutoKillRangeEntity);
		buffer.writeInt(loliPickaxeAutoKillRange);
		buffer.writeInt(loliPickaxeDuration);
		buffer.writeInt(loliPickaxeDropProtectTime);
		buffer.writeBoolean(loliPickaxeCompulsoryRemove);
		buffer.writeBoolean(loliPickaxeValidToAllEntity);
		buffer.writeBoolean(loliPickaxeClearInventory);
		buffer.writeBoolean(loliPickaxeDropItems);
		buffer.writeBoolean(loliPickaxeKickPlayer);
		ByteBufUtils.writeUTF8String(buffer, loliPickaxeKickMessage);
		buffer.writeBoolean(loliPickaxeForbidOnLivingUpdateChangeHealth);
		buffer.writeBoolean(loliPickaxeForbidOnLivingUpdate);
		buffer.writeBoolean(loliPickaxeReincarnation);
		buffer.writeInt(loliPickaxeReincarnationPlayerList.size());
		for (String uuid : loliPickaxeReincarnationPlayerList) {
			ByteBufUtils.writeUTF8String(buffer, uuid);
		}
		buffer.writeBoolean(loliPickaxeBeyondRedemption);
		buffer.writeInt(loliPickaxeBeyondRedemptionPlayerList.size());
		for (String uuid : loliPickaxeBeyondRedemptionPlayerList) {
			ByteBufUtils.writeUTF8String(buffer, uuid);
		}
		if (player != null) {
			AnotherStar.loliConfigNetwork.sendTo(new FMLProxyPacket(buffer, "loliConfig"), player);
		} else {
			AnotherStar.loliConfigNetwork.sendToAll(new FMLProxyPacket(buffer, "loliConfig"));
		}
		buffer.writeBoolean(loliPickaxeFindOwner);
		buffer.writeInt(loliPickaxeFindOwnerRange);
	}

	public static void receptionChange(ByteBuf buffer) {
		loliPickaxeMaxRange = buffer.readInt();
		loliPickaxeMandatoryDrop = buffer.readBoolean();
		loliPickaxeThorns = buffer.readBoolean();
		loliPickaxeKillRangeEntity = buffer.readBoolean();
		loliPickaxeKillRange = buffer.readInt();
		loliPickaxeAutoKillRangeEntity = buffer.readBoolean();
		loliPickaxeAutoKillRange = buffer.readInt();
		loliPickaxeDuration = buffer.readInt();
		loliPickaxeDropProtectTime = buffer.readInt();
		loliPickaxeCompulsoryRemove = buffer.readBoolean();
		loliPickaxeValidToAllEntity = buffer.readBoolean();
		loliPickaxeClearInventory = buffer.readBoolean();
		loliPickaxeDropItems = buffer.readBoolean();
		loliPickaxeKickPlayer = buffer.readBoolean();
		loliPickaxeKickMessage = ByteBufUtils.readUTF8String(buffer);
		loliPickaxeForbidOnLivingUpdateChangeHealth = buffer.readBoolean();
		loliPickaxeForbidOnLivingUpdate = buffer.readBoolean();
		loliPickaxeReincarnation = buffer.readBoolean();
		int size = buffer.readInt();
		loliPickaxeReincarnationPlayerList.clear();
		for (int i = 0; i < size; i++) {
			loliPickaxeReincarnationPlayerList.add(ByteBufUtils.readUTF8String(buffer));
		}
		loliPickaxeBeyondRedemption = buffer.readBoolean();
		size = buffer.readInt();
		loliPickaxeBeyondRedemptionPlayerList.clear();
		for (int i = 0; i < size; i++) {
			loliPickaxeBeyondRedemptionPlayerList.add(ByteBufUtils.readUTF8String(buffer));
		}
		loliPickaxeFindOwner = buffer.readBoolean();
		loliPickaxeFindOwnerRange = buffer.readInt();
	}

	public static Logger logger() {
		return logger;
	}

}
