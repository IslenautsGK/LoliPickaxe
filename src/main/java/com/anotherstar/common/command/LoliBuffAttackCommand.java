package com.anotherstar.common.command;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import com.anotherstar.network.LoliDeadPacket;
import com.anotherstar.network.NetworkHandler;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class LoliBuffAttackCommand extends CommandBase {

	@Override
	public String getName() {
		return "loliattack";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.loliattack.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length == 2) {
			EntityPlayerMP player = getPlayer(server, sender, args[0]);
			switch (args[1]) {
			case "loliPickaxeBlueScreenAttack":
				NetworkHandler.INSTANCE.sendMessageToPlayer(new LoliDeadPacket(false, true, false, false), player);
				break;
			case "loliPickaxeExitAttack":
				NetworkHandler.INSTANCE.sendMessageToPlayer(new LoliDeadPacket(false, false, true, false), player);
				break;
			case "loliPickaxeFailRespondAttack":
				NetworkHandler.INSTANCE.sendMessageToPlayer(new LoliDeadPacket(false, false, false, true), player);
				break;
			default:
				throw new WrongUsageException("commands.loliattack.notfound");
			}
		}
	}

	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			@Nullable BlockPos targetPos) {
		return args.length == 1 ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames())
				: args.length == 2
						? getListOfStringsMatchingLastWord(args, "loliPickaxeBlueScreenAttack", "loliPickaxeExitAttack",
								"loliPickaxeFailRespondAttack")
						: Collections.emptyList();
	}

}
