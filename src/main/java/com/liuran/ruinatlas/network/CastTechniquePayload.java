package com.liuran.ruinatlas.network;

import io.netty.buffer.ByteBuf;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import net.neoforged.neoforge.network.handling.IPayloadContext;

import com.liuran.ruinatlas.technique.RaCasting;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 客户端按键 → 服务端施展契法；techniquePath 为空表示“当前所指”
 */
public record CastTechniquePayload(String techniquePath) implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<CastTechniquePayload> TYPE =
			new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(MODID, "cast_technique"));

	public static final StreamCodec<ByteBuf, CastTechniquePayload> STREAM_CODEC =
			StreamCodec.composite(ByteBufCodecs.STRING_UTF8, CastTechniquePayload::techniquePath,
					CastTechniquePayload::new);

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(CastTechniquePayload payload, IPayloadContext context) {
		context.enqueueWork(() -> {
			if (context.player() instanceof ServerPlayer player) {
				RaCasting.castAndNotify(player, payload.techniquePath());
			}
		});
	}
}
