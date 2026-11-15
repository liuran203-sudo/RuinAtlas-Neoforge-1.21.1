package com.liuran.ruinatlas.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 固定碰撞箱方块：废墟构件、残碑、底座等都用它
 */
public class RuinShapeBlock extends Block {
	private final VoxelShape shape;

	public RuinShapeBlock(Properties properties, VoxelShape shape) {
		super(properties);
		this.shape = shape;
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return this.shape;
	}
}
