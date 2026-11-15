package com.liuran.ruinatlas.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Vector3f;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 测灵台：核验修炼适宜度，结果恒为 0
 */
public class SpiritTestAltarBlock extends RuinShapeBlock {
	private static final DustParticleOptions SPIRIT_DUST = new DustParticleOptions(new Vector3f(0.45F, 0.85F, 0.8F), 1.0F);

	public SpiritTestAltarBlock(Properties properties, VoxelShape shape) {
		super(properties, shape);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (level.isClientSide) {
			RandomSource random = level.getRandom();

			for (int i = 0; i < 20; i++) {
				double angle = random.nextFloat() * (float) (Math.PI * 2);
				double radius = 0.55 + random.nextFloat() * 0.2;
				level.addParticle(
						SPIRIT_DUST,
						pos.getX() + 0.5 + Math.cos(angle) * radius,
						pos.getY() + 0.58 + random.nextFloat() * 0.08,
						pos.getZ() + 0.5 + Math.sin(angle) * radius,
						0.0,
						0.015,
						0.0
				);
			}
		} else {
			player.displayClientMessage(Component.translatable("block." + MODID + ".spirit_test_altar.result"), true);
			level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 0.6F, 0.8F);
		}

		return InteractionResult.sidedSuccess(level.isClientSide);
	}
}
