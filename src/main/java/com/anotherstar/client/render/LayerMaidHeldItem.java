package com.anotherstar.client.render;

import javax.annotation.Nonnull;

import com.anotherstar.common.entity.EntityLoli;
import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntityModelJson;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.Optional.Interface;

@Optional.InterfaceList(value = {
		@Interface(iface = "com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid", modid = TouhouLittleMaid.MOD_ID),
		@Interface(iface = "com.github.tartaricacid.touhoulittlemaid.client.model.EntityModelJson", modid = TouhouLittleMaid.MOD_ID) })
public class LayerMaidHeldItem implements LayerRenderer<EntityLoli> {

	protected final RenderLiving<EntityLoli> livingEntityRenderer;

	public LayerMaidHeldItem(RenderLiving<EntityLoli> livingEntityRendererIn) {
		this.livingEntityRenderer = livingEntityRendererIn;
	}

	@Override
	public void doRenderLayer(@Nonnull EntityLoli entityMaid, float limbSwing, float limbSwingAmount,
			float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		ItemStack mainRightItem = entityMaid.getHeldItemMainhand();
		ItemStack offLeftItem = entityMaid.getHeldItemOffhand();

		if (!mainRightItem.isEmpty() || !offLeftItem.isEmpty()) {
			GlStateManager.pushMatrix();
			this.renderHeldItem(entityMaid, mainRightItem, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND,
					EnumHandSide.RIGHT);
			this.renderHeldItem(entityMaid, offLeftItem, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND,
					EnumHandSide.LEFT);
			GlStateManager.popMatrix();
		}
	}

	private void renderHeldItem(EntityLoli entityMaid, ItemStack itemStack, ItemCameraTransforms.TransformType type,
			EnumHandSide handSide) {
		if (!itemStack.isEmpty()) {
			GlStateManager.pushMatrix();
			if (entityMaid.isSneaking()) {
				GlStateManager.translate(0.0F, 0.2F, 0.0F);
			}
			((EntityModelJson) this.livingEntityRenderer.getMainModel()).postRenderArm(0.0625F, handSide);
			GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
			boolean flag = handSide == EnumHandSide.LEFT;
			GlStateManager.translate((float) (flag ? -1 : 1) / 16.0F, 0.125F, -0.525F);
			Minecraft.getMinecraft().getItemRenderer().renderItemSide(entityMaid, itemStack, type, flag);
			GlStateManager.popMatrix();
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
}
