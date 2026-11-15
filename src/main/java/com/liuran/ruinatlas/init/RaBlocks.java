package com.liuran.ruinatlas.init;

import com.liuran.ruinatlas.block.BlessingNicheBlock;
import com.liuran.ruinatlas.block.BookCasketBlock;
import com.liuran.ruinatlas.block.CeramicUrnBlock;
import com.liuran.ruinatlas.block.GateSteleBlock;
import com.liuran.ruinatlas.block.NarrativeBlock;
import com.liuran.ruinatlas.block.PetalThicketBlock;
import com.liuran.ruinatlas.block.RuinPlantBlock;
import com.liuran.ruinatlas.block.RotatableRuinBlock;
import com.liuran.ruinatlas.block.RuinShapeBlock;
import com.liuran.ruinatlas.block.SmashableUrnBlock;
import com.liuran.ruinatlas.block.SpiritTestAltarBlock;

import com.liuran.ruinatlas.block.InscriptionSteleBlock;
import com.liuran.ruinatlas.block.SpiritConduitBlock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

public class RaBlocks {
	public static final DeferredRegister.Blocks BLOCK_REGISTER = DeferredRegister.createBlocks(MODID);

	// ===== 自然原生方块：地表与岩土 =====
	public static final DeferredBlock<Block> XUN_SOIL = BLOCK_REGISTER.registerBlock("xun_soil", Block::new, Properties.of().sound(SoundType.GRAVEL).strength(0.6F));
	public static final DeferredBlock<Block> XUN_LAYER_ROCK = BLOCK_REGISTER.registerBlock("xun_layer_rock", Block::new, stone(1.6F, 7.0F));
	public static final DeferredBlock<Block> ERODED_STONE = BLOCK_REGISTER.registerBlock("eroded_stone", Block::new, stone(2.0F, 9.0F));

	// ===== 自然原生方块：古木与林木 =====
	public static final DeferredBlock<RotatedPillarBlock> CANGYIN_LOG = BLOCK_REGISTER.registerBlock("cangyin_log", RotatedPillarBlock::new, wood(2.0F));
	public static final DeferredBlock<Block> CANGYIN_BARK = BLOCK_REGISTER.registerBlock("cangyin_bark", Block::new, wood(2.0F));
	public static final DeferredBlock<Block> CANGYIN_PLANKS = BLOCK_REGISTER.registerBlock("cangyin_planks", Block::new, wood(2.0F));

	// ===== 自然原生方块：植被与野生植物 =====
	public static final DeferredBlock<Block> XUN_WILD_GRASS = BLOCK_REGISTER.registerBlock("xun_wild_grass", RuinPlantBlock::new, plant());
	public static final DeferredBlock<Block> XUN_VINE = BLOCK_REGISTER.registerBlock("xun_vine", RuinPlantBlock::new, plant().sound(SoundType.VINE));
	public static final DeferredBlock<Block> XUN_LILY = BLOCK_REGISTER.registerBlock("xun_lily", props -> new RuinPlantBlock(props, true), plant());
	public static final DeferredBlock<Block> GLOOM_MOSS_BLOCK = BLOCK_REGISTER.registerBlock(
			"gloom_moss_block", Block::new, Properties.of().sound(SoundType.MOSS).strength(0.1F)
	);
	public static final DeferredBlock<Block> PETAL_THICKET = BLOCK_REGISTER.registerBlock("petal_thicket", PetalThicketBlock::new, plant().sound(SoundType.PINK_PETALS));
	public static final DeferredBlock<Block> WITHER_REED = BLOCK_REGISTER.registerBlock("wither_reed", RuinPlantBlock::new, plant().sound(SoundType.BAMBOO));

