package com.anotherstar.client.render;

import java.util.List;

import com.anotherstar.client.util.LoliCardOnlineUtil;
import com.anotherstar.client.util.LoliCardUtil;
import com.anotherstar.common.config.ConfigLoader;
import com.anotherstar.common.item.ItemLoliCard;
import com.anotherstar.common.item.ItemLoliCardAlbum;
import com.anotherstar.common.item.ItemLoliCardOnline;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderItemFrame;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.MapData;

public class RenderLoliCardFrame extends RenderItemFrame {

	public static int step = 0;

	private static final ResourceLocation MAP_BACKGROUND_TEXTURES = new ResourceLocation("textures/map/map_background.png");
	private final Minecraft mc = Minecraft.getMinecraft();
	private final ModelResourceLocation itemFrameModel = new ModelResourceLocation("item_frame", "normal");
	private final ModelResourceLocation mapModel = new ModelResourceLocation("item_frame", "map");
	private final RenderItem itemRenderer;

	public RenderLoliCardFrame(RenderManager renderManagerIn, RenderItem itemRendererIn) {
		super(renderManagerIn, itemRendererIn);
		this.itemRenderer = itemRendererIn;
	}

	public void doRender(EntityItemFrame entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		BlockPos blockpos = entity.getHangingPosition();
		double d0 = (double) blockpos.getX() - entity.posX + x;
		double d1 = (double) blockpos.getY() - entity.posY + y;
		double d2 = (double) blockpos.getZ() - entity.posZ + z;
		GlStateManager.translate(d0 + 0.5D, d1 + 0.5D, d2 + 0.5D);
		GlStateManager.rotate(180.0F - entity.rotationYaw, 0.0F, 1.0F, 0.0F);
		this.renderManager.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		BlockRendererDispatcher blockrendererdispatcher = this.mc.getBlockRendererDispatcher();
		ModelManager modelmanager = blockrendererdispatcher.getBlockModelShapes().getModelManager();
		IBakedModel ibakedmodel;
		if (entity.getDisplayedItem().getItem() instanceof ItemMap || LoliCardUtil.customArtNames != null && LoliCardUtil.customArtNames.length != 0 && entity.getDisplayedItem().hasTagCompound() && (entity.getDisplayedItem().getItem() instanceof ItemLoliCard || entity.getDisplayedItem().getItem() instanceof ItemLoliCardAlbum) || entity.getDisplayedItem().getItem() instanceof ItemLoliCardOnline) {
			ibakedmodel = modelmanager.getModel(this.mapModel);
		} else {
			ibakedmodel = modelmanager.getModel(this.itemFrameModel);
		}
		GlStateManager.pushMatrix();
		GlStateManager.translate(-0.5F, -0.5F, -0.5F);
		if (this.renderOutlines) {
			GlStateManager.enableColorMaterial();
			GlStateManager.enableOutlineMode(this.getTeamColor(entity));
		}
		blockrendererdispatcher.getBlockModelRenderer().renderModelBrightnessColor(ibakedmodel, 1.0F, 1.0F, 1.0F, 1.0F);
		if (this.renderOutlines) {
			GlStateManager.disableOutlineMode();
			GlStateManager.disableColorMaterial();
		}
		GlStateManager.popMatrix();
		GlStateManager.translate(0.0F, 0.0F, 0.4375F);
		boolean customScale = this.renderItem(entity);
		GlStateManager.popMatrix();
		if (!customScale) {
			this.renderName(entity, x + (double) ((float) entity.facingDirection.getFrontOffsetX() * 0.3F), y - 0.25D, z + (double) ((float) entity.facingDirection.getFrontOffsetZ() * 0.3F));
		}
	}

