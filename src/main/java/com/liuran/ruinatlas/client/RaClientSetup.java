package com.liuran.ruinatlas.client;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 客户端注册：按键 + 灵息 HUD
 */
@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class RaClientSetup {
	private RaClientSetup() {
	}

	@SubscribeEvent
	public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
		event.register(RaKeyMappings.CAST);
		event.register(RaKeyMappings.WHEEL);
	}

	@SubscribeEvent
	public static void onRegisterGuiLayers(RegisterGuiLayersEvent event) {
		event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(MODID, "spirit_hud"),
				RaSpiritHud::render);
	}
}
