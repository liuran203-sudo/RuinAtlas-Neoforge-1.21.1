package com.liuran.ruinatlas.datagen;

import java.util.List;
import java.util.Set;

import com.liuran.ruinatlas.init.RaBlocks;
import com.liuran.ruinatlas.init.RaItems;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.util.valueproviders.UniformGenerator;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;

/**
 * 方块战利品表：默认掉自身，少数方块追加概率掉落
 */
public class RaBlockLootTable extends BlockLootSubProvider {
	public RaBlockLootTable(HolderLookup.Provider registries) {
		super(Set.of(), registries);
	}

	@Override
	protected void generate() {
		// 只有概率掉自身的方块
		this.add(RaBlocks.PETAL_THICKET.get(), selfWithChance(RaBlocks.PETAL_THICKET.get(), 0.35F));
		this.add(RaBlocks.XUN_WILD_GRASS.get(), selfWithChance(RaBlocks.XUN_WILD_GRASS.get(), 0.30F));

		// 不掉自身、只掉产物的方块
		this.add(RaBlocks.WITHER_REED.get(), single(RaItems.REED_STALK.get()));
		this.add(RaBlocks.XUN_LILY.get(), single(RaItems.XUN_LILY_PETAL.get()));

		// 掉自身 + 概率追加掉落
		this.add(RaBlocks.BONE_PILE.get(), selfAndBonus(RaBlocks.BONE_PILE.get(), RaItems.BONE_SHARD.get(), 0.30F, 1, 2));
		this.add(RaBlocks.ERODED_STONE.get(), selfAndBonus(RaBlocks.ERODED_STONE.get(), RaItems.RAW_SPIRIT_STONE.get(), 0.25F, 1, 1));
		this.add(RaBlocks.JADE_CHIP_ROCK.get(), selfAndBonus(RaBlocks.JADE_CHIP_ROCK.get(), RaItems.PALE_JADE_SHARD.get(), 0.50F, 1, 2));
		this.add(RaBlocks.MARKET_RUBBLE.get(), selfAndBonus(RaBlocks.MARKET_RUBBLE.get(), RaItems.OLD_COPPER_TOKEN.get(), 0.12F, 1, 1));
		this.add(RaBlocks.ROAD_RUBBLE.get(), selfAndBonus(RaBlocks.ROAD_RUBBLE.get(), RaItems.BONE_SHARD.get(), 0.05F, 1, 1));

		LootTable.Builder urn = selfAndBonus(RaBlocks.SEALED_CERAMIC_URN.get(), RaItems.RUSTED_COPPER_DUST.get(), 0.60F, 1, 2);
		urn.withPool(chancePool(0.20F).add(LootItem.lootTableItem(RaItems.BONE_SHARD.get())));
		this.add(RaBlocks.SEALED_CERAMIC_URN.get(), urn);

		// 其余方块一律掉自身
		Set<Block> handled = Set.of(
				RaBlocks.PETAL_THICKET.get(),
				RaBlocks.XUN_WILD_GRASS.get(),
				RaBlocks.WITHER_REED.get(),
				RaBlocks.XUN_LILY.get(),
				RaBlocks.BONE_PILE.get(),
				RaBlocks.ERODED_STONE.get(),
				RaBlocks.JADE_CHIP_ROCK.get(),
				RaBlocks.MARKET_RUBBLE.get(),
				RaBlocks.ROAD_RUBBLE.get(),
				RaBlocks.SEALED_CERAMIC_URN.get()
		);

		for (var entry : RaBlocks.BLOCK_REGISTER.getEntries()) {
			Block block = entry.get();
			if (!handled.contains(block)) {
				this.dropSelf(block);
			}
		}
	}

	@Override
	protected Iterable<Block> getKnownBlocks() {
		return List.copyOf(RaBlocks.BLOCK_REGISTER.getEntries().stream().map(it -> (Block) it.get()).toList());
	}

	private LootTable.Builder single(ItemLike item) {
		return LootTable.lootTable().withPool(fullPool().add(LootItem.lootTableItem(item)));
	}

	private LootTable.Builder selfWithChance(Block block, float chance) {
		return LootTable.lootTable().withPool(chancePool(chance).add(LootItem.lootTableItem(block)));
	}

	private LootTable.Builder selfAndBonus(Block block, ItemLike bonus, float chance, int min, int max) {
		return LootTable.lootTable()
				.withPool(fullPool().add(LootItem.lootTableItem(block)))
				.withPool(chancePool(chance).add(LootItem.lootTableItem(bonus)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)))));
	}

	private LootPool.Builder fullPool() {
		return LootPool.lootPool().setRolls(ConstantValue.exactly(1));
	}

	private LootPool.Builder chancePool(float chance) {
		return LootPool.lootPool()
				.setRolls(ConstantValue.exactly(1))
				.when(LootItemRandomChanceCondition.randomChance(chance));
	}
}
