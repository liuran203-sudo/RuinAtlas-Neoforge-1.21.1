package com.liuran.ruinatlas.init;

import com.liuran.ruinatlas.item.CrushCharmItem;
import com.liuran.ruinatlas.item.HempRobeItem;
import com.liuran.ruinatlas.item.ReusableTalismanItem;
import com.liuran.ruinatlas.item.SpiritCopperBeadItem;
import com.liuran.ruinatlas.item.TechniqueScrollItem;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

public class RaItems {
	public static final DeferredRegister.Items ITEM_REGISTER = DeferredRegister.createItems(MODID);

	// ===== 方块物品：自然原生方块 =====
	public static final DeferredItem<Item> XUN_SOIL = blockItem("xun_soil", RaBlocks.XUN_SOIL);
	public static final DeferredItem<Item> XUN_LAYER_ROCK = blockItem("xun_layer_rock", RaBlocks.XUN_LAYER_ROCK);
	public static final DeferredItem<Item> ERODED_STONE = blockItem("eroded_stone", RaBlocks.ERODED_STONE);
	public static final DeferredItem<Item> CANGYIN_LOG = blockItem("cangyin_log", RaBlocks.CANGYIN_LOG);
	public static final DeferredItem<Item> CANGYIN_BARK = blockItem("cangyin_bark", RaBlocks.CANGYIN_BARK);
	public static final DeferredItem<Item> CANGYIN_PLANKS = blockItem("cangyin_planks", RaBlocks.CANGYIN_PLANKS);
	public static final DeferredItem<Item> XUN_WILD_GRASS = blockItem("xun_wild_grass", RaBlocks.XUN_WILD_GRASS);
	public static final DeferredItem<Item> XUN_VINE = blockItem("xun_vine", RaBlocks.XUN_VINE);
	public static final DeferredItem<Item> XUN_LILY = blockItem("xun_lily", RaBlocks.XUN_LILY);
	public static final DeferredItem<Item> GLOOM_MOSS_BLOCK = blockItem("gloom_moss_block", RaBlocks.GLOOM_MOSS_BLOCK);
	public static final DeferredItem<Item> PETAL_THICKET = blockItem("petal_thicket", RaBlocks.PETAL_THICKET);
	public static final DeferredItem<Item> WITHER_REED = blockItem("wither_reed", RaBlocks.WITHER_REED);

