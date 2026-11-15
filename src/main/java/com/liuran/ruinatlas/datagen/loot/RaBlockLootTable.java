package com.liuran.ruinatlas.datagen.loot;

import java.util.Set;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import com.liuran.ruinatlas.init.RaBlocks;
import com.liuran.ruinatlas.init.RaItems;

/**
 * 方块战利品表：默认掉落自身，特殊方块在下面逐条覆盖
 */
public class RaBlockLootTable extends BlockLootSubProvider {
	public RaBlockLootTable(HolderLookup.Provider lookupProvider) {
		super(Set.of(), FeatureFlags.DEFAULT_FLAGS, lookupProvider);
	}

	@Override
	protected void generate() {
		// 默认：所有已注册方块掉落自身（新加方块自动覆盖，不必逐条登记）
		for (var entry : RaBlocks.BLOCK_REGISTER.getEntries()) {
			dropSelf(entry.get());
		}

		// 台阶
		add(RaBlocks.STEP_STONE.get(), createSlabItemTable(RaBlocks.STEP_STONE.get()));
		add(RaBlocks.CARVED_STEP.get(), createSlabItemTable(RaBlocks.CARVED_STEP.get()));

		//region 特殊掉落
		// 蚀纹岩：自身 + 25% 驭灵石原石
		add(RaBlocks.ERODED_STONE.get(), LootTable.lootTable()
				.withPool(singleItem(RaBlocks.ERODED_STONE.get()))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1))
						.when(LootItemRandomChanceCondition.randomChance(0.25F))
						.add(LootItem.lootTableItem(RaItems.RAW_SPIRIT_STONE.get()))));

		// 玉屑岩：自身 + 50% 淡玉碎料 1~2
		add(RaBlocks.JADE_CHIP_ROCK.get(), LootTable.lootTable()
				.withPool(singleItem(RaBlocks.JADE_CHIP_ROCK.get()))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1))
						.when(LootItemRandomChanceCondition.randomChance(0.5F))
						.add(LootItem.lootTableItem(RaItems.PALE_JADE_SHARD.get())
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))));

		// 集市废墟：自身 + 12% 旧邑铜铢
		add(RaBlocks.MARKET_RUBBLE.get(), LootTable.lootTable()
				.withPool(singleItem(RaBlocks.MARKET_RUBBLE.get()))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1))
						.when(LootItemRandomChanceCondition.randomChance(0.12F))
						.add(LootItem.lootTableItem(RaItems.OLD_COPPER_TOKEN.get()))));

		// 道途废墟：自身 + 5% 骨片
		add(RaBlocks.ROAD_RUBBLE.get(), LootTable.lootTable()
				.withPool(singleItem(RaBlocks.ROAD_RUBBLE.get()))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1))
						.when(LootItemRandomChanceCondition.randomChance(0.05F))
						.add(LootItem.lootTableItem(RaItems.BONE_SHARD.get()))));

		// 枯骨堆：自身 + 30% 骨片 1~2
		add(RaBlocks.BONE_PILE.get(), LootTable.lootTable()
				.withPool(singleItem(RaBlocks.BONE_PILE.get()))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1))
						.when(LootItemRandomChanceCondition.randomChance(0.3F))
						.add(LootItem.lootTableItem(RaItems.BONE_SHARD.get())
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))));

		// 封口陶罐：自身 + 60% 锈蚀铜屑 1~2 + 20% 骨片
		add(RaBlocks.SEALED_CERAMIC_URN.get(), LootTable.lootTable()
				.withPool(singleItem(RaBlocks.SEALED_CERAMIC_URN.get()))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1))
						.when(LootItemRandomChanceCondition.randomChance(0.6F))
						.add(LootItem.lootTableItem(RaItems.RUSTED_COPPER_DUST.get())
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))) )
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1))
						.when(LootItemRandomChanceCondition.randomChance(0.2F))
						.add(LootItem.lootTableItem(RaItems.BONE_SHARD.get()))));

		// 落英丛：各自 35% 掉自身 / 干落瓣 1~2
		add(RaBlocks.PETAL_THICKET.get(), LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1))
						.when(LootItemRandomChanceCondition.randomChance(0.35F))
						.add(LootItem.lootTableItem(RaBlocks.PETAL_THICKET.get())))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1))
						.when(LootItemRandomChanceCondition.randomChance(0.35F))
						.add(LootItem.lootTableItem(RaItems.DRIED_PETAL.get())
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))));

		// 墟荒草：仅 30% 掉自身
		add(RaBlocks.XUN_WILD_GRASS.get(), LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1))
						.when(LootItemRandomChanceCondition.randomChance(0.3F))
						.add(LootItem.lootTableItem(RaBlocks.XUN_WILD_GRASS.get()))));

		// 枯芦秆：掉古木芦秆
		add(RaBlocks.WITHER_REED.get(), createSingleItemTable(RaItems.REED_STALK.get()));

		// 墟莲：掉墟莲瓣
		add(RaBlocks.XUN_LILY.get(), createSingleItemTable(RaItems.XUN_LILY_PETAL.get()));
		//endregion
	}

	@Override
	protected Iterable<Block> getKnownBlocks() {
		// 只校验本模组的方块，否则 datagen 会要求把原版所有方块的战利品表也补齐
		return RaBlocks.BLOCK_REGISTER.getEntries().stream().map(entry -> (Block) entry.get()).toList();
	}

	private static LootPool.Builder singleItem(net.minecraft.world.level.ItemLike item) {
		return LootPool.lootPool()
				.setRolls(ConstantValue.exactly(1))
				.add(LootItem.lootTableItem(item));
	}
}
