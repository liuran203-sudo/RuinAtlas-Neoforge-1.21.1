package com.liuran.ruinatlas.worldgen;

import com.liuran.ruinatlas.datagen.loot.RaChestLootTable;
import com.liuran.ruinatlas.init.RaBlocks;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.registries.DeferredBlock;

/**
 * 古墟遗迹 Feature：按 config 里的 kind 决定放哪种残迹（民舍、祠庙、田垄、孤碑、街墟、古井、
 * 衙署、路岔、地窖、道亭、坟丘、埋龛、玉棚、溪碑、溪坛）
 */
public class RuinStructureFeature extends Feature<RuinStructureFeature.Config> {
	private static final RuinStructureFeature.Sel AIR = new RuinStructureFeature.Air();

	public RuinStructureFeature(Codec<Config> codec) {
		super(codec);
	}

	private static void put(WorldGenLevel level, BlockPos pos, Sel sel) {
		if (sel instanceof RuinStructureFeature.Blk b) {
			level.setBlock(pos, b.block().get().defaultBlockState(), 2);
		} else if (sel instanceof RuinStructureFeature.Lit l) {
			level.setBlock(pos, l.state(), 2);
		} else if (sel instanceof RuinStructureFeature.Air) {
			level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
		}
	}

	private static void fill(WorldGenLevel level, BlockPos from, int sx, int sy, int sz, Sel sel) {
		for (int x = 0; x < sx; x++) {
			for (int y = 0; y < sy; y++) {
				for (int z = 0; z < sz; z++) {
					put(level, from.offset(x, y, z), sel);
				}
			}
		}
	}

	private static void chance(WorldGenLevel level, BlockPos pos, Sel sel, double p, RandomSource r) {
		if (r.nextDouble() < p) {
			put(level, pos, sel);
		}
	}

	private static void chestLoot(WorldGenLevel level, RandomSource r, BlockPos pos, ResourceKey<LootTable> loot) {
		put(level, pos, new RuinStructureFeature.Lit(Blocks.CHEST.defaultBlockState()));
		if (level.getBlockEntity(pos) instanceof RandomizableContainer container) {
			container.setLootTable(loot, r.nextLong());
		}
	}

	private static RuinStructureFeature.Blk blk(DeferredBlock<?> b) {
		return new RuinStructureFeature.Blk(b);
	}

	@Override
	public boolean place(FeaturePlaceContext<Config> context) {
		WorldGenLevel level = context.level();
		RandomSource r = context.random();
		BlockPos origin = context.origin();
		// 先找一处“脚下有实心地面”的落点，避免整座残迹浮空或埋进石头
		BlockPos ground = surface(level, origin);
		foundation(level, ground, 4);

		return switch (context.config().kind()) {
			case FARM_HUT -> this.farmHut(level, ground, r);
			case SHRINE -> this.shrine(level, ground, r);
			case FIELD_RIDGE -> this.fieldRidge(level, ground, r);
			case LONE_STELE -> this.loneStele(level, ground, r);
			case STREET_RUINS -> this.streetRuins(level, ground, r);
			case ANCIENT_WELL -> this.ancientWell(level, ground, r);
			case GOV_HALL -> this.govHall(level, ground, r);
			case ROAD_FORK -> this.roadFork(level, ground, r);
			case CELLAR -> this.cellar(level, ground, r, true);
			case WAY_KIOSK -> this.wayKiosk(level, ground, r);
			case GRAVE_MOUND -> this.graveMound(level, ground, r);
			case BURIED_NICHE -> this.cellar(level, ground, r, false);
			case JADE_SHACK -> this.jadeShack(level, ground, r);
			case STREAM_STELE -> this.streamStele(level, ground, r);
			case STREAM_ALTAR -> this.streamAltar(level, ground, r);
			case NICHE_SHRINE -> this.nicheShrine(level, ground, r);
			case CONTRACT_PIT -> this.contractPit(level, ground, r);
			case STAR_TERRACE -> this.starTerrace(level, ground, r);
			case SUNKEN_RUIN -> this.sunkenRuin(level, seafloor(level, origin), r);
		};
	}

	/** 从落点附近向下找“上方是空气、下方是实心地面”的一格 */
	private static BlockPos surface(WorldGenLevel level, BlockPos origin) {
		MutableBlockPos cursor = new MutableBlockPos(origin.getX(),
				Math.min(origin.getY() + 8, level.getMaxBuildHeight() - 2), origin.getZ());

		for (int i = 0; i < 20; i++) {
			BlockState here = level.getBlockState(cursor);
			BlockState below = level.getBlockState(cursor.below());

			if (here.isAir() && !below.isAir() && below.getFluidState().isEmpty()) {
				return cursor.immutable();
			}

			cursor.move(0, -1, 0);
		}

		return origin.below();
	}