	// ===== 方块物品：古建装饰方块 =====
	public static final DeferredItem<Item> XUN_BRICK = blockItem("xun_brick", RaBlocks.XUN_BRICK);
	public static final DeferredItem<Item> WEATHERED_XUN_BRICK = blockItem("weathered_xun_brick", RaBlocks.WEATHERED_XUN_BRICK);
	public static final DeferredItem<Item> WATERLOGGED_XUN_BRICK = blockItem("waterlogged_xun_brick", RaBlocks.WATERLOGGED_XUN_BRICK);
	public static final DeferredItem<Item> CRACKED_XUN_BRICK = blockItem("cracked_xun_brick", RaBlocks.CRACKED_XUN_BRICK);
	public static final DeferredItem<Item> POLISHED_XUN_STONE = blockItem("polished_xun_stone", RaBlocks.POLISHED_XUN_STONE);
	public static final DeferredItem<Item> BROKEN_XUN_WALL = blockItem("broken_xun_wall", RaBlocks.BROKEN_XUN_WALL);
	public static final DeferredItem<Item> TILE_BLOCK = blockItem("tile_block", RaBlocks.TILE_BLOCK);
	public static final DeferredItem<Item> BROKEN_TILE_PILE = blockItem("broken_tile_pile", RaBlocks.BROKEN_TILE_PILE);
	public static final DeferredItem<Item> EAVE_ORNAMENT = blockItem("eave_ornament", RaBlocks.EAVE_ORNAMENT);
	public static final DeferredItem<Item> BLANK_STELE = blockItem("blank_stele", RaBlocks.BLANK_STELE);
	public static final DeferredItem<Item> INSCRIBED_STELE = blockItem("inscribed_stele", RaBlocks.INSCRIBED_STELE);
	public static final DeferredItem<Item> BOUNDARY_STELE = blockItem("boundary_stele", RaBlocks.BOUNDARY_STELE);
	public static final DeferredItem<Item> SPIRIT_TEST_ALTAR = blockItem("spirit_test_altar", RaBlocks.SPIRIT_TEST_ALTAR);
	public static final DeferredItem<Item> STAR_CARVED_BASE = blockItem("star_carved_base", RaBlocks.STAR_CARVED_BASE);
	public static final DeferredItem<Item> STONE_INCENSE_TABLE = blockItem("stone_incense_table", RaBlocks.STONE_INCENSE_TABLE);
	public static final DeferredItem<Item> STONE_LANTERN = blockItem("stone_lantern", RaBlocks.STONE_LANTERN);
	public static final DeferredItem<Item> EMPTY_CERAMIC_URN = blockItem("empty_ceramic_urn", RaBlocks.EMPTY_CERAMIC_URN);
	public static final DeferredItem<Item> SEALED_CERAMIC_URN = blockItem("sealed_ceramic_urn", RaBlocks.SEALED_CERAMIC_URN);
	public static final DeferredItem<Item> BONE_SACRIFICE_TABLE = blockItem("bone_sacrifice_table", RaBlocks.BONE_SACRIFICE_TABLE);
	public static final DeferredItem<Item> CANGYIN_OFFERING_TABLE = blockItem("cangyin_offering_table", RaBlocks.CANGYIN_OFFERING_TABLE);
	public static final DeferredItem<Item> WOODEN_SCREEN = blockItem("wooden_screen", RaBlocks.WOODEN_SCREEN);
	public static final DeferredItem<Item> STONE_PLINTH = blockItem("stone_plinth", RaBlocks.STONE_PLINTH);
	public static final DeferredItem<Item> BROKEN_PILLAR_LOWER = blockItem("broken_pillar_lower", RaBlocks.BROKEN_PILLAR_LOWER);
	public static final DeferredItem<Item> BROKEN_PILLAR_UPPER = blockItem("broken_pillar_upper", RaBlocks.BROKEN_PILLAR_UPPER);
	public static final DeferredItem<Item> RUBBLE_PILE = blockItem("rubble_pile", RaBlocks.RUBBLE_PILE);
	public static final DeferredItem<Item> BONE_PILE = blockItem("bone_pile", RaBlocks.BONE_PILE);
	public static final DeferredItem<Item> RUSTED_BRONZE_PANEL = blockItem("rusted_bronze_panel", RaBlocks.RUSTED_BRONZE_PANEL);

	// ===== 方块物品：功能交互方块 =====
	public static final DeferredItem<Item> XUN_NICHE = blockItem("xun_niche", RaBlocks.XUN_NICHE);
	public static final DeferredItem<Item> SPIRIT_PLATFORM = blockItem("spirit_platform", RaBlocks.SPIRIT_PLATFORM);
	public static final DeferredItem<Item> ANCIENT_BOOK_CASKET = blockItem("ancient_book_casket", RaBlocks.ANCIENT_BOOK_CASKET);

	// ===== 方块物品：陈国旧疆地基 =====
	public static final DeferredItem<Item> QINGYUAN_LOAM = blockItem("qingyuan_loam", RaBlocks.QINGYUAN_LOAM);
	public static final DeferredItem<Item> PETAL_SPROUT = blockItem("petal_sprout", RaBlocks.PETAL_SPROUT);
	public static final DeferredItem<Item> FARM_RUIN_PLOT = blockItem("farm_ruin_plot", RaBlocks.FARM_RUIN_PLOT);
	public static final DeferredItem<Item> THATCH_RUBBLE = blockItem("thatch_rubble", RaBlocks.THATCH_RUBBLE);
	public static final DeferredItem<Item> SETTLEMENT_SUBSOIL = blockItem("settlement_subsoil", RaBlocks.SETTLEMENT_SUBSOIL);
	public static final DeferredItem<Item> MARKET_RUBBLE = blockItem("market_rubble", RaBlocks.MARKET_RUBBLE);
	public static final DeferredItem<Item> CITY_CURB = blockItem("city_curb", RaBlocks.CITY_CURB);
	public static final DeferredItem<Item> ROAD_RUBBLE = blockItem("road_rubble", RaBlocks.ROAD_RUBBLE);
	public static final DeferredItem<Item> WAYSTONE = blockItem("waystone", RaBlocks.WAYSTONE);
	public static final DeferredItem<Item> COLLAPSED_KIOSK = blockItem("collapsed_kiosk", RaBlocks.COLLAPSED_KIOSK);
	public static final DeferredItem<Item> STREAM_SILT = blockItem("stream_silt", RaBlocks.STREAM_SILT);
	public static final DeferredItem<Item> JADE_CHIP_ROCK = blockItem("jade_chip_rock", RaBlocks.JADE_CHIP_ROCK);

