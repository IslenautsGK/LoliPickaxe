package com.anotherstar.network;

import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.common.item.ItemLoader;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;

public class ServerPacketHandler {

	@SubscribeEvent
	public void onServerPacket(FMLNetworkEvent.ServerCustomPacketEvent event) {
		EntityPlayerMP player = ((NetHandlerPlayServer) event.handler).playerEntity;
		FMLProxyPacket packet = event.packet;
		if (packet != null) {
			switch (packet.channel()) {
			case "loliCard":
				String pictureName = ByteBufUtils.readUTF8String(packet.payload());
				ItemStack itemStack = player.getCurrentEquippedItem();
				if (itemStack != null && itemStack.getItem() == Items.iron_ingot) {
					if (!player.capabilities.isCreativeMode) {
						--itemStack.stackSize;
					}
					ItemStack loliCardStack = new ItemStack(ItemLoader.loliCard);
					NBTTagCompound nbt = new NBTTagCompound();
					nbt.setString("picturename", pictureName);
					loliCardStack.setTagCompound(nbt);
					player.inventory.addItemStackToInventory(loliCardStack);
					player.inventoryContainer.detectAndSendChanges();
				}
				break;
			case "loliConfig":
				ConfigLoader.receptionChange(packet.payload());
				break;
			}
		}
	}

	@SubscribeEvent
	public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent event) {
		FMLProxyPacket packet = event.packet;
		if (packet != null) {
			switch (packet.channel()) {
			case "loliConfig":
				ConfigLoader.receptionChange(packet.payload());
				break;
			}
		}
	}

}
