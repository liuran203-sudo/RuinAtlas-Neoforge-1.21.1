package com.liuran.ruinatlas.datagen.recipe;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import com.liuran.ruinatlas.init.RaBlocks;
import com.liuran.ruinatlas.init.RaItems;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 配方生成器
 * <p>原版数据包自带 3 个配方（干落瓣 / 驭灵石 / 苍垠界碑），此处在其基础上补齐
 * 建材转换链条与器物合成，使探索所得材料可以回流成可制作的物件。
 */
public class RaRecipeProvider extends net.minecraft.data.recipes.RecipeProvider {
	public RaRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}

	@Override
	protected void buildRecipes(RecipeOutput recipeOutput) {
		itemRecipes(recipeOutput);
		buildingRecipes(recipeOutput);
		techniqueRecipes(recipeOutput);
	}

	/**
	 * 术法：拓片誊抄成卷轴，铜铢熔铸成铜珠
	 */
	private void techniqueRecipes(RecipeOutput recipeOutput) {
		// 拓片 / 信物誊抄 → 卷轴
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RaItems.CLOUD_STEP_SCROLL.get(), 1)
				.requires(RaItems.CLOUD_STEP_RUBBING.get())
				.requires(Items.PAPER, 2)
				.requires(Items.INK_SAC)
				.unlockedBy("has_cloud_step_rubbing", has(RaItems.CLOUD_STEP_RUBBING.get()))
				.save(recipeOutput);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RaItems.WIND_BLADE_SCROLL.get(), 1)
				.requires(RaItems.WIND_BLADE_RUBBING.get())
				.requires(Items.PAPER, 2)
				.requires(Items.INK_SAC)
				.unlockedBy("has_wind_blade_rubbing", has(RaItems.WIND_BLADE_RUBBING.get()))
				.save(recipeOutput);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RaItems.WARD_EVIL_SCROLL.get(), 1)
				.requires(RaItems.WARD_EVIL_RUBBING.get())
				.requires(Items.PAPER, 2)
				.requires(Items.INK_SAC)
				.unlockedBy("has_ward_evil_rubbing", has(RaItems.WARD_EVIL_RUBBING.get()))
				.save(recipeOutput);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RaItems.HOLD_BREATH_SCROLL.get(), 1)
				.requires(RaItems.BREATH_JADE.get())
				.requires(Items.PAPER, 2)
				.requires(Items.INK_SAC)
				.unlockedBy("has_breath_jade", has(RaItems.BREATH_JADE.get()))
				.save(recipeOutput);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RaItems.LISTEN_WIND_SCROLL.get(), 1)
				.requires(RaItems.WIND_SHELL_PEARL.get())
				.requires(Items.PAPER, 2)
				.requires(Items.INK_SAC)
				.unlockedBy("has_wind_shell_pearl", has(RaItems.WIND_SHELL_PEARL.get()))
				.save(recipeOutput);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RaItems.GUIDE_LIGHT_SCROLL.get(), 1)
				.requires(RaItems.GLOW_SPAR.get())
				.requires(Items.PAPER, 2)
				.requires(Items.INK_SAC)
				.unlockedBy("has_glow_spar", has(RaItems.GLOW_SPAR.get()))
				.save(recipeOutput);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RaItems.MOUNTAIN_STEP_SCROLL.get(), 1)
				.requires(RaItems.MOUNTAIN_TILE.get())
				.requires(Items.PAPER, 2)
				.requires(Items.INK_SAC)
				.unlockedBy("has_mountain_tile", has(RaItems.MOUNTAIN_TILE.get()))
				.save(recipeOutput);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RaItems.CLEANSE_SCROLL.get(), 1)
				.requires(RaItems.BANNER_SHARD.get())
				.requires(Items.PAPER, 2)
				.requires(Items.INK_SAC)
				.unlockedBy("has_banner_shard", has(RaItems.BANNER_SHARD.get()))
				.save(recipeOutput);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RaItems.STONE_SENSE_SCROLL.get(), 1)
				.requires(RaItems.STELE_RUBBING.get())
				.requires(Items.PAPER, 2)
				.requires(Items.INK_SAC)
				.unlockedBy("has_stele_rubbing", has(RaItems.STELE_RUBBING.get()))
				.save(recipeOutput);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RaItems.SPIRIT_WARD_SCROLL.get(), 1)
				.requires(RaItems.SACRIFICIAL_JADE.get())
				.requires(Items.PAPER, 2)
				.requires(Items.INK_SAC)
				.unlockedBy("has_sacrificial_jade", has(RaItems.SACRIFICIAL_JADE.get()))
				.save(recipeOutput);

		// 旧邑铜铢 ×2 + 锈蚀铜屑 ×2 → 铜珠（一珠足矣，故只出一颗）
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RaItems.SPIRIT_COPPER_BEAD.get(), 1)
				.requires(RaItems.OLD_COPPER_TOKEN.get(), 2)
				.requires(RaItems.RUSTED_COPPER_DUST.get(), 2)
				.unlockedBy("has_old_copper_token", has(RaItems.OLD_COPPER_TOKEN.get()))
				.save(recipeOutput);
	}

	/**
	 * 器物：就地取材的合成
	 */
	private void itemRecipes(RecipeOutput recipeOutput) {
		// 落英丛 → 干落瓣 2
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RaItems.DRIED_PETAL.get(), 2)
				.requires(RaBlocks.PETAL_THICKET.get())
				.unlockedBy("has_petal_thicket", has(RaBlocks.PETAL_THICKET.get()))
				.save(recipeOutput);

		// 驭灵石原石 + 干落瓣 ×2 → 驭灵石
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RaItems.SPIRIT_HOLDING_STONE.get())
				.requires(RaItems.RAW_SPIRIT_STONE.get())
				.requires(RaItems.DRIED_PETAL.get(), 2)
				.unlockedBy("has_raw_spirit_stone", has(RaItems.RAW_SPIRIT_STONE.get()))
				.save(recipeOutput);

		// 刻文碑 + 旧邑底土 + 驭灵石原石 + 淡玉碎料 + 旧邑铜铢 → 苍垠界碑
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RaBlocks.CANGYIN_GATE_STELE.get())
				.requires(RaBlocks.INSCRIBED_STELE.get())
				.requires(RaBlocks.SETTLEMENT_SUBSOIL.get())
				.requires(RaItems.RAW_SPIRIT_STONE.get())
				.requires(RaItems.PALE_JADE_SHARD.get())
				.requires(RaItems.OLD_COPPER_TOKEN.get())
				.unlockedBy("has_inscribed_stele", has(RaBlocks.INSCRIBED_STELE.get()))
				.save(recipeOutput);

		// 主世界可达的入墟之路：石砖 ×3 + 紫水晶碎片 ×2 + 骨 ×1 → 苍垠界碑
		// 不必先入墟才凑得齐材料，否则永远进不去
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RaBlocks.CANGYIN_GATE_STELE.get())
				.requires(Items.STONE_BRICKS, 3)
				.requires(Items.AMETHYST_SHARD, 2)
				.requires(Items.BONE)
				.unlockedBy("has_amethyst_shard", has(Items.AMETHYST_SHARD))
				.save(recipeOutput, ResourceLocation.fromNamespaceAndPath(MODID, "gate_stele_from_common"));

		// 旧邑铜铢 → 锈蚀铜屑 ×2（磨去的铭文，只剩铜锈）
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RaItems.RUSTED_COPPER_DUST.get(), 2)
				.requires(RaItems.OLD_COPPER_TOKEN.get())
				.unlockedBy("has_old_copper_token", has(RaItems.OLD_COPPER_TOKEN.get()))
				.save(recipeOutput);

		// 铜锭 + 骨片 + 木棍 → 苍垠短匕
		ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, RaItems.CANGYIN_DAGGER.get())
				.requires(Items.COPPER_INGOT)
				.requires(RaItems.BONE_SHARD.get())
				.requires(Items.STICK)
				.unlockedBy("has_bone_shard", has(RaItems.BONE_SHARD.get()))
				.save(recipeOutput);

		// 骨片 ×2 + 线 → 镇厄骨佩
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RaItems.CALMING_BONE_TALLY.get())
				.requires(RaItems.BONE_SHARD.get(), 2)
				.requires(Items.STRING)
				.unlockedBy("has_bone_shard", has(RaItems.BONE_SHARD.get()))
				.save(recipeOutput);

		// 古木芦秆 ×2 + 线 ×3 → 麻布蔽衣
		ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, RaItems.HEMP_ROBE.get())
				.requires(RaItems.REED_STALK.get(), 2)
				.requires(Items.STRING, 3)
				.unlockedBy("has_reed_stalk", has(RaItems.REED_STALK.get()))
				.save(recipeOutput);

		// 淡玉碎料 ×2 + 逐光萤晶 → 引息玉玦
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RaItems.BREATH_JADE.get())
				.requires(RaItems.PALE_JADE_SHARD.get(), 2)
				.requires(RaItems.GLOW_SPAR.get())
				.unlockedBy("has_glow_spar", has(RaItems.GLOW_SPAR.get()))
				.save(recipeOutput);

		// 淡玉碎料 ×3 + 旧邑铜铢 → 祭主玉饰
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RaItems.SACRIFICIAL_JADE.get())
				.requires(RaItems.PALE_JADE_SHARD.get(), 3)
				.requires(RaItems.OLD_COPPER_TOKEN.get())
				.unlockedBy("has_pale_jade_shard", has(RaItems.PALE_JADE_SHARD.get()))
				.save(recipeOutput);

		// 旧邑铜铢 ×3 + 线 → 古墟铜佩
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RaItems.ANCIENT_COPPER_PENDANT.get())
				.requires(RaItems.OLD_COPPER_TOKEN.get(), 3)
				.requires(Items.STRING)
				.unlockedBy("has_old_copper_token", has(RaItems.OLD_COPPER_TOKEN.get()))
				.save(recipeOutput);

		// 纸 ×2 + 墨囊 → 引符（空白符纸，借术一次）
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RaItems.REUSABLE_TALISMAN.get())
				.requires(Items.PAPER, 2)
				.requires(Items.INK_SAC)
				.unlockedBy("has_paper", has(Items.PAPER))
				.save(recipeOutput);

		// 瓦砖块 ×2 → 踏岳纹砖
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RaItems.MOUNTAIN_TILE.get())
				.requires(RaBlocks.TILE_BLOCK.get(), 2)
				.unlockedBy("has_tile_block", has(RaBlocks.TILE_BLOCK.get()))
				.save(recipeOutput);
	}

	/**
	 * 建材：墟层岩 → 磨光墟石 → 墟砖 → 裂墟砖
	 */
	private void buildingRecipes(RecipeOutput recipeOutput) {
		// 古木 / 古木树皮 → 古墟木板 ×4
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, RaBlocks.CANGYIN_PLANKS.get(), 4)
				.requires(RaBlocks.CANGYIN_LOG.get())
				.group("planks")
				.unlockedBy("has_cangyin_log", has(RaBlocks.CANGYIN_LOG.get()))
				.save(recipeOutput, ResourceLocation.fromNamespaceAndPath(MODID, "cangyin_planks_from_log"));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, RaBlocks.CANGYIN_PLANKS.get(), 4)
				.requires(RaBlocks.CANGYIN_BARK.get())
				.group("planks")
				.unlockedBy("has_cangyin_bark", has(RaBlocks.CANGYIN_BARK.get()))
				.save(recipeOutput, ResourceLocation.fromNamespaceAndPath(MODID, "cangyin_planks_from_bark"));

		// 墟层岩 ×4 → 磨光墟石 ×4
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RaBlocks.POLISHED_XUN_STONE.get(), 4)
				.pattern("XX")
				.pattern("XX")
				.define('X', RaBlocks.XUN_LAYER_ROCK.get())
				.unlockedBy("has_xun_layer_rock", has(RaBlocks.XUN_LAYER_ROCK.get()))
				.save(recipeOutput);

		// 磨光墟石 ×4 → 墟砖 ×4
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RaBlocks.XUN_BRICK.get(), 4)
				.pattern("XX")
				.pattern("XX")
				.define('X', RaBlocks.POLISHED_XUN_STONE.get())
				.unlockedBy("has_polished_xun_stone", has(RaBlocks.POLISHED_XUN_STONE.get()))
				.save(recipeOutput);

		// 墟砖烧制 → 裂墟砖
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(RaBlocks.XUN_BRICK.get()), RecipeCategory.BUILDING_BLOCKS,
						RaBlocks.CRACKED_XUN_BRICK.get(), 0.1F, 200)
				.unlockedBy("has_xun_brick", has(RaBlocks.XUN_BRICK.get()))
				.save(recipeOutput, ResourceLocation.fromNamespaceAndPath(MODID, "cracked_xun_brick_from_smelting"));

		// 墟砖 ×6 → 墟砖栅栏 ×6
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RaBlocks.XUN_BRICK_FENCE.get(), 6)
				.pattern("XXX")
				.pattern("XXX")
				.define('X', RaBlocks.XUN_BRICK.get())
				.unlockedBy("has_xun_brick", has(RaBlocks.XUN_BRICK.get()))
				.save(recipeOutput);

		// 磨光墟石 ×4 → 刻纹石板 ×4
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RaBlocks.CARVED_SLAB.get(), 4)
				.pattern("XX")
				.pattern("XX")
				.define('X', RaBlocks.POLISHED_XUN_STONE.get())
				.unlockedBy("has_polished_xun_stone", has(RaBlocks.POLISHED_XUN_STONE.get()))
				.save(recipeOutput);

		// 碎石堆 ×3 → 碎石台阶 ×6
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RaBlocks.RUBBLE_STEP.get(), 6)
				.pattern("XXX")
				.define('X', RaBlocks.RUBBLE_PILE.get())
				.unlockedBy("has_rubble_pile", has(RaBlocks.RUBBLE_PILE.get()))
				.save(recipeOutput);

		// 石础 + 萤晶 → 残灯基座
		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, RaBlocks.RUIN_LAMP_BASE.get())
				.requires(RaBlocks.STONE_PLINTH.get())
				.requires(RaItems.GLOW_SPAR.get())
				.unlockedBy("has_stone_plinth", has(RaBlocks.STONE_PLINTH.get()))
				.save(recipeOutput);

		// 磨光墟石 ×3 + 灵息原石 → 灵息引石
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RaBlocks.SPIRIT_CONDUIT.get())
				.pattern("SSS")
				.pattern("SRS")
				.pattern("SSS")
				.define('S', RaBlocks.POLISHED_XUN_STONE.get())
				.define('R', RaItems.RAW_SPIRIT_STONE.get())
				.unlockedBy("has_raw_spirit_stone", has(RaItems.RAW_SPIRIT_STONE.get()))
				.save(recipeOutput);
	}
}