	// ===== 方块物品：中式古建构件 =====
	public static final DeferredItem<Item> CARVED_STONE_PAVING = blockItem("carved_stone_paving", RaBlocks.CARVED_STONE_PAVING);
	public static final DeferredItem<Item> BROKEN_CARVED_PAVING = blockItem("broken_carved_paving", RaBlocks.BROKEN_CARVED_PAVING);
	public static final DeferredItem<Item> STEP_STONE = blockItem("step_stone", RaBlocks.STEP_STONE);
	public static final DeferredItem<Item> CARVED_STEP = blockItem("carved_step", RaBlocks.CARVED_STEP);
	public static final DeferredItem<Item> FOUNDATION_SKIRT = blockItem("foundation_skirt", RaBlocks.FOUNDATION_SKIRT);
	public static final DeferredItem<Item> LOTUS_PLINTH = blockItem("lotus_plinth", RaBlocks.LOTUS_PLINTH);
	public static final DeferredItem<Item> PLAIN_PLINTH = blockItem("plain_plinth", RaBlocks.PLAIN_PLINTH);
	public static final DeferredItem<Item> WOODEN_PILLAR_LOWER = blockItem("wooden_pillar_lower", RaBlocks.WOODEN_PILLAR_LOWER);
	public static final DeferredItem<Item> WOODEN_PILLAR_UPPER = blockItem("wooden_pillar_upper", RaBlocks.WOODEN_PILLAR_UPPER);
	public static final DeferredItem<Item> PILLAR_BRACE_SHARD = blockItem("pillar_brace_shard", RaBlocks.PILLAR_BRACE_SHARD);
	public static final DeferredItem<Item> BALUSTER_SOLID = blockItem("baluster_solid", RaBlocks.BALUSTER_SOLID);
	public static final DeferredItem<Item> BROKEN_BALUSTER = blockItem("broken_baluster", RaBlocks.BROKEN_BALUSTER);
	public static final DeferredItem<Item> NEWEL_POST = blockItem("newel_post", RaBlocks.NEWEL_POST);
	public static final DeferredItem<Item> LATTICE_WINDOW_SQUARE = blockItem("lattice_window_square", RaBlocks.LATTICE_WINDOW_SQUARE);
	public static final DeferredItem<Item> LATTICE_WAVE_WINDOW = blockItem("lattice_wave_window", RaBlocks.LATTICE_WAVE_WINDOW);
	public static final DeferredItem<Item> BROKEN_LATTICE = blockItem("broken_lattice", RaBlocks.BROKEN_LATTICE);
	public static final DeferredItem<Item> BRACKET_SHARD_CORNER = blockItem("bracket_shard_corner", RaBlocks.BRACKET_SHARD_CORNER);
	public static final DeferredItem<Item> BRACKET_SHARD_SIDE = blockItem("bracket_shard_side", RaBlocks.BRACKET_SHARD_SIDE);
	public static final DeferredItem<Item> RIDGE_TILE = blockItem("ridge_tile", RaBlocks.RIDGE_TILE);
	public static final DeferredItem<Item> RIDGE_ORNAMENT_SHARD = blockItem("ridge_ornament_shard", RaBlocks.RIDGE_ORNAMENT_SHARD);
	public static final DeferredItem<Item> EAVE_BOARD_SHARD = blockItem("eave_board_shard", RaBlocks.EAVE_BOARD_SHARD);
	public static final DeferredItem<Item> DOOR_FRAME_RUIN = blockItem("door_frame_ruin", RaBlocks.DOOR_FRAME_RUIN);
	public static final DeferredItem<Item> WALL_NICHE_SMALL = blockItem("wall_niche_small", RaBlocks.WALL_NICHE_SMALL);
	public static final DeferredItem<Item> CORNER_QUOIN = blockItem("corner_quoin", RaBlocks.CORNER_QUOIN);
	public static final DeferredItem<Item> CANGYIN_GATE_STELE = blockItem("cangyin_gate_stele", RaBlocks.CANGYIN_GATE_STELE);

