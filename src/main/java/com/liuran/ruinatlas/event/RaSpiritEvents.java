package com.liuran.ruinatlas.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import com.liuran.ruinatlas.init.RaAttachments;
import com.liuran.ruinatlas.technique.RaTechniqueRegistry;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 灵息相关的逐刻结算：引息抹锁定、踏岳免摔、护息替挡一次伤害
 */
@EventBusSubscriber(modid = MODID)
public final class RaSpiritEvents {
	private RaSpiritEvents() {
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		if (!(event.getEntity() instanceof ServerPlayer player) || !(player.level() instanceof ServerLevel level)) {
			return;
		}

		long silencedUntil = RaAttachments.getSilencedUntil(player);

		if (silencedUntil > 0L) {
			if (level.getGameTime() >= silencedUntil) {
				RaAttachments.setSilencedUntil(player, 0L);
			} else if (player.tickCount % 4 == 0) {
				RaTechniqueRegistry.silenceNearby(level, player);
			}
		}

		long fallGuardUntil = RaAttachments.getFallGuardUntil(player);

		if (fallGuardUntil > 0L) {
			if (level.getGameTime() >= fallGuardUntil) {
				RaAttachments.setFallGuardUntil(player, 0L);
			} else {
				player.resetFallDistance();
			}
		}
	}

	@SubscribeEvent
	public static void onIncomingDamage(LivingIncomingDamageEvent event) {
		if (!(event.getEntity() instanceof ServerPlayer player) || !(player.level() instanceof ServerLevel level)) {
			return;
		}

		long wardUntil = RaAttachments.getWardUntil(player);

		if (wardUntil <= 0L || level.getGameTime() >= wardUntil) {
			return;
		}

		RaAttachments.setWardUntil(player, 0L);
		event.setCanceled(true);

		level.sendParticles(
				com.liuran.ruinatlas.init.RaParticleTypes.SPIRIT_MIST.get(),
				player.getX(), player.getY() + player.getBbHeight() * 0.5, player.getZ(),
				18, 0.4, 0.4, 0.4, 0.01);
	}
}
