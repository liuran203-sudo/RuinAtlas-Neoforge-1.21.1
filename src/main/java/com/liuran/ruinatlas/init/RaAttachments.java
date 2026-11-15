package com.liuran.ruinatlas.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.mojang.serialization.Codec;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 玩家身上的灵息与契痕
 * <p>SPIRIT 与 LEARNED / RANKS / SELECTED 都做了 sync，客户端才能画轮盘与 HUD。
 * 灵息不随死（本来就不是修为），契痕随死保留（已经烙进脉络）。
 */
public final class RaAttachments {
	/** 灵息上限：再多的遗物也只能存到这里 */
	public static final int MAX_SPIRIT = 300;

	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
			DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID);

	public static final Codec<List<ResourceLocation>> LEARNED_CODEC = ResourceLocation.CODEC.listOf();
	public static final Codec<Map<String, Integer>> RANKS_CODEC = Codec.unboundedMap(Codec.STRING, Codec.INT);
	public static final Codec<Map<String, Long>> COOLDOWN_CODEC = Codec.unboundedMap(Codec.STRING, Codec.LONG);

	public static final StreamCodec<FriendlyByteBuf, List<ResourceLocation>> LEARNED_STREAM =
			ByteBufCodecs.collection(ArrayList::new, ResourceLocation.STREAM_CODEC);
	public static final StreamCodec<FriendlyByteBuf, Map<String, Integer>> RANKS_STREAM =
			ByteBufCodecs.map(HashMap::new, ByteBufCodecs.STRING_UTF8, ByteBufCodecs.VAR_INT);

	/** 灵息余量 */
	public static final Supplier<AttachmentType<Integer>> SPIRIT = ATTACHMENT_TYPES.register("spirit",
			() -> AttachmentType.<Integer>builder(() -> 0)
					.serialize(Codec.INT)
					.sync(ByteBufCodecs.VAR_INT)
					.build());

	/** 已契悟的契法 id */
	public static final Supplier<AttachmentType<List<ResourceLocation>>> LEARNED = ATTACHMENT_TYPES.register("learned",
			() -> AttachmentType.<List<ResourceLocation>>builder(() -> List.of())
					.serialize(LEARNED_CODEC)
					.sync(LEARNED_STREAM)
					.copyOnDeath()
					.build());

	/** 各契法的契痕层数（1 初契 / 2 道凝 / 3 契满） */
	public static final Supplier<AttachmentType<Map<String, Integer>>> RANKS = ATTACHMENT_TYPES.register("technique_ranks",
			() -> AttachmentType.<Map<String, Integer>>builder(() -> Map.of())
					.serialize(RANKS_CODEC)
					.sync(RANKS_STREAM)
					.copyOnDeath()
					.build());

	/** 当前所指的契法（存 path） */
	public static final Supplier<AttachmentType<String>> SELECTED = ATTACHMENT_TYPES.register("selected",
			() -> AttachmentType.<String>builder(() -> "")
					.serialize(Codec.STRING)
					.sync(ByteBufCodecs.STRING_UTF8)
					.copyOnDeath()
					.build());

	/** 各契法的冷却到期刻（不落盘、不随死） */
	public static final Supplier<AttachmentType<Map<String, Long>>> COOLDOWNS = ATTACHMENT_TYPES.register("technique_cooldowns",
			() -> AttachmentType.<Map<String, Long>>builder(() -> Map.of())
					.serialize(COOLDOWN_CODEC)
					.build());

	/** 引息静默到期时刻（游戏刻） */
	public static final Supplier<AttachmentType<Long>> SILENCED_UNTIL = ATTACHMENT_TYPES.register("silenced_until",
			() -> AttachmentType.<Long>builder(() -> 0L).serialize(Codec.LONG).sync(ByteBufCodecs.VAR_LONG).build());

	/** 踏岳免摔到期时刻（游戏刻） */
	public static final Supplier<AttachmentType<Long>> FALL_GUARD_UNTIL = ATTACHMENT_TYPES.register("fall_guard_until",
			() -> AttachmentType.<Long>builder(() -> 0L).serialize(Codec.LONG).sync(ByteBufCodecs.VAR_LONG).build());

	/** 护息挡伤到期时刻（游戏刻），挡下一次伤害后立即归零 */
	public static final Supplier<AttachmentType<Long>> WARD_UNTIL = ATTACHMENT_TYPES.register("ward_until",
			() -> AttachmentType.<Long>builder(() -> 0L).serialize(Codec.LONG).sync(ByteBufCodecs.VAR_LONG).build());

	/** 墟龛代付的免灵息施术次数 */
	public static final Supplier<AttachmentType<Integer>> FREE_CASTS = ATTACHMENT_TYPES.register("free_casts",
			() -> AttachmentType.<Integer>builder(() -> 0).serialize(Codec.INT).build());

	/** 灵潮漫墟到期时刻（游戏刻） */
	public static final Supplier<AttachmentType<Long>> SPIRIT_TIDE_UNTIL = ATTACHMENT_TYPES.register("spirit_tide_until",
			() -> AttachmentType.<Long>builder(() -> 0L).serialize(Codec.LONG).sync(ByteBufCodecs.VAR_LONG).build());

	/** 墟尘囊加成到期时刻 */
	public static final Supplier<AttachmentType<Long>> DUST_BOOST_UNTIL = ATTACHMENT_TYPES.register("dust_boost_until",
			() -> AttachmentType.<Long>builder(() -> 0L).serialize(Codec.LONG).sync(ByteBufCodecs.VAR_LONG).build());

	/** 静封领域到期时刻 */
	public static final Supplier<AttachmentType<Long>> DOMAIN_UNTIL = ATTACHMENT_TYPES.register("domain_until",
			() -> AttachmentType.<Long>builder(() -> 0L).serialize(Codec.LONG).sync(ByteBufCodecs.VAR_LONG).build());

	/** 静封领域中心（BlockPos#asLong） */
	public static final Supplier<AttachmentType<Long>> DOMAIN_POS = ATTACHMENT_TYPES.register("domain_pos",
			() -> AttachmentType.<Long>builder(() -> 0L).serialize(Codec.LONG).build());

	/** 静封领域半径 */
	public static final Supplier<AttachmentType<Integer>> DOMAIN_RADIUS = ATTACHMENT_TYPES.register("domain_radius",
			() -> AttachmentType.<Integer>builder(() -> 0).serialize(Codec.INT).sync(ByteBufCodecs.VAR_INT).build());

	/** 守墟碎玉锁定的契法 path */
	public static final Supplier<AttachmentType<String>> LOCKED_TECHNIQUE = ATTACHMENT_TYPES.register("locked_technique",
			() -> AttachmentType.<String>builder(() -> "").serialize(Codec.STRING).sync(ByteBufCodecs.STRING_UTF8).build());

	/** 承契铜珠当前模式：flow 契润 / mark 凝痕 */
	public static final Supplier<AttachmentType<String>> BEAD_MODE = ATTACHMENT_TYPES.register("bead_mode",
			() -> AttachmentType.<String>builder(() -> "flow").serialize(Codec.STRING).sync(ByteBufCodecs.STRING_UTF8).build());

	/** 连续供奉次数与上一次供奉时刻 */
	public static final Supplier<AttachmentType<Integer>> OFFER_STREAK = ATTACHMENT_TYPES.register("offer_streak",
			() -> AttachmentType.<Integer>builder(() -> 0).serialize(Codec.INT).build());
	public static final Supplier<AttachmentType<Long>> OFFER_LAST = ATTACHMENT_TYPES.register("offer_last",
			() -> AttachmentType.<Long>builder(() -> 0L).serialize(Codec.LONG).build());

	/** 凝痕触发的内部冷却到期刻 */
	public static final Supplier<AttachmentType<Long>> MARK_READY = ATTACHMENT_TYPES.register("mark_ready",
			() -> AttachmentType.<Long>builder(() -> 0L).serialize(Codec.LONG).build());

	/** 原地发呆计时与上一次记录的位置 */
	public static final Supplier<AttachmentType<Integer>> IDLE_TICKS = ATTACHMENT_TYPES.register("idle_ticks",
			() -> AttachmentType.<Integer>builder(() -> 0).serialize(Codec.INT).build());
	public static final Supplier<AttachmentType<Long>> LAST_POS = ATTACHMENT_TYPES.register("last_pos",
			() -> AttachmentType.<Long>builder(() -> 0L).serialize(Codec.LONG).build());

	private RaAttachments() {
	}

	public static void register(net.neoforged.bus.api.IEventBus bus) {
		ATTACHMENT_TYPES.register(bus);
	}

	// ===== 灵息 =====

	public static int getSpirit(Player player) {
		return player.getData(SPIRIT);
	}

	public static void setSpirit(Player player, int value) {
		player.setData(SPIRIT, Math.max(0, Math.min(MAX_SPIRIT, value)));
	}

	public static void addSpirit(Player player, int amount) {
		setSpirit(player, getSpirit(player) + amount);
	}

	public static boolean spendSpirit(Player player, int amount) {
		if (getSpirit(player) < amount) {
			return false;
		}

		setSpirit(player, getSpirit(player) - amount);
		return true;
	}

	// ===== 契痕 =====

	public static List<ResourceLocation> getLearned(Player player) {
		return player.getData(LEARNED);
	}

	public static boolean hasLearned(Player player, ResourceLocation id) {
		return getLearned(player).contains(id);
	}

	public static void learn(Player player, ResourceLocation id) {
		List<ResourceLocation> learned = new ArrayList<>(getLearned(player));

		if (!learned.contains(id)) {
			learned.add(id);
			player.setData(LEARNED, List.copyOf(learned));
			setRank(player, id, 1);
		}
	}

	public static int getRank(Player player, ResourceLocation id) {
		return player.getData(RANKS).getOrDefault(id.getPath(), 1);
	}

	public static void setRank(Player player, ResourceLocation id, int rank) {
		Map<String, Integer> ranks = new HashMap<>(player.getData(RANKS));
		ranks.put(id.getPath(), Math.min(3, Math.max(1, rank)));
		player.setData(RANKS, Map.copyOf(ranks));
	}

	/** 加深一层契痕，返回新层数（已满则不升） */
	public static int raiseRank(Player player, ResourceLocation id) {
		int current = getRank(player, id);

		if (current >= 3) {
			return 3;
		}

		setRank(player, id, current + 1);
		return current + 1;
	}

	public static String getSelected(Player player) {
		return player.getData(SELECTED);
	}

	public static void setSelected(Player player, String path) {
		player.setData(SELECTED, path);
	}

	// ===== 冷却 =====

	public static long getCooldownReady(Player player, String path) {
		return player.getData(COOLDOWNS).getOrDefault(path, 0L);
	}

	public static void setCooldownReady(Player player, String path, long tick) {
		Map<String, Long> map = new HashMap<>(player.getData(COOLDOWNS));

		if (tick <= 0L) {
			map.remove(path);
		} else {
			map.put(path, tick);
		}

		player.setData(COOLDOWNS, Map.copyOf(map));
	}

	// ===== 状态计时 =====

	public static long getSilencedUntil(Player player) {
		return player.getData(SILENCED_UNTIL);
	}

	public static void setSilencedUntil(Player player, long tick) {
		player.setData(SILENCED_UNTIL, tick);
	}

	public static long getFallGuardUntil(Player player) {
		return player.getData(FALL_GUARD_UNTIL);
	}

	public static void setFallGuardUntil(Player player, long tick) {
		player.setData(FALL_GUARD_UNTIL, tick);
	}

	public static long getWardUntil(Player player) {
		return player.getData(WARD_UNTIL);
	}

	public static void setWardUntil(Player player, long tick) {
		player.setData(WARD_UNTIL, tick);
	}

	public static int getFreeCasts(Player player) {
		return player.getData(FREE_CASTS);
	}

	public static void setFreeCasts(Player player, int casts) {
		player.setData(FREE_CASTS, Math.max(0, casts));
	}

	// ===== 灵潮与墟尘 =====

	public static long getSpiritTideUntil(Player player) {
		return player.getData(SPIRIT_TIDE_UNTIL);
	}

	public static void setSpiritTideUntil(Player player, long tick) {
		player.setData(SPIRIT_TIDE_UNTIL, tick);
	}

	public static boolean isSpiritTide(Player player, long now) {
		return getSpiritTideUntil(player) > now;
	}

	public static long getDustBoostUntil(Player player) {
		return player.getData(DUST_BOOST_UNTIL);
	}

	public static void setDustBoostUntil(Player player, long tick) {
		player.setData(DUST_BOOST_UNTIL, tick);
	}

	public static boolean hasDustBoost(Player player, long now) {
		return getDustBoostUntil(player) > now;
	}

	// ===== 静封领域 =====

	public static long getDomainUntil(Player player) {
		return player.getData(DOMAIN_UNTIL);
	}

	public static void setDomain(Player player, BlockPos center, int radius, long until) {
		player.setData(DOMAIN_POS, center.asLong());
		player.setData(DOMAIN_RADIUS, radius);
		player.setData(DOMAIN_UNTIL, until);
	}

	public static void clearDomain(Player player) {
		player.setData(DOMAIN_UNTIL, 0L);
		player.setData(DOMAIN_RADIUS, 0);
	}

	public static BlockPos getDomainCenter(Player player) {
		return BlockPos.of(player.getData(DOMAIN_POS));
	}

	public static int getDomainRadius(Player player) {
		return player.getData(DOMAIN_RADIUS);
	}

	// ===== 锁法与铜珠模式 =====

	public static String getLockedTechnique(Player player) {
		return player.getData(LOCKED_TECHNIQUE);
	}

	public static void setLockedTechnique(Player player, String path) {
		player.setData(LOCKED_TECHNIQUE, path == null ? "" : path);
	}

	public static String getBeadMode(Player player) {
		return player.getData(BEAD_MODE);
	}

	public static void setBeadMode(Player player, String mode) {
		player.setData(BEAD_MODE, "mark".equals(mode) ? "mark" : "flow");
	}

	/** 切换铜珠模式，返回新模式 */
	public static String toggleBeadMode(Player player) {
		String next = "mark".equals(getBeadMode(player)) ? "flow" : "mark";
		setBeadMode(player, next);
		return next;
	}

	// ===== 供奉递减与凝痕冷却 =====

	public static int getOfferStreak(Player player) {
		return player.getData(OFFER_STREAK);
	}

	public static void setOfferStreak(Player player, int streak) {
		player.setData(OFFER_STREAK, Math.max(0, streak));
	}

	public static long getOfferLast(Player player) {
		return player.getData(OFFER_LAST);
	}

	public static void setOfferLast(Player player, long tick) {
		player.setData(OFFER_LAST, tick);
	}

	public static long getMarkReady(Player player) {
		return player.getData(MARK_READY);
	}

	public static void setMarkReady(Player player, long tick) {
		player.setData(MARK_READY, tick);
	}

	// ===== 发呆计时 =====

	public static int getIdleTicks(Player player) {
		return player.getData(IDLE_TICKS);
	}

	public static void setIdleTicks(Player player, int ticks) {
		player.setData(IDLE_TICKS, Math.max(0, ticks));
	}

	public static long getLastPos(Player player) {
		return player.getData(LAST_POS);
	}

	public static void setLastPos(Player player, long packed) {
		player.setData(LAST_POS, packed);
	}
}
