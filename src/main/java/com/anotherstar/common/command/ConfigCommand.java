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
					try {
						switch (ConfigLoader.flagAnnotations.get(args[0]).valueType()) {
						case INT:
							ConfigLoader.flagFields.get(args[0]).setInt(null, parseInt(args[1]));
							break;
						case DOUBLE:
							ConfigLoader.flagFields.get(args[0]).setDouble(null, parseDouble(args[1]));
							break;
						case BOOLEAN:
							ConfigLoader.flagFields.get(args[0]).setBoolean(null, parseBoolean(args[1]));
							break;
						case STRING:
							ConfigLoader.flagFields.get(args[0]).set(null, args[1]);
							break;
						default:
							throw new WrongUsageException("commands.loli.usage");
						}
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				} else {
					throw new WrongUsageException("commands.loli.usage");
				}
				ConfigLoader.save();
				if (!sender.getEntityWorld().isRemote) {
					ConfigLoader.sandChange(null);
				}
				TextComponentString flagText = new TextComponentString(args[0]);
				flagText.getStyle().setColor(TextFormatting.AQUA);
				TextComponentString commentText = new TextComponentString(
						ConfigLoader.flagAnnotations.get(args[0]).comment());
				commentText.getStyle().setColor(TextFormatting.LIGHT_PURPLE);
				TextComponentString valueText = new TextComponentString(args[1]);
				valueText.getStyle().setColor(TextFormatting.RED);
				sender.sendMessage(new TextComponentTranslation("commands.loli.set", flagText, commentText, valueText));
			} else {
				if (ConfigLoader.commandFlags.contains(args[0])) {
					try {
						String value;
						Field field = ConfigLoader.flagFields.get(args[0]);
						switch (ConfigLoader.flagAnnotations.get(args[0]).valueType()) {
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
							throw new WrongUsageException("commands.loli.usage");
						}
						TextComponentString flagText = new TextComponentString(args[0]);
						flagText.getStyle().setColor(TextFormatting.AQUA);
						TextComponentString commentText = new TextComponentString(
								ConfigLoader.flagAnnotations.get(args[0]).comment());
						commentText.getStyle().setColor(TextFormatting.LIGHT_PURPLE);
						TextComponentString valueText = new TextComponentString(value);
						valueText.getStyle().setColor(TextFormatting.RED);
						sender.sendMessage(
								new TextComponentTranslation("commands.loli.get", flagText, commentText, valueText));
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				} else if (args[0].equals("reload")) {
					ConfigLoader.load();
					if (!sender.getEntityWorld().isRemote) {
						ConfigLoader.sandChange(null);
					}
					sender.sendMessage(new TextComponentTranslation("commands.loli.reload"));
				} else if (args[0].equals("listFlag")) {
					for (String flag : ConfigLoader.commandFlags) {
						TextComponentString flagText = new TextComponentString(flag);
						flagText.getStyle().setColor(TextFormatting.AQUA)
								.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/loli " + flag));
						TextComponentString commentText = new TextComponentString(
								ConfigLoader.flagAnnotations.get(flag).comment());
						commentText.getStyle().setColor(TextFormatting.LIGHT_PURPLE);
						sender.sendMessage(new TextComponentTranslation("commands.loli.list", flagText, commentText));
					}
				} else if (args[0].equals("listValue")) {
					for (String flag : ConfigLoader.commandFlags) {
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
								throw new WrongUsageException("commands.loli.usage");
							}
							TextComponentString flagText = new TextComponentString(flag);
							flagText.getStyle().setColor(TextFormatting.AQUA)
									.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/loli " + flag));
							TextComponentString commentText = new TextComponentString(
									ConfigLoader.flagAnnotations.get(flag).comment());
							commentText.getStyle().setColor(TextFormatting.LIGHT_PURPLE);
							TextComponentString valueText = new TextComponentString(value);
							valueText.getStyle().setColor(TextFormatting.RED);
							sender.sendMessage(new TextComponentTranslation("commands.loli.get", flagText, commentText,
									valueText));
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				} else {
					throw new WrongUsageException("commands.loli.usage");
				}
			}
		} else {
			throw new WrongUsageException("commands.loli.usage");
		}
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
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
