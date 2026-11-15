package com.liuran.ruinatlas.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 可击碎的陶罐：右键直接击破
 */
public class SmashableUrnBlock extends RuinShapeBlock {
	public SmashableUrnBlock(Properties properties, VoxelShape shape) {
		super(properties, shape);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (!level.isClientSide) {
			level.playSound(null, pos, SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 0.9F, 0.8F);
			level.destroyBlock(pos, true, player);
		}

		return InteractionResult.sidedSuccess(level.isClientSide);
	}
}
