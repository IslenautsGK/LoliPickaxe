package com.anotherstar.common.config;

import java.lang.reflect.Field;
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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ConfigLoader {

	private static Configuration config;
	public static final List<String> flags = Lists.newArrayList();
	public static final List<String> commandFlags = Lists.newArrayList();
	public static final List<String> guiFlags = Lists.newArrayList();
	public static final Map<String, ConfigField> flagAnnotations = Maps.newHashMap();
	public static final Map<String, Field> flagFields = Maps.newHashMap();
	@ConfigField(type = { ConfigType.CONFIG,
			ConfigType.COMMAND }, comment = "最大采掘范围", valueType = ValurType.INT, intDefaultValue = 5)
	public static int loliPickaxeMaxRange;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND,
			ConfigType.GUI }, comment = "强制掉落方块", valueType = ValurType.BOOLEAN, booleanDefaultValue = true)
	public static boolean loliPickaxeMandatoryDrop;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND,
			ConfigType.GUI }, comment = "反伤", valueType = ValurType.BOOLEAN, booleanDefaultValue = true)
	public static boolean loliPickaxeThorns;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND,
			ConfigType.GUI }, comment = "潜行右键杀死周围实体", valueType = ValurType.BOOLEAN, booleanDefaultValue = true)
	public static boolean loliPickaxeKillRangeEntity;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND,
			ConfigType.GUI }, comment = "潜行右键杀死周围实体的范围", valueType = ValurType.INT, intDefaultValue = 50)
	public static int loliPickaxeKillRange;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND,
			ConfigType.GUI }, comment = "自动杀死周围实体", valueType = ValurType.BOOLEAN, booleanDefaultValue = false)
	public static boolean loliPickaxeAutoKillRangeEntity;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND,
			ConfigType.GUI }, comment = "自动杀死周围实体的范围", valueType = ValurType.INT, intDefaultValue = 5)
	public static int loliPickaxeAutoKillRange;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND,
			ConfigType.GUI }, comment = "效果持续时间(Tick)", valueType = ValurType.INT, intDefaultValue = 200)
	public static int loliPickaxeDuration;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND,
			ConfigType.GUI }, comment = "丢弃保护时间(ms)", valueType = ValurType.INT, intDefaultValue = 200)
	public static int loliPickaxeDropProtectTime;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND,
			ConfigType.GUI }, comment = "强制清除生物", valueType = ValurType.BOOLEAN, booleanDefaultValue = true)
	public static boolean loliPickaxeCompulsoryRemove;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND,
			ConfigType.GUI }, comment = "范围攻击对非怪物有效", valueType = ValurType.BOOLEAN, booleanDefaultValue = true)
	public static boolean loliPickaxeValidToAmityEntity;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND,
			ConfigType.GUI }, comment = "对全部实体有效", valueType = ValurType.BOOLEAN, booleanDefaultValue = false)
	public static boolean loliPickaxeValidToAllEntity;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND,
			ConfigType.GUI }, comment = "清空玩家背包", valueType = ValurType.BOOLEAN, booleanDefaultValue = false)
	public static boolean loliPickaxeClearInventory;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND,
			ConfigType.GUI }, comment = "缴械", valueType = ValurType.BOOLEAN, booleanDefaultValue = false)
	public static boolean loliPickaxeDropItems;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND,
			ConfigType.GUI }, comment = "踢出玩家", valueType = ValurType.BOOLEAN, booleanDefaultValue = false)
	public static boolean loliPickaxeKickPlayer;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND,
			ConfigType.GUI }, comment = "踢出玩家消息", valueType = ValurType.STRING, stringDefaultValue = "你被氪金萝莉踢出了服务器")
	public static String loliPickaxeKickMessage;
	@ConfigField(type = { ConfigType.CONFIG,
			ConfigType.COMMAND }, comment = "禁止死亡实体触发实体更新事件", valueType = ValurType.BOOLEAN, booleanDefaultValue = false)
	public static boolean loliPickaxeForbidOnLivingUpdate;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND,
			ConfigType.GUI }, comment = "伊邪那美(需同时开启踢出玩家)", valueType = ValurType.BOOLEAN, booleanDefaultValue = false)
	public static boolean loliPickaxeReincarnation;
	@ConfigField(type = { ConfigType.CONFIG }, comment = "伊邪那美玩家列表", valueType = ValurType.LIST, listDefaultValue = {})
	public static List<String> loliPickaxeReincarnationPlayerList;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND,
			ConfigType.GUI }, comment = "灵魂超度", valueType = ValurType.BOOLEAN, booleanDefaultValue = false)
	public static boolean loliPickaxeBeyondRedemption;
	@ConfigField(type = { ConfigType.CONFIG }, comment = "灵魂超度玩家列表", valueType = ValurType.LIST, listDefaultValue = {})
	public static List<String> loliPickaxeBeyondRedemptionPlayerList;
	@ConfigField(type = { ConfigType.CONFIG,
			ConfigType.COMMAND }, comment = "寻找所有者", valueType = ValurType.BOOLEAN, booleanDefaultValue = true)
	public static boolean loliPickaxeFindOwner;
	@ConfigField(type = { ConfigType.CONFIG,
			ConfigType.COMMAND }, comment = "寻找所有者范围", valueType = ValurType.INT, intDefaultValue = 50)
	public static int loliPickaxeFindOwnerRange;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND,
			ConfigType.GUI }, comment = "蓝屏打击", valueType = ValurType.BOOLEAN, booleanDefaultValue = false)
	public static boolean loliPickaxeBlueScreenAttack;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND,
			ConfigType.GUI }, comment = "崩溃打击", valueType = ValurType.BOOLEAN, booleanDefaultValue = false)
	public static boolean loliPickaxeExitAttack;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND,
			ConfigType.GUI }, comment = "未响应打击", valueType = ValurType.BOOLEAN, booleanDefaultValue = false)
	public static boolean loliPickaxeFailRespondAttack;
	@ConfigField(type = {
			ConfigType.CONFIG }, comment = "强制死亡延迟特化列表(实体ID:Tick)", valueType = ValurType.MAP, mapDefaultValue = {
					"ender_dragon:201" }, mapKeyType = ValurType.STRING, mapValueType = ValurType.INT)
	public static Map<String, Integer> loliPickaxeDelayRemoveList;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND,
			ConfigType.GUI }, comment = "左键范围攻击", valueType = ValurType.BOOLEAN, booleanDefaultValue = true)
	public static boolean loliPickaxeKillFacing;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND,
			ConfigType.GUI }, comment = "范围攻击范围", valueType = ValurType.INT, intDefaultValue = 100)
	public static int loliPickaxeKillFacingRange;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND,
			ConfigType.GUI }, comment = "范围攻击斜率", valueType = ValurType.DOUBLE, doubleDefaultValue = 0.1)
	public static double loliPickaxeKillFacingSlope;
	@ConfigField(type = { ConfigType.CONFIG }, comment = "GUI可修改选项", valueType = ValurType.LIST, listDefaultValue = {
			"loliPickaxeMandatoryDrop", "loliPickaxeThorns", "loliPickaxeKillRangeEntity", "loliPickaxeKillRange",
			"loliPickaxeAutoKillRangeEntity", "loliPickaxeAutoKillRange", "loliPickaxeDuration",
			"loliPickaxeDropProtectTime", "loliPickaxeCompulsoryRemove", "loliPickaxeValidToAmityEntity",
			"loliPickaxeValidToAllEntity", "loliPickaxeClearInventory", "loliPickaxeDropItems", "loliPickaxeKickPlayer",
			"loliPickaxeKickMessage", "loliPickaxeReincarnation", "loliPickaxeBeyondRedemption",
			"loliPickaxeBlueScreenAttack", "loliPickaxeExitAttack", "loliPickaxeFailRespondAttack",
			"loliPickaxeKillFacing", "loliPickaxeKillFacingRange", "loliPickaxeKillFacingSlope",
			"loliPickaxeAutoAccept" })
	public static List<String> loliPickaxeGuiChangeList;
	@ConfigField(type = {}, comment = "额外唱片列表(声音:唱片名:唱片ID)", valueType = ValurType.LIST, listDefaultValue = {
			"lolirecord:loliRecord:loli_record" })
	public static List<String> loliRecodeNames;
	@ConfigField(type = { ConfigType.CONFIG,
			ConfigType.COMMAND }, comment = "萝莉卡片掉落概率", valueType = ValurType.DOUBLE, doubleDefaultValue = 0.1)
	public static double loliCardDropProbability;
	@ConfigField(type = { ConfigType.CONFIG,
			ConfigType.COMMAND }, comment = "萝莉唱片掉落概率", valueType = ValurType.DOUBLE, doubleDefaultValue = 0.001)
	public static double loliRecordDropProbability;
	@ConfigField(type = { ConfigType.CONFIG,
			ConfigType.COMMAND }, comment = "储藏室最大页数", valueType = ValurType.INT, intDefaultValue = 50)
	public static int loliPickaxeMaxPage;
	@ConfigField(type = { ConfigType.CONFIG, ConfigType.COMMAND,
			ConfigType.GUI }, comment = "自动收纳进储藏室", valueType = ValurType.BOOLEAN, booleanDefaultValue = true)
	public static boolean loliPickaxeAutoAccept;

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
					field.setInt(null, config.get(Configuration.CATEGORY_GENERAL, field.getName(),
							annotation.intDefaultValue(), annotation.comment()).getInt());
					break;
				case DOUBLE:
					field.setDouble(null, config.get(Configuration.CATEGORY_GENERAL, field.getName(),
							annotation.doubleDefaultValue(), annotation.comment()).getDouble());
					break;
				case BOOLEAN:
					field.setBoolean(null, config.get(Configuration.CATEGORY_GENERAL, field.getName(),
							annotation.booleanDefaultValue(), annotation.comment()).getBoolean());
					break;
				case STRING:
					field.set(null, config.get(Configuration.CATEGORY_GENERAL, field.getName(),
							annotation.stringDefaultValue(), annotation.comment()).getString());
					break;
				case LIST: {
					String[] strs = config.get(Configuration.CATEGORY_GENERAL, field.getName(),
							annotation.listDefaultValue(), annotation.comment()).getStringList();
					List<String> list = Lists.newArrayList();
					for (String str : strs) {
						list.add(str);
					}
					field.set(null, list);
					break;
				}
				case MAP: {
					String[] strs = config.get(Configuration.CATEGORY_GENERAL, field.getName(),
							annotation.mapDefaultValue(), annotation.comment()).getStringList();
					Map map = Maps.newHashMap();
					for (String str : strs) {
						int index = str.lastIndexOf(':');
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
							value = Integer.parseInt(str.substring(index + 1, str.length()));
							break;
						case DOUBLE:
							value = Double.parseDouble(str.substring(index + 1, str.length()));
							break;
						case BOOLEAN:
							value = Boolean.parseBoolean(str.substring(index + 1, str.length()));
							break;
						case STRING:
							value = str.substring(index + 1, str.length());
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
					config.get(Configuration.CATEGORY_GENERAL, field.getName(), annotation.intDefaultValue(),
							annotation.comment()).setValue(field.getInt(null));
					break;
				case DOUBLE:
					config.get(Configuration.CATEGORY_GENERAL, field.getName(), annotation.doubleDefaultValue(),
							annotation.comment()).setValue(field.getDouble(null));
					break;
				case BOOLEAN:
					config.get(Configuration.CATEGORY_GENERAL, field.getName(), annotation.booleanDefaultValue(),
							annotation.comment()).setValue(field.getBoolean(null));
					break;
				case STRING:
					config.get(Configuration.CATEGORY_GENERAL, field.getName(), annotation.stringDefaultValue(),
							annotation.comment()).setValue((String) field.get(null));
					break;
				case LIST: {
					config.get(Configuration.CATEGORY_GENERAL, field.getName(), annotation.listDefaultValue(),
							annotation.comment()).setValues(((List<String>) field.get(null)).toArray(new String[0]));
					break;
				}
				case MAP: {
					config.get(Configuration.CATEGORY_GENERAL, field.getName(), annotation.mapDefaultValue(),
							annotation.comment())
							.setValues(((Map<?, ?>) field.get(null)).entrySet().stream()
									.map(entry -> entry.getKey().toString() + ":" + entry.getValue().toString())
									.toArray(String[]::new));
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
					for (String str : (List<String>) field.get(null)) {
						list.appendTag(new NBTTagString(str));
					}
					data.setTag(field.getName(), list);
					break;
				}
				case MAP: {
					NBTTagList list = new NBTTagList();
					for (Map.Entry entry : ((Map<?, ?>) field.get(null)).entrySet()) {
						list.appendTag(new NBTTagString(entry.getKey().toString() + ":" + entry.getValue().toString()));
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

	public static void receptionChange(NBTTagCompound data) {
		try {
			for (String flag : flags) {
				Field field = flagFields.get(flag);
				ConfigField annotation = flagAnnotations.get(flag);
				switch (annotation.valueType()) {
				case INT:
					field.setInt(null, data.getInteger(field.getName()));
					break;
				case DOUBLE:
					field.setDouble(null, data.getDouble(field.getName()));
					break;
				case BOOLEAN:
					field.setBoolean(null, data.getBoolean(field.getName()));
					break;
				case STRING:
					field.set(null, data.getString(field.getName()));
					break;
				case LIST: {
					NBTTagList nbtlist = data.getTagList(field.getName(), 8);
					List<String> list = Lists.newArrayList();
					for (NBTBase nbt : nbtlist) {
						list.add(((NBTTagString) nbt).getString());
					}
					field.set(null, list);
					break;
				}
				case MAP: {
					NBTTagList list = data.getTagList(field.getName(), 8);
					Map map = Maps.newHashMap();
					for (NBTBase nbt : list) {
						String str = ((NBTTagString) nbt).getString();
						int index = str.lastIndexOf(':');
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
							value = Integer.parseInt(str.substring(index + 1, str.length()));
							break;
						case DOUBLE:
							value = Double.parseDouble(str.substring(index + 1, str.length()));
							break;
						case BOOLEAN:
							value = Boolean.parseBoolean(str.substring(index + 1, str.length()));
							break;
						case STRING:
							value = str.substring(index + 1, str.length());
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
	}

	public static int getInt(ItemStack stack, String flag) {
		if (flags.contains(flag)) {
			ConfigField annotation = flagAnnotations.get(flag);
			if (annotation.valueType() == ValurType.INT) {
				if (loliPickaxeGuiChangeList.contains(flag) && !stack.isEmpty() && stack.getItem() instanceof ILoli
						&& stack.hasTagCompound() && stack.getTagCompound().hasKey(ILoli.CONFIG)
						&& stack.getTagCompound().getCompoundTag(ILoli.CONFIG).hasKey(flag)) {
					return stack.getTagCompound().getCompoundTag(ILoli.CONFIG).getInteger(flag);
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
				if (loliPickaxeGuiChangeList.contains(flag) && !stack.isEmpty() && stack.getItem() instanceof ILoli
						&& stack.hasTagCompound() && stack.getTagCompound().hasKey(ILoli.CONFIG)
						&& stack.getTagCompound().getCompoundTag(ILoli.CONFIG).hasKey(flag)) {
					NBTTagCompound stackFlags = stack.getTagCompound().getCompoundTag(ILoli.CONFIG);
					return stack.getTagCompound().getCompoundTag(ILoli.CONFIG).getDouble(flag);
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
				if (loliPickaxeGuiChangeList.contains(flag) && !stack.isEmpty() && stack.getItem() instanceof ILoli
						&& stack.hasTagCompound() && stack.getTagCompound().hasKey(ILoli.CONFIG)
						&& stack.getTagCompound().getCompoundTag(ILoli.CONFIG).hasKey(flag)) {
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
				if (loliPickaxeGuiChangeList.contains(flag) && !stack.isEmpty() && stack.getItem() instanceof ILoli
						&& stack.hasTagCompound() && stack.getTagCompound().hasKey(ILoli.CONFIG)
						&& stack.getTagCompound().getCompoundTag(ILoli.CONFIG).hasKey(flag)) {
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

	public static void setInt(ItemStack stack, String flag, int value) {
		if (flags.contains(flag)) {
			ConfigField annotation = flagAnnotations.get(flag);
			if (annotation.valueType() == ValurType.INT) {
				if (loliPickaxeGuiChangeList.contains(flag) && !stack.isEmpty() && stack.getItem() instanceof ILoli) {
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
					stackFlags.setInteger(flag, value);
				}
			}
		}
	}

	public static void setDouble(ItemStack stack, String flag, double value) {
		if (flags.contains(flag)) {
			ConfigField annotation = flagAnnotations.get(flag);
			if (annotation.valueType() == ValurType.DOUBLE) {
				if (loliPickaxeGuiChangeList.contains(flag) && !stack.isEmpty() && stack.getItem() instanceof ILoli) {
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
					stackFlags.setDouble(flag, value);
				}
			}
		}
	}

	public static void setBoolean(ItemStack stack, String flag, boolean value) {
		if (flags.contains(flag)) {
			ConfigField annotation = flagAnnotations.get(flag);
			if (annotation.valueType() == ValurType.BOOLEAN) {
				if (loliPickaxeGuiChangeList.contains(flag) && !stack.isEmpty() && stack.getItem() instanceof ILoli) {
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
				if (loliPickaxeGuiChangeList.contains(flag) && !stack.isEmpty() && stack.getItem() instanceof ILoli) {
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

}