	// ===== 素材物品 =====
	public static final DeferredItem<Item> REED_STALK = ITEM_REGISTER.registerSimpleItem("reed_stalk", new Properties().stacksTo(64));
	public static final DeferredItem<Item> XUN_LILY_PETAL = ITEM_REGISTER.registerSimpleItem("xun_lily_petal", new Properties().stacksTo(64));
	public static final DeferredItem<Item> BONE_SHARD = ITEM_REGISTER.registerSimpleItem("bone_shard", new Properties().stacksTo(64));
	public static final DeferredItem<Item> RUSTED_COPPER_DUST = ITEM_REGISTER.registerSimpleItem("rusted_copper_dust", new Properties().stacksTo(64));
	public static final DeferredItem<Item> RAW_SPIRIT_STONE = ITEM_REGISTER.registerSimpleItem("raw_spirit_stone", new Properties().stacksTo(64));
	public static final DeferredItem<Item> DRIED_PETAL = ITEM_REGISTER.registerSimpleItem("dried_petal", new Properties().stacksTo(64));
	public static final DeferredItem<Item> OLD_COPPER_TOKEN = ITEM_REGISTER.registerSimpleItem("old_copper_token", new Properties().stacksTo(16));
	public static final DeferredItem<Item> TATTERED_MESSAGE = ITEM_REGISTER.registerSimpleItem("tattered_message", new Properties().stacksTo(16));
	public static final DeferredItem<Item> PALE_JADE_SHARD = ITEM_REGISTER.registerSimpleItem("pale_jade_shard", new Properties().stacksTo(64));

	// ===== 借灵消耗道具（一次性，捏碎即得灵息，不给状态效果） =====
	public static final DeferredItem<Item> SPIRIT_HOLDING_STONE = ITEM_REGISTER.registerItem(
			"spirit_holding_stone", props -> new CrushCharmItem(props, "item.ruinatlas.spirit_holding_stone.effect", 100), new Properties()
	);
	public static final DeferredItem<Item> BREATH_JADE = ITEM_REGISTER.registerItem(
			"breath_jade", props -> new CrushCharmItem(props, "item.ruinatlas.breath_jade.effect", 20), new Properties()
	);
	public static final DeferredItem<Item> CALMING_BONE_TALLY = ITEM_REGISTER.registerItem(
			"calming_bone_tally", props -> new CrushCharmItem(props, "item.ruinatlas.calming_bone_tally.effect", 15), new Properties()
	);
	public static final DeferredItem<Item> GLOW_SPAR = ITEM_REGISTER.registerItem(
			"glow_spar", props -> new CrushCharmItem(props, "item.ruinatlas.glow_spar.effect", 15), new Properties()
	);
	public static final DeferredItem<Item> MOUNTAIN_TILE = ITEM_REGISTER.registerItem(
			"mountain_tile", props -> new CrushCharmItem(props, "item.ruinatlas.mountain_tile.effect", 20), new Properties()
	);
	public static final DeferredItem<Item> BANNER_SHARD = ITEM_REGISTER.registerItem(
			"banner_shard", props -> new CrushCharmItem(props, "item.ruinatlas.banner_shard.effect", 25), new Properties()
	);
	public static final DeferredItem<Item> WIND_SHELL_PEARL = ITEM_REGISTER.registerItem(
			"wind_shell_pearl", props -> new CrushCharmItem(props, "item.ruinatlas.wind_shell_pearl.effect", 15), new Properties()
	);

	// ===== 术法卷轴：借灵息契悟，一式一册 =====
	public static final DeferredItem<Item> CLOUD_STEP_SCROLL = ITEM_REGISTER.registerItem(
			"cloud_step_scroll", TechniqueScrollItem::new, new Properties());
	public static final DeferredItem<Item> WIND_BLADE_SCROLL = ITEM_REGISTER.registerItem(
			"wind_blade_scroll", TechniqueScrollItem::new, new Properties());
	public static final DeferredItem<Item> WARD_EVIL_SCROLL = ITEM_REGISTER.registerItem(
			"ward_evil_scroll", TechniqueScrollItem::new, new Properties());
	public static final DeferredItem<Item> HOLD_BREATH_SCROLL = ITEM_REGISTER.registerItem(
			"hold_breath_scroll", TechniqueScrollItem::new, new Properties());
	public static final DeferredItem<Item> LISTEN_WIND_SCROLL = ITEM_REGISTER.registerItem(
			"listen_wind_scroll", TechniqueScrollItem::new, new Properties());
	public static final DeferredItem<Item> GUIDE_LIGHT_SCROLL = ITEM_REGISTER.registerItem(
			"guide_light_scroll", TechniqueScrollItem::new, new Properties());
	public static final DeferredItem<Item> MOUNTAIN_STEP_SCROLL = ITEM_REGISTER.registerItem(
			"mountain_step_scroll", TechniqueScrollItem::new, new Properties());
	public static final DeferredItem<Item> CLEANSE_SCROLL = ITEM_REGISTER.registerItem(
			"cleanse_scroll", TechniqueScrollItem::new, new Properties());
	public static final DeferredItem<Item> STONE_SENSE_SCROLL = ITEM_REGISTER.registerItem(
			"stone_sense_scroll", TechniqueScrollItem::new, new Properties());
	public static final DeferredItem<Item> SPIRIT_WARD_SCROLL = ITEM_REGISTER.registerItem(
			"spirit_ward_scroll", TechniqueScrollItem::new, new Properties());
	/** 铜珠：载术之器 */
	public static final DeferredItem<Item> SPIRIT_COPPER_BEAD = ITEM_REGISTER.registerItem(
			"spirit_copper_bead", SpiritCopperBeadItem::new, new Properties());

