package com.liuran.ruinatlas.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 本模组的网络包：只走客户端 → 服务端
 */
@EventBusSubscriber(modid = MODID)
public final class RaNetwork {
	private RaNetwork() {
	}

	@SubscribeEvent
	public static void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
		PayloadRegistrar registrar = event.registrar("1");
		registrar.playToServer(CastTechniquePayload.TYPE, CastTechniquePayload.STREAM_CODEC, CastTechniquePayload::handle);
		registrar.playToServer(SelectTechniquePayload.TYPE, SelectTechniquePayload.STREAM_CODEC, SelectTechniquePayload::handle);
	}
}
