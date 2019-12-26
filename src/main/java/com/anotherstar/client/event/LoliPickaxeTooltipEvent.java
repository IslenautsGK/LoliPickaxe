package com.anotherstar.client.event;

import java.util.List;

import com.anotherstar.common.item.tool.ILoli;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class LoliPickaxeTooltipEvent {

	private int tick = 0;
	private int curColor = 0;
	private TextFormatting[] colors = { TextFormatting.GOLD, TextFormatting.BLUE, TextFormatting.GREEN,
			TextFormatting.AQUA, TextFormatting.RED, TextFormatting.LIGHT_PURPLE, TextFormatting.YELLOW };

	@SubscribeEvent
	public void onLoliPickaxeTooltip(ItemTooltipEvent event) {
		if (!event.getItemStack().isEmpty() && event.getItemStack().getItem() instanceof ILoli) {
			List<String> tooltip = event.getToolTip();
			for (int i = 0; i < tooltip.size(); i++) {
				String tip = tooltip.get(i);
				if (tip.endsWith(I18n.format("attribute.name.generic.attackDamage"))) {
					String str = I18n.format("loliPickaxe.damage");
					StringBuilder sb = new StringBuilder();
					for (int j = 0; j < str.length(); j++) {
						sb.append(colors[(curColor + j) % colors.length].toString());
						sb.append(str.charAt(j));
					}
					tooltip.set(i, " " + I18n.format("attribute.modifier.equals.0", sb.toString() + TextFormatting.GRAY,
							I18n.format("attribute.name.generic.attackDamage")));

				} else if (tip.endsWith(I18n.format("attribute.name.generic.attackSpeed"))) {
					String str = I18n.format("loliPickaxe.speed");
					StringBuilder sb = new StringBuilder();
					for (int j = 0; j < str.length(); j++) {
						sb.append(colors[(curColor + j) % colors.length].toString());
						sb.append(str.charAt(j));
					}
					tooltip.set(i, " " + I18n.format("attribute.modifier.equals.0", sb.toString() + TextFormatting.GRAY,
							I18n.format("attribute.name.generic.attackSpeed")));
				}
			}
		}
	}

	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
		if (++tick >= 5) {
			tick = 0;
			if (--curColor < 0) {
				curColor = colors.length - 1;
			}
		}
	}

}
