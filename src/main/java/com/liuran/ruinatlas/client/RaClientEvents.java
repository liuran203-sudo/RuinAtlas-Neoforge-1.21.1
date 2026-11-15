package com.liuran.ruinatlas.client;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import com.liuran.ruinatlas.network.CastTechniquePayload;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 客户端按键处理：R 施法，G 打开灵契轮盘（占位界面）
 */
@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public final class RaClientEvents {
	private RaClientEvents() {
	}

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		Minecraft minecraft = Minecraft.getInstance();

		if (minecraft.player == null) {
			return;
		}

		while (RaKeyMappings.CAST.consumeClick()) {
			if (minecraft.screen == null) {
				PacketDistributor.sendToServer(new CastTechniquePayload(""));
			}
		}

		if (RaKeyMappings.WHEEL.consumeClick() && minecraft.screen == null) {
			minecraft.setScreen(new RaTechniqueScreen());
		}
	}
}