	private boolean renderItem(EntityItemFrame itemFrame) {
		boolean customScale = false;
		ItemStack itemstack = itemFrame.getDisplayedItem();
		if (!itemstack.isEmpty()) {
			GlStateManager.pushMatrix();
			GlStateManager.disableLighting();
			boolean isMap = itemstack.getItem() instanceof ItemMap;
			boolean isLoliCard = LoliCardUtil.customArtNames != null && LoliCardUtil.customArtNames.length != 0 && itemstack.hasTagCompound() && itemstack.getItem() instanceof ItemLoliCard;
			boolean isLoliCardAlbum = LoliCardUtil.customArtNames != null && LoliCardUtil.customArtNames.length != 0 && itemstack.hasTagCompound() && itemstack.getItem() instanceof ItemLoliCardAlbum;
			boolean isLoliCardOnline = itemstack.hasTagCompound() && itemstack.getItem() instanceof ItemLoliCardOnline;
			int i = isMap || isLoliCard || isLoliCardAlbum || isLoliCardOnline ? itemFrame.getRotation() % 4 * 2 : itemFrame.getRotation();
			GlStateManager.rotate((float) i * 360.0F / 8.0F, 0.0F, 0.0F, 1.0F);
			net.minecraftforge.client.event.RenderItemInFrameEvent event = new net.minecraftforge.client.event.RenderItemInFrameEvent(itemFrame, this);
			if (!net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) {
				if (isMap) {
					this.renderManager.renderEngine.bindTexture(MAP_BACKGROUND_TEXTURES);
					GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
					float f = 0.0078125F;
					GlStateManager.scale(0.0078125F, 0.0078125F, 0.0078125F);
					GlStateManager.translate(-64.0F, -64.0F, 0.0F);
					MapData mapdata = ((ItemMap) itemstack.getItem()).getMapData(itemstack, itemFrame.world);
					GlStateManager.translate(0.0F, 0.0F, -1.0F);
					if (mapdata != null) {
						this.mc.entityRenderer.getMapItemRenderer().renderMap(mapdata, true);
					}
				} else if (isLoliCard) {
					String name = itemstack.getTagCompound().getString("picture");
					int width = 1;
					int height = 1;
					boolean find = false;
					if (!name.isEmpty()) {
						for (int j = 0; j < LoliCardUtil.customArtNames.length; j++) {
							if (LoliCardUtil.customArtNames[j].equals(name)) {
								this.renderManager.renderEngine.bindTexture(LoliCardUtil.customArtResources[j]);
								width = LoliCardUtil.customArtWidths[j];
								height = LoliCardUtil.customArtHeights[j];
								find = true;
								break;
							}
						}
					}
					if (!find) {
						this.renderManager.renderEngine.bindTexture(MAP_BACKGROUND_TEXTURES);
					}
					double ratio = (double) width / (double) height;
					GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
					Tessellator tessellator = Tessellator.getInstance();
					BufferBuilder bufferbuilder = tessellator.getBuffer();
					double scale = ConfigLoader.loliCardScale;
					if (itemstack.hasDisplayName()) {
						try {
							scale = Double.parseDouble(itemstack.getDisplayName());
							customScale = true;
						} catch (NumberFormatException e) {
						}
					}
					double dx;
					double dy;
					if (ratio < 1) {
						dy = 0.5;
						dx = dy * ratio;
					} else {
						dx = 0.5;
						dy = dx / ratio;
					}
					dx *= scale;
					dy *= scale;
					bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
					bufferbuilder.pos(-dx, -dy, -0.0078125).tex(0, 0).endVertex();
					bufferbuilder.pos(-dx, dy, -0.0078125).tex(0, 1).endVertex();
					bufferbuilder.pos(dx, dy, -0.0078125).tex(1, 1).endVertex();
					bufferbuilder.pos(dx, -dy, -0.0078125).tex(1, 0).endVertex();
					bufferbuilder.pos(dx, -dy, -0.0078125).tex(1, 0).endVertex();
					bufferbuilder.pos(dx, dy, -0.0078125).tex(1, 1).endVertex();
					bufferbuilder.pos(-dx, dy, -0.0078125).tex(0, 1).endVertex();
					bufferbuilder.pos(-dx, -dy, -0.0078125).tex(0, 0).endVertex();
					tessellator.draw();
				} else if (isLoliCardAlbum) {
					String group = itemstack.getTagCompound().getString("PictureGroup") + "'";
					List<ResourceLocation> resources = Lists.newArrayList();
					List<Integer> widths = Lists.newArrayList();
					List<Integer> heights = Lists.newArrayList();
					for (int j = 0; j < LoliCardUtil.customArtNames.length; j++) {
						if (LoliCardUtil.customArtNames[j].startsWith(group)) {
							resources.add(LoliCardUtil.customArtResources[j]);
							widths.add(LoliCardUtil.customArtWidths[j]);
							heights.add(LoliCardUtil.customArtHeights[j]);
						}
					}
					int width = 1;
					int height = 1;
					if (resources.isEmpty()) {
						this.renderManager.renderEngine.bindTexture(MAP_BACKGROUND_TEXTURES);
					} else {
						int index = step % resources.size();
						this.renderManager.renderEngine.bindTexture(resources.get(index));
						width = widths.get(index);
						height = heights.get(index);
					}
					double ratio = (double) width / (double) height;
					GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
					Tessellator tessellator = Tessellator.getInstance();
					BufferBuilder bufferbuilder = tessellator.getBuffer();
					double scale = ConfigLoader.loliCardScale;
					if (itemstack.hasDisplayName()) {
						try {
							scale = Double.parseDouble(itemstack.getDisplayName());
							customScale = true;
						} catch (NumberFormatException e) {
						}
					}
					double dx;
					double dy;
					if (ratio < 1) {
						dy = 0.5;
						dx = dy * ratio;
					} else {
						dx = 0.5;
						dy = dx / ratio;
					}
					dx *= scale;
					dy *= scale;
					bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
					bufferbuilder.pos(-dx, -dy, -0.0078125).tex(0, 0).endVertex();
					bufferbuilder.pos(-dx, dy, -0.0078125).tex(0, 1).endVertex();
					bufferbuilder.pos(dx, dy, -0.0078125).tex(1, 1).endVertex();
					bufferbuilder.pos(dx, -dy, -0.0078125).tex(1, 0).endVertex();
					bufferbuilder.pos(dx, -dy, -0.0078125).tex(1, 0).endVertex();
					bufferbuilder.pos(dx, dy, -0.0078125).tex(1, 1).endVertex();
					bufferbuilder.pos(-dx, dy, -0.0078125).tex(0, 1).endVertex();
					bufferbuilder.pos(-dx, -dy, -0.0078125).tex(0, 0).endVertex();
					tessellator.draw();
				} else if (isLoliCardOnline) {
					String url = itemstack.getTagCompound().getString("ImageUrl");
					int width = 1;
					int height = 1;
					boolean find = false;
					if (!url.isEmpty()) {
						if (LoliCardOnlineUtil.isLoad(url)) {
							LoliCardOnlineUtil.bind(url);
							width = LoliCardOnlineUtil.getWidth(url);
							height = LoliCardOnlineUtil.getHeight(url);
							find = true;
						} else {
							LoliCardOnlineUtil.load(url);
						}
					}
					if (!find) {
						this.renderManager.renderEngine.bindTexture(MAP_BACKGROUND_TEXTURES);
					}
					double ratio = (double) width / (double) height;
					GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
					Tessellator tessellator = Tessellator.getInstance();
					BufferBuilder bufferbuilder = tessellator.getBuffer();
					double scale = ConfigLoader.loliCardScale;
					if (itemstack.hasDisplayName()) {
						try {
							scale = Double.parseDouble(itemstack.getDisplayName());
							customScale = true;
						} catch (NumberFormatException e) {
						}
					}
					double dx;
					double dy;
					if (ratio < 1) {
						dy = 0.5;
						dx = dy * ratio;
					} else {
						dx = 0.5;
						dy = dx / ratio;
					}
					dx *= scale;
					dy *= scale;
					bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
					bufferbuilder.pos(-dx, -dy, -0.0078125).tex(0, 0).endVertex();
					bufferbuilder.pos(-dx, dy, -0.0078125).tex(0, 1).endVertex();
					bufferbuilder.pos(dx, dy, -0.0078125).tex(1, 1).endVertex();
					bufferbuilder.pos(dx, -dy, -0.0078125).tex(1, 0).endVertex();
					bufferbuilder.pos(dx, -dy, -0.0078125).tex(1, 0).endVertex();
					bufferbuilder.pos(dx, dy, -0.0078125).tex(1, 1).endVertex();
					bufferbuilder.pos(-dx, dy, -0.0078125).tex(0, 1).endVertex();
					bufferbuilder.pos(-dx, -dy, -0.0078125).tex(0, 0).endVertex();
					tessellator.draw();
				} else {
					GlStateManager.scale(0.5F, 0.5F, 0.5F);
					GlStateManager.pushAttrib();
					RenderHelper.enableStandardItemLighting();
					this.itemRenderer.renderItem(itemstack, ItemCameraTransforms.TransformType.FIXED);
					RenderHelper.disableStandardItemLighting();
					GlStateManager.popAttrib();
				}
			}
			GlStateManager.enableLighting();
			GlStateManager.popMatrix();
		}
		return customScale;
	}

}