package com.liuran.ruinatlas.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 有朝向的废墟构件：案、桌、屏风、铜牌一类
 * <p>放置时朝向玩家（与熔炉等原版方块一致），选中框随之旋转。
 * blockstate 由 datagen 出四向变体，模型本体直接沿用现有手绘文件并做 y 旋转。
 */
public class RotatableRuinBlock extends RuinShapeBlock {
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

	private final VoxelShape northShape;
	private final VoxelShape[] rotated = new VoxelShape[4];

	public RotatableRuinBlock(Properties properties, VoxelShape northShape) {
		super(properties, northShape);
		this.northShape = northShape;
		registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return shapeFor(state.getValue(FACING));
	}

	private VoxelShape shapeFor(Direction facing) {
		int quarters = switch (facing) {
			case EAST -> 1;
			case SOUTH -> 2;
			case WEST -> 3;
			default -> 0;
		};

		VoxelShape shape = this.rotated[quarters];

		if (shape == null) {
			shape = rotateY(this.northShape, quarters);
			this.rotated[quarters] = shape;
		}

		return shape;
	}

	/** 绕竖轴顺时针旋转若干 90°（16 格坐标） */
	private static VoxelShape rotateY(VoxelShape shape, int quarters) {
		if (quarters == 0) {
			return shape;
		}

		VoxelShape result = Shapes.empty();

		for (AABB box : shape.toAabbs()) {
			double x1 = box.minX * 16.0D;
			double y1 = box.minY * 16.0D;
			double z1 = box.minZ * 16.0D;
			double x2 = box.maxX * 16.0D;
			double y2 = box.maxY * 16.0D;
			double z2 = box.maxZ * 16.0D;

			for (int i = 0; i < quarters; i++) {
				double nx1 = 16.0D - z2;
				double nz1 = x1;
				double nx2 = 16.0D - z1;
				double nz2 = x2;
				x1 = nx1;
				x2 = nx2;
				z1 = nz1;
				z2 = nz2;
			}

			result = Shapes.or(result, Block.box(x1, y1, z1, x2, y2, z2));
		}

		return result;
	}
}
