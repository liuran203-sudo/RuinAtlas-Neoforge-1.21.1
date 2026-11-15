package com.liuran.ruinatlas.network;

import io.netty.buffer.ByteBuf;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import net.neoforged.neoforge.network.handling.IPayloadContext;

import com.liuran.ruinatlas.init.RaAttachments;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 灵契轮盘选中一式 → 服务端记下当前所指
 */
public record SelectTechniquePayload(String techniquePath) implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<SelectTechniquePayload> TYPE =
			new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(MODID, "select_technique"));

	public static final StreamCodec<ByteBuf, SelectTechniquePayload> STREAM_CODEC =
			StreamCodec.composite(ByteBufCodecs.STRING_UTF8, SelectTechniquePayload::techniquePath,
					SelectTechniquePayload::new);

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(SelectTechniquePayload payload, IPayloadContext context) {
		context.enqueueWork(() -> {
			if (context.player() instanceof ServerPlayer player) {
				RaAttachments.setSelected(player, payload.techniquePath());
			}
		});
	}
}
