package com.liuran.ruinatlas.client;

import com.liuran.ruinatlas.init.RaParticleTypes;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 客户端注册：粒子渲染提供器
 */
@EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class RuinAtlasClient {
	private RuinAtlasClient() {
	}

	@SubscribeEvent
	public static void onRegisterParticleProviders(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(
				RaParticleTypes.SPIRIT_MIST.get(),
				sprites -> (type, level, x, y, z, dx, dy, dz) -> new SpiritMistParticle(level, x, y, z, sprites)
		);
		event.registerSpriteSet(
				RaParticleTypes.SPIRIT_GLOW.get(),
				sprites -> (type, level, x, y, z, dx, dy, dz) -> new SpiritGlowParticle(level, x, y, z, sprites)
		);
	}
}
