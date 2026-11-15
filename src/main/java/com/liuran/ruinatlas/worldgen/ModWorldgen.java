package com.liuran.ruinatlas.worldgen;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 世界生成注册入口：群系源、区块生成器、两种 Feature
 */
public final class ModWorldgen {
	public static final DeferredRegister<MapCodec<? extends BiomeSource>> BIOME_SOURCES = DeferredRegister.create(Registries.BIOME_SOURCE, MODID);
	public static final DeferredRegister<MapCodec<? extends ChunkGenerator>> CHUNK_GENERATORS = DeferredRegister.create(Registries.CHUNK_GENERATOR, MODID);
	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, MODID);

	public static final DeferredHolder<MapCodec<? extends BiomeSource>, MapCodec<CangyinBiomeSource>> CANGYIN_BIOMES = BIOME_SOURCES.register(
			"cangyin_biomes", () -> CangyinBiomeSource.CODEC
	);
	public static final DeferredHolder<MapCodec<? extends ChunkGenerator>, MapCodec<CangyinTerrainGenerator>> CANGYIN_TERRAIN = CHUNK_GENERATORS.register(
			"cangyin_terrain", () -> CangyinTerrainGenerator.CODEC
	);
	public static final DeferredHolder<Feature<?>, RuinStructureFeature> RUIN_STRUCTURE = FEATURES.register(
			"ruin_structure", () -> new RuinStructureFeature(RuinStructureFeature.Config.CODEC)
	);
	public static final DeferredHolder<Feature<?>, ScatterVegetationFeature> SCATTER_VEGETATION = FEATURES.register(
			"scatter_vegetation", () -> new ScatterVegetationFeature(ScatterVegetationFeature.Config.CODEC)
	);

	private ModWorldgen() {
	}

	public static void register(IEventBus bus) {
		BIOME_SOURCES.register(bus);
		CHUNK_GENERATORS.register(bus);
		FEATURES.register(bus);
	}
}