	// ===== 古建装饰方块：墙体与砖材 =====
	public static final DeferredBlock<Block> XUN_BRICK = BLOCK_REGISTER.registerBlock("xun_brick", Block::new, stone(1.5F, 6.0F));
	public static final DeferredBlock<Block> WEATHERED_XUN_BRICK = BLOCK_REGISTER.registerBlock("weathered_xun_brick", Block::new, stone(1.4F, 6.0F));
	public static final DeferredBlock<Block> WATERLOGGED_XUN_BRICK = BLOCK_REGISTER.registerBlock("waterlogged_xun_brick", Block::new, stone(1.4F, 6.0F));
	public static final DeferredBlock<Block> CRACKED_XUN_BRICK = BLOCK_REGISTER.registerBlock("cracked_xun_brick", Block::new, stone(1.2F, 5.0F));
	public static final DeferredBlock<Block> POLISHED_XUN_STONE = BLOCK_REGISTER.registerBlock("polished_xun_stone", Block::new, stone(1.6F, 7.0F));
	public static final DeferredBlock<Block> BROKEN_XUN_WALL = BLOCK_REGISTER.registerBlock(
			"broken_xun_wall", props -> new RotatableRuinBlock(props, Block.box(0.0, 0.0, 5.0, 16.0, 12.0, 11.0)), panel(SoundType.STONE)
	);

