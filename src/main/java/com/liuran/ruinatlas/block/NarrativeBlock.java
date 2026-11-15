package com.liuran.ruinatlas.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 叙事方块：右键弹出一段碑文或旧事
 */
public class NarrativeBlock extends RuinShapeBlock {
	private final Component text;

	public NarrativeBlock(Properties properties, VoxelShape shape, String translationKey) {
		super(properties, shape);
		this.text = Component.translatable(translationKey);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (!level.isClientSide) {
			player.displayClientMessage(this.text, false);
			level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 0.5F, 1.1F);
		}

		return InteractionResult.sidedSuccess(level.isClientSide);
	}
}
