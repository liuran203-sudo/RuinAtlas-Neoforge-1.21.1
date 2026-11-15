package com.liuran.ruinatlas.datagen;

import java.util.function.BiConsumer;

import com.liuran.ruinatlas.init.RaItems;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.util.valueproviders.UniformGenerator;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 箱子战利品表：墟窖（ruin_cellar）与道亭（way_kiosk）
 */
public class RaChestLoot implements LootTableSubProvider {
	@Override
	public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
		output.accept(key("chest/ruin_cellar"), LootTable.lootTable().withPool(
				LootPool.lootPool()
						.setRolls(UniformGenerator.between(2, 4))
						.add(LootItem.lootTableItem(RaItems.BONE_SHARD.get()).setWeight(4)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
						.add(LootItem.lootTableItem(RaItems.RUSTED_COPPER_DUST.get()).setWeight(3)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
						.add(LootItem.lootTableItem(RaItems.REED_STALK.get()).setWeight(3)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
						.add(LootItem.lootTableItem(RaItems.RAW_SPIRIT_STONE.get()).setWeight(2))
						.add(LootItem.lootTableItem(RaItems.OLD_COPPER_TOKEN.get()).setWeight(2))
						.add(LootItem.lootTableItem(RaItems.PALE_JADE_SHARD.get()).setWeight(2)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
						.add(LootItem.lootTableItem(RaItems.STELE_RUBBING.get()).setWeight(1))
						.add(LootItem.lootTableItem(RaItems.SPIRIT_HOLDING_STONE.get()).setWeight(1))
		));

		output.accept(key("chest/way_kiosk"), LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(UniformGenerator.between(1, 2))
						.add(LootItem.lootTableItem(RaItems.TATTERED_MESSAGE.get())))
				.withPool(LootPool.lootPool()
						.setRolls(UniformGenerator.between(1, 3))
						.add(LootItem.lootTableItem(RaItems.REED_STALK.get()).setWeight(4)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
						.add(LootItem.lootTableItem(RaItems.RUSTED_COPPER_DUST.get()).setWeight(3)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
						.add(LootItem.lootTableItem(RaItems.BONE_SHARD.get()).setWeight(3)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
						.add(LootItem.lootTableItem(RaItems.XUN_LILY_PETAL.get()).setWeight(2)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
						.add(LootItem.lootTableItem(RaItems.DRIED_PETAL.get()).setWeight(2)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
						.add(LootItem.lootTableItem(RaItems.RAW_SPIRIT_STONE.get()).setWeight(1)))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1))
						.when(LootItemRandomChanceCondition.randomChance(0.4F))
						.add(LootItem.lootTableItem(RaItems.OLD_COPPER_TOKEN.get()).setWeight(1)));
	}

	private static ResourceKey<LootTable> key(String path) {
		return ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(MODID, path));
	}
}
