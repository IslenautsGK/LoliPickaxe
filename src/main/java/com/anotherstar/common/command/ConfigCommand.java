package com.anotherstar.common.command;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.common.config.annotation.ConfigField.ValurType;
import com.google.common.collect.Lists;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;

public class ConfigCommand extends CommandBase {

	@Override
	public String getName() {
		return "loli";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.loli.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length > 0 && args.length < 3) {
			if (args.length > 1) {
				if (ConfigLoader.commandFlags.contains(args[0])) {
					setAndSendFlag(args[0], args[1], sender);
					if (!sender.getEntityWorld().isRemote) {
						ConfigLoader.sandChange(null);
					}
				} else if (args[0].equals("listFlag")) {
					sendFlags(parseInt(args[1], 1, (ConfigLoader.commandFlags.size() - 1) / 18 + 1), sender);
				} else if (args[0].equals("listValue")) {
					sendValues(parseInt(args[1], 1, (ConfigLoader.commandFlags.size() - 1) / 18 + 1), sender);
				} else {
					throw new WrongUsageException("commands.loli.notfound");
				}
			} else {
				if (ConfigLoader.commandFlags.contains(args[0])) {
					sendFlag(args[0], sender);
				} else if (args[0].equals("reload")) {
					ConfigLoader.load(true);
					if (!sender.getEntityWorld().isRemote) {
						ConfigLoader.sandChange(null);
					}
					sender.sendMessage(new TextComponentTranslation("commands.loli.reload"));
				} else if (args[0].equals("listFlag")) {
					sendFlags(1, sender);
				} else if (args[0].equals("listValue")) {
					sendValues(1, sender);
				} else {
					throw new WrongUsageException("commands.loli.usage");
				}
			}
		} else {
			throw new WrongUsageException("commands.loli.usage");
		}
	}

	private void setAndSendFlag(String flag, String value, ICommandSender sender) throws CommandException {
		try {
			switch (ConfigLoader.flagAnnotations.get(flag).valueType()) {
			case INT:
				ConfigLoader.flagFields.get(flag).setInt(null, parseInt(value));
				break;
			case DOUBLE:
				ConfigLoader.flagFields.get(flag).setDouble(null, parseDouble(value));
				break;
			case BOOLEAN:
				ConfigLoader.flagFields.get(flag).setBoolean(null, parseBoolean(value));
				break;
			case STRING:
				ConfigLoader.flagFields.get(flag).set(null, value);
				break;
			default:
				throw new WrongUsageException("commands.loli.errortype");
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		ConfigLoader.save();
		TextComponentString flagText = new TextComponentString(flag);
		flagText.getStyle().setColor(TextFormatting.AQUA);
		TextComponentString commentText = new TextComponentString(ConfigLoader.flagAnnotations.get(flag).comment());
		commentText.getStyle().setColor(TextFormatting.LIGHT_PURPLE);
		TextComponentString valueText = new TextComponentString(value);
		valueText.getStyle().setColor(TextFormatting.RED);
		sender.sendMessage(new TextComponentTranslation("commands.loli.set", flagText, commentText, valueText));
	}

	private void sendFlag(String flag, ICommandSender sender) throws CommandException {
		try {
			String value;
			Field field = ConfigLoader.flagFields.get(flag);
			switch (ConfigLoader.flagAnnotations.get(flag).valueType()) {
			case INT:
				value = String.valueOf(field.getInt(null));
				break;
			case DOUBLE:
				value = String.valueOf(field.getDouble(null));
				break;
			case BOOLEAN:
				value = String.valueOf(field.getBoolean(null));
				break;
			case STRING:
				value = (String) field.get(null);
				break;
			default:
				throw new WrongUsageException("commands.loli.errortype");
			}
			TextComponentString flagText = new TextComponentString(flag);
			flagText.getStyle().setColor(TextFormatting.AQUA);
			TextComponentString commentText = new TextComponentString(ConfigLoader.flagAnnotations.get(flag).comment());
			commentText.getStyle().setColor(TextFormatting.LIGHT_PURPLE);
			TextComponentString valueText = new TextComponentString(value);
			valueText.getStyle().setColor(TextFormatting.RED);
			sender.sendMessage(new TextComponentTranslation("commands.loli.get", flagText, commentText, valueText));
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new CommandException(e.getMessage());
		}
	}

	private void sendFlags(int page, ICommandSender sender) {
		int maxPage = (ConfigLoader.commandFlags.size() - 1) / 18 + 1;
		page = MathHelper.clamp(page, 1, maxPage);
		sender.sendMessage(new TextComponentString(String.format("§a%39d/%-39d", page, maxPage).replaceAll(" ", "-")));
		for (int i = (page - 1) * 18; i < page * 18; i++) {
			if (i < ConfigLoader.commandFlags.size()) {
				String flag = ConfigLoader.commandFlags.get(i);
				TextComponentString flagText = new TextComponentString(flag);
				flagText.getStyle().setColor(TextFormatting.AQUA).setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/loli " + flag + " "));
				TextComponentString commentText = new TextComponentString(ConfigLoader.flagAnnotations.get(flag).comment());
				commentText.getStyle().setColor(TextFormatting.LIGHT_PURPLE);
				sender.sendMessage(new TextComponentTranslation("commands.loli.list", flagText, commentText));
			} else {
				sender.sendMessage(new TextComponentString(""));
			}
		}
		TextComponentTranslation preButton = new TextComponentTranslation("commands.page.button.pre");
		if (page > 1) {
			preButton.getStyle().setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/loli listFlag " + (page - 1)));
			preButton.getStyle().setColor(TextFormatting.GOLD);
		} else {
			preButton.getStyle().setColor(TextFormatting.GRAY);
		}
		TextComponentTranslation nextButton = new TextComponentTranslation("commands.page.button.next");
		if (page < maxPage) {
			nextButton.getStyle().setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/loli listFlag " + (page + 1)));
			nextButton.getStyle().setColor(TextFormatting.GOLD);
		} else {
			nextButton.getStyle().setColor(TextFormatting.GRAY);
		}
		TextComponentTranslation bottom = new TextComponentTranslation("--------------------------------%1$s/%2$s--------------------------------", preButton, nextButton);
		bottom.getStyle().setColor(TextFormatting.GREEN);
		sender.sendMessage(bottom);
	}

	private void sendValues(int page, ICommandSender sender) throws CommandException {
		int maxPage = (ConfigLoader.commandFlags.size() - 1) / 18 + 1;
		page = MathHelper.clamp(page, 1, maxPage);
		sender.sendMessage(new TextComponentString(String.format("§a%39d/%-39d", page, maxPage).replaceAll(" ", "-")));
		for (int i = (page - 1) * 18; i < page * 18; i++) {
			if (i < ConfigLoader.commandFlags.size()) {
				String flag = ConfigLoader.commandFlags.get(i);
				try {
					String value;
					Field field = ConfigLoader.flagFields.get(flag);
					switch (ConfigLoader.flagAnnotations.get(flag).valueType()) {
					case INT:
						value = String.valueOf(field.getInt(null));
						break;
					case DOUBLE:
						value = String.valueOf(field.getDouble(null));
						break;
					case BOOLEAN:
						value = String.valueOf(field.getBoolean(null));
						break;
					case STRING:
						value = (String) field.get(null);
						break;
					default:
						throw new WrongUsageException("commands.loli.errortype");
					}
					TextComponentString flagText = new TextComponentString(flag);
					flagText.getStyle().setColor(TextFormatting.AQUA).setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/loli " + flag + " " + value));
					TextComponentString commentText = new TextComponentString(ConfigLoader.flagAnnotations.get(flag).comment());
					commentText.getStyle().setColor(TextFormatting.LIGHT_PURPLE);
					TextComponentString valueText = new TextComponentString(value);
					valueText.getStyle().setColor(TextFormatting.RED);
					sender.sendMessage(new TextComponentTranslation("commands.loli.get", flagText, commentText, valueText));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new CommandException(e.getMessage());
				}
			} else {
				sender.sendMessage(new TextComponentString(""));
			}
		}
		TextComponentTranslation preButton = new TextComponentTranslation("commands.page.button.pre");
		if (page > 1) {
			preButton.getStyle().setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/loli listValue " + (page - 1)));
			preButton.getStyle().setColor(TextFormatting.GOLD);
		} else {
			preButton.getStyle().setColor(TextFormatting.GRAY);
		}
		TextComponentTranslation nextButton = new TextComponentTranslation("commands.page.button.next");
		if (page < maxPage) {
			nextButton.getStyle().setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/loli listValue " + (page + 1)));
			nextButton.getStyle().setColor(TextFormatting.GOLD);
		} else {
			nextButton.getStyle().setColor(TextFormatting.GRAY);
		}
		TextComponentTranslation bottom = new TextComponentTranslation("--------------------------------%1$s/%2$s--------------------------------", preButton, nextButton);
		bottom.getStyle().setColor(TextFormatting.GREEN);
		sender.sendMessage(bottom);
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
		if (args.length == 1) {
			List<String> list = Lists.newArrayList(ConfigLoader.commandFlags);
			list.add("reload");
			list.add("listFlag");
			list.add("listValue");
			return getListOfStringsMatchingLastWord(args, list);
		} else if (args.length == 2) {
			if (ConfigLoader.commandFlags.contains(args[0])) {
				if (ConfigLoader.flagAnnotations.get(args[0]).valueType() == ValurType.BOOLEAN) {
					return getListOfStringsMatchingLastWord(args, Arrays.asList(new String[] { "true", "false" }));
				}
			}
		}
		return Collections.<String>emptyList();
	}

}
