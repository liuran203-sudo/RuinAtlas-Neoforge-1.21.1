package com.liuran.ruinatlas.technique;

import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;

import com.liuran.ruinatlas.init.RaAttachments;
import com.liuran.ruinatlas.init.RaItems;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 契法施放：契痕烙在脉络里，只要有灵息，就能心念引动，不再需要手持承契铜珠。
 * <p>承契铜珠改为助灵法器：手持（主手或副手）时灵息消耗与冷却各减两成。
 */
public final class RaCasting {
	/** 持珠施术的灵息折扣 */
	public static final double BEAD_COST_FACTOR = 0.8D;
	/** 持珠施术的冷却折扣 */
	public static final double BEAD_COOLDOWN_FACTOR = 0.8D;

	public enum Result {
		SUCCESS,
		NOT_LEARNED,
		NO_SPIRIT,
		COOLDOWN,
		FAILED
	}

	private RaCasting() {
	}

	/** 当前所指的契法 path；无记录时自动落到第一道 */
	public static String currentPath(ServerPlayer player) {
		String selected = RaAttachments.getSelected(player);
		List<ResourceLocation> learned = RaAttachments.getLearned(player);

		if (!selected.isEmpty() && learned.stream().anyMatch(id -> id.getPath().equals(selected))) {
			return selected;
		}

		if (learned.isEmpty()) {
			return "";
		}

		String first = learned.get(0).getPath();
		RaAttachments.setSelected(player, first);
		return first;
	}

	/** 循环换一道已契悟的契法，返回新的 path */
	public static String cycle(ServerPlayer player) {
		List<ResourceLocation> learned = RaAttachments.getLearned(player);

		if (learned.isEmpty()) {
			RaAttachments.setSelected(player, "");
			return "";
		}

		String current = currentPath(player);
		int index = 0;

		for (int i = 0; i < learned.size(); i++) {
			if (learned.get(i).getPath().equals(current)) {
				index = i;
				break;
			}
		}

		String next = learned.get((index + 1) % learned.size()).getPath();
		RaAttachments.setSelected(player, next);
		return next;
	}

	/**
	 * 施放一式；techniquePath 为空时取当前所指
	 */
	public static Result cast(ServerPlayer player, String techniquePath) {
		String path = techniquePath == null || techniquePath.isBlank() ? currentPath(player) : techniquePath;

		if (path.isEmpty()) {
			return Result.NOT_LEARNED;
		}

		RaTechniqueInfo info = RaTechniqueRegistry.byId(path);

		if (info == null || !RaAttachments.hasLearned(player, info.id())) {
			return Result.NOT_LEARNED;
		}

		long now = player.level().getGameTime();

		if (RaAttachments.getCooldownReady(player, path) > now) {
			return Result.COOLDOWN;
		}

		boolean bead = holdsBead(player);
		int cost = bead
				? Math.max(1, (int) Math.ceil(info.spiritCost() * BEAD_COST_FACTOR))
				: info.spiritCost();

		boolean paidByNiche = RaAttachments.getFreeCasts(player) > 0;

		if (paidByNiche) {
			RaAttachments.setFreeCasts(player, RaAttachments.getFreeCasts(player) - 1);
		} else if (!RaAttachments.spendSpirit(player, cost)) {
			return Result.NO_SPIRIT;
		}

		int rank = RaAttachments.getRank(player, info.id());
		boolean cast = info.effect().cast((ServerLevel) player.level(), player, rank);

		if (!cast) {
			if (paidByNiche) {
				RaAttachments.setFreeCasts(player, RaAttachments.getFreeCasts(player) + 1);
			} else {
				RaAttachments.addSpirit(player, cost);
			}

			return Result.FAILED;
		}

		long cooldown = Math.round(info.cooldown() * (bead ? BEAD_COOLDOWN_FACTOR : 1.0D));
		RaAttachments.setCooldownReady(player, path, now + cooldown);
		return Result.SUCCESS;
	}

	/** 施放并把结果告知玩家 */
	public static Result castAndNotify(ServerPlayer player, String techniquePath) {
		String path = techniquePath == null || techniquePath.isBlank() ? currentPath(player) : techniquePath;
		RaTechniqueInfo info = RaTechniqueRegistry.byId(path);

		if (info == null) {
			player.displayClientMessage(Component.translatable("technique." + MODID + ".cast.not_learned"), true);
			return Result.NOT_LEARNED;
		}

		Result result = cast(player, path);

		switch (result) {
			case SUCCESS -> player.displayClientMessage(Component.translatable("technique." + MODID + ".cast.success",
					Component.translatable(info.nameKey()),
					Component.translatable("technique." + MODID + ".rank." + RaAttachments.getRank(player, info.id()))), true);
			case NOT_LEARNED -> player.displayClientMessage(Component.translatable("technique." + MODID + ".cast.not_learned"), true);
			case NO_SPIRIT -> player.displayClientMessage(Component.translatable("technique." + MODID + ".cast.no_spirit",
					info.spiritCost(), RaAttachments.getSpirit(player)), true);
			case COOLDOWN -> player.displayClientMessage(Component.translatable("technique." + MODID + ".cast.cooldown",
					Component.translatable(info.nameKey())), true);
			case FAILED -> player.displayClientMessage(Component.translatable("technique." + MODID + ".cast.failed",
					Component.translatable(info.nameKey())), true);
			default -> {
			}
		}

		return result;
	}

	/** 主手或副手是否握着承契铜珠（助灵法器，减耗两成） */
	public static boolean holdsBead(Player player) {
		ItemStack main = player.getMainHandItem();
		ItemStack off = player.getOffhandItem();
		return main.is(RaItems.SPIRIT_COPPER_BEAD.get()) || off.is(RaItems.SPIRIT_COPPER_BEAD.get());
	}
}
