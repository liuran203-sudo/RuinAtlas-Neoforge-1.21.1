package com.liuran.ruinatlas.technique;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

/**
 * 一式的记录：来源契卷、灵息消耗、冷却、归类与施术实现
 *
 * @param id         契法 id（也用作契法册的键）
 * @param nameKey    语言键
 * @param spiritCost 每次施术消耗的灵息点数（初契价，铜珠在手持时可减两成）
 * @param cooldown   冷却刻
 * @param scope      归类，取值见 {@link RaTechniqueRegistry} 的 SCOPE_* 常量
 * @param effect     施术实现，rank 为契痕层数（1 初契 / 2 道凝 / 3 契满），
 *                   返回 false 表示这一次未能成术
 */
public record RaTechniqueInfo(ResourceLocation id, String nameKey, int spiritCost, int cooldown, String scope,
                              Effect effect) {
	public String path() {
		return id.getPath();
	}

	@FunctionalInterface
	public interface Effect {
		boolean cast(ServerLevel level, ServerPlayer player, int rank);
	}
}
