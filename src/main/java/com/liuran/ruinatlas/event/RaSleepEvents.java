package com.liuran.ruinatlas.event;

import com.liuran.ruinatlas.worldgen.RuinDimensions;

import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.SleepFinishedTimeEvent;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 睡觉跳夜：苍垠这类非主世界维度用的是 DerivedLevelData，
 * 其中 setDayTime 是空实现，新时间会被直接丢弃；
 * 各维度共用一份昼时，所以在睡觉结束时把新时间写回主世界即可。
 */
@EventBusSubscriber(modid = MODID)
public final class RaSleepEvents {
	private RaSleepEvents() {
	}

	@SubscribeEvent
	public static void onSleepFinished(SleepFinishedTimeEvent event) {
		if (!(event.getLevel() instanceof ServerLevel level) || !RuinDimensions.CANGYIN.equals(level.dimension())) {
			return;
		}

		level.getServer().overworld().setDayTime(event.getNewTime());
	}
}
