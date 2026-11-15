package com.liuran.ruinatlas.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import com.liuran.ruinatlas.entity.XuShadowEntity;
import com.liuran.ruinatlas.init.RaAttachments;
import com.liuran.ruinatlas.init.RaBlocks;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 灵息自然恢复
 * <p>基础回复很慢，而且原地发呆超过半分钟就彻底停下；
 * 靠近墟龛、碑刻、引石与灵潮区域会更快；墟影贴近时恢复减半。
 */
@EventBusSubscriber(modid = MODID)
public final class RaSpiritEvents {
	private static final int REGEN_INTERVAL = 40;
	private static final int IDLE_LIMIT_TICKS = 600;

	private RaSpiritEvents() {
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		if (!(event.getEntity() instanceof ServerPlayer player)) {
			return;
		}

		ServerLevel level = (ServerLevel) player.level();
		long now = level.getGameTime();

		// 静封领域到期后清掉，别让旧领域继续压人
		if (RaAttachments.getDomainRadius(player) > 0 && RaAttachments.getDomainUntil(player) <= now) {
			RaAttachments.clearDomain(player);
		}

		if (player.tickCount % REGEN_INTERVAL != 0) {
			return;
		}

		BlockPos pos = player.blockPosition();
		long packed = pos.asLong();

		if (packed != RaAttachments.getLastPos(player)) {
			RaAttachments.setLastPos(player, packed);
			RaAttachments.setIdleTicks(player, 0);
		} else {
			RaAttachments.setIdleTicks(player, RaAttachments.getIdleTicks(player) + REGEN_INTERVAL);
		}

		if (RaAttachments.getIdleTicks(player) > IDLE_LIMIT_TICKS) {
			return;
		}

		int gain = 1;

		if (nearRuin(level, pos)) {
			gain++;
		}

		if (RaAttachments.isSpiritTide(player, now)) {
			gain++;
		}

		if (RaAttachments.hasDustBoost(player, now)) {
			gain += 2;
		}

		if (XuShadowEntity.interferes(player, 12.0D)) {
			gain = Math.max(0, gain / 2);
		}

		if (gain > 0) {
			RaAttachments.addSpirit(player, gain);
		}
	}

	/** 附近是否有古墟灵迹（墟龛、碑刻、引石、祭台、星纹基座） */
	private static boolean nearRuin(ServerLevel level, BlockPos center) {
		BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();

		for (int dx = -6; dx <= 6; dx++) {
			for (int dz = -6; dz <= 6; dz++) {
				for (int dy = -3; dy <= 3; dy++) {
					BlockState state = level.getBlockState(cursor.set(center.getX() + dx, center.getY() + dy, center.getZ() + dz));

					if (state.is(RaBlocks.XUN_NICHE.get())
							|| state.is(RaBlocks.INSCRIBED_STELE.get())
							|| state.is(RaBlocks.BLANK_STELE.get())
							|| state.is(RaBlocks.RUBBING_STELE.get())
							|| state.is(RaBlocks.STORY_STELE.get())
							|| state.is(RaBlocks.SUNKEN_STELE.get())
							|| state.is(RaBlocks.CANGYIN_GATE_STELE.get())
							|| state.is(RaBlocks.SPIRIT_CONDUIT.get())
							|| state.is(RaBlocks.SPIRIT_TEST_ALTAR.get())
							|| state.is(RaBlocks.STAR_CARVED_BASE.get())) {
						return true;
					}
				}
			}
		}

		return false;
	}
}