	// ===== 古建装饰方块：屋面构件 =====
	public static final DeferredBlock<Block> TILE_BLOCK = BLOCK_REGISTER.registerBlock("tile_block", Block::new, stone(1.4F, 6.0F));
	public static final DeferredBlock<Block> BROKEN_TILE_PILE = BLOCK_REGISTER.registerBlock(
			"broken_tile_pile", props -> new RuinShapeBlock(props, Block.box(1.0, 0.0, 1.0, 15.0, 8.0, 15.0)), panel(SoundType.GRAVEL)
	);
	public static final DeferredBlock<Block> EAVE_ORNAMENT = BLOCK_REGISTER.registerBlock(
			"eave_ornament", props -> new RuinShapeBlock(props, Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0)), panel(SoundType.STONE)
	);

	// ===== 古建装饰方块：石作与碑刻 =====
	private static final VoxelShape STELE_SHAPE = Block.box(3.0, 0.0, 6.0, 13.0, 14.0, 10.0);
	public static final DeferredBlock<Block> BLANK_STELE = BLOCK_REGISTER.registerBlock(
			"blank_stele", props -> new RotatableRuinBlock(props, STELE_SHAPE), panel(SoundType.STONE)
	);
	public static final DeferredBlock<Block> INSCRIBED_STELE = BLOCK_REGISTER.registerBlock(
			"inscribed_stele", props -> new RotatableRuinBlock(props, STELE_SHAPE), panel(SoundType.STONE)
	);
	public static final DeferredBlock<Block> BOUNDARY_STELE = BLOCK_REGISTER.registerBlock(
			"boundary_stele",
			props -> new NarrativeBlock(props, Block.box(2.0, 0.0, 5.0, 14.0, 16.0, 11.0), "block.ruinatlas.boundary_stele.text"),
			panel(SoundType.STONE)
	);
	public static final DeferredBlock<Block> SPIRIT_TEST_ALTAR = BLOCK_REGISTER.registerBlock(
			"spirit_test_altar", props -> new SpiritTestAltarBlock(props, Block.box(0.0, 0.0, 0.0, 16.0, 9.0, 16.0)), panel(SoundType.STONE)
	);
	public static final DeferredBlock<Block> STAR_CARVED_BASE = BLOCK_REGISTER.registerBlock("star_carved_base", Block::new, hardStone(1.8F, 7.0F));

	// ===== 古建装饰方块：祭祀与陈设 =====
	private static final VoxelShape TABLE_SHAPE = Shapes.or(Block.box(0.0, 10.0, 0.0, 16.0, 12.0, 16.0), Block.box(1.0, 0.0, 1.0, 4.0, 10.0, 4.0));
	public static final DeferredBlock<Block> STONE_INCENSE_TABLE = BLOCK_REGISTER.registerBlock(
			"stone_incense_table", props -> new RotatableRuinBlock(props, TABLE_SHAPE), panel(SoundType.STONE)
	);
	public static final DeferredBlock<Block> STONE_LANTERN = BLOCK_REGISTER.registerBlock(
			"stone_lantern", props -> new RuinShapeBlock(props, Block.box(4.0, 0.0, 4.0, 12.0, 14.0, 12.0)), panel(SoundType.STONE).lightLevel(state -> 12)
	);
	public static final DeferredBlock<Block> EMPTY_CERAMIC_URN = BLOCK_REGISTER.registerBlock(
			"empty_ceramic_urn", CeramicUrnBlock::new, panel(SoundType.DECORATED_POT)
	);
	public static final DeferredBlock<Block> SEALED_CERAMIC_URN = BLOCK_REGISTER.registerBlock(
			"sealed_ceramic_urn", SmashableUrnBlock::new, panel(SoundType.DECORATED_POT)
	);
	public static final DeferredBlock<Block> BONE_SACRIFICE_TABLE = BLOCK_REGISTER.registerBlock(
			"bone_sacrifice_table", props -> new RotatableRuinBlock(props, TABLE_SHAPE), panel(SoundType.BONE_BLOCK)
	);
	public static final DeferredBlock<Block> CANGYIN_OFFERING_TABLE = BLOCK_REGISTER.registerBlock(
			"cangyin_offering_table", props -> new RotatableRuinBlock(props, TABLE_SHAPE), panel(SoundType.WOOD)
	);

	// ===== 古建装饰方块：小型陈设 =====
	public static final DeferredBlock<Block> WOODEN_SCREEN = BLOCK_REGISTER.registerBlock(
			"wooden_screen", props -> new RotatableRuinBlock(props, Block.box(0.0, 0.0, 7.0, 16.0, 15.0, 9.0)), panel(SoundType.WOOD)
	);
	public static final DeferredBlock<Block> STONE_PLINTH = BLOCK_REGISTER.registerBlock(
			"stone_plinth",
			props -> new RuinShapeBlock(props, Shapes.or(Block.box(1.0, 0.0, 1.0, 15.0, 4.0, 15.0), Block.box(3.0, 4.0, 3.0, 13.0, 16.0, 13.0))),
			panel(SoundType.STONE)
	);
	public static final DeferredBlock<Block> BROKEN_PILLAR_LOWER = BLOCK_REGISTER.registerBlock(
			"broken_pillar_lower", props -> new RuinShapeBlock(props, Block.box(3.0, 0.0, 3.0, 13.0, 16.0, 13.0)), panel(SoundType.STONE)
	);
	public static final DeferredBlock<Block> BROKEN_PILLAR_UPPER = BLOCK_REGISTER.registerBlock(
			"broken_pillar_upper", props -> new RuinShapeBlock(props, Block.box(3.0, 0.0, 3.0, 13.0, 13.0, 13.0)), panel(SoundType.STONE)
	);
	public static final DeferredBlock<Block> RUBBLE_PILE = BLOCK_REGISTER.registerBlock(
			"rubble_pile", props -> new RuinShapeBlock(props, Block.box(2.0, 0.0, 2.0, 14.0, 6.0, 14.0)), panel(SoundType.GRAVEL)
	);
	public static final DeferredBlock<Block> BONE_PILE = BLOCK_REGISTER.registerBlock(
			"bone_pile", props -> new RuinShapeBlock(props, Block.box(2.0, 0.0, 2.0, 14.0, 6.0, 14.0)), panel(SoundType.BONE_BLOCK)
	);
	public static final DeferredBlock<Block> RUSTED_BRONZE_PANEL = BLOCK_REGISTER.registerBlock(
			"rusted_bronze_panel", props -> new RotatableRuinBlock(props, Block.box(0.0, 0.0, 6.0, 16.0, 16.0, 10.0)), panel(SoundType.COPPER)
	);

	// ===== 功能交互方块 =====
	public static final DeferredBlock<Block> XUN_NICHE = BLOCK_REGISTER.registerBlock("xun_niche", BlessingNicheBlock::new, panel(SoundType.STONE));
	public static final DeferredBlock<Block> SPIRIT_PLATFORM = BLOCK_REGISTER.registerBlock(
			"spirit_platform",
			props -> new NarrativeBlock(props, Block.box(1.0, 0.0, 1.0, 15.0, 7.0, 15.0), "block.ruinatlas.spirit_platform.text"),
			panel(SoundType.STONE)
	);
	public static final DeferredBlock<Block> ANCIENT_BOOK_CASKET = BLOCK_REGISTER.registerBlock(
			"ancient_book_casket", props -> new BookCasketBlock(props, Block.box(2.0, 0.0, 2.0, 14.0, 9.0, 14.0)), panel(SoundType.WOOD)
	);

	// ===== 群系专属地基：陈国旧疆 =====
	public static final DeferredBlock<Block> QINGYUAN_LOAM = BLOCK_REGISTER.registerBlock(
			"qingyuan_loam", Block::new, Properties.of().sound(SoundType.GRAVEL).strength(0.5F, 2.0F)
	);
	public static final DeferredBlock<Block> PETAL_SPROUT = BLOCK_REGISTER.registerBlock("petal_sprout", RuinPlantBlock::new, plant().sound(SoundType.PINK_PETALS));
	public static final DeferredBlock<Block> FARM_RUIN_PLOT = BLOCK_REGISTER.registerBlock(
			"farm_ruin_plot",
			props -> new RuinShapeBlock(props, Block.box(1.0, 0.0, 1.0, 15.0, 9.0, 15.0)),
			Properties.of().sound(SoundType.GRAVEL).strength(0.5F, 2.0F).noOcclusion()
	);
	public static final DeferredBlock<Block> THATCH_RUBBLE = BLOCK_REGISTER.registerBlock(
			"thatch_rubble",
			props -> new RuinShapeBlock(props, Block.box(2.0, 0.0, 2.0, 14.0, 8.0, 14.0)),
			Properties.of().sound(SoundType.GRASS).strength(0.4F).noOcclusion()
	);
	public static final DeferredBlock<Block> SETTLEMENT_SUBSOIL = BLOCK_REGISTER.registerBlock(
			"settlement_subsoil", Block::new, Properties.of().sound(SoundType.GRAVEL).strength(0.8F, 2.0F)
	);
	public static final DeferredBlock<Block> MARKET_RUBBLE = BLOCK_REGISTER.registerBlock(
			"market_rubble", props -> new RuinShapeBlock(props, Block.box(1.0, 0.0, 1.0, 15.0, 7.0, 15.0)), panel(SoundType.GRAVEL)
	);
	public static final DeferredBlock<Block> CITY_CURB = BLOCK_REGISTER.registerBlock(
			"city_curb", props -> new RuinShapeBlock(props, Block.box(0.0, 0.0, 0.0, 16.0, 5.0, 16.0)), panel(SoundType.STONE)
	);
	public static final DeferredBlock<Block> ROAD_RUBBLE = BLOCK_REGISTER.registerBlock(
			"road_rubble", Block::new, Properties.of().sound(SoundType.GRAVEL).strength(0.6F, 2.0F)
	);
	public static final DeferredBlock<Block> WAYSTONE = BLOCK_REGISTER.registerBlock(
			"waystone", props -> new NarrativeBlock(props, Block.box(4.0, 0.0, 6.0, 12.0, 14.0, 10.0), "block.ruinatlas.waystone.text"), panel(SoundType.STONE)
	);
	public static final DeferredBlock<Block> COLLAPSED_KIOSK = BLOCK_REGISTER.registerBlock(
			"collapsed_kiosk",
			props -> new RuinShapeBlock(
					props,
					Shapes.or(
							Block.box(2.0, 0.0, 2.0, 4.0, 11.0, 4.0),
							new VoxelShape[]{Block.box(12.0, 0.0, 12.0, 14.0, 11.0, 14.0), Block.box(0.0, 11.0, 0.0, 16.0, 13.0, 16.0)}
					)
			),
			panel(SoundType.WOOD)
	);
	public static final DeferredBlock<Block> STREAM_SILT = BLOCK_REGISTER.registerBlock(
			"stream_silt", Block::new, Properties.of().sound(SoundType.GRAVEL).strength(0.4F, 1.5F)
	);
	public static final DeferredBlock<Block> JADE_CHIP_ROCK = BLOCK_REGISTER.registerBlock("jade_chip_rock", Block::new, hardStone(2.0F, 9.0F));

	// ===== 中式古建构件：台基与铺地 =====
	public static final DeferredBlock<Block> CARVED_STONE_PAVING = BLOCK_REGISTER.registerBlock("carved_stone_paving", Block::new, stone(1.5F, 6.0F));
	public static final DeferredBlock<Block> BROKEN_CARVED_PAVING = BLOCK_REGISTER.registerBlock("broken_carved_paving", Block::new, stone(1.2F, 5.0F));
	public static final DeferredBlock<Block> STEP_STONE = BLOCK_REGISTER.registerBlock(
			"step_stone", SlabBlock::new, stone(1.5F, 6.0F)
	);
	public static final DeferredBlock<Block> CARVED_STEP = BLOCK_REGISTER.registerBlock(
			"carved_step", SlabBlock::new, stone(1.5F, 6.0F)
	);
	public static final DeferredBlock<Block> FOUNDATION_SKIRT = BLOCK_REGISTER.registerBlock(
			"foundation_skirt", props -> new RuinShapeBlock(props, Block.box(0.0, 0.0, 0.0, 16.0, 5.0, 16.0)), panel(SoundType.STONE)
	);

	// ===== 中式古建构件：柱作 =====
	public static final DeferredBlock<Block> LOTUS_PLINTH = BLOCK_REGISTER.registerBlock(
			"lotus_plinth",
			props -> new RuinShapeBlock(props, Shapes.or(Block.box(1.0, 0.0, 1.0, 15.0, 5.0, 15.0), Block.box(3.0, 5.0, 3.0, 13.0, 16.0, 13.0))),
			panel(SoundType.STONE)
	);
	public static final DeferredBlock<Block> PLAIN_PLINTH = BLOCK_REGISTER.registerBlock(
			"plain_plinth",
			props -> new RuinShapeBlock(props, Shapes.or(Block.box(2.0, 0.0, 2.0, 14.0, 4.0, 14.0), Block.box(4.0, 4.0, 4.0, 12.0, 16.0, 12.0))),
			panel(SoundType.STONE)
	);
	public static final DeferredBlock<Block> WOODEN_PILLAR_LOWER = BLOCK_REGISTER.registerBlock(
			"wooden_pillar_lower", props -> new RuinShapeBlock(props, Block.box(4.0, 0.0, 4.0, 12.0, 16.0, 12.0)), panel(SoundType.WOOD)
	);
	public static final DeferredBlock<Block> WOODEN_PILLAR_UPPER = BLOCK_REGISTER.registerBlock(
			"wooden_pillar_upper", props -> new RuinShapeBlock(props, Block.box(4.0, 0.0, 4.0, 12.0, 16.0, 12.0)), panel(SoundType.WOOD)
	);
	public static final DeferredBlock<Block> PILLAR_BRACE_SHARD = BLOCK_REGISTER.registerBlock(
			"pillar_brace_shard", props -> new RuinShapeBlock(props, Block.box(2.0, 0.0, 2.0, 14.0, 5.0, 14.0)), panel(SoundType.COPPER)
	);

	// ===== 中式古建构件：栏杆与望柱 =====
	public static final DeferredBlock<Block> BALUSTER_SOLID = BLOCK_REGISTER.registerBlock(
			"baluster_solid", props -> new RotatableRuinBlock(props, Block.box(0.0, 0.0, 6.0, 16.0, 12.0, 10.0)), panel(SoundType.STONE)
	);
	public static final DeferredBlock<Block> BROKEN_BALUSTER = BLOCK_REGISTER.registerBlock(
			"broken_baluster", props -> new RotatableRuinBlock(props, Block.box(0.0, 0.0, 6.0, 8.0, 9.0, 10.0)), panel(SoundType.STONE)
	);
	public static final DeferredBlock<Block> NEWEL_POST = BLOCK_REGISTER.registerBlock(
			"newel_post",
			props -> new RuinShapeBlock(props, Shapes.or(Block.box(5.0, 0.0, 5.0, 11.0, 12.0, 11.0), Block.box(6.0, 12.0, 6.0, 10.0, 15.0, 10.0))),
			panel(SoundType.STONE)
	);

	// ===== 中式古建构件：花窗与棂格 =====
	private static final VoxelShape WINDOW_FRAME = Shapes.or(
			Block.box(0.0, 13.0, 7.0, 16.0, 16.0, 9.0),
			new VoxelShape[]{Block.box(0.0, 0.0, 7.0, 16.0, 3.0, 9.0), Block.box(0.0, 0.0, 7.0, 3.0, 16.0, 9.0), Block.box(13.0, 0.0, 7.0, 16.0, 16.0, 9.0)}
	);
	public static final DeferredBlock<Block> LATTICE_WINDOW_SQUARE = BLOCK_REGISTER.registerBlock(
			"lattice_window_square",
			props -> new RotatableRuinBlock(
					props,
					Shapes.or(
							WINDOW_FRAME,
							new VoxelShape[]{Block.box(5.0, 3.0, 7.5, 6.0, 13.0, 8.5), Block.box(10.0, 3.0, 7.5, 11.0, 13.0, 8.5), Block.box(3.0, 7.0, 7.5, 13.0, 8.0, 8.5)}
					)
			),
			panel(SoundType.STONE)
	);
	public static final DeferredBlock<Block> LATTICE_WAVE_WINDOW = BLOCK_REGISTER.registerBlock(
			"lattice_wave_window",
			props -> new RotatableRuinBlock(
					props,
					Shapes.or(
							WINDOW_FRAME,
							new VoxelShape[]{
									Block.box(3.0, 2.0, 7.5, 4.0, 10.0, 8.5),
									Block.box(6.0, 4.0, 7.5, 7.0, 12.0, 8.5),
									Block.box(9.0, 6.0, 7.5, 10.0, 14.0, 8.5),
									Block.box(12.0, 3.0, 7.5, 13.0, 11.0, 8.5)
							}
					)
			),
			panel(SoundType.STONE)
	);
	public static final DeferredBlock<Block> BROKEN_LATTICE = BLOCK_REGISTER.registerBlock(
			"broken_lattice", props -> new RotatableRuinBlock(props, Shapes.or(WINDOW_FRAME, Block.box(12.0, 3.0, 7.5, 13.0, 13.0, 8.5))), panel(SoundType.STONE)
	);

	// ===== 中式古建构件：斗拱残件 =====
	public static final DeferredBlock<Block> BRACKET_SHARD_CORNER = BLOCK_REGISTER.registerBlock(
			"bracket_shard_corner",
			props -> new RotatableRuinBlock(
					props,
					Shapes.or(
							Block.box(2.0, 0.0, 2.0, 14.0, 4.0, 14.0), new VoxelShape[]{Block.box(4.0, 4.0, 4.0, 12.0, 7.0, 12.0), Block.box(6.0, 7.0, 6.0, 13.0, 10.0, 10.0)}
					)
			),
			panel(SoundType.STONE)
	);
	public static final DeferredBlock<Block> BRACKET_SHARD_SIDE = BLOCK_REGISTER.registerBlock(
			"bracket_shard_side",
			props -> new RotatableRuinBlock(props, Shapes.or(Block.box(1.0, 0.0, 5.0, 15.0, 5.0, 11.0), Block.box(3.0, 5.0, 6.0, 13.0, 8.0, 10.0))),
			panel(SoundType.STONE)
	);

	// ===== 中式古建构件：檐、脊、瓦饰 =====
	public static final DeferredBlock<Block> RIDGE_TILE = BLOCK_REGISTER.registerBlock(
			"ridge_tile", props -> new RuinShapeBlock(props, Block.box(2.0, 0.0, 2.0, 14.0, 6.0, 14.0)), panel(SoundType.STONE)
	);
	public static final DeferredBlock<Block> RIDGE_ORNAMENT_SHARD = BLOCK_REGISTER.registerBlock(
			"ridge_ornament_shard", props -> new RotatableRuinBlock(props, Block.box(3.0, 0.0, 2.0, 11.0, 8.0, 14.0)), panel(SoundType.STONE)
	);
	public static final DeferredBlock<Block> EAVE_BOARD_SHARD = BLOCK_REGISTER.registerBlock(
			"eave_board_shard", props -> new RotatableRuinBlock(props, Block.box(0.0, 4.0, 6.0, 16.0, 8.0, 10.0)), panel(SoundType.WOOD)
	);

	// ===== 中式古建构件：门与墙 =====
	public static final DeferredBlock<Block> DOOR_FRAME_RUIN = BLOCK_REGISTER.registerBlock(
			"door_frame_ruin",
			props -> new RotatableRuinBlock(
					props,
					Shapes.or(
							Block.box(0.0, 0.0, 6.0, 3.0, 16.0, 10.0),
							new VoxelShape[]{Block.box(13.0, 0.0, 6.0, 16.0, 16.0, 10.0), Block.box(0.0, 13.0, 5.0, 16.0, 16.0, 11.0)}
					)
			),
			panel(SoundType.WOOD)
	);
	public static final DeferredBlock<Block> WALL_NICHE_SMALL = BLOCK_REGISTER.registerBlock(
			"wall_niche_small",
			props -> new RotatableRuinBlock(
					props,
					Shapes.or(
							Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 3.0),
							new VoxelShape[]{
									Block.box(0.0, 0.0, 3.0, 3.0, 16.0, 10.0),
									Block.box(13.0, 0.0, 3.0, 16.0, 16.0, 10.0),
									Block.box(3.0, 0.0, 3.0, 13.0, 3.0, 10.0),
									Block.box(3.0, 13.0, 3.0, 13.0, 16.0, 10.0)
							}
					)
			),
			panel(SoundType.STONE)
	);
	public static final DeferredBlock<Block> CORNER_QUOIN = BLOCK_REGISTER.registerBlock("corner_quoin", Block::new, stone(1.6F, 7.0F));
	public static final DeferredBlock<Block> CANGYIN_GATE_STELE = BLOCK_REGISTER.registerBlock("cangyin_gate_stele", GateSteleBlock::new, panel(SoundType.STONE));

	// ===== 沉泽墟与古墟陈设 =====
	public static final DeferredBlock<Block> RUIN_LAMP_BASE = BLOCK_REGISTER.registerBlock(
			"ruin_lamp_base",
			props -> new RuinShapeBlock(props, Block.box(4.0, 0.0, 4.0, 12.0, 10.0, 12.0)),
			panel(SoundType.STONE).lightLevel(state -> 7)
	);
	public static final DeferredBlock<Block> RUBBLE_STEP = BLOCK_REGISTER.registerBlock(
			"rubble_step", props -> new SlabBlock(props), stone(1.2F, 5.0F)
	);
	public static final DeferredBlock<Block> CARVED_SLAB = BLOCK_REGISTER.registerBlock("carved_slab", Block::new, stone(1.5F, 6.0F));
	public static final DeferredBlock<Block> ROTTEN_BEAM = BLOCK_REGISTER.registerBlock(
			"rotten_beam",
			props -> new RotatableRuinBlock(props, Block.box(0.0, 4.0, 4.0, 16.0, 12.0, 12.0)),
			panel(SoundType.WOOD)
	);
	public static final DeferredBlock<Block> SEALED_STONE_CASKET = BLOCK_REGISTER.registerBlock(
			"sealed_stone_casket",
			props -> new RuinShapeBlock(props, Block.box(2.0, 0.0, 2.0, 14.0, 10.0, 14.0)),
			panel(SoundType.STONE)
	);
	public static final DeferredBlock<Block> XUN_BRICK_FENCE = BLOCK_REGISTER.registerBlock(
			"xun_brick_fence", props -> new FenceBlock(props), stone(1.5F, 6.0F)
	);
	public static final DeferredBlock<Block> HALF_OFFERING_TABLE = BLOCK_REGISTER.registerBlock(
			"half_offering_table",
			props -> new RotatableRuinBlock(
					props,
					Shapes.or(
							Block.box(0.0, 8.0, 0.0, 16.0, 10.0, 16.0),
							new VoxelShape[]{Block.box(1.0, 0.0, 1.0, 5.0, 8.0, 5.0)}
					)
			),
			panel(SoundType.WOOD)
	);
	public static final DeferredBlock<Block> SPIRIT_CONDUIT = BLOCK_REGISTER.registerBlock(
			"spirit_conduit", SpiritConduitBlock::new, panel(SoundType.STONE).lightLevel(state -> 4)
	);
	// 碑刻四型：拓印碑、叙事残碑、沉水碑
	public static final VoxelShape STELE_READING_SHAPE = Shapes.or(
			Block.box(4.0, 0.0, 6.0, 12.0, 3.0, 10.0),
			new VoxelShape[]{Block.box(5.0, 3.0, 7.0, 11.0, 14.0, 9.0)}
	);
	public static final DeferredBlock<Block> RUBBING_STELE = BLOCK_REGISTER.registerBlock(
			"rubbing_stele",
			props -> new InscriptionSteleBlock(props, STELE_READING_SHAPE, "block." + MODID + ".rubbing_stele.text"),
			panel(SoundType.STONE)
	);
	public static final DeferredBlock<Block> STORY_STELE = BLOCK_REGISTER.registerBlock(
			"story_stele",
			props -> new InscriptionSteleBlock(props, STELE_READING_SHAPE, "block." + MODID + ".story_stele.text"),
			panel(SoundType.STONE)
	);
	public static final DeferredBlock<Block> SUNKEN_STELE = BLOCK_REGISTER.registerBlock(
			"sunken_stele",
			props -> new InscriptionSteleBlock(props, STELE_READING_SHAPE, "block." + MODID + ".sunken_stele.text"),
			panel(SoundType.STONE)
	);

	private RaBlocks() {
	}

	private static Properties stone(float hardness, float resistance) {
		return Properties.of().sound(SoundType.STONE).strength(hardness, resistance);
	}

	/**
	 * 硬质石材：必须用正确工具才掉落（配合 needs_stone_tool 标签生效）
	 */
	private static Properties hardStone(float hardness, float resistance) {
		return stone(hardness, resistance).requiresCorrectToolForDrops();
	}

	private static Properties wood(float hardness) {
		return Properties.of().sound(SoundType.WOOD).strength(hardness);
	}

	private static Properties plant() {
		return Properties.of().sound(SoundType.GRASS).strength(0.0F).noOcclusion().noCollission().instabreak();
	}

	private static Properties panel(SoundType sound) {
		return Properties.of().sound(sound).strength(1.5F, 6.0F).noOcclusion();
	}

	public static void register(IEventBus bus) {
		BLOCK_REGISTER.register(bus);
	}
}
