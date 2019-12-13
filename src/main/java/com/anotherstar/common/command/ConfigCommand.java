package com.anotherstar.common.command;

import java.util.Arrays;
import java.util.List;

import com.anotherstar.common.config.ConfigLoader;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;

public class ConfigCommand extends CommandBase {

	private static final String[] flags = { "loliPickaxeMaxRange", "loliPickaxeMandatoryDrop", "loliPickaxeThorns",
			"loliPickaxeKillRangeEntity", "loliPickaxeKillRange", "loliPickaxeAutoKillRangeEntity",
			"loliPickaxeAutoKillRange", "loliPickaxeDuration", "loliPickaxeDropProtectTime",
			"loliPickaxeCompulsoryRemove", "loliPickaxeValidToAllEntity", "loliPickaxeClearInventory",
			"loliPickaxeDropItems", "loliPickaxeKickPlayer", "loliPickaxeKickMessage",
			"loliPickaxeForbidOnLivingUpdate", "loliPickaxeReincarnation", "loliPickaxeBeyondRedemption",
			"loliPickaxeFindOwner", "loliPickaxeFindOwnerRange", "loliPickaxeBlueScreenAttack", "reload", "flagList" };

	@Override
	public String getCommandName() {
		return "loli";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "commands.loli.usage";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (args.length > 0 && args.length < 3) {
			if (args.length > 1) {
				switch (args[0]) {
				case "loliPickaxeMaxRange":
					ConfigLoader.loliPickaxeMaxRange = parseInt(sender, args[1]);
					break;
				case "loliPickaxeMandatoryDrop":
					ConfigLoader.loliPickaxeMandatoryDrop = parseBoolean(sender, args[1]);
					break;
				case "loliPickaxeThorns":
					ConfigLoader.loliPickaxeThorns = parseBoolean(sender, args[1]);
					break;
				case "loliPickaxeKillRangeEntity":
					ConfigLoader.loliPickaxeKillRangeEntity = parseBoolean(sender, args[1]);
					break;
				case "loliPickaxeKillRange":
					ConfigLoader.loliPickaxeKillRange = parseInt(sender, args[1]);
					break;
				case "loliPickaxeAutoKillRangeEntity":
					ConfigLoader.loliPickaxeAutoKillRangeEntity = parseBoolean(sender, args[1]);
					break;
				case "loliPickaxeAutoKillRange":
					ConfigLoader.loliPickaxeAutoKillRange = parseInt(sender, args[1]);
					break;
				case "loliPickaxeDuration":
					ConfigLoader.loliPickaxeDuration = parseInt(sender, args[1]);
					break;
				case "loliPickaxeDropProtectTime":
					ConfigLoader.loliPickaxeDropProtectTime = parseInt(sender, args[1]);
					break;
				case "loliPickaxeCompulsoryRemove":
					ConfigLoader.loliPickaxeCompulsoryRemove = parseBoolean(sender, args[1]);
					break;
				case "loliPickaxeValidToAllEntity":
					ConfigLoader.loliPickaxeValidToAllEntity = parseBoolean(sender, args[1]);
					break;
				case "loliPickaxeClearInventory":
					ConfigLoader.loliPickaxeClearInventory = parseBoolean(sender, args[1]);
					break;
				case "loliPickaxeDropItems":
					ConfigLoader.loliPickaxeDropItems = parseBoolean(sender, args[1]);
					break;
				case "loliPickaxeKickPlayer":
					ConfigLoader.loliPickaxeKickPlayer = parseBoolean(sender, args[1]);
					break;
				case "loliPickaxeKickMessage":
					ConfigLoader.loliPickaxeKickMessage = args[1];
					break;
				case "loliPickaxeForbidOnLivingUpdate":
					ConfigLoader.loliPickaxeForbidOnLivingUpdate = parseBoolean(sender, args[1]);
					break;
				case "loliPickaxeReincarnation":
					ConfigLoader.loliPickaxeReincarnation = parseBoolean(sender, args[1]);
					break;
				case "loliPickaxeBeyondRedemption":
					ConfigLoader.loliPickaxeBeyondRedemption = parseBoolean(sender, args[1]);
					break;
				case "loliPickaxeFindOwner":
					ConfigLoader.loliPickaxeFindOwner = parseBoolean(sender, args[1]);
					break;
				case "loliPickaxeFindOwnerRange":
					ConfigLoader.loliPickaxeFindOwnerRange = parseInt(sender, args[1]);
					break;
				case "loliPickaxeBlueScreenAttack":
					ConfigLoader.loliPickaxeBlueScreenAttack = parseBoolean(sender, args[1]);
					break;
				default:
					throw new WrongUsageException("commands.loli.usage");
				}
				ConfigLoader.save();
				if (!sender.getEntityWorld().isRemote) {
					ConfigLoader.sandChange(null);
				}
				sender.addChatMessage(new ChatComponentTranslation("commands.loli.set", args[0], args[1]));
			} else {
				if (args[0].equals("reload")) {
					ConfigLoader.load();
					if (!sender.getEntityWorld().isRemote) {
						ConfigLoader.sandChange(null);
					}
					sender.addChatMessage(new ChatComponentTranslation("commands.loli.reload"));
				} else if (args[0].equals("flagList")) {
					sender.addChatMessage(new ChatComponentText("loliPickaxeMaxRange:最大采掘范围"));
					sender.addChatMessage(new ChatComponentText("loliPickaxeMandatoryDrop:是否会强制掉落方块"));
					sender.addChatMessage(new ChatComponentText("loliPickaxeThorns:是否会反伤"));
					sender.addChatMessage(new ChatComponentText("loliPickaxeKillRangeEntity:是否可以潜行右键杀死周围实体"));
					sender.addChatMessage(new ChatComponentText("loliPickaxeKillRange:杀死周围实体的范围"));
					sender.addChatMessage(new ChatComponentText("loliPickaxeAutoKillRangeEntity:是否自动杀死周围实体"));
					sender.addChatMessage(new ChatComponentText("loliPickaxeAutoKillRange:自动杀死周围实体的范围"));
					sender.addChatMessage(new ChatComponentText("loliPickaxeDuration:效果持续时间(Tick)"));
					sender.addChatMessage(new ChatComponentText("loliPickaxeDropProtectTime:丢弃保护时间(ms)"));
					sender.addChatMessage(new ChatComponentText("loliPickaxeCompulsoryRemove:强制清除生物"));
					sender.addChatMessage(new ChatComponentText("loliPickaxeValidToAllEntity:致死效果对全部实体有效"));
					sender.addChatMessage(new ChatComponentText("loliPickaxeClearInventory:清除被攻击者的背包"));
					sender.addChatMessage(new ChatComponentText("loliPickaxeDropItems:强制死亡掉落"));
					sender.addChatMessage(new ChatComponentText("loliPickaxeKickPlayer:踢出玩家"));
					sender.addChatMessage(new ChatComponentText("loliPickaxeKickMessage:踢出玩家消息"));
					sender.addChatMessage(new ChatComponentText("loliPickaxeForbidOnLivingUpdate:禁止死亡实体触发实体更新事件"));
					sender.addChatMessage(new ChatComponentText("loliPickaxeReincarnation:对被攻击者发动伊邪那美(需同时开启踢出玩家)"));
					sender.addChatMessage(new ChatComponentText("loliPickaxeBeyondRedemption:对被攻击者发动灵魂超度"));
					sender.addChatMessage(new ChatComponentText("loliPickaxeFindOwner:是否自动寻找所有者"));
					sender.addChatMessage(new ChatComponentText("loliPickaxeFindOwnerRange:自动寻找所有者范围"));
					sender.addChatMessage(new ChatComponentText("loliPickaxeBlueScreenAttack:蓝屏打击"));
				} else {
					String value;
					switch (args[0]) {
					case "loliPickaxeMaxRange":
						value = String.valueOf(ConfigLoader.loliPickaxeMaxRange);
						break;
					case "loliPickaxeMandatoryDrop":
						value = String.valueOf(ConfigLoader.loliPickaxeMandatoryDrop);
						break;
					case "loliPickaxeThorns":
						value = String.valueOf(ConfigLoader.loliPickaxeThorns);
						break;
					case "loliPickaxeKillRangeEntity":
						value = String.valueOf(ConfigLoader.loliPickaxeKillRangeEntity);
						break;
					case "loliPickaxeKillRange":
						value = String.valueOf(ConfigLoader.loliPickaxeKillRange);
						break;
					case "loliPickaxeAutoKillRangeEntity":
						value = String.valueOf(ConfigLoader.loliPickaxeAutoKillRangeEntity);
						break;
					case "loliPickaxeAutoKillRange":
						value = String.valueOf(ConfigLoader.loliPickaxeAutoKillRange);
						break;
					case "loliPickaxeDuration":
						value = String.valueOf(ConfigLoader.loliPickaxeDuration);
						break;
					case "loliPickaxeDropProtectTime":
						value = String.valueOf(ConfigLoader.loliPickaxeDropProtectTime);
						break;
					case "loliPickaxeCompulsoryRemove":
						value = String.valueOf(ConfigLoader.loliPickaxeCompulsoryRemove);
						break;
					case "loliPickaxeValidToAllEntity":
						value = String.valueOf(ConfigLoader.loliPickaxeValidToAllEntity);
						break;
					case "loliPickaxeClearInventory":
						value = String.valueOf(ConfigLoader.loliPickaxeClearInventory);
						break;
					case "loliPickaxeDropItems":
						value = String.valueOf(ConfigLoader.loliPickaxeDropItems);
						break;
					case "loliPickaxeKickPlayer":
						value = String.valueOf(ConfigLoader.loliPickaxeKickPlayer);
						break;
					case "loliPickaxeKickMessage":
						value = ConfigLoader.loliPickaxeKickMessage;
						break;
					case "loliPickaxeForbidOnLivingUpdate":
						value = String.valueOf(ConfigLoader.loliPickaxeForbidOnLivingUpdate);
						break;
					case "loliPickaxeReincarnation":
						value = String.valueOf(ConfigLoader.loliPickaxeReincarnation);
						break;
					case "loliPickaxeBeyondRedemption":
						value = String.valueOf(ConfigLoader.loliPickaxeBeyondRedemption);
						break;
					case "loliPickaxeFindOwner":
						value = String.valueOf(ConfigLoader.loliPickaxeFindOwner);
						break;
					case "loliPickaxeFindOwnerRange":
						value = String.valueOf(ConfigLoader.loliPickaxeFindOwnerRange);
						break;
					case "loliPickaxeBlueScreenAttack":
						value = String.valueOf(ConfigLoader.loliPickaxeBlueScreenAttack);
						break;
					default:
						throw new WrongUsageException("commands.loli.usage");
					}
					sender.addChatMessage(new ChatComponentTranslation("commands.loli.get", args[0], value));
				}
			}
		} else {
			throw new WrongUsageException("commands.loli.usage");
		}
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args) {
		if (args.length == 1) {
			return getListOfStringsFromIterableMatchingLastWord(args, Arrays.asList(flags));
		} else if (args.length == 2) {
			switch (args[0]) {
			case "loliPickaxeMandatoryDrop":
			case "loliPickaxeThorns":
			case "loliPickaxeKillRangeEntity":
			case "loliPickaxeAutoKillRangeEntity":
			case "loliPickaxeCompulsoryRemove":
			case "loliPickaxeValidToAllEntity":
			case "loliPickaxeClearInventory":
			case "loliPickaxeDropItems":
			case "loliPickaxeKickPlayer":
			case "loliPickaxeForbidOnLivingUpdate":
			case "loliPickaxeReincarnation":
			case "loliPickaxeBeyondRedemption":
			case "loliPickaxeFindOwner":
			case "loliPickaxeBlueScreenAttack":
				return getListOfStringsFromIterableMatchingLastWord(args,
						Arrays.asList(new String[] { "true", "false" }));
			default:
				return null;
			}
		}
		return null;
	}

}
