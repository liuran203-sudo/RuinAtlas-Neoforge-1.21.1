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
		FAILED,
		SILENCED
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

		if (isSuppressed(player, now)) {
			return Result.SILENCED;
		}

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
		tryMark(player, now, bead);

		return Result.SUCCESS;
	}

	/**
	 * 凝痕模式：持珠施术时，有概率让当前所指的契法契痕再进一分
	 * <p>内部冷却 30 秒，避免连续触发。
	 */
	private static void tryMark(ServerPlayer player, long now, boolean bead) {
		if (!bead || !"mark".equals(RaAttachments.getBeadMode(player)) || now < RaAttachments.getMarkReady(player)) {
			return;
		}

		if (player.getRandom().nextFloat() >= 0.25F) {
			return;
		}

		String path = currentPath(player);

		if (path.isEmpty()) {
			return;
		}

		ResourceLocation id = ResourceLocation.fromNamespaceAndPath(MODID, path);
		int rank = RaAttachments.getRank(player, id);

		if (rank >= 3) {
			return;
		}

		int newRank = RaAttachments.raiseRank(player, id);
		RaAttachments.setMarkReady(player, now + 600L);
		player.displayClientMessage(Component.translatable("technique." + MODID + ".mark.trigger",
				Component.translatable("technique." + MODID + "." + path),
				Component.translatable("technique." + MODID + ".rank." + newRank)), true);
	}

	/** 是否落在别人的静封领域内 */
	private static boolean isSuppressed(ServerPlayer player, long now) {
		if (!(player.level() instanceof ServerLevel level)) {
			return false;
		}

		for (ServerPlayer other : level.players()) {
			if (other == player) {
				continue;
			}

			long until = RaAttachments.getDomainUntil(other);

			if (until <= now) {
				continue;
			}

			int radius = RaAttachments.getDomainRadius(other);

			if (radius > 0 && RaAttachments.getDomainCenter(other).distSqr(player.blockPosition()) <= (double) radius * radius) {
				return true;
			}
		}

		return false;
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
			case SILENCED -> player.displayClientMessage(Component.translatable("technique." + MODID + ".cast.silenced",
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
