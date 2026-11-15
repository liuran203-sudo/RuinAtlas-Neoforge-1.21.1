package com.liuran.ruinatlas.datagen.loot;

import java.util.function.BiConsumer;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import com.liuran.ruinatlas.init.RaItems;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 箱子战利品表：废墟地窖、道途亭舍
 */
public class RaChestLootTable implements net.minecraft.data.loot.LootTableSubProvider {
	public static final ResourceKey<LootTable> RUIN_CELLAR = key("chest/ruin_cellar");
	public static final ResourceKey<LootTable> WAY_KIOSK = key("chest/way_kiosk");

	public RaChestLootTable(net.minecraft.core.HolderLookup.Provider registries) {
		// 箱子表用不到注册表查询，构造参数只为满足 SubProviderEntry 的函数式签名
	}

	private static ResourceKey<LootTable> key(String path) {
		return ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(MODID, path));
	}

	@Override
	public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
		output.accept(RUIN_CELLAR, LootTable.lootTable().withPool(
				LootPool.lootPool()
						.setRolls(UniformGenerator.between(2.0F, 4.0F))
						.add(LootItem.lootTableItem(RaItems.BONE_SHARD.get()).setWeight(4)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
						.add(LootItem.lootTableItem(RaItems.RUSTED_COPPER_DUST.get()).setWeight(3)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
						.add(LootItem.lootTableItem(RaItems.REED_STALK.get()).setWeight(3)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
						.add(LootItem.lootTableItem(RaItems.RAW_SPIRIT_STONE.get()).setWeight(2))
						.add(LootItem.lootTableItem(RaItems.OLD_COPPER_TOKEN.get()).setWeight(2))
						.add(LootItem.lootTableItem(RaItems.PALE_JADE_SHARD.get()).setWeight(2)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
						.add(LootItem.lootTableItem(RaItems.STELE_RUBBING.get()).setWeight(1))
						.add(LootItem.lootTableItem(RaItems.SPIRIT_HOLDING_STONE.get()).setWeight(1))));

		output.accept(WAY_KIOSK, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(UniformGenerator.between(1.0F, 2.0F))
						.add(LootItem.lootTableItem(RaItems.TATTERED_MESSAGE.get())))
				.withPool(LootPool.lootPool()
						.setRolls(UniformGenerator.between(1.0F, 3.0F))
						.add(LootItem.lootTableItem(RaItems.REED_STALK.get()).setWeight(4)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
						.add(LootItem.lootTableItem(RaItems.RUSTED_COPPER_DUST.get()).setWeight(3)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
						.add(LootItem.lootTableItem(RaItems.BONE_SHARD.get()).setWeight(3)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
						.add(LootItem.lootTableItem(RaItems.XUN_LILY_PETAL.get()).setWeight(2)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
						.add(LootItem.lootTableItem(RaItems.DRIED_PETAL.get()).setWeight(2)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
						.add(LootItem.lootTableItem(RaItems.RAW_SPIRIT_STONE.get()).setWeight(1)))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1))
						.when(LootItemRandomChanceCondition.randomChance(0.4F))
						.add(LootItem.lootTableItem(RaItems.OLD_COPPER_TOKEN.get()).setWeight(1))));
	}
}
