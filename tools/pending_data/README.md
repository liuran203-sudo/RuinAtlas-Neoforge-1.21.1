# 待启用数据（已全部启用，仅留档）

2026-11-14：worldgen 6 个类移植后，本目录下的 5 个 json 已全部移入 `src/main/resources/data/ruinatlas/`，
`pending_data` 现在只剩这份说明。留存是为了记录当初为什么搁置。

## 当初的阻塞点与现在的状态

| 原阻塞 | 现在 |
|---|---|
| `dimension/cangyin.json` 引用 `ruinatlas:cangyin_terrain` / `ruinatlas:cangyin_biomes` | 已实现 `CangyinTerrainGenerator` / `CangyinBiomeSource`，注册在 `worldgen/ModWorldgen` |
| 4 个群系引用 `ruinatlas:ruin_structure` | 已实现 `RuinStructureFeature`（15 种 kind） |
| 4 个群系引用 `ruinatlas:scatter_vegetation` | 已实现 `ScatterVegetationFeature`（原版文件已补传，为忠实移植版） |

## 迁移后的最终路径

- `src/main/resources/data/ruinatlas/dimension/cangyin.json`
- `src/main/resources/data/ruinatlas/dimension_type/cangyin.json`
- `src/main/resources/data/ruinatlas/worldgen/biome/{qingyuan_greenfield,pinggao_old_town,long_road_wasteway,shenyu_ravine}.json`

## 注意

`ScatterVegetationFeature` 的 config 与 json 字段完全对齐（tries / xz / y_down / y_up / plants / grounds），
行为按字段语义实现：随机抛点 → 落点须空气 → 正下方须是 grounds 之一 → 从 plants 随机放一个。
若后续拿到原始实现，直接替换该类即可，群系 json 不用改。
