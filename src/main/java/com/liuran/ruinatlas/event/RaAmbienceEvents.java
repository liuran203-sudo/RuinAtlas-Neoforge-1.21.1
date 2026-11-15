package com.liuran.ruinatlas.event;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import com.liuran.ruinatlas.init.RaAttachments;
import com.liuran.ruinatlas.worldgen.RuinDimensions;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 苍垠环境事件
 * <p>灵潮漫墟：在苍垠停留时偶发一阵灵雾，持续一分多钟，回复与供奉都更旺。
 * 墟风过碑的氛围表现放在客户端 {@code RaClientEvents}。
 */
@EventBusSubscriber(modid = MODID)
public final class RaAmbienceEvents {
	private static final int TIDE_MIN_TICKS = 2400;
	private static final int TIDE_EXTRA_TICKS = 1200;

	private RaAmbienceEvents() {
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		if (!(event.getEntity() instanceof ServerPlayer player)) {
			return;
		}

		if (player.level().dimension() != RuinDimensions.CANGYIN) {
			return;
		}

		if (player.tickCount % 20 != 0) {
			return;
		}

		ServerLevel level = (ServerLevel) player.level();
		long now = level.getGameTime();

		if (RaAttachments.isSpiritTide(player, now)) {
			return;
		}

		if (player.getRandom().nextFloat() < 0.008F) {
			int duration = TIDE_MIN_TICKS + player.getRandom().nextInt(TIDE_EXTRA_TICKS);
			RaAttachments.setSpiritTideUntil(player, now + duration);
			player.displayClientMessage(Component.translatable("event." + MODID + ".spirit_tide.start"), false);
			level.playSound(null, player.getX(), player.getY(), player.getZ(),
					SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.AMBIENT, 0.5F, 0.6F);
		}
	}
}