	/** 水底落点：向下找第一块实心方块 */
	private static BlockPos seafloor(WorldGenLevel level, BlockPos origin) {
		MutableBlockPos cursor = origin.mutable();

		for (int i = 0; i < 24; i++) {
			BlockState here = level.getBlockState(cursor);

			if (!here.isAir() && !here.getFluidState().isEmpty()) {
				cursor.move(0, -1, 0);
				continue;
			}

			if (!here.isAir() && here.getFluidState().isEmpty()) {
				return cursor.above().immutable();
			}

			cursor.move(0, -1, 0);
		}

		return origin.below();
	}

	/** 地基：把落点下方 3 格内的空腔用墟土填实，减少悬空 */
	private static void foundation(WorldGenLevel level, BlockPos c, int radius) {
		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {
				for (int depth = 1; depth <= 3; depth++) {
					BlockPos p = c.offset(x, -depth, z);

					if (level.getBlockState(p).isAir()) {
						put(level, p, blk(RaBlocks.XUN_SOIL));
					}
				}
			}
		}
	}

	private boolean farmHut(WorldGenLevel level, BlockPos c, RandomSource r) {
		int w = 5 + r.nextInt(2);
		int d = 5 + r.nextInt(2);
		BlockPos corner = c.offset(-w / 2, 0, -d / 2);
		fill(level, corner.offset(1, 1, 1), w - 1, 1, d - 1, blk(RaBlocks.FARM_RUIN_PLOT));

		for (int x = 0; x < w; x++) {
			for (int z = 0; z < d; z++) {
				boolean north = z == 0;
				boolean west = x == 0;
				boolean south = z == d - 1;
				boolean east = x == w - 1;
				if (north || west || south || east) {
					BlockPos wall = corner.offset(x, 1, z);
					Sel wallBlock = r.nextBoolean() ? blk(RaBlocks.THATCH_RUBBLE) : blk(RaBlocks.XUN_SOIL);
					chance(level, wall, wallBlock, 0.82, r);
					if ((north || west) && r.nextDouble() < 0.5) {
						chance(level, wall.above(), blk(RaBlocks.THATCH_RUBBLE), 0.55, r);
					}
				}
			}
		}

		for (int x = 1; x < 3; x++) {
			for (int z = 1; z < 3; z++) {
				chance(level, corner.offset(x, 3, z), blk(RaBlocks.EAVE_BOARD_SHARD), 0.7, r);
			}
		}

		chance(level, corner.offset(1, 1, 1).above(2), blk(RaBlocks.COLLAPSED_KIOSK), 0.4, r);
		chance(level, corner.offset(-1, 2, 2), blk(RaBlocks.WITHER_REED), 0.4, r);
		chance(level, corner.offset(w, 2, 1), blk(RaBlocks.PETAL_SPROUT), 0.5, r);
		chance(level, corner.offset(w + 1, 2, 2), blk(RaBlocks.XUN_WILD_GRASS), 0.5, r);
		return true;
	}

	private boolean shrine(WorldGenLevel level, BlockPos c, RandomSource r) {
		int w = 7;
		int d = 5;
		BlockPos corner = c.offset(-w / 2, 0, -d / 2);
		fill(level, corner, w, 1, d, blk(RaBlocks.CARVED_STONE_PAVING));

		for (int x = 0; x < w; x++) {
			for (int z = 0; z < d; z++) {
				if (x == 0 || z == 0 || x == w - 1 || z == d - 1) {
					BlockPos wall = corner.offset(x, 1, z);
					if (x == w / 2 && z == d - 1) {
						put(level, wall, AIR);
					} else {
						Sel brick = r.nextBoolean() ? blk(RaBlocks.XUN_BRICK) : blk(RaBlocks.WEATHERED_XUN_BRICK);
						put(level, wall, brick);
						if (r.nextDouble() < 0.72) {
							put(level, wall.above(), brick);
						}
					}
				}
			}
		}

		put(level, corner, blk(RaBlocks.CORNER_QUOIN));
		put(level, corner.offset(w - 1, 1, 0), blk(RaBlocks.CORNER_QUOIN));
		put(level, corner.offset(0, 1, d - 1), blk(RaBlocks.CORNER_QUOIN));
		put(level, corner.offset(w - 1, 1, d - 1), blk(RaBlocks.CORNER_QUOIN));
		put(level, corner.offset(w / 2, 1, d - 1), blk(RaBlocks.DOOR_FRAME_RUIN));
		put(level, corner.offset(1, 2, 2), blk(RaBlocks.BROKEN_LATTICE));
		put(level, corner.offset(w / 2, 1, 1), blk(RaBlocks.STONE_INCENSE_TABLE));
		put(level, corner.offset(2, 1, 0).above(), blk(RaBlocks.WALL_NICHE_SMALL));
		chance(level, corner.offset(w - 3, 1, 2), blk(RaBlocks.CANGYIN_OFFERING_TABLE), 0.6, r);

		for (int x = 1; x < w - 1; x++) {
			chance(level, corner.offset(x, 3, 2), blk(RaBlocks.RIDGE_TILE), 0.5, r);
		}

		chance(level, corner.offset(0, 3, 2), blk(RaBlocks.EAVE_ORNAMENT), 0.6, r);
		chance(level, corner.offset(w - 1, 3, 2), blk(RaBlocks.RIDGE_ORNAMENT_SHARD), 0.45, r);
		return true;
	}

	private boolean fieldRidge(WorldGenLevel level, BlockPos c, RandomSource r) {
		for (int i = 0; i < 4 + r.nextInt(3); i++) {
			int len = 4 + r.nextInt(7);
			BlockPos p = c.offset(r.nextInt(9) - 7, 0, i * 2 - 4);

			for (int x = 0; x < len; x++) {
				Sel ridge = r.nextDouble() < 0.65 ? blk(RaBlocks.FARM_RUIN_PLOT) : blk(RaBlocks.XUN_SOIL);
				put(level, p.offset(x, 1, 0), ridge);
				chance(level, p.offset(x, 2, 0), blk(RaBlocks.PETAL_SPROUT), 0.05, r);
				chance(level, p.offset(x, 2, 0), blk(RaBlocks.XUN_WILD_GRASS), 0.04, r);
			}
		}

		chance(level, c.offset(3, 1, -3), blk(RaBlocks.THATCH_RUBBLE), 0.5, r);
		return true;
	}

	private boolean loneStele(WorldGenLevel level, BlockPos c, RandomSource r) {
		fill(level, c.offset(-2, 0, -2), 5, 1, 5, blk(RaBlocks.BROKEN_CARVED_PAVING));
		put(level, c.offset(0, 1, 0), blk(RaBlocks.STONE_PLINTH));
		Sel stele = r.nextBoolean() ? blk(RaBlocks.BLANK_STELE) : blk(RaBlocks.INSCRIBED_STELE);
		put(level, c.offset(0, 2, 0), stele);
		chance(level, c.offset(2, 1, 1), blk(RaBlocks.PLAIN_PLINTH), 0.6, r);
		chance(level, c.offset(-2, 1, -1), blk(RaBlocks.RUBBLE_PILE), 0.5, r);
		chance(level, c.offset(1, 2, -2), blk(RaBlocks.PETAL_SPROUT), 0.6, r);
		chance(level, c.offset(-1, 2, 2), blk(RaBlocks.XUN_WILD_GRASS), 0.6, r);
		return true;
	}

	private boolean streetRuins(WorldGenLevel level, BlockPos c, RandomSource r) {
		int len = 9 + r.nextInt(5);
		BlockPos corner = c.offset(-len / 2, 0, -3);
		fill(level, corner, len, 1, 7, blk(RaBlocks.SETTLEMENT_SUBSOIL));

		for (int x = 0; x < len; x++) {
			put(level, corner.offset(x, 1, 0), blk(RaBlocks.CITY_CURB));
			put(level, corner.offset(x, 1, 6), blk(RaBlocks.CITY_CURB));
		}

		for (int x = 0; x < len; x++) {
			if (!(r.nextDouble() < 0.3)) {
				BlockPos wallA = corner.offset(x, 1, -2);
				Sel brick = r.nextDouble() < 0.4 ? blk(RaBlocks.CRACKED_XUN_BRICK) : blk(RaBlocks.XUN_BRICK);
				put(level, wallA, brick);
				chance(level, wallA.above(), brick, 0.4, r);
				BlockPos wallB = corner.offset(x, 1, 8);
				if (r.nextDouble() < 0.8) {
					put(level, wallB, blk(RaBlocks.WEATHERED_XUN_BRICK));
				}
			}
		}

		for (int x = 1; x < len - 1; x += 3) {
			put(level, corner.offset(x, 1, 3), blk(RaBlocks.BROKEN_CARVED_PAVING));
			chance(level, corner.offset(x, 1, 4), blk(RaBlocks.MARKET_RUBBLE), 0.35, r);
		}

		chance(level, corner.offset(2, 1, -1), blk(RaBlocks.DOOR_FRAME_RUIN), 0.5, r);
		chance(level, corner.offset(len - 3, 2, 8), blk(RaBlocks.LATTICE_WINDOW_SQUARE), 0.4, r);
		chance(level, corner.offset(len - 1, 1, 4), blk(RaBlocks.RUBBLE_PILE), 0.5, r);
		return true;
	}

	private boolean ancientWell(WorldGenLevel level, BlockPos c, RandomSource r) {
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				put(level, c.offset(x, 0, z), blk(RaBlocks.CITY_CURB));
			}
		}

		put(level, c, new RuinStructureFeature.Lit(Blocks.WATER.defaultBlockState()));
		put(level, c.below(), new RuinStructureFeature.Lit(Blocks.WATER.defaultBlockState()));
		put(level, c.below(2), blk(RaBlocks.STREAM_SILT));
		put(level, c.above(2), blk(RaBlocks.CITY_CURB));
		chance(level, c.offset(2, 1, 0), blk(RaBlocks.MARKET_RUBBLE), 0.6, r);
		chance(level, c.offset(-2, 2, 1), blk(RaBlocks.XUN_VINE), 0.4, r);
		return true;
	}

	private boolean govHall(WorldGenLevel level, BlockPos c, RandomSource r) {
		int w = 9;
		int d = 9;
		BlockPos corner = c.offset(-w / 2, 0, -d / 2);
		fill(level, corner, w, 3, d, blk(RaBlocks.SETTLEMENT_SUBSOIL));

		for (int x = 0; x < w; x++) {
			for (int z = 0; z < d; z++) {
				if (x != 0 && z != 0 && x != w - 1 && z != d - 1) {
					put(level, corner.offset(x, 3, z), blk(RaBlocks.CARVED_STONE_PAVING));
				} else {
					put(level, corner.offset(x, 3, z), blk(RaBlocks.FOUNDATION_SKIRT));
				}
			}
		}

		put(level, corner.offset(w / 2, 1, d), blk(RaBlocks.STEP_STONE));
		put(level, corner.offset(w / 2, 2, d), blk(RaBlocks.CARVED_STEP));
		BlockPos[] posts = new BlockPos[]{
				corner.offset(1, 4, 1), corner.offset(w - 2, 4, 1), corner.offset(1, 4, d - 2), corner.offset(w - 2, 4, d - 2)
		};

		for (BlockPos post : posts) {
			put(level, post, blk(RaBlocks.LOTUS_PLINTH));
			put(level, post.above(), blk(RaBlocks.WOODEN_PILLAR_LOWER));
			if (r.nextDouble() < 0.5) {
				put(level, post.above(2), blk(RaBlocks.WOODEN_PILLAR_UPPER));
			} else {
				put(level, post.above(2), AIR);
			}

			chance(level, post.offset(1, 0, 1), blk(RaBlocks.PILLAR_BRACE_SHARD), 0.4, r);
		}

		chance(level, corner.offset(w / 2, 6, 1), blk(RaBlocks.BRACKET_SHARD_SIDE), 0.55, r);
		chance(level, corner.offset(1, 6, 1), blk(RaBlocks.BRACKET_SHARD_CORNER), 0.5, r);
		put(level, corner.offset(w / 2, 4, 3), blk(RaBlocks.CANGYIN_OFFERING_TABLE));
		put(level, corner.offset(w / 2, 4, 1).offset(2, 0, 0), blk(RaBlocks.WOODEN_SCREEN));

		for (int x = 2; x < w - 2; x += 2) {
			chance(level, corner.offset(x, 4, d - 1), blk(RaBlocks.BALUSTER_SOLID), 0.45, r);
		}

		put(level, corner.offset(1, 4, d - 1), blk(RaBlocks.NEWEL_POST));
		chance(level, corner.offset(w - 2, 4, d - 1), blk(RaBlocks.BROKEN_BALUSTER), 0.5, r);
		return true;
	}

	private boolean roadFork(WorldGenLevel level, BlockPos c, RandomSource r) {
		for (int i = -4; i <= 4; i++) {
			put(level, c.offset(i, 0, 0), blk(RaBlocks.ROAD_RUBBLE));
			put(level, c.offset(0, 0, i), blk(RaBlocks.ROAD_RUBBLE));
		}

		put(level, c.offset(0, 1, 0), blk(RaBlocks.WAYSTONE));
		chance(level, c.offset(2, 1, 1), blk(RaBlocks.COLLAPSED_KIOSK), 0.4, r);
		chance(level, c.offset(-2, 1, -1), blk(RaBlocks.RUBBLE_PILE), 0.5, r);
		chance(level, c.offset(3, 2, -2), blk(RaBlocks.WITHER_REED), 0.5, r);
		chance(level, c.offset(-3, 2, 2), blk(RaBlocks.BONE_PILE), 0.3, r);
		return true;
	}

	private boolean cellar(WorldGenLevel level, BlockPos c, RandomSource r, boolean withLantern) {
		int depth = 8 + r.nextInt(5);
		BlockPos p = c;

		for (int i = 1; i <= depth; i++) {
			BlockPos shaft = p.offset(r.nextInt(2) - r.nextInt(2), -i, r.nextInt(2) - r.nextInt(2));
			put(level, shaft, AIR);
			put(level, shaft.offset(1, 0, 0), blk(RaBlocks.XUN_LAYER_ROCK));
			if (i % 2 == 0) {
				put(level, shaft.offset(-1, 0, 0), new RuinStructureFeature.Lit(Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.EAST)));
			}

			p = shaft;
		}

		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				put(level, p.offset(x, 0, z), AIR);
				put(level, p.offset(x, -1, z), blk(RaBlocks.BROKEN_CARVED_PAVING));
				put(level, p.offset(x, 1, z), blk(RaBlocks.XUN_LAYER_ROCK));
			}
		}

		for (int y = 0; y <= 1; y++) {
			put(level, p.offset(2, y, 0), blk(RaBlocks.XUN_LAYER_ROCK));
			put(level, p.offset(-2, y, 0), blk(RaBlocks.XUN_LAYER_ROCK));
			put(level, p.offset(0, y, 2), blk(RaBlocks.XUN_LAYER_ROCK));
			put(level, p.offset(0, y, -2), blk(RaBlocks.XUN_LAYER_ROCK));
		}

		if (r.nextBoolean()) {
			chestLoot(level, r, p.offset(1, 0, 1), RuinDimensions.CELLAR_CHEST);
		} else {
			put(level, p.offset(1, 0, 1), blk(RaBlocks.EMPTY_CERAMIC_URN));
		}

		if (withLantern) {
			put(level, p.offset(-1, 0, -1), blk(RaBlocks.STONE_LANTERN));
		}

		put(level, p.offset(0, 0, -1), blk(RaBlocks.WALL_NICHE_SMALL));
		chance(level, p.offset(1, 0, -1), blk(RaBlocks.SEALED_CERAMIC_URN), 0.4, r);
		return true;
	}

	private boolean wayKiosk(WorldGenLevel level, BlockPos c, RandomSource r) {
		for (int x = -2; x <= 2; x++) {
			for (int z = -2; z <= 2; z++) {
				put(level, c.offset(x, 0, z), blk(RaBlocks.ROAD_RUBBLE));
			}
		}

		BlockPos[] posts = new BlockPos[]{c.offset(-2, 1, -2), c.offset(2, 1, -2), c.offset(-2, 1, 2), c.offset(2, 1, 2)};

		for (BlockPos post : posts) {
			put(level, post, blk(RaBlocks.WOODEN_PILLAR_LOWER));
		}

		put(level, c.offset(0, 1, -2), blk(RaBlocks.BRACKET_SHARD_SIDE));

		for (int x = -2; x <= 2; x++) {
			chance(level, c.offset(x, 2, -2), blk(RaBlocks.EAVE_BOARD_SHARD), 0.6, r);
			chance(level, c.offset(x, 2, 2), blk(RaBlocks.EAVE_BOARD_SHARD), 0.45, r);
		}

		chance(level, c.offset(0, 3, 0), blk(RaBlocks.RIDGE_ORNAMENT_SHARD), 0.5, r);
		put(level, c.offset(1, 1, 1), new RuinStructureFeature.Lit(Blocks.CHEST.defaultBlockState()));
		if (level.getBlockEntity(c.offset(1, 1, 1)) instanceof RandomizableContainer container) {
			container.setLootTable(RuinDimensions.KIOSK_CHEST, r.nextLong());
		}

		put(level, c.offset(-1, 1, 0), blk(RaBlocks.STONE_INCENSE_TABLE));
		chance(level, c.offset(3, 1, 0), blk(RaBlocks.WITHER_REED), 0.5, r);
		chance(level, c.offset(0, 2, 3), blk(RaBlocks.WITHER_REED), 0.5, r);
		chance(level, c.offset(-3, 1, -1), blk(RaBlocks.BONE_PILE), 0.35, r);
		return true;
	}

	private boolean graveMound(WorldGenLevel level, BlockPos c, RandomSource r) {
		for (int m = 0; m < 2 + r.nextInt(2); m++) {
			int mx = m * 3 - 2;
			int mz = r.nextInt(3) - 1;

			for (int x = -1; x <= 1; x++) {
				for (int z = -1; z <= 1; z++) {
					put(level, c.offset(mx + x, 0, mz + z), blk(RaBlocks.XUN_SOIL));
				}
			}

			put(level, c.offset(mx, 1, mz), blk(RaBlocks.XUN_SOIL));
			Sel top = r.nextDouble() < 0.5 ? blk(RaBlocks.BONE_PILE) : blk(RaBlocks.BLANK_STELE);
			put(level, c.offset(mx, 2, mz), top);
			chance(level, c.offset(mx + 1, 2, mz + 1), blk(RaBlocks.WITHER_REED), 0.55, r);
		}

		chance(level, c.offset(0, 1, 3), blk(RaBlocks.RUBBLE_PILE), 0.4, r);
		return true;
	}

	private boolean jadeShack(WorldGenLevel level, BlockPos c, RandomSource r) {
		for (int x = -2; x <= 2; x++) {
			for (int z = -1; z <= 1; z++) {
				put(level, c.offset(x, 0, z), blk(RaBlocks.STREAM_SILT));
			}
		}

		for (int x = -2; x <= 2; x++) {
			put(level, c.offset(x, 2 - Math.abs(x) / 2, -1), blk(RaBlocks.EAVE_BOARD_SHARD));
		}

		put(level, c.offset(-2, 1, -1), blk(RaBlocks.WOODEN_PILLAR_LOWER));
		put(level, c.offset(2, 1, -1), blk(RaBlocks.WOODEN_PILLAR_LOWER));

		for (int i = 0; i < 3; i++) {
			put(level, c.offset(r.nextInt(5) - 2, 1, r.nextInt(3) - 1), blk(RaBlocks.JADE_CHIP_ROCK));
		}

		chance(level, c.offset(1, 1, 1), blk(RaBlocks.RUBBLE_PILE), 0.5, r);
		chance(level, c.offset(-1, 1, 1), blk(RaBlocks.EMPTY_CERAMIC_URN), 0.4, r);
		chance(level, c.offset(0, 1, 1).above(), blk(RaBlocks.XUN_LILY), 0.25, r);
		return true;
	}

	private boolean streamStele(WorldGenLevel level, BlockPos c, RandomSource r) {
		put(level, c, blk(RaBlocks.INSCRIBED_STELE));

		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				if (x != 0 || z != 0) {
					Sel rock = r.nextDouble() < 0.5 ? blk(RaBlocks.JADE_CHIP_ROCK) : blk(RaBlocks.GLOOM_MOSS_BLOCK);
					put(level, c.offset(x, 0, z), rock);
				}
			}
		}

		chance(level, c.offset(2, 0, -1), blk(RaBlocks.XUN_LILY), 0.4, r);
		return true;
	}

	private boolean streamAltar(WorldGenLevel level, BlockPos c, RandomSource r) {
		put(level, c, blk(RaBlocks.JADE_CHIP_ROCK));
		put(level, c.above(), blk(RaBlocks.JADE_CHIP_ROCK));
		put(level, c.above(2), blk(RaBlocks.STONE_INCENSE_TABLE));

		for (int i = 0; i < 2; i++) {
			put(level, c.offset(i == 0 ? 2 : -2, 0, 0), blk(RaBlocks.CORNER_QUOIN));
		}

		chance(level, c.offset(1, 1, 2), blk(RaBlocks.XUN_LILY), 0.4, r);
		chance(level, c.offset(-1, 2, -1), blk(RaBlocks.GLOOM_MOSS_BLOCK), 0.4, r);
		return true;
	}

	private boolean nicheShrine(WorldGenLevel level, BlockPos c, RandomSource r) {
		int w = 7;
		int d = 5;
		BlockPos corner = c.offset(-w / 2, 0, -d / 2);
		fill(level, corner, w, 1, d, blk(RaBlocks.CARVED_STONE_PAVING));

		for (int x = 0; x < w; x++) {
			for (int z = 0; z < d; z++) {
				boolean edge = x == 0 || z == 0 || x == w - 1 || z == d - 1;

				if (!edge) {
					continue;
				}

				if (x == w / 2 && z == d - 1) {
					put(level, corner.offset(x, 1, z), AIR);
					continue;
				}

				Sel brick = r.nextBoolean() ? blk(RaBlocks.XUN_BRICK) : blk(RaBlocks.WEATHERED_XUN_BRICK);
				put(level, corner.offset(x, 1, z), brick);
				if (r.nextDouble() < 0.6) {
					put(level, corner.offset(x, 2, z), brick);
				}
			}
		}

		// 四角柱与残檐
		put(level, corner, blk(RaBlocks.CORNER_QUOIN));
		put(level, corner.offset(w - 1, 1, 0), blk(RaBlocks.CORNER_QUOIN));
		put(level, corner.offset(0, 1, d - 1), blk(RaBlocks.CORNER_QUOIN));
		chance(level, corner.offset(w - 1, 2, d - 1), blk(RaBlocks.RIDGE_ORNAMENT_SHARD), 0.5, r);
		chance(level, corner.offset(w / 2, 3, 2), blk(RaBlocks.RIDGE_TILE), 0.6, r);

		// 龛前的供台与龛本体
		put(level, corner.offset(w / 2, 1, 2), blk(RaBlocks.STONE_PLINTH));
		put(level, corner.offset(w / 2, 2, 2), blk(RaBlocks.XUN_NICHE));
		put(level, corner.offset(w / 2, 1, 1), blk(RaBlocks.HALF_OFFERING_TABLE));
		chance(level, corner.offset(1, 1, 1), blk(RaBlocks.RUIN_LAMP_BASE), 0.55, r);
		chance(level, corner.offset(1, 1, 3), blk(RaBlocks.RUBBING_STELE), 0.35, r);
		chance(level, corner.offset(w - 2, 1, 1), blk(RaBlocks.SEALED_STONE_CASKET), 0.45, r);
		chance(level, corner.offset(w - 2, 1, 3), blk(RaBlocks.EMPTY_CERAMIC_URN), 0.4, r);
		chance(level, corner.offset(w / 2, 1, 4), blk(RaBlocks.BROKEN_BALUSTER), 0.4, r);

		if (r.nextDouble() < 0.5) {
			chestLoot(level, r, corner.offset(1, 1, 4), RuinDimensions.CELLAR_CHEST);
		}

		return true;
	}

	private boolean contractPit(WorldGenLevel level, BlockPos c, RandomSource r) {
		// 半埋的坑：刮掉地表，铺一层墟土与碎石，中间放石匣
		fill(level, c.offset(-2, 0, -2), 5, 1, 5, blk(RaBlocks.STREAM_SILT));
		fill(level, c.offset(-1, 0, -1), 3, 1, 3, blk(RaBlocks.RUBBLE_PILE));

		for (int x = -2; x <= 2; x++) {
			for (int z = -2; z <= 2; z++) {
				if (Math.abs(x) == 2 || Math.abs(z) == 2) {
					chance(level, c.offset(x, 1, z), blk(RaBlocks.BROKEN_CARVED_PAVING), 0.35, r);
				}
			}
		}

		put(level, c.offset(0, 1, 0), blk(RaBlocks.SEALED_STONE_CASKET));
		chance(level, c.offset(1, 1, 1), blk(RaBlocks.BROKEN_PILLAR_LOWER), 0.4, r);
		chance(level, c.offset(-1, 1, 1), blk(RaBlocks.RUBBLE_PILE), 0.5, r);
		chance(level, c.offset(1, 1, -1), blk(RaBlocks.STONE_PLINTH), 0.35, r);
		chance(level, c.offset(0, 2, -2), blk(RaBlocks.WITHER_REED), 0.35, r);
		chance(level, c.offset(2, 1, 0), blk(RaBlocks.SUNKEN_STELE), 0.25, r);
		return true;
	}

	private boolean starTerrace(WorldGenLevel level, BlockPos c, RandomSource r) {
		int w = 7;
		int d = 7;
		BlockPos corner = c.offset(-w / 2, 0, -d / 2);
		fill(level, corner, w, 1, d, blk(RaBlocks.CARVED_STONE_PAVING));
		fill(level, corner.offset(1, 1, 1), w - 2, 1, d - 2, blk(RaBlocks.CARVED_SLAB));

		// 四角残柱
		for (int i = 0; i < 4; i++) {
			int x = i % 2 == 0 ? 0 : w - 1;
			int z = i < 2 ? 0 : d - 1;
			put(level, corner.offset(x, 1, z), blk(RaBlocks.BROKEN_PILLAR_LOWER));
			if (r.nextDouble() < 0.5) {
				put(level, corner.offset(x, 2, z), blk(RaBlocks.BROKEN_PILLAR_UPPER));
			}
		}

		// 台心星纹基座与叙事碑
		put(level, corner.offset(w / 2, 1, d / 2), blk(RaBlocks.STAR_CARVED_BASE));
		put(level, corner.offset(w / 2, 2, d / 2), blk(RaBlocks.STORY_STELE));
		chance(level, corner.offset(w / 2, 1, 1), blk(RaBlocks.HALF_OFFERING_TABLE), 0.45, r);
		chance(level, corner.offset(1, 1, d / 2), blk(RaBlocks.RUIN_LAMP_BASE), 0.4, r);
		chance(level, corner.offset(w - 2, 1, d / 2), blk(RaBlocks.RUBBING_STELE), 0.35, r);
		chance(level, corner.offset(w / 2 + 1, 1, d - 2), blk(RaBlocks.ROTTEN_BEAM), 0.4, r);
		chance(level, corner.offset(w / 2 - 2, 2, d / 2 + 1), blk(RaBlocks.BROKEN_BALUSTER), 0.4, r);

		// 台阶与散落的观星残件
		for (int x = 1; x < w - 1; x += 2) {
			put(level, corner.offset(x, 0, -1), blk(RaBlocks.RUBBLE_STEP));
		}

		if (r.nextDouble() < 0.6) {
			chestLoot(level, r, corner.offset(w / 2, 1, 1), RuinDimensions.CELLAR_CHEST);
		}

		return true;
	}

	private boolean sunkenRuin(WorldGenLevel level, BlockPos c, RandomSource r) {
		int w = 5;
		int d = 5;
		BlockPos corner = c.offset(-2, 0, -2);
		fill(level, corner, w, 1, d, blk(RaBlocks.CARVED_STONE_PAVING));

		for (int x = 0; x < w; x++) {
			for (int z = 0; z < d; z++) {
				if (x == 0 || z == 0 || x == w - 1 || z == d - 1) {
					Sel brick = r.nextBoolean() ? blk(RaBlocks.WATERLOGGED_XUN_BRICK) : blk(RaBlocks.XUN_BRICK);
					put(level, corner.offset(x, 1, z), brick);
					if (r.nextDouble() < 0.55) {
						put(level, corner.offset(x, 2, z), brick);
					}
				}
			}
		}

		put(level, corner.offset(2, 1, 1), blk(RaBlocks.SUNKEN_STELE));
		put(level, corner.offset(1, 1, 2), blk(RaBlocks.EMPTY_CERAMIC_URN));
		chance(level, corner.offset(3, 1, 2), blk(RaBlocks.SEALED_CERAMIC_URN), 0.45, r);
		chance(level, corner.offset(2, 1, 3), blk(RaBlocks.XUN_LILY), 0.4, r);
		chance(level, corner.offset(0, 2, 2), blk(RaBlocks.BROKEN_PILLAR_LOWER), 0.5, r);
		chance(level, corner.offset(4, 2, 2), blk(RaBlocks.BROKEN_XUN_WALL), 0.5, r);
		chestLoot(level, r, corner.offset(2, 1, 2), RaChestLootTable.SUNKEN_RUIN);
		return true;
	}

	private record Air() implements Sel {
	}

	private record Blk(DeferredBlock<?> block) implements Sel {
	}

	private record Lit(BlockState state) implements Sel {
	}

	private sealed interface Sel permits RuinStructureFeature.Blk, RuinStructureFeature.Lit, RuinStructureFeature.Air {
	}

	public record Config(RuinStructureFeature.Kind kind) implements FeatureConfiguration {
		public static final Codec<Config> CODEC = RecordCodecBuilder.create(
				i -> i.group(Kind.CODEC.fieldOf("kind").forGetter(Config::kind)).apply(i, Config::new)
		);
	}

	public enum Kind implements StringRepresentable {
		FARM_HUT("farm_hut"),
		SHRINE("shrine"),
		FIELD_RIDGE("field_ridge"),
		LONE_STELE("lone_stele"),
		STREET_RUINS("street_ruins"),
		ANCIENT_WELL("ancient_well"),
		GOV_HALL("gov_hall"),
		ROAD_FORK("road_fork"),
		CELLAR("cellar"),
		WAY_KIOSK("way_kiosk"),
		GRAVE_MOUND("grave_mound"),
		BURIED_NICHE("buried_niche"),
		JADE_SHACK("jade_shack"),
		STREAM_STELE("stream_stele"),
		STREAM_ALTAR("stream_altar"),
		NICHE_SHRINE("niche_shrine"),
		CONTRACT_PIT("contract_pit"),
		STAR_TERRACE("star_terrace"),
		SUNKEN_RUIN("sunken_ruin");

		public static final Codec<Kind> CODEC = StringRepresentable.fromEnum(Kind::values);
		private final String name;

		Kind(String name) {
			this.name = name;
		}

		@Override
		public String getSerializedName() {
			return this.name;
		}
	}
}
