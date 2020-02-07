package com.anotherstar.common.config;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import com.anotherstar.common.config.annotation.ConfigField;
import com.anotherstar.common.config.annotation.ConfigField.ConfigType;
import com.anotherstar.common.config.annotation.ConfigField.ValurType;
import com.anotherstar.common.item.tool.ILoli;
import com.anotherstar.network.LoliConfigPacket;
import com.anotherstar.network.NetworkHandler;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ConfigLoader {

	private static Configuration config;
	public static final List<String> flags = Lists.newArrayList();
	public static final List<String> commandFlags = Lists.newArrayList();
	public static final List<String> guiFlags = Lists.newArrayList();
	public static final Map<String, ConfigField> flagAnnotations = Maps.newHashMap();
	public static final Map<String, Field> flagFields = Maps.newHashMap();
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "最大采掘范围", valueType = ValurType.INT, intDefaultValue = 5)
	public static int loliPickaxeMaxRange;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "强制掉落方块", valueType = ValurType.BOOLEAN, booleanDefaultValue = true)
	public static boolean loliPickaxeMandatoryDrop;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "显示流体边框", valueType = ValurType.BOOLEAN, booleanDefaultValue = false)
	public static boolean loliPickaxeStopOnLiquid;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "挖掘距离", valueType = ValurType.DOUBLE, doubleDefaultValue = 0.0, doubleMinValue = 0, doubleMaxValueField = "loliPickaxeBlockReachMaxDistance")
	public static double loliPickaxeBlockReachDistance;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "最大挖掘距离", valueType = ValurType.DOUBLE, doubleDefaultValue = 20.0)
	public static double loliPickaxeBlockReachMaxDistance;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "储藏室最大页数", valueType = ValurType.INT, intDefaultValue = 100)
	public static int loliPickaxeMaxPage;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "储藏室取消物品堆叠限制", valueType = ValurType.BOOLEAN, booleanDefaultValue = true)
	public static boolean loliPickaxeCancelStackLimit;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "储藏室最大堆叠数", valueType = ValurType.INT, intDefaultValue = 2000000000)
	public static int loliPickaxeSlotStackLimit;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "自动收纳进储藏室", valueType = ValurType.BOOLEAN, booleanDefaultValue = true)
	public static boolean loliPickaxeAutoAccept;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "反伤", valueType = ValurType.BOOLEAN, booleanDefaultValue = true)
	public static boolean loliPickaxeThorns;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "潜行右键杀死周围实体", valueType = ValurType.BOOLEAN, booleanDefaultValue = true)
	public static boolean loliPickaxeKillRangeEntity;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "潜行右键杀死周围实体的范围", valueType = ValurType.INT, intDefaultValue = 50, intMinValue = 0, intMaxValueField = "loliPickaxeMaxKillRange")
	public static int loliPickaxeKillRange;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "潜行右键杀死周围实体的最大范围", valueType = ValurType.INT, intDefaultValue = 100)
	public static int loliPickaxeMaxKillRange;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "自动杀死周围实体", valueType = ValurType.BOOLEAN, booleanDefaultValue = false)
	public static boolean loliPickaxeAutoKillRangeEntity;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "自动杀死周围实体的范围", valueType = ValurType.INT, intDefaultValue = 5, intMinValue = 0, intMaxValueField = "loliPickaxeMaxAutoKillRange")
	public static int loliPickaxeAutoKillRange;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "自动杀死周围实体的最大范围", valueType = ValurType.INT, intDefaultValue = 10)
	public static int loliPickaxeMaxAutoKillRange;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "效果持续时间(Tick)", valueType = ValurType.INT, intDefaultValue = 200)
	public static int loliPickaxeDuration;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "丢弃保护时间(ms)", valueType = ValurType.INT, intDefaultValue = 200)
	public static int loliPickaxeDropProtectTime;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "强制清除生物", valueType = ValurType.BOOLEAN, booleanDefaultValue = true)
	public static boolean loliPickaxeCompulsoryRemove;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "范围攻击对非怪物有效", valueType = ValurType.BOOLEAN, booleanDefaultValue = true)
	public static boolean loliPickaxeValidToAmityEntity;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "对全部实体有效", valueType = ValurType.BOOLEAN, booleanDefaultValue = false)
	public static boolean loliPickaxeValidToAllEntity;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "清空玩家背包", valueType = ValurType.BOOLEAN, booleanDefaultValue = false)
	public static boolean loliPickaxeClearInventory;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "缴械", valueType = ValurType.BOOLEAN, booleanDefaultValue = false)
	public static boolean loliPickaxeDropItems;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "踢出玩家", valueType = ValurType.BOOLEAN, booleanDefaultValue = false)
	public static boolean loliPickaxeKickPlayer;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "踢出玩家消息", valueType = ValurType.STRING, stringDefaultValue = "你被氪金萝莉踢出了服务器")
	public static String loliPickaxeKickMessage;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "禁止死亡实体触发实体更新事件", valueType = ValurType.BOOLEAN, booleanDefaultValue = false)
	public static boolean loliPickaxeForbidOnLivingUpdate;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "伊邪那美(需同时开启踢出玩家)", valueType = ValurType.BOOLEAN, booleanDefaultValue = false, warning = true)
	public static boolean loliPickaxeReincarnation;
	@ConfigField(type = { ConfigType.CONFIG }, comment = "伊邪那美玩家列表", valueType = ValurType.LIST, listDefaultValue = {})
	public static List<String> loliPickaxeReincarnationPlayerList;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "灵魂超度", valueType = ValurType.BOOLEAN, booleanDefaultValue = false)
	public static boolean loliPickaxeBeyondRedemption;
	@ConfigField(type = { ConfigType.CONFIG }, comment = "灵魂超度玩家列表", valueType = ValurType.LIST, listDefaultValue = {})
	public static List<String> loliPickaxeBeyondRedemptionPlayerList;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "寻找所有者", valueType = ValurType.BOOLEAN, booleanDefaultValue = true)
	public static boolean loliPickaxeFindOwner;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "寻找所有者范围", valueType = ValurType.INT, intDefaultValue = 50)
	public static int loliPickaxeFindOwnerRange;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "蓝屏打击", valueType = ValurType.BOOLEAN, booleanDefaultValue = false, warning = true)
	public static boolean loliPickaxeBlueScreenAttack;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "蹦溃打击", valueType = ValurType.BOOLEAN, booleanDefaultValue = false, warning = true)
	public static boolean loliPickaxeExitAttack;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "未响应打击", valueType = ValurType.BOOLEAN, booleanDefaultValue = false, warning = true)
	public static boolean loliPickaxeFailRespondAttack;
	@ConfigField(type = { ConfigType.CONFIG }, comment = "强制死亡延迟特化列表(实体ID:Tick)", valueType = ValurType.MAP, mapDefaultValue = { "ender_dragon:::201" }, mapKeyType = ValurType.STRING, mapValueType = ValurType.INT)
	public static Map<String, Integer> loliPickaxeDelayRemoveList;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "左键范围攻击", valueType = ValurType.BOOLEAN, booleanDefaultValue = true)
	public static boolean loliPickaxeKillFacing;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "范围攻击范围", valueType = ValurType.INT, intDefaultValue = 50, intMinValue = 0, intMaxValueField = "loliPickaxeMaxKillFacingRange")
	public static int loliPickaxeKillFacingRange;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "范围攻击最大范围", valueType = ValurType.INT, intDefaultValue = 200)
	public static int loliPickaxeMaxKillFacingRange;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "范围攻击斜率", valueType = ValurType.DOUBLE, doubleDefaultValue = 0.1, doubleMinValue = 0, doubleMaxValueField = "loliPickaxeMaxKillFacingSlope")
	public static double loliPickaxeKillFacingSlope;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "范围攻击最大斜率", valueType = ValurType.DOUBLE, doubleDefaultValue = 1.0)
	public static double loliPickaxeMaxKillFacingSlope;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "视觉迷惑", valueType = ValurType.BOOLEAN, booleanDefaultValue = false)
	public static boolean loliPickaxeInvisible;
	@ConfigField(type = { ConfigType.CONFIG }, comment = "GUI可修改选项", valueType = ValurType.LIST, listType = ValurType.STRING, listDefaultValue = { "loliPickaxeMandatoryDrop", "loliPickaxeStopOnLiquid", "loliPickaxeBlockReachDistance", "loliPickaxeAutoAccept", "loliPickaxeThorns", "loliPickaxeKillRangeEntity", "loliPickaxeKillRange", "loliPickaxeAutoKillRangeEntity", "loliPickaxeAutoKillRange", "loliPickaxeCompulsoryRemove", "loliPickaxeValidToAmityEntity", "loliPickaxeValidToAllEntity", "loliPickaxeClearInventory", "loliPickaxeDropItems", "loliPickaxeKickPlayer", "loliPickaxeKickMessage", "loliPickaxeReincarnation", "loliPickaxeBeyondRedemption", "loliPickaxeBlueScreenAttack", "loliPickaxeExitAttack", "loliPickaxeFailRespondAttack", "loliPickaxeKillFacing", "loliPickaxeKillFacingRange", "loliPickaxeKillFacingSlope", "loliPickaxeInfiniteBattery", "loliPickaxeInvisible" }, warning = true, warningMethod = "guiChangeListWarning")
	public static List<String> loliPickaxeGuiChangeList;
	@ConfigField(type = {}, comment = "额外唱片列表(声音:唱片名:唱片ID)", valueType = ValurType.LIST, listType = ValurType.STRING, listDefaultValue = { "lolirecord:loliRecord:loli_record" })
	public static List<String> loliRecodeNames;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "萝莉卡片掉落概率", valueType = ValurType.DOUBLE, doubleDefaultValue = 0.1)
	public static double loliCardDropProbability;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "萝莉卡片掉落概率", valueType = ValurType.DOUBLE, doubleDefaultValue = 0.01)
	public static double loliCardAlbumDropProbability;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "萝莉唱片掉落概率", valueType = ValurType.DOUBLE, doubleDefaultValue = 0.001)
	public static double loliRecordDropProbability;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "生物灵魂掉落概率", valueType = ValurType.DOUBLE, doubleDefaultValue = 0.01)
	public static double entitySoulDropProbability;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "萝莉行走速度", valueType = ValurType.DOUBLE, doubleDefaultValue = 1.0)
	public static double loliSpeed;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "萝莉主动攻击", valueType = ValurType.BOOLEAN, booleanDefaultValue = true)
	public static boolean loliAttack;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "萝莉瞬移", valueType = ValurType.BOOLEAN, booleanDefaultValue = true)
	public static boolean loliTeleport;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "萝莉模型(0:萝莉,1:纳文摩尔,2:纸片人,3:车万女仆)", valueType = ValurType.INT, intDefaultValue = 0)
	public static int loliModelType;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "萝莉模型ID", valueType = ValurType.STRING, stringDefaultValue = "touhou_little_maid:remilia_scarlet")
	public static String loliModelId;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "萝莉卡片展示框缩放比例", valueType = ValurType.DOUBLE, doubleDefaultValue = 1.0)
	public static double loliCardScale;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "萝莉卡片册切换速度", valueType = ValurType.INT, intDefaultValue = 100)
	public static int loliCardAlbumSwitchSpeed;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "萝莉卡片渲染展示框", valueType = ValurType.BOOLEAN, booleanDefaultValue = false)
	public static boolean loliCardRenderFrame;
	@ConfigField(type = { ConfigType.CONFIG }, comment = "附魔最大等级列表", valueType = ValurType.MAP, mapDefaultValue = {}, mapKeyType = ValurType.STRING, mapValueType = ValurType.INT)
	public static Map<String, Integer> loliPickaxeEnchantmentLimit;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "默认附魔最大等级", valueType = ValurType.INT, intDefaultValue = 32)
	public static int loliPickaxeEnchantmentDefaultLimit;
	@ConfigField(type = { ConfigType.CONFIG }, comment = "药水最大等级列表", valueType = ValurType.MAP, mapDefaultValue = {}, mapKeyType = ValurType.STRING, mapValueType = ValurType.INT)
	public static Map<String, Integer> loliPickaxePotionLimit;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "默认药水最大等级", valueType = ValurType.INT, intDefaultValue = 32)
	public static int loliPickaxePotionDefaultLimit;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "启用特效攻击炸弹", valueType = ValurType.BOOLEAN, booleanDefaultValue = false, warning = true)
	public static boolean loliEnableBuffAttackTNT;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND, ConfigType.GUI }, comment = "超级电池", valueType = ValurType.BOOLEAN, booleanDefaultValue = true)
	public static boolean loliPickaxeInfiniteBattery;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "跨世界传送", valueType = ValurType.BOOLEAN, booleanDefaultValue = true)
	public static boolean loliPickaxeSpaceFolding;
	@ConfigField(type = { ConfigType.CONFIG }, comment = "跨世界传送黑名单", valueType = ValurType.LIST, listType = ValurType.INT, listDefaultValue = {})
	public static List<Integer> loliPickaxeWorldBlacklist;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND }, comment = "传送最远距离", valueType = ValurType.DOUBLE, doubleDefaultValue = 512.0)
	public static double loliPickaxeMaxTeleportDistance;
	@ConfigField(type = { ConfigType.CONFIG }, comment = "萝莉卡片URL", valueType = ValurType.MAP, mapDefaultValue = { "gk_head_portrait.png:::https://www.pixiv.net/artworks/61282195", "小莫女儿:::https://www.pixiv.net/users/5776001" }, mapKeyType = ValurType.STRING, mapValueType = ValurType.STRING)
	public static Map<String, String> loliCardURL;
	@ConfigField(type = { ConfigType.CONFIG }, comment = "创造模式物品栏默认网络卡片", valueType = ValurType.LIST, listType = ValurType.STRING, listDefaultValue = { "https://bigimg.cheerfun.dev/get/https://i.pximg.net/img-original/img/2017/03/18/03/44/39/61965296_p0.png", "https://bigimg.cheerfun.dev/get/https://i.pximg.net/img-original/img/2015/10/23/18/05/06/53170539_p0.jpg", "https://bigimg.cheerfun.dev/get/https://i.pximg.net/img-original/img/2015/09/27/07/15/20/52735806_p0.jpg" })
	public static List<String> loliCardOnlineDefURL;

	static {
		try {
			Field[] fields = ConfigLoader.class.getFields();
			for (Field field : fields) {
				if (field.isAnnotationPresent(ConfigField.class)) {
					ConfigField annotation = field.getAnnotation(ConfigField.class);
					flags.add(field.getName());
					flagAnnotations.put(field.getName(), annotation);
					flagFields.put(field.getName(), field);
					ConfigType[] types = annotation.type();
					for (ConfigType type : types) {
						switch (type) {
						case COMMAND:
							commandFlags.add(field.getName());
							break;
						case GUI:
							guiFlags.add(field.getName());
							break;
						default:
							break;
						}
					}
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	public static void init(FMLPreInitializationEvent event) {
		config = new Configuration(event.getSuggestedConfigurationFile());
		load(false);
	}

	public static void load(boolean reload) {
		config.load();
		try {
			for (String flag : flags) {
				Field field = flagFields.get(flag);
				ConfigField annotation = flagAnnotations.get(flag);
				if (reload) {
					boolean canReload = false;
					for (ConfigType type : annotation.type()) {
						if (type == ConfigType.CONFIG) {
							canReload = true;
							break;
						}
					}
					if (!canReload) {
						continue;
					}
				}
				switch (annotation.valueType()) {
				case INT:
					field.setInt(null, config.get(Configuration.CATEGORY_GENERAL, field.getName(), annotation.intDefaultValue(), annotation.comment()).getInt());
					break;
				case DOUBLE:
					field.setDouble(null, config.get(Configuration.CATEGORY_GENERAL, field.getName(), annotation.doubleDefaultValue(), annotation.comment()).getDouble());
					break;
				case BOOLEAN:
					field.setBoolean(null, config.get(Configuration.CATEGORY_GENERAL, field.getName(), annotation.booleanDefaultValue(), annotation.comment()).getBoolean());
					break;
				case STRING:
					field.set(null, config.get(Configuration.CATEGORY_GENERAL, field.getName(), annotation.stringDefaultValue(), annotation.comment()).getString());
					break;
				case LIST: {
					String[] strs = config.get(Configuration.CATEGORY_GENERAL, field.getName(), annotation.listDefaultValue(), annotation.comment()).getStringList();
					List list = Lists.newArrayList();
					for (String str : strs) {
						switch (annotation.listType()) {
						case INT:
							list.add(Integer.parseInt(str));
							break;
						case DOUBLE:
							list.add(Double.parseDouble(str));
							break;
						case BOOLEAN:
							list.add(Boolean.parseBoolean(str));
							break;
						case STRING:
							list.add(str);
							break;
						default:
							continue;
						}
					}
					field.set(null, list);
					break;
				}
				case MAP: {
					String[] strs = config.get(Configuration.CATEGORY_GENERAL, field.getName(), annotation.mapDefaultValue(), annotation.comment()).getStringList();
					Map map = Maps.newHashMap();
					for (String str : strs) {
						int index = str.lastIndexOf(":::");
						Object key;
						switch (annotation.mapKeyType()) {
						case INT:
							key = Integer.parseInt(str.substring(0, index));
							break;
						case DOUBLE:
							key = Double.parseDouble(str.substring(0, index));
							break;
						case BOOLEAN:
							key = Boolean.parseBoolean(str.substring(0, index));
							break;
						case STRING:
							key = str.substring(0, index);
							break;
						default:
							continue;
						}
						Object value;
						switch (annotation.mapValueType()) {
						case INT:
							value = Integer.parseInt(str.substring(index + 3, str.length()));
							break;
						case DOUBLE:
							value = Double.parseDouble(str.substring(index + 3, str.length()));
							break;
						case BOOLEAN:
							value = Boolean.parseBoolean(str.substring(index + 3, str.length()));
							break;
						case STRING:
							value = str.substring(index + 3, str.length());
							break;
						default:
							continue;
						}
						map.put(key, value);
					}
					field.set(null, map);
					break;
				}
				default:
					break;
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		config.save();
	}

	public static void save() {
		try {
			for (String flag : flags) {
				Field field = flagFields.get(flag);
				ConfigField annotation = flagAnnotations.get(flag);
				boolean canSave = false;
				for (ConfigType type : annotation.type()) {
					if (type == ConfigType.CONFIG) {
						canSave = true;
						break;
					}
				}
				if (!canSave) {
					continue;
				}
				switch (annotation.valueType()) {
				case INT:
					config.get(Configuration.CATEGORY_GENERAL, field.getName(), annotation.intDefaultValue(), annotation.comment()).setValue(field.getInt(null));
					break;
				case DOUBLE:
					config.get(Configuration.CATEGORY_GENERAL, field.getName(), annotation.doubleDefaultValue(), annotation.comment()).setValue(field.getDouble(null));
					break;
				case BOOLEAN:
					config.get(Configuration.CATEGORY_GENERAL, field.getName(), annotation.booleanDefaultValue(), annotation.comment()).setValue(field.getBoolean(null));
					break;
				case STRING:
					config.get(Configuration.CATEGORY_GENERAL, field.getName(), annotation.stringDefaultValue(), annotation.comment()).setValue((String) field.get(null));
					break;
				case LIST: {
					config.get(Configuration.CATEGORY_GENERAL, field.getName(), annotation.listDefaultValue(), annotation.comment()).setValues(((List<?>) field.get(null)).stream().map(element -> element.toString()).toArray(String[]::new));
					break;
				}
				case MAP: {
					config.get(Configuration.CATEGORY_GENERAL, field.getName(), annotation.mapDefaultValue(), annotation.comment()).setValues(((Map<?, ?>) field.get(null)).entrySet().stream().map(entry -> entry.getKey().toString() + ":::" + entry.getValue().toString()).toArray(String[]::new));
					break;
				}
				default:
					break;
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		config.save();
	}

	public static void addPlayerToReincarnation(EntityPlayer player) {
		addPlayerToReincarnation(player.getUniqueID().toString());
	}

	public static void addPlayerToReincarnation(String uuid) {
		loliPickaxeReincarnationPlayerList.add(uuid);
		config.get(Configuration.CATEGORY_GENERAL, "loliPickaxeReincarnationPlayerList", new String[0], "伊邪那美玩家列表").setValues(loliPickaxeReincarnationPlayerList.toArray(new String[0]));
		config.save();
	}

	public static void addPlayerToBeyondRedemption(EntityPlayer player) {
		addPlayerToBeyondRedemption(player.getUniqueID().toString());
	}

	public static void addPlayerToBeyondRedemption(String uuid) {
		loliPickaxeBeyondRedemptionPlayerList.add(uuid);
		config.get(Configuration.CATEGORY_GENERAL, "loliPickaxeBeyondRedemptionPlayerList", new String[0], "万劫不复玩家列表").setValues(loliPickaxeBeyondRedemptionPlayerList.toArray(new String[0]));
		config.save();
	}

	public static void sandChange(EntityPlayerMP player) {
		try {
			NBTTagCompound data = new NBTTagCompound();
			for (String flag : flags) {
				Field field = flagFields.get(flag);
				ConfigField annotation = flagAnnotations.get(flag);
				switch (annotation.valueType()) {
				case INT:
					data.setInteger(field.getName(), field.getInt(null));
					break;
				case DOUBLE:
					data.setDouble(field.getName(), field.getDouble(null));
					break;
				case BOOLEAN:
					data.setBoolean(field.getName(), field.getBoolean(null));
					break;
				case STRING:
					data.setString(field.getName(), (String) field.get(null));
					break;
				case LIST: {
					NBTTagList list = new NBTTagList();
					for (Object element : (List<?>) field.get(null)) {
						list.appendTag(new NBTTagString(element.toString()));
					}
					data.setTag(field.getName(), list);
					break;
				}
				case MAP: {
					NBTTagList list = new NBTTagList();
					for (Map.Entry entry : ((Map<?, ?>) field.get(null)).entrySet()) {
						list.appendTag(new NBTTagString(entry.getKey().toString() + ":::" + entry.getValue().toString()));
					}
					data.setTag(field.getName(), list);
					break;
				}
				default:
					break;
				}
			}
			if (player == null) {
				NetworkHandler.INSTANCE.sendMessageToAll(new LoliConfigPacket(data));
			} else {
				NetworkHandler.INSTANCE.sendMessageToPlayer(new LoliConfigPacket(data), player);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@SideOnly(Side.CLIENT)
	public static void receptionChange(NBTTagCompound data) {
		try {
			for (String flag : flags) {
				Field field = flagFields.get(flag);
				ConfigField annotation = flagAnnotations.get(flag);
				switch (annotation.valueType()) {
				case INT:
					field.setInt(null, data.getInteger(field.getName()));
					if (annotation.warning()) {
						boolean success = false;
						if (!annotation.warningMethod().isEmpty()) {
							try {
								ConfigLoader.class.getMethod(annotation.warningMethod()).invoke(null);
								success = true;
							} catch (NoSuchMethodException e) {
							} catch (SecurityException e) {
							} catch (InvocationTargetException e) {
							}
						}
						if (!success) {
							Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation("config.worning.int", annotation.comment(), data.getInteger(field.getName())));
						}
					}
					break;
				case DOUBLE:
					field.setDouble(null, data.getDouble(field.getName()));
					if (annotation.warning()) {
						boolean success = false;
						if (!annotation.warningMethod().isEmpty()) {
							try {
								ConfigLoader.class.getMethod(annotation.warningMethod()).invoke(null);
								success = true;
							} catch (NoSuchMethodException e) {
							} catch (SecurityException e) {
							} catch (InvocationTargetException e) {
							}
						}
						if (!success) {
							Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation("config.worning.double", annotation.comment(), data.getDouble(field.getName())));
						}
					}
					break;
				case BOOLEAN:
					field.setBoolean(null, data.getBoolean(field.getName()));
					if (annotation.warning()) {
						boolean success = false;
						if (!annotation.warningMethod().isEmpty()) {
							try {
								ConfigLoader.class.getMethod(annotation.warningMethod()).invoke(null);
								success = true;
							} catch (NoSuchMethodException e) {
							} catch (SecurityException e) {
							} catch (InvocationTargetException e) {
							}
						}
						if (!success) {
							if (data.getBoolean(field.getName())) {
								Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation("config.worning.boolean.enable", annotation.comment()));
							} else {
								Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation("config.worning.boolean.disable", annotation.comment()));
							}
						}
					}
					break;
				case STRING:
					field.set(null, data.getString(field.getName()));
					if (annotation.warning()) {
						boolean success = false;
						if (!annotation.warningMethod().isEmpty()) {
							try {
								ConfigLoader.class.getMethod(annotation.warningMethod()).invoke(null);
								success = true;
							} catch (NoSuchMethodException e) {
							} catch (SecurityException e) {
							} catch (InvocationTargetException e) {
							}
						}
						if (!success) {
							Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation("config.worning.string", annotation.comment(), data.getString(field.getName())));
						}
					}
					break;
				case LIST: {
					NBTTagList nbtlist = data.getTagList(field.getName(), 8);
					List list = Lists.newArrayList();
					for (NBTBase nbt : nbtlist) {
						switch (annotation.listType()) {
						case INT:
							list.add(Integer.parseInt(((NBTTagString) nbt).getString()));
							break;
						case DOUBLE:
							list.add(Double.parseDouble(((NBTTagString) nbt).getString()));
							break;
						case BOOLEAN:
							list.add(Boolean.parseBoolean(((NBTTagString) nbt).getString()));
							break;
						case STRING:
							list.add(((NBTTagString) nbt).getString());
							break;
						default:
							continue;
						}
					}
					field.set(null, list);
					if (annotation.warning()) {
						boolean success = false;
						if (!annotation.warningMethod().isEmpty()) {
							try {
								ConfigLoader.class.getMethod(annotation.warningMethod()).invoke(null);
								success = true;
							} catch (NoSuchMethodException e) {
							} catch (SecurityException e) {
							} catch (InvocationTargetException e) {
							}
						}
						if (!success) {
							Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation("config.worning.list", annotation.comment()));
							for (Object element : list) {
								Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation("config.worning.list.element", element.toString()));
							}
						}
					}
					break;
				}
				case MAP: {
					NBTTagList list = data.getTagList(field.getName(), 8);
					Map map = Maps.newHashMap();
					for (NBTBase nbt : list) {
						String str = ((NBTTagString) nbt).getString();
						int index = str.lastIndexOf(":::");
						Object key;
						switch (annotation.mapKeyType()) {
						case INT:
							key = Integer.parseInt(str.substring(0, index));
							break;
						case DOUBLE:
							key = Double.parseDouble(str.substring(0, index));
							break;
						case BOOLEAN:
							key = Boolean.parseBoolean(str.substring(0, index));
							break;
						case STRING:
							key = str.substring(0, index);
							break;
						default:
							continue;
						}
						Object value;
						switch (annotation.mapValueType()) {
						case INT:
							value = Integer.parseInt(str.substring(index + 3, str.length()));
							break;
						case DOUBLE:
							value = Double.parseDouble(str.substring(index + 3, str.length()));
							break;
						case BOOLEAN:
							value = Boolean.parseBoolean(str.substring(index + 3, str.length()));
							break;
						case STRING:
							value = str.substring(index + 3, str.length());
							break;
						default:
							continue;
						}
						map.put(key, value);
					}
					field.set(null, map);
					if (annotation.warning()) {
						boolean success = false;
						if (!annotation.warningMethod().isEmpty()) {
							try {
								ConfigLoader.class.getMethod(annotation.warningMethod()).invoke(null);
								success = true;
							} catch (NoSuchMethodException e) {
							} catch (SecurityException e) {
							} catch (InvocationTargetException e) {
							}
						}
						if (!success) {
							Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation("config.worning.map", annotation.comment()));
							for (NBTBase nbt : list) {
								String element = ((NBTTagString) nbt).getString();
								Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation("config.worning.map.element", element));
							}
						}
					}
					break;
				}
				default:
					break;
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public static int getInt(ItemStack stack, String flag) {
		if (flags.contains(flag)) {
			ConfigField annotation = flagAnnotations.get(flag);
			if (annotation.valueType() == ValurType.INT) {
				if (guiFlags.contains(flag) && loliPickaxeGuiChangeList.contains(flag) && !stack.isEmpty() && stack.getItem() instanceof ILoli && stack.hasTagCompound() && stack.getTagCompound().hasKey(ILoli.CONFIG) && stack.getTagCompound().getCompoundTag(ILoli.CONFIG).hasKey(flag)) {
					int result = stack.getTagCompound().getCompoundTag(ILoli.CONFIG).getInteger(flag);
					int max;
					if (flags.contains(annotation.intMaxValueField())) {
						max = getInt(stack, annotation.intMaxValueField());
					} else {
						max = annotation.intMaxValue();
					}
					int min;
					if (flags.contains(annotation.intMinValueField())) {
						min = getInt(stack, annotation.intMinValueField());
					} else {
						min = annotation.intMinValue();
					}
					return MathHelper.clamp(stack.getTagCompound().getCompoundTag(ILoli.CONFIG).getInteger(flag), min, max);
				} else {
					try {
						return flagFields.get(flag).getInt(null);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return 0;
	}

	public static double getDouble(ItemStack stack, String flag) {
		if (flags.contains(flag)) {
			ConfigField annotation = flagAnnotations.get(flag);
			if (annotation.valueType() == ValurType.DOUBLE) {
				if (guiFlags.contains(flag) && loliPickaxeGuiChangeList.contains(flag) && !stack.isEmpty() && stack.getItem() instanceof ILoli && stack.hasTagCompound() && stack.getTagCompound().hasKey(ILoli.CONFIG) && stack.getTagCompound().getCompoundTag(ILoli.CONFIG).hasKey(flag)) {
					double result = stack.getTagCompound().getCompoundTag(ILoli.CONFIG).getDouble(flag);
					double max;
					if (flags.contains(annotation.doubleMaxValueField())) {
						max = getDouble(stack, annotation.doubleMaxValueField());
					} else {
						max = annotation.doubleMaxValue();
					}
					double min;
					if (flags.contains(annotation.doubleMinValueField())) {
						min = getDouble(stack, annotation.doubleMinValueField());
					} else {
						min = annotation.doubleMinValue();
					}
					return MathHelper.clamp(stack.getTagCompound().getCompoundTag(ILoli.CONFIG).getDouble(flag), min, max);
				} else {
					try {
						return flagFields.get(flag).getDouble(null);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return 0;
	}

	public static boolean getBoolean(ItemStack stack, String flag) {
		if (flags.contains(flag)) {
			ConfigField annotation = flagAnnotations.get(flag);
			if (annotation.valueType() == ValurType.BOOLEAN) {
				if (guiFlags.contains(flag) && loliPickaxeGuiChangeList.contains(flag) && !stack.isEmpty() && stack.getItem() instanceof ILoli && stack.hasTagCompound() && stack.getTagCompound().hasKey(ILoli.CONFIG) && stack.getTagCompound().getCompoundTag(ILoli.CONFIG).hasKey(flag)) {
					return stack.getTagCompound().getCompoundTag(ILoli.CONFIG).getBoolean(flag);
				} else {
					try {
						return flagFields.get(flag).getBoolean(null);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return false;
	}

	public static String getString(ItemStack stack, String flag) {
		if (flags.contains(flag)) {
			ConfigField annotation = flagAnnotations.get(flag);
			if (annotation.valueType() == ValurType.STRING) {
				if (guiFlags.contains(flag) && loliPickaxeGuiChangeList.contains(flag) && !stack.isEmpty() && stack.getItem() instanceof ILoli && stack.hasTagCompound() && stack.getTagCompound().hasKey(ILoli.CONFIG) && stack.getTagCompound().getCompoundTag(ILoli.CONFIG).hasKey(flag)) {
					return stack.getTagCompound().getCompoundTag(ILoli.CONFIG).getString(flag);
				} else {
					try {
						return (String) flagFields.get(flag).get(null);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

	public static NBTTagCompound getItemConfigs(ItemStack stack) {
		if (!stack.isEmpty() && stack.getItem() instanceof ILoli && stack.hasTagCompound() && stack.getTagCompound().hasKey(ILoli.CONFIG)) {
			return stack.getTagCompound().getCompoundTag(ILoli.CONFIG);
		}
		return null;
	}

	public static void setInt(ItemStack stack, String flag, int value) {
		if (flags.contains(flag)) {
			ConfigField annotation = flagAnnotations.get(flag);
			if (annotation.valueType() == ValurType.INT) {
				if (guiFlags.contains(flag) && loliPickaxeGuiChangeList.contains(flag) && !stack.isEmpty() && stack.getItem() instanceof ILoli) {
					NBTTagCompound nbt;
					if (!stack.hasTagCompound()) {
						nbt = new NBTTagCompound();
						stack.setTagCompound(nbt);
					} else {
						nbt = stack.getTagCompound();
					}
					NBTTagCompound stackFlags;
					if (nbt.hasKey(ILoli.CONFIG)) {
						stackFlags = nbt.getCompoundTag(ILoli.CONFIG);
					} else {
						stackFlags = new NBTTagCompound();
						nbt.setTag(ILoli.CONFIG, stackFlags);
					}
					int max;
					if (flags.contains(annotation.intMaxValueField())) {
						max = getInt(stack, annotation.intMaxValueField());
					} else {
						max = annotation.intMaxValue();
					}
					int min;
					if (flags.contains(annotation.intMinValueField())) {
						min = getInt(stack, annotation.intMinValueField());
					} else {
						min = annotation.intMinValue();
					}
					stackFlags.setInteger(flag, MathHelper.clamp(value, min, max));
				}
			}
		}
	}

	public static void setDouble(ItemStack stack, String flag, double value) {
		if (flags.contains(flag)) {
			ConfigField annotation = flagAnnotations.get(flag);
			if (annotation.valueType() == ValurType.DOUBLE) {
				if (guiFlags.contains(flag) && loliPickaxeGuiChangeList.contains(flag) && !stack.isEmpty() && stack.getItem() instanceof ILoli) {
					NBTTagCompound nbt;
					if (!stack.hasTagCompound()) {
						nbt = new NBTTagCompound();
						stack.setTagCompound(nbt);
					} else {
						nbt = stack.getTagCompound();
					}
					NBTTagCompound stackFlags;
					if (nbt.hasKey(ILoli.CONFIG)) {
						stackFlags = nbt.getCompoundTag(ILoli.CONFIG);
					} else {
						stackFlags = new NBTTagCompound();
						nbt.setTag(ILoli.CONFIG, stackFlags);
					}
					double max;
					if (flags.contains(annotation.doubleMaxValueField())) {
						max = getDouble(stack, annotation.doubleMaxValueField());
					} else {
						max = annotation.doubleMaxValue();
					}
					double min;
					if (flags.contains(annotation.doubleMinValueField())) {
						min = getDouble(stack, annotation.doubleMinValueField());
					} else {
						min = annotation.doubleMinValue();
					}

					stackFlags.setDouble(flag, MathHelper.clamp(value, min, max));
				}
			}
		}
	}

	public static void setBoolean(ItemStack stack, String flag, boolean value) {
		if (flags.contains(flag)) {
			ConfigField annotation = flagAnnotations.get(flag);
			if (annotation.valueType() == ValurType.BOOLEAN) {
				if (guiFlags.contains(flag) && loliPickaxeGuiChangeList.contains(flag) && !stack.isEmpty() && stack.getItem() instanceof ILoli) {
					NBTTagCompound nbt;
					if (!stack.hasTagCompound()) {
						nbt = new NBTTagCompound();
						stack.setTagCompound(nbt);
					} else {
						nbt = stack.getTagCompound();
					}
					NBTTagCompound stackFlags;
					if (nbt.hasKey(ILoli.CONFIG)) {
						stackFlags = nbt.getCompoundTag(ILoli.CONFIG);
					} else {
						stackFlags = new NBTTagCompound();
						nbt.setTag(ILoli.CONFIG, stackFlags);
					}
					stackFlags.setBoolean(flag, value);
				}
			}
		}
	}

	public static void setString(ItemStack stack, String flag, String value) {
		if (flags.contains(flag)) {
			ConfigField annotation = flagAnnotations.get(flag);
			if (annotation.valueType() == ValurType.STRING) {
				if (guiFlags.contains(flag) && loliPickaxeGuiChangeList.contains(flag) && !stack.isEmpty() && stack.getItem() instanceof ILoli) {
					NBTTagCompound nbt;
					if (!stack.hasTagCompound()) {
						nbt = new NBTTagCompound();
						stack.setTagCompound(nbt);
					} else {
						nbt = stack.getTagCompound();
					}
					NBTTagCompound stackFlags;
					if (nbt.hasKey(ILoli.CONFIG)) {
						stackFlags = nbt.getCompoundTag(ILoli.CONFIG);
					} else {
						stackFlags = new NBTTagCompound();
						nbt.setTag(ILoli.CONFIG, stackFlags);
					}
					stackFlags.setString(flag, value);
				}
			}
		}
	}

	public static void setItemConfigs(ItemStack stack, NBTTagCompound config) {
		if (!stack.isEmpty() && stack.getItem() instanceof ILoli) {
			if (!stack.hasTagCompound()) {
				stack.setTagCompound(new NBTTagCompound());
			}
			if (config == null) {
				stack.getTagCompound().removeTag(ILoli.CONFIG);
			} else {
				stack.getTagCompound().setTag(ILoli.CONFIG, config);
			}
		}
	}

	public static void guiChangeListWarning() {
		boolean title = false;
		for (String element : loliPickaxeGuiChangeList) {
			ConfigField annotation = flagAnnotations.get(element);
			if (annotation.warning()) {
				if (!title) {
					Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation("config.worning.guiChangeList"));
					title = true;
				}
				Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation("config.worning.guiChangeList.element", annotation.comment()));
			}
		}
	}

}
