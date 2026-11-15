package com.liuran.ruinatlas.worldgen;

import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.liuran.ruinatlas.init.RaBlocks;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.GenerationStep.Carving;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import net.minecraft.core.BlockPos.MutableBlockPos;

/**
 * 苍垠古墟地形生成器：0~191 的世界高度，基准 71，含河道下切与 208 格周期的道途压平
 */
public class CangyinTerrainGenerator extends ChunkGenerator {
	public static final MapCodec<CangyinTerrainGenerator> CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(BiomeSource.CODEC.fieldOf("biome_source").forGetter(gen -> gen.biomeSource))
					.apply(instance, CangyinTerrainGenerator::new)
	);
	public static final int SEA_LEVEL = 62;
	public static final int MIN_Y = 0;
	public static final int HEIGHT = 192;
	public static final int ROAD_HALF_WIDTH = 7;
	private static final PerlinSimplexNoise CONTINENTAL = new PerlinSimplexNoise(RandomSource.create(202374881L), List.of(0, 1, 2));
	private static final PerlinSimplexNoise BASIN = new PerlinSimplexNoise(RandomSource.create(200208546L), List.of(0, 1));
	private static final PerlinSimplexNoise DETAIL = new PerlinSimplexNoise(RandomSource.create(85845923L), List.of(0));
	private static final PerlinSimplexNoise RIVER = new PerlinSimplexNoise(RandomSource.create(19575628L), List.of(0, 1));
	private static final PerlinSimplexNoise ROAD = new PerlinSimplexNoise(RandomSource.create(125705198L), List.of(0, 1));

	public CangyinTerrainGenerator(BiomeSource biomeSource) {
		super(biomeSource);
	}

	@Override
	protected MapCodec<? extends ChunkGenerator> codec() {
		return CODEC;
	}

	public static int terrainHeight(int bx, int bz) {
		double c = CONTINENTAL.getValue(bx * 0.00135, bz * 0.00135, false);
		double b = BASIN.getValue(bx * 0.0042, bz * 0.0042, false);
		double d = DETAIL.getValue(bx * 0.017, bz * 0.017, false);
		double y = 71.0 + 7.5 * c + 3.0 * b + 1.3 * d;
		double r = Math.abs(RIVER.getValue(bx * 0.0031, bz * 0.0031, false));
		if (r < 0.13) {
			double t = (0.13 - r) / 0.13;
			y -= t * t * 10.5;
		}

		int road = roadDistance(bx, bz);
		if (road >= 0 && road < 7) {
			double k = (7 - road) / 7.0;
			double target = 70.0;
			y = y * (1.0 - 0.72 * k) + target * 0.72 * k;
		}

		return Mth.clamp((int) Math.round(y), 46, 118);
	}

	public static int roadDistance(int bx, int bz) {
		double warp = ROAD.getValue(bx * 0.0021, bz * 0.0021, false) * 46.0;
		double q = (bz + warp) * 0.0048076925F;
		double f = q - Math.floor(q);
		double dist = Math.min(f, 1.0 - f) * 208.0;
		return (int) Math.floor(dist);
	}

	private static BlockState topBlockFor(Holder<Biome> biome, int h) {
		ResourceKey<Biome> key = biome.unwrapKey().orElse(null);
		if (key == RuinDimensions.SHENYU_RAVINE) {
			return (h <= 64 ? RaBlocks.STREAM_SILT : RaBlocks.QINGYUAN_LOAM).get().defaultBlockState();
		} else if (key == RuinDimensions.PINGGAO_OLD_TOWN) {
			return RaBlocks.SETTLEMENT_SUBSOIL.get().defaultBlockState();
		} else {
			return key == RuinDimensions.LONG_ROAD_WASTEWAY
					? RaBlocks.XUN_SOIL.get().defaultBlockState()
					: RaBlocks.QINGYUAN_LOAM.get().defaultBlockState();
		}
	}

	private static BlockState subBlockFor(Holder<Biome> biome, int h) {
		ResourceKey<Biome> key = biome.unwrapKey().orElse(null);
		return key == RuinDimensions.SHENYU_RAVINE && h <= 65
				? RaBlocks.STREAM_SILT.get().defaultBlockState()
				: RaBlocks.XUN_SOIL.get().defaultBlockState();
	}

	@Override
	public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState randomState, StructureManager sm, ChunkAccess chunk) {
		return CompletableFuture.supplyAsync(() -> {
			ChunkPos cp = chunk.getPos();
			int bx0 = cp.getMinBlockX();
			int bz0 = cp.getMinBlockZ();
			MutableBlockPos cursor = new MutableBlockPos();

			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					int wx = bx0 + x;
					int wz = bz0 + z;
					Holder<Biome> biome = chunk.getNoiseBiome(x >> 2, 15, z >> 2);
					int h = terrainHeight(wx, wz);
					boolean onRoad = roadDistance(wx, wz) < 7;
					BlockState top = topBlockFor(biome, h);
					if (onRoad) {
						top = RaBlocks.ROAD_RUBBLE.get().defaultBlockState();
					}

					BlockState sub = subBlockFor(biome, h);
					int waterTo = Math.max(h, SEA_LEVEL);

					for (int y = 0; y <= waterTo; y++) {
						BlockState st;
						if (y > h) {
							st = Blocks.WATER.defaultBlockState();
						} else if (y == h) {
							st = top;
						} else if (y >= h - 3) {
							st = sub;
						} else if (y < 14) {
							st = RaBlocks.ERODED_STONE.get().defaultBlockState();
						} else {
							st = RaBlocks.XUN_LAYER_ROCK.get().defaultBlockState();
						}

						chunk.setBlockState(cursor.set(wx, y, wz), st, false);
					}
				}
			}

			Heightmap.primeHeightmaps(chunk, EnumSet.of(
					Heightmap.Types.OCEAN_FLOOR_WG,
					Heightmap.Types.WORLD_SURFACE_WG,
					Heightmap.Types.OCEAN_FLOOR,
					Heightmap.Types.WORLD_SURFACE,
					Heightmap.Types.MOTION_BLOCKING,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES
			));
			return chunk;
		});
	}

	@Override
	public int getBaseHeight(int x, int z, Heightmap.Types type, LevelHeightAccessor level, RandomState randomState) {
		return terrainHeight(x, z) + 1;
	}

	@Override
	public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor level, RandomState randomState) {
		Holder<Biome> biome = this.biomeSource.getNoiseBiome(x >> 2, 15, z >> 2, Climate.empty());
		int h = terrainHeight(x, z);
		boolean onRoad = roadDistance(x, z) < 7;
		BlockState top = onRoad ? RaBlocks.ROAD_RUBBLE.get().defaultBlockState() : topBlockFor(biome, h);
		BlockState sub = subBlockFor(biome, h);
		BlockState[] column = new BlockState[HEIGHT];

		for (int y = 0; y < HEIGHT; y++) {
			if (y > h && y <= SEA_LEVEL) {
				column[y] = Blocks.WATER.defaultBlockState();
			} else if (y > h) {
				column[y] = Blocks.AIR.defaultBlockState();
			} else if (y == h) {
				column[y] = top;
			} else if (y >= h - 3) {
				column[y] = sub;
			} else if (y < 14) {
				column[y] = RaBlocks.ERODED_STONE.get().defaultBlockState();
			} else {
				column[y] = RaBlocks.XUN_LAYER_ROCK.get().defaultBlockState();
			}
		}

		return new NoiseColumn(MIN_Y, column);
	}

	@Override
	public int getSeaLevel() {
		return SEA_LEVEL;
	}

	@Override
	public int getMinY() {
		return MIN_Y;
	}

	@Override
	public int getGenDepth() {
		return HEIGHT;
	}

	@Override
	public void applyCarvers(WorldGenRegion level, long seed, RandomState random, BiomeManager biomeManager, StructureManager sm, ChunkAccess chunk, Carving stage) {
	}

	@Override
	public void buildSurface(WorldGenRegion level, StructureManager sm, RandomState random, ChunkAccess chunk) {
	}

	@Override
	public void spawnOriginalMobs(WorldGenRegion region) {
	}

	@Override
	public void addDebugScreenInfo(List<String> info, RandomState random, BlockPos pos) {
	}
}
