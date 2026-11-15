package com.liuran.ruinatlas.client;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent.Post;
import net.neoforged.neoforge.registries.DeferredBlock;

import com.liuran.ruinatlas.init.RaBlocks;
import com.liuran.ruinatlas.init.RaParticleTypes;
import com.liuran.ruinatlas.worldgen.RuinDimensions;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 苍垠古墟界的环境灵息：白日地表低概率飘灵霭，夜间灵迹方块旁升起微光
 */
@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public final class SpiritAuraClient {
	private static final List<DeferredBlock<?>> SPIRIT_MARKS = List.of(
			RaBlocks.ERODED_STONE,
			RaBlocks.JADE_CHIP_ROCK,
			RaBlocks.XUN_NICHE,
			RaBlocks.BOUNDARY_STELE,
			RaBlocks.SPIRIT_TEST_ALTAR,
			RaBlocks.SEALED_CERAMIC_URN,
			RaBlocks.INSCRIBED_STELE,
			RaBlocks.BLANK_STELE
	);

	private SpiritAuraClient() {
	}

	@SubscribeEvent
	public static void onClientTick(Post event) {
		Minecraft mc = Minecraft.getInstance();
		ClientLevel level = mc.level;
		if (level != null && mc.player != null) {
			if (level.dimension().equals(RuinDimensions.CANGYIN)) {
				long tick = level.getGameTime();
				RandomSource r = level.getRandom();
				if (tick % 30L == 0L) {
					double px = mc.player.getX() + (r.nextDouble() * 2.0 - 1.0) * 26.0;
					double pz = mc.player.getZ() + (r.nextDouble() * 2.0 - 1.0) * 26.0;
					int bx = Mth.floor(px);
					int bz = Mth.floor(pz);
					int by = level.getHeight(Types.MOTION_BLOCKING, bx, bz) + 1;
					if (by >= 68 && r.nextFloat() < 0.75F) {
						boolean nearSpirit = isSpiritMarkNear(level, bx, by, bz);
						if (!nearSpirit || r.nextFloat() < 0.5F) {
							int count = nearSpirit ? 2 : 1;

							for (int i = 0; i < count; i++) {
								level.addParticle(
										(ParticleOptions) RaParticleTypes.SPIRIT_MIST.get(),
										px + r.nextDouble() * 3.0 - 1.5,
										by + r.nextDouble() * 2.0,
										pz + r.nextDouble() * 3.0 - 1.5,
										0.0,
										0.002,
										0.0
								);
							}
						}
					}
				}

				if ((tick & 63L) == 0L && level.isNight()) {
					int cx = Mth.floor(mc.player.getX());
					int cz = Mth.floor(mc.player.getZ());

					for (int i = 0; i < 6; i++) {
						int bx = cx + r.nextInt(24) - 12;
						int bz = cz + r.nextInt(24) - 12;
						int by = Mth.floor(mc.player.getY()) + r.nextInt(9) - 4;
						BlockPos pos = new BlockPos(bx, by, bz);
						if (spiritMarkAt(level, pos)) {
							level.addParticle(
									(ParticleOptions) RaParticleTypes.SPIRIT_GLOW.get(),
									bx + 0.5 + r.nextDouble() * 0.4 - 0.2,
									by + 1.1 + r.nextDouble() * 0.3,
									bz + 0.5 + r.nextDouble() * 0.4 - 0.2,
									0.0,
									0.0,
									0.0
							);
						}
					}
				}
			}
		}
	}

	private static boolean isSpiritMarkNear(ClientLevel level, int x, int y, int z) {
		for (int dx = -3; dx <= 3; dx += 3) {
			for (int dz = -3; dz <= 3; dz += 3) {
				if (spiritMarkAt(level, new BlockPos(x + dx, y, z + dz)) || spiritMarkAt(level, new BlockPos(x + dx, y - 1, z + dz))) {
					return true;
				}
			}
		}

		return false;
	}

	private static boolean spiritMarkAt(ClientLevel level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);

		for (DeferredBlock<?> mark : SPIRIT_MARKS) {
			if (state.is((Block) mark.get())) {
				return true;
			}
		}

		return false;
	}
}
