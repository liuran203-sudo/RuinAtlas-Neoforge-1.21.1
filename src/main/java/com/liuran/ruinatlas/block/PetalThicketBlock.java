package com.liuran.ruinatlas.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;

/**
 * 落英丛：偶尔飘落淡粉花瓣尘
 */
public class PetalThicketBlock extends RuinPlantBlock {
	private static final DustParticleOptions PETAL = new DustParticleOptions(new Vector3f(0.93F, 0.72F, 0.78F), 0.9F);

	public PetalThicketBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (random.nextFloat() < 0.12F) {
			double x = pos.getX() + 0.2 + random.nextFloat() * 0.6;
			double y = pos.getY() + 0.6 + random.nextFloat() * 0.5;
			double z = pos.getZ() + 0.2 + random.nextFloat() * 0.6;
			level.addParticle(PETAL, x, y, z, 0.0, -0.012, 0.0);
		}
	}
}
