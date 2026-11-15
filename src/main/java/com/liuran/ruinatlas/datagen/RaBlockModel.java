package com.liuran.ruinatlas.datagen;

import java.util.Set;

import com.liuran.ruinatlas.block.RotatableRuinBlock;
import com.liuran.ruinatlas.init.RaBlocks;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 方块状态与方块模型生成器
 * <p>
 * 分三类处理：
 * <ol>
 *   <li>手绘自定义模型（Blockbench 导出，放在 src/main/resources/assets/ruinatlas/models/block）
 *       —— 只生成 blockstate 与 item 模型，模型本体直接引用现成文件，不再覆盖</li>
 *   <li>古木（RotatedPillarBlock）—— 走 cube_column + axis 变体 × 3</li>
 *   <li>其余 —— 统一 cube_all 占位</li>
 * </ol>
 */
public class RaBlockModel extends BlockStateProvider {
	/** 已有手绘模型的方块，模型文件在 src/main/resources，datagen 不能覆盖 */
	private static final Set<String> CUSTOM_MODELS = Set.of(
			"ancient_book_casket",
			"baluster_solid",
			"blank_stele",
			"bone_pile",
			"bone_sacrifice_table",
			"boundary_stele",
			"bracket_shard_corner",
			"bracket_shard_side",
			"broken_baluster",
			"broken_lattice",
			"broken_pillar_lower",
			"broken_pillar_upper",
			"broken_tile_pile",
			"broken_xun_wall",
			"cangyin_gate_stele",
			"cangyin_offering_table",
			"carved_step",
			"city_curb",
			"collapsed_kiosk",
			"door_frame_ruin",
			"eave_board_shard",
			"eave_ornament",
			"empty_ceramic_urn",
			"farm_ruin_plot",
			"foundation_skirt",
			"inscribed_stele",
			"lattice_wave_window",
			"lattice_window_square",
			"lotus_plinth",
			"market_rubble",
			"newel_post",
			"pillar_brace_shard",
			"plain_plinth",
			"ridge_ornament_shard",
			"ridge_tile",
			"rusted_bronze_panel",
			"rubble_pile",
			"sealed_ceramic_urn",
			"spirit_platform",
			"spirit_test_altar",
			"step_stone",
			"stone_incense_table",
			"stone_lantern",
			"stone_plinth",
			"thatch_rubble",
			"wall_niche_small",
			"waystone",
			"wooden_pillar_lower",
			"wooden_pillar_upper",
			"wooden_screen"
	);

	/** 植被方块：走原版 cross（十字）模型，不用 cube_all */
	private static final Set<String> PLANT_BLOCKS = Set.of(
			"petal_sprout",
			"petal_thicket",
			"wither_reed",
			"xun_lily",
			"xun_vine",
			"xun_wild_grass"
	);

	/** 有朝向的陈设：案、桌、屏风、铜牌，blockstate 出四向变体 */
	private static final Set<String> FACING_BLOCKS = Set.of(
			"bone_sacrifice_table",
			"cangyin_offering_table",
			"rusted_bronze_panel",
			"stone_incense_table",
			"wooden_screen"
	);

	public RaBlockModel(PackOutput output, ExistingFileHelper exFileHelper) {
		super(output, MODID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		for (var entry : RaBlocks.BLOCK_REGISTER.getEntries()) {
			Block block = entry.get();
			String path = entry.getId().getPath();

			if (block == RaBlocks.CANGYIN_LOG.get()) {
				// 古木单独处理，见下方
				continue;
			}

			if (FACING_BLOCKS.contains(path)) {
				ModelFile model = models().getExistingFile(ResourceLocation.fromNamespaceAndPath(MODID, "block/" + path));
				var builder = getVariantBuilder(block);
				builder.partialState().with(RotatableRuinBlock.FACING, Direction.NORTH)
						.modelForState().modelFile(model).rotationY(0).addModel();
				builder.partialState().with(RotatableRuinBlock.FACING, Direction.EAST)
						.modelForState().modelFile(model).rotationY(90).addModel();
				builder.partialState().with(RotatableRuinBlock.FACING, Direction.SOUTH)
						.modelForState().modelFile(model).rotationY(180).addModel();
				builder.partialState().with(RotatableRuinBlock.FACING, Direction.WEST)
						.modelForState().modelFile(model).rotationY(270).addModel();
				simpleBlockItem(block, model);
				continue;
			}

			if (CUSTOM_MODELS.contains(path)) {
				simpleBlockWithItem(block, models().getExistingFile(ResourceLocation.fromNamespaceAndPath(MODID, "block/" + path)));
				continue;
			}

			if (PLANT_BLOCKS.contains(path)) {
				// 植被走原版 cross 模型：十字交叉片 + cutout，物品栏用平面贴图
				ModelFile cross = models().cross(path, blockTexture(block)).renderType("cutout");
				simpleBlock(block, cross);
				itemModels().withExistingParent(path, mcLoc("item/generated")).texture("layer0", blockTexture(block));
				continue;
			}

			simpleBlockWithItem(block, cubeAll(block));
		}

		RotatedPillarBlock log = RaBlocks.CANGYIN_LOG.get();
		ResourceLocation side = modLoc("block/cangyin_log_side");
		ResourceLocation end = modLoc("block/cangyin_log_top");
		ModelFile logModel = models().cubeColumn("cangyin_log", side, end);
		ModelFile logHorizontal = models().cubeColumnHorizontal("cangyin_log_horizontal", side, end);
		axisBlock(log, logModel, logHorizontal);
		simpleBlockItem(log, logModel);
	}
}
