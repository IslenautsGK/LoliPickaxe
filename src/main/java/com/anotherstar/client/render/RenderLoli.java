package com.anotherstar.client.render;

import com.anotherstar.client.model.ModelLoli;
import com.anotherstar.client.model.ModelNevermore;
import com.anotherstar.client.model.ModelPaperLoli;
import com.anotherstar.common.LoliPickaxe;
import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.common.entity.EntityLoli;
import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;

public class RenderLoli extends RenderLiving<EntityLoli> {

	private static final ResourceLocation TEXTURE_LOLI = new ResourceLocation(LoliPickaxe.MODID, "textures/entities/loli.png");
	private static final ResourceLocation TEXTURE_PAPER_LOLI = new ResourceLocation(LoliPickaxe.MODID, "textures/entities/paper_loli.png");

	private final ModelLoli loli;
	private final ModelNevermore nevermore;
	private final ModelPaperLoli paperLoli;
	private final RenderRemiliaLoli remilia;

	public RenderLoli(RenderManager rendermanager, ModelBase modelbase, float shadowsize) {
		super(rendermanager, modelbase, shadowsize);
		this.loli = new ModelLoli();
		this.nevermore = new ModelNevermore(rendermanager);
		this.paperLoli = new ModelPaperLoli();
		if (Loader.isModLoaded(TouhouLittleMaid.MOD_ID)) {
			this.remilia = new RenderRemiliaLoli(rendermanager, modelbase, shadowsize);
		} else {
			this.remilia = null;
		}
	}

	@Override
	public void doRender(EntityLoli entity, double x, double y, double z, float entityYaw, float partialTicks) {
		switch (ConfigLoader.loliModelType) {
		case 0:
			mainModel = loli;
			break;
		case 1:
			mainModel = nevermore;
			break;
		case 2:
			mainModel = paperLoli;
			break;
		case 3:
			if (Loader.isModLoaded(TouhouLittleMaid.MOD_ID)) {
				remilia.doRender(entity, x, y, z, entityYaw, partialTicks);
				return;
			} else {
				mainModel = loli;
			}
			break;
		}
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityLoli entity) {
		switch (ConfigLoader.loliModelType) {
		case 0:
			return TEXTURE_LOLI;
		case 1:
			return TEXTURE_LOLI;
		case 2:
			return TEXTURE_PAPER_LOLI;
		case 3:
			if (Loader.isModLoaded(TouhouLittleMaid.MOD_ID)) {
				return remilia.getEntityTexture(entity);
			} else {
				return TEXTURE_LOLI;
			}
		}
		return TEXTURE_LOLI;
	}

}
