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
			() -> AttachmentType.<Long>builder(() -> 0L).serialize(Codec.LONG).build());

	/** 踏岳免摔到期时刻（游戏刻） */
	public static final Supplier<AttachmentType<Long>> FALL_GUARD_UNTIL = ATTACHMENT_TYPES.register("fall_guard_until",
			() -> AttachmentType.<Long>builder(() -> 0L).serialize(Codec.LONG).build());

	/** 护息挡伤到期时刻（游戏刻），挡下一次伤害后立即归零 */
	public static final Supplier<AttachmentType<Long>> WARD_UNTIL = ATTACHMENT_TYPES.register("ward_until",
			() -> AttachmentType.<Long>builder(() -> 0L).serialize(Codec.LONG).build());

	/** 墟龛代付的免灵息施术次数 */
	public static final Supplier<AttachmentType<Integer>> FREE_CASTS = ATTACHMENT_TYPES.register("free_casts",
			() -> AttachmentType.<Integer>builder(() -> 0).serialize(Codec.INT).build());

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
		player.setData(SPIRIT, Math.max(0, value));
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
}
