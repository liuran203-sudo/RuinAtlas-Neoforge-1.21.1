package com.liuran.ruinatlas.block;

import com.liuran.ruinatlas.blockentity.SpiritConduitBlockEntity;
import com.liuran.ruinatlas.init.RaItems;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 灵息引石：立在墟龛附近，把龛里供奉的灵息牵引得更远
 * <p>内部存一份引石灵息，每次供奉被抽走一点；用灵息原石/藏灵石右键可以为它续灵。
 * 多块引石只有在有效蓄灵时才算数，且每次供奉最多受益三块，避免无限堆叠。
 */
public class SpiritConduitBlock extends BaseEntityBlock {
	public static final MapCodec<SpiritConduitBlock> CODEC = simpleCodec(SpiritConduitBlock::new);
	private static final VoxelShape SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 12.0, 13.0);

	public SpiritConduitBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	protected RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
	                                          Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (!(level.getBlockEntity(pos) instanceof SpiritConduitBlockEntity conduit)) {
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}

		int charge = 0;
		if (stack.is(RaItems.RAW_SPIRIT_STONE.get())) {
			charge = 20;
		} else if (stack.is(RaItems.SPIRIT_HOLDING_STONE.get())) {
			charge = 40;
		}

		if (charge <= 0) {
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}

		if (level.isClientSide) {
			return ItemInteractionResult.CONSUME;
		}

		conduit.setCharge(conduit.getCharge() + charge);
		stack.shrink(1);
		player.displayClientMessage(Component.translatable("block." + MODID + ".spirit_conduit.charged",
				charge, conduit.getCharge()), true);
		level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 0.7F, 0.9F);

		return ItemInteractionResult.SUCCESS;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (!level.isClientSide) {
			if (level.getBlockEntity(pos) instanceof SpiritConduitBlockEntity conduit) {
				player.displayClientMessage(Component.translatable("block." + MODID + ".spirit_conduit.charge",
						conduit.getCharge(), SpiritConduitBlockEntity.MAX_CHARGE), true);
			}
		}

		return InteractionResult.sidedSuccess(level.isClientSide);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SpiritConduitBlockEntity(pos, state);
	}
}
