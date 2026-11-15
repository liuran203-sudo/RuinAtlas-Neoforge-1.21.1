package com.liuran.ruinatlas.datagen.tag;

import java.util.concurrent.CompletableFuture;

import javax.annotation.CheckForNull;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

import com.liuran.ruinatlas.init.RaBlocks;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 方块标签生成器：挖掘工具分类 + 原版木料标签
 */
public class RaBlockTags extends BlockTagsProvider {
	public RaBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
	                   @CheckForNull ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, MODID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.@NotNull Provider capability) {
		// ===== 木构：斧 =====
		addTags(BlockTags.MINEABLE_WITH_AXE,
				RaBlocks.CANGYIN_LOG,
				RaBlocks.CANGYIN_BARK,
				RaBlocks.CANGYIN_PLANKS,
				RaBlocks.ANCIENT_BOOK_CASKET,
				RaBlocks.CANGYIN_OFFERING_TABLE,
				RaBlocks.COLLAPSED_KIOSK,
				RaBlocks.DOOR_FRAME_RUIN,
				RaBlocks.EAVE_BOARD_SHARD,
				RaBlocks.WOODEN_PILLAR_LOWER,
				RaBlocks.WOODEN_PILLAR_UPPER,
				RaBlocks.WOODEN_SCREEN);

		// ===== 松软堆积与土：铲 =====
		addTags(BlockTags.MINEABLE_WITH_SHOVEL,
				RaBlocks.BROKEN_TILE_PILE,
				RaBlocks.FARM_RUIN_PLOT,
				RaBlocks.MARKET_RUBBLE,
				RaBlocks.QINGYUAN_LOAM,
				RaBlocks.ROAD_RUBBLE,
				RaBlocks.RUBBLE_PILE,
				RaBlocks.SETTLEMENT_SUBSOIL,
				RaBlocks.STREAM_SILT,
				RaBlocks.XUN_SOIL);

		// ===== 石作与砖瓦：镐 =====
		addTags(BlockTags.MINEABLE_WITH_PICKAXE,
				RaBlocks.BALUSTER_SOLID,
				RaBlocks.BLANK_STELE,
				RaBlocks.BONE_PILE,
				RaBlocks.BONE_SACRIFICE_TABLE,
				RaBlocks.BOUNDARY_STELE,
				RaBlocks.BRACKET_SHARD_CORNER,
				RaBlocks.BRACKET_SHARD_SIDE,
				RaBlocks.BROKEN_BALUSTER,
				RaBlocks.BROKEN_CARVED_PAVING,
				RaBlocks.BROKEN_LATTICE,
				RaBlocks.BROKEN_PILLAR_LOWER,
				RaBlocks.BROKEN_PILLAR_UPPER,
				RaBlocks.BROKEN_XUN_WALL,
				RaBlocks.CANGYIN_GATE_STELE,
				RaBlocks.CARVED_STEP,
				RaBlocks.CARVED_STONE_PAVING,
				RaBlocks.CITY_CURB,
				RaBlocks.CORNER_QUOIN,
				RaBlocks.CRACKED_XUN_BRICK,
				RaBlocks.EAVE_ORNAMENT,
				RaBlocks.ERODED_STONE,
				RaBlocks.FOUNDATION_SKIRT,
				RaBlocks.INSCRIBED_STELE,
				RaBlocks.JADE_CHIP_ROCK,
				RaBlocks.LATTICE_WAVE_WINDOW,
				RaBlocks.LATTICE_WINDOW_SQUARE,
				RaBlocks.LOTUS_PLINTH,
				RaBlocks.NEWEL_POST,
				RaBlocks.PILLAR_BRACE_SHARD,
				RaBlocks.PLAIN_PLINTH,
				RaBlocks.POLISHED_XUN_STONE,
				RaBlocks.RIDGE_ORNAMENT_SHARD,
				RaBlocks.RIDGE_TILE,
				RaBlocks.RUSTED_BRONZE_PANEL,
				RaBlocks.SPIRIT_PLATFORM,
				RaBlocks.SPIRIT_TEST_ALTAR,
				RaBlocks.STAR_CARVED_BASE,
				RaBlocks.STEP_STONE,
				RaBlocks.STONE_INCENSE_TABLE,
				RaBlocks.STONE_LANTERN,
				RaBlocks.STONE_PLINTH,
				RaBlocks.TILE_BLOCK,
				RaBlocks.WALL_NICHE_SMALL,
				RaBlocks.WATERLOGGED_XUN_BRICK,
				RaBlocks.WAYSTONE,
				RaBlocks.WEATHERED_XUN_BRICK,
				RaBlocks.XUN_BRICK,
				RaBlocks.XUN_LAYER_ROCK,
				RaBlocks.XUN_NICHE);

		// 玉脉与星纹基座：需要石质镐才能掉落
		addTags(BlockTags.NEEDS_STONE_TOOL,
				RaBlocks.JADE_CHIP_ROCK,
				RaBlocks.STAR_CARVED_BASE);

		// ===== 原版木料标签：让古木能参与原版配方 =====
		addTags(BlockTags.LOGS,
				RaBlocks.CANGYIN_LOG,
				RaBlocks.CANGYIN_BARK);
		addTags(BlockTags.LOGS_THAT_BURN,
				RaBlocks.CANGYIN_LOG,
				RaBlocks.CANGYIN_BARK);
		addTags(BlockTags.PLANKS,
				RaBlocks.CANGYIN_PLANKS);

		// ===== 自建标签：古墟残件（供后续战利品/交互引用）=====
		addTags(RaBlockTags.RUIN_DEBRIS,
				RaBlocks.BROKEN_BALUSTER,
				RaBlocks.BROKEN_CARVED_PAVING,
				RaBlocks.BROKEN_LATTICE,
				RaBlocks.BROKEN_PILLAR_LOWER,
				RaBlocks.BROKEN_PILLAR_UPPER,
				RaBlocks.BROKEN_TILE_PILE,
				RaBlocks.BROKEN_XUN_WALL,
				RaBlocks.BRACKET_SHARD_CORNER,
				RaBlocks.BRACKET_SHARD_SIDE,
				RaBlocks.EAVE_BOARD_SHARD,
				RaBlocks.MARKET_RUBBLE,
				RaBlocks.PILLAR_BRACE_SHARD,
				RaBlocks.RIDGE_ORNAMENT_SHARD,
				RaBlocks.ROAD_RUBBLE,
				RaBlocks.RUBBLE_PILE);
	}

	/**
	 * 自建方块标签
	 */
	public static final TagKey<Block> RUIN_DEBRIS =
			TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(MODID, "ruin_debris"));

	@SafeVarargs
	protected final void addTags(TagKey<Block> tagKey, DeferredHolder<Block, ? extends Block>... blocks) {
		for (DeferredHolder<Block, ? extends Block> block : blocks) {
			tag(tagKey).add(block.get());
		}
	}
}
