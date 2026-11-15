package com.liuran.ruinatlas.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import com.liuran.ruinatlas.init.RaAttachments;
import com.liuran.ruinatlas.init.RaParticleTypes;
import com.liuran.ruinatlas.network.CastTechniquePayload;
import com.liuran.ruinatlas.worldgen.RuinDimensions;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 客户端按键处理：R 施法，G 打开灵契轮盘（占位界面）
 */
@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public final class RaClientEvents {
	private static int windCooldown;

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

		tickAmbience(minecraft);
	}

	/** 苍垠氛围：灵潮的雾絮，以及偶发的墟风过碑 */
	private static void tickAmbience(Minecraft minecraft) {
		if (minecraft.player == null || minecraft.level == null) {
			return;
		}

		if (minecraft.player.level().dimension() != RuinDimensions.CANGYIN) {
			return;
		}

		if (windCooldown > 0) {
			windCooldown--;
		}

		long now = minecraft.level.getGameTime();

		if (RaAttachments.isSpiritTide(minecraft.player, now)) {
			if (minecraft.player.getRandom().nextFloat() < 0.12F) {
				double x = minecraft.player.getX() + (minecraft.player.getRandom().nextFloat() - 0.5F) * 10.0;
				double y = minecraft.player.getY() + 0.5 + minecraft.player.getRandom().nextFloat() * 2.0;
				double z = minecraft.player.getZ() + (minecraft.player.getRandom().nextFloat() - 0.5F) * 10.0;
				minecraft.level.addParticle(RaParticleTypes.SPIRIT_MIST.get(), x, y, z,
						(minecraft.player.getRandom().nextFloat() - 0.5F) * 0.03, 0.01,
						(minecraft.player.getRandom().nextFloat() - 0.5F) * 0.03);
			}

			return;
		}

		if (windCooldown <= 0 && minecraft.player.getRandom().nextFloat() < 0.0005F) {
			windCooldown = 2400;
			minecraft.gui.setOverlayMessage(Component.translatable("event." + MODID + ".wind_stele"), false);

			for (int i = 0; i < 10; i++) {
				double x = minecraft.player.getX() + (minecraft.player.getRandom().nextFloat() - 0.5F) * 14.0;
				double y = minecraft.player.getY() + minecraft.player.getRandom().nextFloat() * 3.0;
				double z = minecraft.player.getZ() + (minecraft.player.getRandom().nextFloat() - 0.5F) * 14.0;
				minecraft.level.addParticle(RaParticleTypes.SPIRIT_MIST.get(), x, y, z, 0.02, 0.0, 0.01);
			}
		}
	}
}
