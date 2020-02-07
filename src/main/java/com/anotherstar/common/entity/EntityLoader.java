package com.anotherstar.common.entity;

import com.anotherstar.client.model.ModelLoli;
import com.anotherstar.client.render.RenderLoli;
import com.anotherstar.client.render.RenderLoliBuffAttackTNT;
import com.anotherstar.common.LoliPickaxe;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityLoader {

	@SubscribeEvent
	public void registerEntity(RegistryEvent.Register<EntityEntry> event) {
		event.getRegistry().register(EntityEntryBuilder.create().entity(EntityLoli.class).id(new ResourceLocation(LoliPickaxe.MODID, "loli"), 219).name("Loli").tracker(80, 3, false).build());
		EntityRegistry.registerEgg(new ResourceLocation(LoliPickaxe.MODID, "loli"), 0xFFFFFF, 0x000000);
		event.getRegistry().register(EntityEntryBuilder.create().entity(EntityLoliBuffAttackTNT.class).id(new ResourceLocation(LoliPickaxe.MODID, "loli_buff_attack_tnt"), 220).name("LoliBuffAttackTNT").tracker(80, 3, false).build());
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void registerModel(ModelRegistryEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(EntityLoli.class, manager -> new RenderLoli(manager, new ModelLoli(), 0.3f));
		RenderingRegistry.registerEntityRenderingHandler(EntityLoliBuffAttackTNT.class, manager -> new RenderLoliBuffAttackTNT(manager));
	}

}
