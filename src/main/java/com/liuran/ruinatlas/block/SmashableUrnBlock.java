package com.liuran.ruinatlas.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * 封藏陶瓮：内部同样能存物，但空手右键会直接击破，内容物随方块一起掉落
 */
public class SmashableUrnBlock extends CeramicUrnBlock {
	public static final MapCodec<SmashableUrnBlock> CODEC = simpleCodec(SmashableUrnBlock::new);

	public SmashableUrnBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
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
