package com.liuran.ruinatlas.client;

import com.liuran.ruinatlas.entity.XuShadowEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

import org.joml.Matrix4f;

/**
 * 墟影渲染：一张朝向镜头的灵体剪影，半透明，轻轻上下浮动
 */
public class XuShadowRenderer extends EntityRenderer<XuShadowEntity> {
	private static final ResourceLocation TEXTURE =
			ResourceLocation.fromNamespaceAndPath("ruinatlas", "textures/entity/xu_shadow.png");

	public XuShadowRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(XuShadowEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
	                   MultiBufferSource buffer, int packedLight) {
		float bob = (float) Math.sin((entity.tickCount + partialTick) * 0.08F) * 0.08F;
		float width = 0.9F;
		float height = 1.35F;

		poseStack.pushPose();
		poseStack.translate(0.0F, entity.getBbHeight() * 0.5F + bob, 0.0F);
		poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
		Matrix4f pose = poseStack.last().pose();
		VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(TEXTURE));

		vertex(consumer, pose, -width / 2.0F, -height / 2.0F, 0.0F, 0.0F, 1.0F, packedLight);
		vertex(consumer, pose, width / 2.0F, -height / 2.0F, 0.0F, 1.0F, 1.0F, packedLight);
		vertex(consumer, pose, width / 2.0F, height / 2.0F, 0.0F, 1.0F, 0.0F, packedLight);
		vertex(consumer, pose, -width / 2.0F, height / 2.0F, 0.0F, 0.0F, 0.0F, packedLight);
		poseStack.popPose();
		super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
	}

	private static void vertex(VertexConsumer consumer, Matrix4f pose, float x, float y, float z,
	                           float u, float v, int packedLight) {
		consumer.addVertex(pose, x, y, z)
				.setColor(255, 255, 255, 185)
				.setUv(u, v)
				.setOverlay(OverlayTexture.NO_OVERLAY)
				.setLight(packedLight)
				.setNormal(0.0F, 1.0F, 0.0F);
	}

	@Override
	public ResourceLocation getTextureLocation(XuShadowEntity entity) {
		return TEXTURE;
	}
}
