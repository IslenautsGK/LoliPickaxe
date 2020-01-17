package com.anotherstar.common.item;

import com.anotherstar.client.creative.CreativeTabLoader;
import com.anotherstar.common.LoliPickaxe;

import net.minecraft.item.ItemRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class ItemLoliRecord extends ItemRecord {

	public ItemLoliRecord(String name, String record) {
		super(record, new SoundEvent(new ResourceLocation(LoliPickaxe.MODID, record)));
		setUnlocalizedName(name);
		setCreativeTab(CreativeTabLoader.loliTabs);
	}

}
