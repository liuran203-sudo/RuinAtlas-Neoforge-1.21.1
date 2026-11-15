package com.liuran.ruinatlas.worldgen;

import java.util.List;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

/**
 * 地表散布植被 Feature：随机抛点后分三种情况落子
 * <ul>
 *   <li>地面替换：落点地块属于 grounds 且命中 replace_chance，则换成 ground_replace（如长苔）</li>
 *   <li>植被：落点为空气且正下方属于 grounds，从 plants 随机放一个</li>
 *   <li>水生植物：落点是水且上方空气，放到 water_plant</li>
 * </ul>
 */
public class ScatterVegetationFeature extends Feature<ScatterVegetationFeature.Config> {
	public ScatterVegetationFeature(Codec<Config> codec) {
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<Config> context) {
		Config cfg = context.config();
		WorldGenLevel level = context.level();
		BlockPos origin = context.origin();
		RandomSource random = context.random();
		boolean placed = false;

		for (int i = 0; i < cfg.tries(); i++) {
			BlockPos p = origin.offset(
					random.nextInt(cfg.xz() * 2 + 1) - cfg.xz(),
					random.nextInt(cfg.yDown() + cfg.yUp() + 1) - cfg.yDown(),
					random.nextInt(cfg.xz() * 2 + 1) - cfg.xz()
			);
			BlockState here = level.getBlockState(p);
			BlockState below = level.getBlockState(p.below());

			if (cfg.groundReplace().isPresent() && matches(cfg.grounds(), here) && random.nextFloat() < cfg.replaceChance()) {
				level.setBlock(p, cfg.groundReplace().get(), 2);
				placed = true;
			} else if (here.isAir() && matches(cfg.grounds(), below) && !cfg.plants().isEmpty()) {
				level.setBlock(p, cfg.plants().get(random.nextInt(cfg.plants().size())), 2);
				placed = true;
			} else if (cfg.waterPlant().isPresent() && here.is(Blocks.WATER) && level.getBlockState(p.above()).isAir()) {
				level.setBlock(p.above(), cfg.waterPlant().get(), 2);
				placed = true;
			}
		}

		return placed;
	}

	private static boolean matches(List<BlockState> states, BlockState actual) {
		for (BlockState state : states) {
			if (state.getBlock() == actual.getBlock()) {
				return true;
			}
		}

		return false;
	}

	public record Config(
			int tries,
			int xz,
			int yDown,
			int yUp,
			List<BlockState> plants,
			List<BlockState> grounds,
			Optional<BlockState> waterPlant,
			Optional<BlockState> groundReplace,
			float replaceChance
	) implements FeatureConfiguration {
		public static final Codec<Config> CODEC = RecordCodecBuilder.create(
				i -> i.group(
						Codec.INT.fieldOf("tries").forGetter(Config::tries),
						Codec.INT.fieldOf("xz").forGetter(Config::xz),
						Codec.INT.fieldOf("y_down").forGetter(Config::yDown),
						Codec.INT.fieldOf("y_up").forGetter(Config::yUp),
						BlockState.CODEC.listOf().fieldOf("plants").forGetter(Config::plants),
						BlockState.CODEC.listOf().fieldOf("grounds").forGetter(Config::grounds),
						BlockState.CODEC.optionalFieldOf("water_plant").forGetter(Config::waterPlant),
						BlockState.CODEC.optionalFieldOf("ground_replace").forGetter(Config::groundReplace),
						Codec.FLOAT.optionalFieldOf("replace_chance", 0.08F).forGetter(Config::replaceChance)
				).apply(i, Config::new)
		);
	}
}