	// ===== 秘术拓片与符引 =====
	public static final DeferredItem<Item> CLOUD_STEP_RUBBING = ITEM_REGISTER.registerSimpleItem("cloud_step_rubbing", new Properties().stacksTo(16));
	public static final DeferredItem<Item> WARD_EVIL_RUBBING = ITEM_REGISTER.registerSimpleItem("ward_evil_rubbing", new Properties().stacksTo(16));
	public static final DeferredItem<Item> WIND_BLADE_RUBBING = ITEM_REGISTER.registerSimpleItem("wind_blade_rubbing", new Properties().stacksTo(16));
	public static final DeferredItem<Item> REUSABLE_TALISMAN = ITEM_REGISTER.registerItem("reusable_talisman", ReusableTalismanItem::new, new Properties());
	public static final DeferredItem<Item> STELE_RUBBING = ITEM_REGISTER.registerSimpleItem("stele_rubbing", new Properties().stacksTo(16));

	// ===== 藏品 =====
	public static final DeferredItem<Item> ANCIENT_COPPER_PENDANT = ITEM_REGISTER.registerSimpleItem("ancient_copper_pendant", new Properties().stacksTo(16));
	public static final DeferredItem<Item> SACRIFICIAL_JADE = ITEM_REGISTER.registerSimpleItem("sacrificial_jade", new Properties().stacksTo(16));

	// ===== 装备 =====
	/** 苍垠古铜：旧先民锻打的低阶材质，不做成长强化 */
	private static final Tier CANGYIN_BRONZE_TIER = new Tier() {
		@Override
		public int getUses() {
			return 320;
		}

		@Override
		public float getSpeed() {
			return 4.0F;
		}

		@Override
		public float getAttackDamageBonus() {
			return 2.0F;
		}

		@Override
		public TagKey<Block> getIncorrectBlocksForDrops() {
			return BlockTags.INCORRECT_FOR_STONE_TOOL;
		}

		@Override
		public int getEnchantmentValue() {
			return 8;
		}

		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.EMPTY;
		}
	};
	public static final DeferredItem<Item> CANGYIN_DAGGER = ITEM_REGISTER.registerItem(
			"cangyin_dagger", props -> new SwordItem(CANGYIN_BRONZE_TIER, props), new Properties().stacksTo(1).durability(320)
	);
	private static final ItemAttributeModifiers HEMP_ROBE_MODIFIERS = ItemAttributeModifiers.builder()
			.add(Attributes.ARMOR, new AttributeModifier(ResourceLocation.parse("ruinatlas.hemp_robe.armor"), 2.0, Operation.ADD_VALUE), EquipmentSlotGroup.CHEST)
			.build();
	public static final DeferredItem<Item> HEMP_ROBE = ITEM_REGISTER.registerItem(
			"hemp_robe", HempRobeItem::new, new Properties().stacksTo(1).durability(90).attributes(HEMP_ROBE_MODIFIERS)
	);

	private RaItems() {
	}

	private static DeferredItem<Item> blockItem(String name, DeferredBlock<? extends Block> block) {
		return ITEM_REGISTER.registerItem(name, props -> new BlockItem(block.get(), props), new Properties());
	}

	public static void register(IEventBus bus) {
		ITEM_REGISTER.register(bus);
	}
}
