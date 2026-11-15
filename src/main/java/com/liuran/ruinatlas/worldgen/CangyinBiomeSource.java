package com.liuran.ruinatlas.worldgen;

import java.util.List;
import java.util.stream.Stream;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate.Sampler;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;

/**
 * 苍垠疆域群系源：按 28×28 噪声格划出势力地块，格心带扰动，同一格内恒定一个群系
 */
public class CangyinBiomeSource extends BiomeSource {
	public static final MapCodec<CangyinBiomeSource> CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(Biome.LIST_CODEC.fieldOf("biomes").forGetter(src -> src.biomeSet))
					.apply(instance, src -> new CangyinBiomeSource(src, List.copyOf(src.stream().toList()))
					)
	);
	public static final int REGION_NOISE = 28;
	public static final int REGION_BLOCKS = 112;
	private static final PerlinSimplexNoise JITTER_X = new PerlinSimplexNoise(RandomSource.create(5904353L), List.of(0, 1));
	private static final PerlinSimplexNoise JITTER_Z = new PerlinSimplexNoise(RandomSource.create(10368889L), List.of(0, 1));
	private final HolderSet<Biome> biomeSet;
	private final List<Holder<Biome>> biomes;

	public CangyinBiomeSource(HolderSet<Biome> biomeSet) {
		this(biomeSet, List.copyOf(biomeSet.stream().toList()));
	}

	private CangyinBiomeSource(HolderSet<Biome> biomeSet, List<Holder<Biome>> biomes) {
		this.biomeSet = biomeSet;
		this.biomes = biomes;
	}

	@Override
	protected MapCodec<? extends BiomeSource> codec() {
		return CODEC;
	}

	@Override
	protected Stream<Holder<Biome>> collectPossibleBiomes() {
		return this.biomes.stream();
	}

	@Override
	public Holder<Biome> getNoiseBiome(int x, int y, int z, Sampler sampler) {
		return this.territoryBiome(x, z);
	}

	public Holder<Biome> territoryBiome(int noiseX, int noiseZ) {
		if (this.biomes.isEmpty()) {
			throw new IllegalStateException("苍垠疆域群系源缺少群系");
		}

		int cellX = Mth.floorDiv(noiseX, 28);
		int cellZ = Mth.floorDiv(noiseZ, 28);
		int bestX = cellX;
		int bestZ = cellZ;
		double bestDist = Double.MAX_VALUE;

		for (int ox = -1; ox <= 1; ox++) {
			for (int oz = -1; oz <= 1; oz++) {
				int nx = cellX + ox;
				int nz = cellZ + oz;
				double sx = (nx + siteJitter(nx, nz, 0)) * 28.0;
				double sz = (nz + siteJitter(nx, nz, 1)) * 28.0;
				double dx = sx - noiseX;
				double dz = sz - noiseZ;
				double d = dx * dx + dz * dz;
				if (d < bestDist) {
					bestDist = d;
					bestX = nx;
					bestZ = nz;
				}
			}
		}

		return this.biomes.get(Math.floorMod(longHash(bestX, bestZ), this.biomes.size()));
	}

	private static double siteJitter(int cellX, int cellZ, int axis) {
		double n = axis == 0
				? JITTER_X.getValue(cellX * 0.73, cellZ * 0.73, true)
				: JITTER_Z.getValue(cellX * 0.73 + 3.1, cellZ * 0.73 - 4.7, true);
		return Mth.clamp(n, -1.0, 1.0) * 0.42;
	}

	private static long longHash(int x, int z) {
		long h = x * 341873128712L + z * 132897987541L + 10889L;
		h ^= h >>> 33;
		h *= -49064778989728563L;
		return h ^ h >>> 33;
	}
}
