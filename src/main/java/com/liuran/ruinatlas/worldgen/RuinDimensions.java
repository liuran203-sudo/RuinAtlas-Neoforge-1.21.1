package com.liuran.ruinatlas.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.loot.LootTable;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 维度键与注册键集合：苍垠古墟界、四个疆域群系、两处箱子战利品表
 */
public final class RuinDimensions {
	public static final ResourceKey<Level> CANGYIN = ResourceKey.create(Registries.DIMENSION, key("cangyin"));
	public static final ResourceKey<Biome> QINGYUAN_GREENFIELD = ResourceKey.create(Registries.BIOME, key("qingyuan_greenfield"));
	public static final ResourceKey<Biome> PINGGAO_OLD_TOWN = ResourceKey.create(Registries.BIOME, key("pinggao_old_town"));
	public static final ResourceKey<Biome> LONG_ROAD_WASTEWAY = ResourceKey.create(Registries.BIOME, key("long_road_wasteway"));
	public static final ResourceKey<Biome> SHENYU_RAVINE = ResourceKey.create(Registries.BIOME, key("shenyu_ravine"));
	public static final ResourceKey<LootTable> KIOSK_CHEST = ResourceKey.create(Registries.LOOT_TABLE, key("chest/way_kiosk"));
	public static final ResourceKey<LootTable> CELLAR_CHEST = ResourceKey.create(Registries.LOOT_TABLE, key("chest/ruin_cellar"));

	private RuinDimensions() {
	}

	public static ResourceLocation key(String name) {
		return ResourceLocation.fromNamespaceAndPath(MODID, name);
	}

	public static ServerLevel cangyinLevel(MinecraftServer server) {
		return server.getLevel(CANGYIN);
	}
}
