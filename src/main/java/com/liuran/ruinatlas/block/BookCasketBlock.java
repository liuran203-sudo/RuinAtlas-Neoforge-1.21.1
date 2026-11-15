package com.liuran.ruinatlas.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;

import com.liuran.ruinatlas.init.RaItems;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 墟藏书椟：开启后弹出拓片，自身消失
 */
public class BookCasketBlock extends RuinShapeBlock {
	public BookCasketBlock(Properties properties, VoxelShape shape) {
		super(properties, shape);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (!level.isClientSide) {
			Block.popResource(level, pos, new ItemStack(RaItems.STELE_RUBBING.get()));
			if (level.random.nextFloat() < 0.4F) {
				Block.popResource(level, pos, new ItemStack(RaItems.CLOUD_STEP_RUBBING.get()));
			}

			level.playSound(null, pos, SoundEvents.CHEST_OPEN, SoundSource.BLOCKS, 0.7F, 0.9F);
			level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
			player.displayClientMessage(Component.translatable("block." + MODID + ".ancient_book_casket.opened"), true);
		}

		return InteractionResult.sidedSuccess(level.isClientSide);
	}
}
