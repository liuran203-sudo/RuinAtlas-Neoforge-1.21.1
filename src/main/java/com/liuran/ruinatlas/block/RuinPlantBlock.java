package com.liuran.ruinatlas.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import com.liuran.ruinatlas.init.RaBlocks;

/**
 * 废墟植被：有选中框、没有碰撞箱、也不遮邻面
 * <p>只能种在土类地面上（水植另可种在水面）；下方地面被挖掉会自行脱落。
 * 注意：{@link #getShape} 不能返回空——射线选取用的是这个形状，
 * 返回 empty 会导致方块既无法瞄准也无法破坏。
 */
public class RuinPlantBlock extends Block {
	/** 通用植株选中框（与原版灌木一致的比例） */
	private static final VoxelShape PLANT_SHAPE = Shapes.box(0.125D, 0.0D, 0.125D, 0.875D, 0.8125D, 0.875D);

	private final boolean waterPlant;

	public RuinPlantBlock(Properties properties) {
		this(properties, false);
	}

	public RuinPlantBlock(Properties properties, boolean waterPlant) {
		super(properties);
		this.waterPlant = waterPlant;
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return PLANT_SHAPE;
	}

	@Override
	protected VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
		return Shapes.empty();
	}

	@Override
	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return mayPlaceOn(level.getBlockState(pos.below()));
	}

	/** 能承托植株的地面：土类、耕土与墟中土系；水植亦可浮于水面 */
	protected boolean mayPlaceOn(BlockState ground) {
		if (this.waterPlant && ground.is(Blocks.WATER)) {
			return true;
		}

		return ground.is(BlockTags.DIRT)
				|| ground.is(Blocks.FARMLAND)
				|| ground.is(Blocks.MUD)
				|| ground.is(Blocks.MOSS_BLOCK)
				|| ground.is(Blocks.CLAY)
				|| ground.is(RaBlocks.QINGYUAN_LOAM.get())
				|| ground.is(RaBlocks.XUN_SOIL.get())
				|| ground.is(RaBlocks.SETTLEMENT_SUBSOIL.get())
				|| ground.is(RaBlocks.GLOOM_MOSS_BLOCK.get())
				|| ground.is(RaBlocks.STREAM_SILT.get())
				|| ground.is(RaBlocks.FARM_RUIN_PLOT.get());
	}

	@Override
	protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
	                                 LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
		if (!state.canSurvive(level, pos)) {
			level.scheduleTick(pos, this, 1);
		}

		return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
	}

	@Override
	protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (!state.canSurvive(level, pos)) {
			level.destroyBlock(pos, true);
		}
	}
}
