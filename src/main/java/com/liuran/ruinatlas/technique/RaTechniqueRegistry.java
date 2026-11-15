package com.liuran.ruinatlas.technique;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import com.liuran.ruinatlas.datagen.tag.RaBlockTags;
import com.liuran.ruinatlas.init.RaAttachments;
import com.liuran.ruinatlas.init.RaBlocks;
import com.liuran.ruinatlas.init.RaItems;
import com.liuran.ruinatlas.init.RaParticleTypes;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 契法登记：照 StoneAdventure 石珠登记表的做法，用 Map 自登记，
 * 在 commonSetup 的 enqueueWork 里调用 {@link #bootstrap()}。
 * <p>所有契法一律代码驱动，不给原版状态效果；契痕层数（1~3）只放大同一道的强度与灵相。
 */
public final class RaTechniqueRegistry {
	/** 身法：位移类 */
	public static final String SCOPE_MOVE = "move";
	/** 攻伐：直接伤害类 */
	public static final String SCOPE_ATTACK = "attack";
	/** 压胜：驱邪、护身类 */
	public static final String SCOPE_WARD = "ward";
	/** 观息：探查、照明类 */
	public static final String SCOPE_SENSE = "sense";

	private static final Map<Item, RaTechniqueInfo> BY_SCROLL = new HashMap<>();
	private static final Map<String, RaTechniqueInfo> BY_ID = new LinkedHashMap<>();

	private RaTechniqueRegistry() {
	}

	public static void bootstrap() {
		register(RaItems.CLOUD_STEP_SCROLL.get(), "cloud_step", 15, 30, SCOPE_MOVE, RaTechniqueRegistry::cloudStep);
		register(RaItems.WIND_BLADE_SCROLL.get(), "wind_blade", 20, 40, SCOPE_ATTACK, RaTechniqueRegistry::windBlade);
		register(RaItems.WARD_EVIL_SCROLL.get(), "ward_evil", 25, 60, SCOPE_WARD, RaTechniqueRegistry::wardEvil);
		register(RaItems.HOLD_BREATH_SCROLL.get(), "hold_breath", 20, 60, SCOPE_WARD, RaTechniqueRegistry::holdBreath);
		register(RaItems.LISTEN_WIND_SCROLL.get(), "listen_wind", 15, 30, SCOPE_SENSE, RaTechniqueRegistry::listenWind);
		register(RaItems.GUIDE_LIGHT_SCROLL.get(), "guide_light", 10, 15, SCOPE_SENSE, RaTechniqueRegistry::guideLight);
		register(RaItems.MOUNTAIN_STEP_SCROLL.get(), "mountain_step", 15, 45, SCOPE_WARD, RaTechniqueRegistry::mountainStep);
		register(RaItems.CLEANSE_SCROLL.get(), "cleanse", 20, 60, SCOPE_WARD, RaTechniqueRegistry::cleanse);
		register(RaItems.STONE_SENSE_SCROLL.get(), "stone_sense", 15, 40, SCOPE_SENSE, RaTechniqueRegistry::stoneSense);
		register(RaItems.SPIRIT_WARD_SCROLL.get(), "spirit_ward", 30, 90, SCOPE_WARD, RaTechniqueRegistry::spiritWard);
	}

	private static void register(Item scroll, String id, int spiritCost, int cooldown, String scope,
	                             RaTechniqueInfo.Effect effect) {
		RaTechniqueInfo info = new RaTechniqueInfo(
				ResourceLocation.fromNamespaceAndPath(MODID, id),
				"technique." + MODID + "." + id,
				spiritCost, cooldown, scope, effect);
		BY_SCROLL.put(scroll, info);
		BY_ID.put(id, info);
	}

	public static RaTechniqueInfo byScroll(ItemStack stack) {
		return BY_SCROLL.get(stack.getItem());
	}

	public static RaTechniqueInfo byId(String id) {
		return BY_ID.get(id);
	}

	public static List<RaTechniqueInfo> all() {
		return new ArrayList<>(BY_ID.values());
	}

	// ===== 具体契法 =====

	/** 云步：借流云之息挪移，契痕越深，踏得越远、云气越浓 */
	private static boolean cloudStep(ServerLevel level, ServerPlayer player, int rank) {
		Direction dir = player.getDirection();
		BlockPos base = player.blockPosition();
		int reach = 5 + rank * 2;

		for (int distance = reach; distance >= 1; distance--) {
			BlockPos target = base.relative(dir, distance);

			if (level.getBlockState(target).getCollisionShape(level, target).isEmpty()
					&& level.getBlockState(target.above()).getCollisionShape(level, target.above()).isEmpty()
					&& !level.getBlockState(target.below()).getCollisionShape(level, target.below()).isEmpty()) {
				burst(level, player.getX(), player.getY() + 0.1, player.getZ(), 4 + rank * 2);
				player.teleportTo(level, target.getX() + 0.5, target.getY(), target.getZ() + 0.5,
						player.getYRot(), player.getXRot());
				burst(level, target.getX() + 0.5, target.getY() + 0.1, target.getZ() + 0.5, 6 + rank * 3);
				return true;
			}
		}

		return false;
	}

	/** 凝风：聚风成刃，契满则三道交错风痕齐出 */
	private static boolean windBlade(ServerLevel level, ServerPlayer player, int rank) {
		float damage = 5.0F + rank * 2.0F;

		if (rank < 3) {
			return bladeRay(level, player, 0.0D, damage);
		}

		boolean first = bladeRay(level, player, -0.24D, damage);
		bladeRay(level, player, 0.0D, damage);
		bladeRay(level, player, 0.24D, damage);
		return first;
	}

	private static boolean bladeRay(ServerLevel level, ServerPlayer player, double angleOffset, float damage) {
		Vec3 eye = player.getEyePosition();
		Vec3 look = player.getLookAngle().yRot((float) angleOffset);

		for (double step = 1.0; step <= 12.0; step += 0.5) {
			Vec3 point = eye.add(look.scale(step));

			BlockHitResult blockHit = level.clip(new ClipContext(eye, point, ClipContext.Block.COLLIDER,
					ClipContext.Fluid.NONE, player));

			if (blockHit.getType() == BlockHitResult.Type.BLOCK && blockHit.getLocation().distanceToSqr(eye) <= step * step) {
				glowLine(level, eye, blockHit.getLocation(), 1.0);
				return false;
			}

			AABB box = new AABB(point.x - 0.55, point.y - 0.55, point.z - 0.55,
					point.x + 0.55, point.y + 0.55, point.z + 0.55);
			List<LivingEntity> hits = level.getEntitiesOfClass(LivingEntity.class, box,
					entity -> entity != player && entity.isAlive() && !entity.isSpectator());

			if (!hits.isEmpty()) {
				LivingEntity target = hits.get(0);
				target.hurt(level.damageSources().magic(), damage);

				Vec3 knock = new Vec3(look.x, 0.28, look.z).normalize().scale(0.55);
				target.push(knock.x, knock.y, knock.z);

				glowLine(level, eye, point, 0.75);
				burst(level, point.x, point.y, point.z, 10);
				return true;
			}
		}

		glowLine(level, eye, eye.add(look.scale(12.0)), 0.6);
		return true;
	}

	/** 镇厄：散诸邪凶煞之念，契痕越深，灵域越广 */
	private static boolean wardEvil(ServerLevel level, ServerPlayer player, int rank) {
		double radius = 7.0 + rank * 2.0;
		List<Monster> monsters = level.getEntitiesOfClass(Monster.class, player.getBoundingBox().inflate(radius),
				monster -> monster.isAlive());

		for (Monster monster : monsters) {
			monster.setTarget(null);

			Vec3 away = monster.position().subtract(player.position());

			if (away.lengthSqr() > 1.0E-4) {
				Vec3 push = away.normalize().scale(0.45 + rank * 0.15);
				monster.push(push.x, 0.12, push.z);
			}
		}

		player.clearFire();
		burst(level, player.getX(), player.getY() + 0.9, player.getZ(), 18 + rank * 8);

		if (rank >= 2) {
			ring(level, player, radius * 0.7, 16 + rank * 8);
		}

		return true;
	}

	/** 引息：敛住生人气息，契满者近乎寂灭 */
	private static boolean holdBreath(ServerLevel level, ServerPlayer player, int rank) {
		RaAttachments.setSilencedUntil(player, level.getGameTime() + 120L + rank * 60L);
		silenceNearby(level, player);
		burst(level, player.getX(), player.getY() + 1.0, player.getZ(), 14 + rank * 6);
		return true;
	}

	/** 听风：以灵听万类，远处活物浮现微光 */
	private static boolean listenWind(ServerLevel level, ServerPlayer player, int rank) {
		double radius = 24.0 + rank * 6.0;
		List<LivingEntity> living = level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(radius),
				entity -> entity != player && entity.isAlive() && entity.getType().getCategory() != MobCategory.MISC);

		for (LivingEntity entity : living) {
			level.sendParticles((ParticleOptions) RaParticleTypes.SPIRIT_GLOW.get(),
					entity.getX(), entity.getY() + entity.getBbHeight() * 0.5, entity.getZ(),
					3 + rank, 0.2, 0.3 * rank, 0.2, 0.0);
		}

		return true;
	}

	/** 逐光：凝萤灵一点，破幽晦，契痕越深照得越远 */
	private static boolean guideLight(ServerLevel level, ServerPlayer player, int rank) {
		double reach = 12.0 + rank * 3.0;
		BlockHitResult hit = (BlockHitResult) player.pick(reach, 0.0F, false);
		Vec3 end = hit.getType() == BlockHitResult.Type.MISS
				? player.getEyePosition().add(player.getLookAngle().scale(reach))
				: hit.getLocation();

		glowLine(level, player.getEyePosition(), end, 1.0 + rank * 0.3);
		burst(level, end.x, end.y + 0.2, end.z, 6 + rank * 4);

		if (rank >= 2) {
			ring(level, player, 1.6, 8 + rank * 4);
		}

		return true;
	}

	/** 踏岳：承山岳沉势，卸坠落之势，契满落地还能震开石霭 */
	private static boolean mountainStep(ServerLevel level, ServerPlayer player, int rank) {
		RaAttachments.setFallGuardUntil(player, level.getGameTime() + 160L + rank * 80L);
		player.resetFallDistance();
		burst(level, player.getX(), player.getY() + 0.1, player.getZ(), 10 + rank * 4);

		if (rank >= 2) {
			level.sendParticles((ParticleOptions) RaParticleTypes.SPIRIT_MIST.get(),
					player.getX(), player.getY(), player.getZ(), 10 + rank * 6, 0.6, 0.05, 0.6, 0.02);
		}

		return true;
	}

	/** 清尘：唤清净灵机，涤火厄；契满连周遭邪火一并湮灭 */
	private static boolean cleanse(ServerLevel level, ServerPlayer player, int rank) {
		player.clearFire();
		BlockPos center = player.blockPosition();
		int radius = 5 + rank * 2;
		int removed = 0;

		for (BlockPos pos : BlockPos.betweenClosed(center.offset(-radius, -3, -radius), center.offset(radius, 4, radius))) {
			if (level.getBlockState(pos).is(Blocks.FIRE)) {
				level.removeBlock(pos, false);
				removed++;
			}
		}

		if (rank >= 3) {
			List<LivingEntity> burning = level.getEntitiesOfClass(LivingEntity.class,
					player.getBoundingBox().inflate(8.0), Entity::isOnFire);

			for (LivingEntity entity : burning) {
				entity.clearFire();
			}
		}

		burst(level, player.getX(), player.getY() + 1.0, player.getZ(), 16 + Math.min(removed, 10) + rank * 4);
		return true;
	}

	/** 听脉：叩地而闻灵络，寻埋土遗珍 */
	private static boolean stoneSense(ServerLevel level, ServerPlayer player, int rank) {
		BlockPos center = player.blockPosition();
		int radius = 12 + rank * 4;
		List<BlockPos> found = new ArrayList<>();

		for (BlockPos pos : BlockPos.betweenClosed(center.offset(-radius, -8, -radius), center.offset(radius, 8, radius))) {
			BlockState state = level.getBlockState(pos);

			if (state.is(RaBlockTags.RUIN_DEBRIS) || isSpiritMark(state)) {
				found.add(pos.immutable());
			}
		}

		if (found.isEmpty()) {
			return false;
		}

		found.sort(Comparator.comparingDouble(pos -> pos.distSqr(center)));
		int shown = Math.min(24 + rank * 8, found.size());

		for (int i = 0; i < shown; i++) {
			BlockPos pos = found.get(i);
			level.sendParticles((ParticleOptions) RaParticleTypes.SPIRIT_GLOW.get(),
					pos.getX() + 0.5, pos.getY() + 0.6, pos.getZ() + 0.5, 2 + rank, 0.25, 0.25, 0.25, 0.0);
		}

		return true;
	}

	/** 护息：结一环灵契，替你先受一次伤 */
	private static boolean spiritWard(ServerLevel level, ServerPlayer player, int rank) {
		RaAttachments.setWardUntil(player, level.getGameTime() + 600L + rank * 200L);
		burst(level, player.getX(), player.getY() + player.getBbHeight() * 0.6, player.getZ(), 18 + rank * 6);
		ring(level, player, 1.2 + rank * 0.2, 10 + rank * 6);
		return true;
	}

	private static boolean isSpiritMark(BlockState state) {
		return state.is(RaBlocks.ERODED_STONE.get())
				|| state.is(RaBlocks.JADE_CHIP_ROCK.get())
				|| state.is(RaBlocks.XUN_NICHE.get())
				|| state.is(RaBlocks.BOUNDARY_STELE.get())
				|| state.is(RaBlocks.SPIRIT_TEST_ALTAR.get())
				|| state.is(RaBlocks.INSCRIBED_STELE.get())
				|| state.is(RaBlocks.BLANK_STELE.get());
	}

	// ===== 公用表现 =====

	private static void burst(ServerLevel level, double x, double y, double z, int count) {
		level.sendParticles((ParticleOptions) RaParticleTypes.SPIRIT_MIST.get(), x, y, z, count, 0.35, 0.3, 0.35, 0.01);
	}

	private static void glowLine(ServerLevel level, Vec3 from, Vec3 to, double density) {
		Vec3 delta = to.subtract(from);
		double length = delta.length();
		int points = Math.max(3, (int) (length * 2.0 * density));

		for (int i = 1; i <= points; i++) {
			Vec3 point = from.add(delta.scale((double) i / points));
			level.sendParticles((ParticleOptions) RaParticleTypes.SPIRIT_GLOW.get(),
					point.x, point.y, point.z, 1, 0.05, 0.05, 0.05, 0.0);
		}
	}

	/** 环身一圈灵纹 */
	private static void ring(ServerLevel level, ServerPlayer player, double radius, int points) {
		for (int i = 0; i < points; i++) {
			double angle = Math.PI * 2.0 * i / points;
			double x = player.getX() + Math.cos(angle) * radius;
			double z = player.getZ() + Math.sin(angle) * radius;
			level.sendParticles((ParticleOptions) RaParticleTypes.SPIRIT_GLOW.get(),
					x, player.getY() + 0.2, z, 1, 0.02, 0.02, 0.02, 0.0);
		}
	}

	/** 把附近盯着玩家的敌意生物的目标清掉 */
	public static void silenceNearby(ServerLevel level, ServerPlayer player) {
		List<Mob> mobs = level.getEntitiesOfClass(Mob.class, player.getBoundingBox().inflate(12.0),
				mob -> mob.getTarget() == player);

		for (Mob mob : mobs) {
			mob.setTarget(null);
		}
	}

	/** 供调试与提示用：某个实体是否需要被忽略 */
	public static boolean isIgnoredEntity(Entity entity) {
		return entity.getType().getCategory() == MobCategory.MISC;
	}
}
