package com.liuran.ruinatlas.datagen.loot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

/**
 * 战利品表主提供器
 */
public class RaLootTableProvider extends net.minecraft.data.loot.LootTableProvider {
	public RaLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, Collections.emptySet(), createTables(), registries);
	}

	private static List<SubProviderEntry> createTables() {
		List<SubProviderEntry> tables = new ArrayList<>();
		tables.add(new SubProviderEntry(RaBlockLootTable::new, LootContextParamSets.BLOCK));
		tables.add(new SubProviderEntry(RaChestLootTable::new, LootContextParamSets.CHEST));
		return tables;
	}
}
