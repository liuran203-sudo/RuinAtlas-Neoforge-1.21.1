package com.liuran.ruinatlas.datagen;

import com.liuran.ruinatlas.init.RaBlocks;
import com.liuran.ruinatlas.init.RaItems;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

public class RaI18ZhCn extends LanguageProvider {
	public RaI18ZhCn(PackOutput output) {
		super(output, MODID, "zh_cn");
	}

	@Override
	protected void addTranslations() {
		add("pack." + MODID + ".description", "墟舆录内置资源包");

		//region 方块
		add(RaBlocks.XUN_SOIL.get(), "墟壤");
		add(RaBlocks.XUN_LAYER_ROCK.get(), "墟层岩");
		add(RaBlocks.ERODED_STONE.get(), "蚀纹岩");
		add(RaBlocks.CANGYIN_LOG.get(), "苍垠古木原木");
		add(RaBlocks.CANGYIN_BARK.get(), "苍垠古木树皮");
		add(RaBlocks.CANGYIN_PLANKS.get(), "苍垠古木木板");
		add(RaBlocks.XUN_WILD_GRASS.get(), "墟荒草");
		add(RaBlocks.XUN_VINE.get(), "墟蔓");
		add(RaBlocks.XUN_LILY.get(), "墟莲");
		add(RaBlocks.GLOOM_MOSS_BLOCK.get(), "幽苔块");
		add(RaBlocks.PETAL_THICKET.get(), "落英丛");
		add(RaBlocks.WITHER_REED.get(), "枯芦秆");
		add(RaBlocks.XUN_BRICK.get(), "墟古砖");
		add(RaBlocks.WEATHERED_XUN_BRICK.get(), "风化墟古砖");
		add(RaBlocks.WATERLOGGED_XUN_BRICK.get(), "浸水墟古砖");
		add(RaBlocks.CRACKED_XUN_BRICK.get(), "龟裂墟古砖");
		add(RaBlocks.POLISHED_XUN_STONE.get(), "磨面墟石");
		add(RaBlocks.BROKEN_XUN_WALL.get(), "残墟石垛");
		add(RaBlocks.TILE_BLOCK.get(), "青瓦方块");
		add(RaBlocks.BROKEN_TILE_PILE.get(), "破瓦残片堆");
		add(RaBlocks.EAVE_ORNAMENT.get(), "瓦当饰块");
		add(RaBlocks.BLANK_STELE.get(), "无字残碑");
		add(RaBlocks.INSCRIBED_STELE.get(), "刻文残碑");
		add(RaBlocks.BOUNDARY_STELE.get(), "界墟碑");
		add(RaBlocks.SPIRIT_TEST_ALTAR.get(), "测灵台");
		add(RaBlocks.STAR_CARVED_BASE.get(), "星纹础石");
		add(RaBlocks.STONE_INCENSE_TABLE.get(), "石香案");
		add(RaBlocks.STONE_LANTERN.get(), "石灯台");
		add(RaBlocks.EMPTY_CERAMIC_URN.get(), "空陶瓮");
		add(RaBlocks.SEALED_CERAMIC_URN.get(), "封藏陶瓮");
		add(RaBlocks.BONE_SACRIFICE_TABLE.get(), "骨祭台");
		add(RaBlocks.CANGYIN_OFFERING_TABLE.get(), "木供桌");
		add(RaBlocks.WOODEN_SCREEN.get(), "木屏扇");
		add(RaBlocks.STONE_PLINTH.get(), "石础");
		add(RaBlocks.BROKEN_PILLAR_LOWER.get(), "断柱下段");
		add(RaBlocks.BROKEN_PILLAR_UPPER.get(), "断柱上段");
		add(RaBlocks.RUBBLE_PILE.get(), "碎石堆");
		add(RaBlocks.BONE_PILE.get(), "枯骨堆");
		add(RaBlocks.RUSTED_BRONZE_PANEL.get(), "古铜饰件");
		add(RaBlocks.XUN_NICHE.get(), "墟龛");
		add(RaBlocks.SPIRIT_PLATFORM.get(), "引灵石台");
		add(RaBlocks.ANCIENT_BOOK_CASKET.get(), "墟藏书椟");
		add(RaBlocks.QINGYUAN_LOAM.get(), "青渊墟壤");
		add(RaBlocks.PETAL_SPROUT.get(), "落英幼丛");
		add(RaBlocks.FARM_RUIN_PLOT.get(), "荒弃田垄");
		add(RaBlocks.THATCH_RUBBLE.get(), "茅舍残堆");
		add(RaBlocks.SETTLEMENT_SUBSOIL.get(), "邑墟土");
		add(RaBlocks.MARKET_RUBBLE.get(), "市井残堆");
		add(RaBlocks.CITY_CURB.get(), "城基条石");
		add(RaBlocks.ROAD_RUBBLE.get(), "驿道碎石土");
		add(RaBlocks.WAYSTONE.get(), "道记石");
		add(RaBlocks.COLLAPSED_KIOSK.get(), "驿亭残架");
		add(RaBlocks.STREAM_SILT.get(), "涧底淤壤");
		add(RaBlocks.JADE_CHIP_ROCK.get(), "玉屑岩");
		add(RaBlocks.CARVED_STONE_PAVING.get(), "纹石铺地");
		add(RaBlocks.BROKEN_CARVED_PAVING.get(), "残纹铺地");
		add(RaBlocks.STEP_STONE.get(), "素面阶石");
		add(RaBlocks.CARVED_STEP.get(), "雕纹阶石");
		add(RaBlocks.FOUNDATION_SKIRT.get(), "台基散水石");
		add(RaBlocks.LOTUS_PLINTH.get(), "莲纹柱础");
		add(RaBlocks.PLAIN_PLINTH.get(), "素面石础");
		add(RaBlocks.WOODEN_PILLAR_LOWER.get(), "古木残柱下段");
		add(RaBlocks.WOODEN_PILLAR_UPPER.get(), "古木残柱上段");
		add(RaBlocks.PILLAR_BRACE_SHARD.get(), "抱柱残箍");
		add(RaBlocks.BALUSTER_SOLID.get(), "实心栏板");
		add(RaBlocks.BROKEN_BALUSTER.get(), "残栏板");
		add(RaBlocks.NEWEL_POST.get(), "望柱");
		add(RaBlocks.LATTICE_WINDOW_SQUARE.get(), "方棂花窗");
		add(RaBlocks.LATTICE_WAVE_WINDOW.get(), "曲纹花窗");
		add(RaBlocks.BROKEN_LATTICE.get(), "破棂窗");
		add(RaBlocks.BRACKET_SHARD_CORNER.get(), "转角斗拱残块");
		add(RaBlocks.BRACKET_SHARD_SIDE.get(), "平身斗拱残片");
		add(RaBlocks.RIDGE_TILE.get(), "素面脊筒");
		add(RaBlocks.RIDGE_ORNAMENT_SHARD.get(), "脊兽残件");
		add(RaBlocks.EAVE_BOARD_SHARD.get(), "檐板残片");
		add(RaBlocks.DOOR_FRAME_RUIN.get(), "朽门残框");
		add(RaBlocks.WALL_NICHE_SMALL.get(), "小型壁龛");
		add(RaBlocks.CORNER_QUOIN.get(), "隅石");
		add(RaBlocks.CANGYIN_GATE_STELE.get(), "苍垠界碑");
		//endregion

		//region 物品
		add(RaItems.REED_STALK.get(), "古木芦秆");
		add(RaItems.XUN_LILY_PETAL.get(), "墟莲瓣");
		add(RaItems.BONE_SHARD.get(), "骨片");
		add(RaItems.RUSTED_COPPER_DUST.get(), "锈蚀铜屑");
		add(RaItems.RAW_SPIRIT_STONE.get(), "驭灵石原石");
		add(RaItems.DRIED_PETAL.get(), "干落瓣");
		add(RaItems.OLD_COPPER_TOKEN.get(), "旧邑铜铢");
		add(RaItems.TATTERED_MESSAGE.get(), "残简信札");
		add(RaItems.PALE_JADE_SHARD.get(), "淡玉碎料");
		add(RaItems.SPIRIT_HOLDING_STONE.get(), "驭灵石");
		add(RaItems.BREATH_JADE.get(), "引息玉玦");
		add(RaItems.CALMING_BONE_TALLY.get(), "镇厄骨佩");
		add(RaItems.GLOW_SPAR.get(), "逐光萤晶");
		add(RaItems.MOUNTAIN_TILE.get(), "踏岳纹砖");
		add(RaItems.BANNER_SHARD.get(), "清尘灵幡残片");
		add(RaItems.WIND_SHELL_PEARL.get(), "听风螺珠");
		add(RaItems.CLOUD_STEP_RUBBING.get(), "云步拓片");
		add(RaItems.WARD_EVIL_RUBBING.get(), "镇厄拓片");
		add(RaItems.WIND_BLADE_RUBBING.get(), "凝风拓片");
		add(RaItems.REUSABLE_TALISMAN.get(), "引符");
		add(RaItems.STELE_RUBBING.get(), "碑文拓本");
		add(RaItems.ANCIENT_COPPER_PENDANT.get(), "古墟铜佩");
		add(RaItems.SACRIFICIAL_JADE.get(), "祭主玉饰");
		add(RaItems.CANGYIN_DAGGER.get(), "苍垠短匕");
		add(RaItems.HEMP_ROBE.get(), "麻布蔽衣");
		//endregion

		//region 术法卷轴与铜珠
		add(RaItems.CLOUD_STEP_SCROLL.get(), "云步卷轴");
		add(RaItems.WIND_BLADE_SCROLL.get(), "凝风卷轴");
		add(RaItems.WARD_EVIL_SCROLL.get(), "镇厄卷轴");
		add(RaItems.HOLD_BREATH_SCROLL.get(), "引息卷轴");
		add(RaItems.LISTEN_WIND_SCROLL.get(), "听风卷轴");
		add(RaItems.GUIDE_LIGHT_SCROLL.get(), "逐光卷轴");
		add(RaItems.MOUNTAIN_STEP_SCROLL.get(), "踏岳卷轴");
		add(RaItems.CLEANSE_SCROLL.get(), "清尘卷轴");
		add(RaItems.STONE_SENSE_SCROLL.get(), "听脉卷轴");
		add(RaItems.SPIRIT_WARD_SCROLL.get(), "护息卷轴");
		add(RaItems.SPIRIT_COPPER_BEAD.get(), "铜珠");
		add("technique.ruinatlas.cloud_step", "云步");
		add("technique.ruinatlas.wind_blade", "凝风");
		add("technique.ruinatlas.ward_evil", "镇厄");
		add("technique.ruinatlas.hold_breath", "引息");
		add("technique.ruinatlas.listen_wind", "听风");
		add("technique.ruinatlas.guide_light", "逐光");
		add("technique.ruinatlas.mountain_step", "踏岳");
		add("technique.ruinatlas.cleanse", "清尘");
		add("technique.ruinatlas.stone_sense", "听脉");
		add("technique.ruinatlas.spirit_ward", "护息");
		add("item.ruinatlas.technique_scroll.no_spirit", "卷上字迹只是笔画。需先借灵息，方可契悟。");
		add("item.ruinatlas.technique_scroll.learned", "此式早已在册，不必再悟。");
		add("item.ruinatlas.technique_scroll.learn", "「%s」已记入你的术法册。");
		add("item.ruinatlas.technique_scroll.first_read", "契文入目，「%s」初契已成；再研可深契痕。");
		add("item.ruinatlas.technique_scroll.deepen", "契痕深了一层，「%s」进至%s。");
		add("item.ruinatlas.technique_scroll.burnt", "灵纸燃尽，「%s」契满，此卷已无用。");
		add("item.ruinatlas.technique_scroll.full", "此卷道痕已满，再研无益。");
		add("item.ruinatlas.technique_scroll.rank_line", "契痕三层：初契 · 道凝 · 契满，契满则灵纸自燃。");
		add("item.ruinatlas.technique_scroll.tooltip", "右键契悟：初契耗 1 点灵息，之后每深一层耗 3 点。");
		add("item.ruinatlas.technique_scroll.detail", "所载「%s」· 灵息 %s · 冷却 %s 秒");
		add("item.ruinatlas.spirit_copper_bead.tooltip", "手持助力引契：灵息减两成、冷却减两成。");
		add("item.ruinatlas.spirit_copper_bead.tooltip_detail", "契法不靠此珠施放，心念引动即可；此珠只是助灵法器。");
		add("item.ruinatlas.crush_charm.tooltip", "右键捏碎：灵息 +%s。");
		add("item.ruinatlas.crush_charm.tooltip_detail", "灵息不生状态效果，只供契悟与施术；捏碎后此物即散。");
		add("item.ruinatlas.spirit_copper_bead.empty", "铜珠空转——你还未契悟任何一式。");
		add("item.ruinatlas.spirit_copper_bead.no_spirit", "此式需 %s 点灵息，你只剩 %s 点。");
		add("item.ruinatlas.spirit_copper_bead.switched", "铜珠所指转为「%s」。潜行右键可再换一式。");
		add("item.ruinatlas.spirit_copper_bead.cast", "灵息一动，「%s」随念而出。");
		add("item.ruinatlas.spirit_copper_bead.failed", "术未成，灵息未动。");
		//endregion

		//region 创造栏与配置
		add("itemGroup.ruinatlas", "墟舆录");
		add("block.ruinatlas.boundary_stele.text", "界墟碑上刻着一行旧字：此界之内，曾有邦国。如今荒草覆道，无人再守。");
		add("block.ruinatlas.spirit_platform.text", "石台灵纹早已黯淡，唯余一处浅浅的凹槽，像是当年引灵之所。");
		add("block.ruinatlas.waystone.text", "道记石记着往日的里程：去邑三里，去驿七里。字迹被风雨磨去一半。");
		add("block.ruinatlas.cangyin_gate_stele.text", "苍垠界碑横卧荒途，碑阴处有人补刻了八个字：来者皆客，去者无名。碑侧小字：潜行再抚碑身，可越入苍垠。");
		add("block.ruinatlas.spirit_test_altar.result", "引气核验完毕……修炼适宜度：0。此身无载道之基。");
		add("block.ruinatlas.xun_niche.blessing", "旧物入龛，折成灵息 +%s（现余 %s）。");
		add("block.ruinatlas.xun_niche.hint", "墟龛空置已久。献卷轴可代你解卷，献铜珠可记一次不耗灵息的施术，献拓本铜佩玉饰可折成灵息。");
		add("block.ruinatlas.xun_niche.learned_scroll", "龛中残念替你解了此卷，「%s」已录，未耗灵息。");
		add("block.ruinatlas.xun_niche.already_learned", "此式册中已有，龛不收重卷。");
		add("block.ruinatlas.xun_niche.bead_blessed", "残念附上铜珠：可免灵息施术 %s 次。");
		add("block.ruinatlas.xun_niche.bead_full", "铜珠已附满 %s 道残念，先用掉再来。");
		add("block.ruinatlas.xun_niche.rank_filled", "龛中残念替你续了契痕，「%s」直达契满。");
		add("technique.ruinatlas.rank.1", "初契");
		add("technique.ruinatlas.rank.2", "道凝");
		add("technique.ruinatlas.rank.3", "契满");
		add("technique.ruinatlas.cast.success", "灵息一动，「%s」应念而成（%s）。");
		add("technique.ruinatlas.cast.not_learned", "此式尚未契悟，先寻契卷。");
		add("technique.ruinatlas.cast.no_spirit", "催动此契需 %s 点灵息，汝身只余 %s。");
		add("technique.ruinatlas.cast.cooldown", "「%s」契痕未平，稍待再引。");
		add("technique.ruinatlas.cast.failed", "「%s」未成，灵息已还。");
		add("key.categories.ruinatlas", "墟舆录");
		add("key.ruinatlas.cast", "施展契法");
		add("key.ruinatlas.wheel", "灵契轮盘");
		add("hud.ruinatlas.spirit", "灵息 %s");
		add("hud.ruinatlas.technique", "「%s」%s");
		add("screen.ruinatlas.wheel", "灵契轮盘");
		add("screen.ruinatlas.wheel.empty", "尚未契悟任何契卷。");
		add("screen.ruinatlas.wheel.entry", "「%s」%s · 灵息 %s");
		add("screen.ruinatlas.wheel.hint", "点选一道契法，即可换作当前所指。");
		add("block.ruinatlas.ancient_book_casket.opened", "椟盖翻落，积年的拓片从中滑出，落在尘土里。");
		add("block.ruinatlas.cangyin_gate_stele.entered", "碑身一震，荒途在眼前铺开。你已踏入苍垠古墟界。");
		add("block.ruinatlas.cangyin_gate_stele.returned", "碑影一晃，你回到了熟悉的土地上。");
		add("item.ruinatlas.spirit_holding_stone.effect", "石碎灵涌：灵息 +%s（现余 %s）。灵息散尽，你依旧是零基之人。");
		add("item.ruinatlas.breath_jade.effect", "玉碎息藏：灵息 +%s（现余 %s）。生人气息暂且不见，一旦出手，隐韵即刻消散。");
		add("item.ruinatlas.calming_bone_tally.effect", "骨佩压厄：灵息 +%s（现余 %s）。邪秽的侵蚀暂且退去。");
		add("item.ruinatlas.glow_spar.effect", "萤晶引光：灵息 +%s（现余 %s）。光尽则晶散。");
		add("item.ruinatlas.mountain_tile.effect", "纹砖承岳：灵息 +%s（现余 %s）。脚步沉凝，跌落皆免。");
		add("item.ruinatlas.banner_shard.effect", "灵幡残片涤除浊恶：灵息 +%s（现余 %s）。符文耗尽便不复存在。");
		add("item.ruinatlas.wind_shell_pearl.effect", "螺珠引动流风：灵息 +%s（现余 %s）。珠毁风息断绝。");
		add("item.ruinatlas.reusable_talisman.drawn", "符纸燃起一线亮光，古人的秘术被你借出一瞬。");
		add("item.ruinatlas.hemp_robe.tooltip", "粗糙的麻布，勉强遮身。右键可披上身，再右键收回。");
		add("ruinatlas.configuration.title", "墟舆录配置");
		add("ruinatlas.configuration.section.ruinatlas.common.toml", "墟舆录通用配置");
		add("ruinatlas.configuration.section.ruinatlas.common.toml.title", "墟舆录通用配置");
		add("ruinatlas.configuration.items", "物品列表");
		add("ruinatlas.configuration.logDirtBlock", "输出泥土方块日志");
		add("ruinatlas.configuration.magicNumberIntroduction", "数字说明文本");
		add("ruinatlas.configuration.magicNumber", "示例数字");
		//endregion
	}
}
