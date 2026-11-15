package com.liuran.ruinatlas.datagen;

import com.liuran.ruinatlas.init.RaBlocks;
import com.liuran.ruinatlas.init.RaItems;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

public class RaI18EnUs extends LanguageProvider {
	public RaI18EnUs(PackOutput output) {
		super(output, MODID, "en_us");
	}

	@Override
	protected void addTranslations() {
		add("pack." + MODID + ".description", "Ruin Atlas generated resources");

		//region 方块
		add(RaBlocks.XUN_SOIL.get(), "Xun Soil");
		add(RaBlocks.XUN_LAYER_ROCK.get(), "Xun Layer Rock");
		add(RaBlocks.ERODED_STONE.get(), "Eroded Stone");
		add(RaBlocks.CANGYIN_LOG.get(), "Cangyin Log");
		add(RaBlocks.CANGYIN_BARK.get(), "Cangyin Bark");
		add(RaBlocks.CANGYIN_PLANKS.get(), "Cangyin Planks");
		add(RaBlocks.XUN_WILD_GRASS.get(), "Xun Wild Grass");
		add(RaBlocks.XUN_VINE.get(), "Xun Vine");
		add(RaBlocks.XUN_LILY.get(), "Xun Lily");
		add(RaBlocks.GLOOM_MOSS_BLOCK.get(), "Gloom Moss Block");
		add(RaBlocks.PETAL_THICKET.get(), "Petal Thicket");
		add(RaBlocks.WITHER_REED.get(), "Wither Reed");
		add(RaBlocks.XUN_BRICK.get(), "Xun Brick");
		add(RaBlocks.WEATHERED_XUN_BRICK.get(), "Weathered Xun Brick");
		add(RaBlocks.WATERLOGGED_XUN_BRICK.get(), "Waterlogged Xun Brick");
		add(RaBlocks.CRACKED_XUN_BRICK.get(), "Cracked Xun Brick");
		add(RaBlocks.POLISHED_XUN_STONE.get(), "Polished Xun Stone");
		add(RaBlocks.BROKEN_XUN_WALL.get(), "Broken Xun Wall");
		add(RaBlocks.TILE_BLOCK.get(), "Tile Block");
		add(RaBlocks.BROKEN_TILE_PILE.get(), "Broken Tile Pile");
		add(RaBlocks.EAVE_ORNAMENT.get(), "Eave Ornament");
		add(RaBlocks.BLANK_STELE.get(), "Blank Stele");
		add(RaBlocks.INSCRIBED_STELE.get(), "Inscribed Stele");
		add(RaBlocks.BOUNDARY_STELE.get(), "Boundary Stele");
		add(RaBlocks.SPIRIT_TEST_ALTAR.get(), "Spirit Test Altar");
		add(RaBlocks.STAR_CARVED_BASE.get(), "Star Carved Base");
		add(RaBlocks.STONE_INCENSE_TABLE.get(), "Stone Incense Table");
		add(RaBlocks.STONE_LANTERN.get(), "Stone Lantern");
		add(RaBlocks.EMPTY_CERAMIC_URN.get(), "Empty Ceramic Urn");
		add(RaBlocks.SEALED_CERAMIC_URN.get(), "Sealed Ceramic Urn");
		add(RaBlocks.BONE_SACRIFICE_TABLE.get(), "Bone Sacrifice Table");
		add(RaBlocks.CANGYIN_OFFERING_TABLE.get(), "Cangyin Offering Table");
		add(RaBlocks.WOODEN_SCREEN.get(), "Wooden Screen");
		add(RaBlocks.STONE_PLINTH.get(), "Stone Plinth");
		add(RaBlocks.BROKEN_PILLAR_LOWER.get(), "Broken Pillar Lower");
		add(RaBlocks.BROKEN_PILLAR_UPPER.get(), "Broken Pillar Upper");
		add(RaBlocks.RUBBLE_PILE.get(), "Rubble Pile");
		add(RaBlocks.BONE_PILE.get(), "Bone Pile");
		add(RaBlocks.RUSTED_BRONZE_PANEL.get(), "Rusted Bronze Panel");
		add(RaBlocks.XUN_NICHE.get(), "Xun Niche");
		add(RaBlocks.SPIRIT_PLATFORM.get(), "Spirit Platform");
		add(RaBlocks.ANCIENT_BOOK_CASKET.get(), "Ancient Book Casket");
		add(RaBlocks.QINGYUAN_LOAM.get(), "Qingyuan Loam");
		add(RaBlocks.PETAL_SPROUT.get(), "Petal Sprout");
		add(RaBlocks.FARM_RUIN_PLOT.get(), "Farm Ruin Plot");
		add(RaBlocks.THATCH_RUBBLE.get(), "Thatch Rubble");
		add(RaBlocks.SETTLEMENT_SUBSOIL.get(), "Settlement Subsoil");
		add(RaBlocks.MARKET_RUBBLE.get(), "Market Rubble");
		add(RaBlocks.CITY_CURB.get(), "City Curb");
		add(RaBlocks.ROAD_RUBBLE.get(), "Road Rubble");
		add(RaBlocks.WAYSTONE.get(), "Waystone");
		add(RaBlocks.COLLAPSED_KIOSK.get(), "Collapsed Kiosk");
		add(RaBlocks.STREAM_SILT.get(), "Stream Silt");
		add(RaBlocks.JADE_CHIP_ROCK.get(), "Jade Chip Rock");
		add(RaBlocks.CARVED_STONE_PAVING.get(), "Carved Stone Paving");
		add(RaBlocks.BROKEN_CARVED_PAVING.get(), "Broken Carved Paving");
		add(RaBlocks.STEP_STONE.get(), "Step Stone");
		add(RaBlocks.CARVED_STEP.get(), "Carved Step");
		add(RaBlocks.FOUNDATION_SKIRT.get(), "Foundation Skirt");
		add(RaBlocks.LOTUS_PLINTH.get(), "Lotus Plinth");
		add(RaBlocks.PLAIN_PLINTH.get(), "Plain Plinth");
		add(RaBlocks.WOODEN_PILLAR_LOWER.get(), "Wooden Pillar Lower");
		add(RaBlocks.WOODEN_PILLAR_UPPER.get(), "Wooden Pillar Upper");
		add(RaBlocks.PILLAR_BRACE_SHARD.get(), "Pillar Brace Shard");
		add(RaBlocks.BALUSTER_SOLID.get(), "Baluster Solid");
		add(RaBlocks.BROKEN_BALUSTER.get(), "Broken Baluster");
		add(RaBlocks.NEWEL_POST.get(), "Newel Post");
		add(RaBlocks.LATTICE_WINDOW_SQUARE.get(), "Lattice Window Square");
		add(RaBlocks.LATTICE_WAVE_WINDOW.get(), "Lattice Wave Window");
		add(RaBlocks.BROKEN_LATTICE.get(), "Broken Lattice");
		add(RaBlocks.BRACKET_SHARD_CORNER.get(), "Bracket Shard Corner");
		add(RaBlocks.BRACKET_SHARD_SIDE.get(), "Bracket Shard Side");
		add(RaBlocks.RIDGE_TILE.get(), "Ridge Tile");
		add(RaBlocks.RIDGE_ORNAMENT_SHARD.get(), "Ridge Ornament Shard");
		add(RaBlocks.EAVE_BOARD_SHARD.get(), "Eave Board Shard");
		add(RaBlocks.DOOR_FRAME_RUIN.get(), "Door Frame Ruin");
		add(RaBlocks.WALL_NICHE_SMALL.get(), "Wall Niche Small");
		add(RaBlocks.CORNER_QUOIN.get(), "Corner Quoin");
		add(RaBlocks.CANGYIN_GATE_STELE.get(), "Cangyin Gate Stele");
		//endregion

		//region 物品
		add(RaItems.REED_STALK.get(), "Reed Stalk");
		add(RaItems.XUN_LILY_PETAL.get(), "Xun Lily Petal");
		add(RaItems.BONE_SHARD.get(), "Bone Shard");
		add(RaItems.RUSTED_COPPER_DUST.get(), "Rusted Copper Dust");
		add(RaItems.RAW_SPIRIT_STONE.get(), "Raw Spirit Stone");
		add(RaItems.DRIED_PETAL.get(), "Dried Petal");
		add(RaItems.OLD_COPPER_TOKEN.get(), "Old Copper Token");
		add(RaItems.TATTERED_MESSAGE.get(), "Tattered Message");
		add(RaItems.PALE_JADE_SHARD.get(), "Pale Jade Shard");
		add(RaItems.SPIRIT_HOLDING_STONE.get(), "Spirit Holding Stone");
		add(RaItems.BREATH_JADE.get(), "Breath Jade");
		add(RaItems.CALMING_BONE_TALLY.get(), "Calming Bone Tally");
		add(RaItems.GLOW_SPAR.get(), "Glow Spar");
		add(RaItems.MOUNTAIN_TILE.get(), "Mountain Tile");
		add(RaItems.BANNER_SHARD.get(), "Banner Shard");
		add(RaItems.WIND_SHELL_PEARL.get(), "Wind Shell Pearl");
		add(RaItems.CLOUD_STEP_RUBBING.get(), "Cloud Step Rubbing");
		add(RaItems.WARD_EVIL_RUBBING.get(), "Ward Evil Rubbing");
		add(RaItems.WIND_BLADE_RUBBING.get(), "Wind Blade Rubbing");
		add(RaItems.REUSABLE_TALISMAN.get(), "Reusable Talisman");
		add(RaItems.STELE_RUBBING.get(), "Stele Rubbing");
		add(RaItems.ANCIENT_COPPER_PENDANT.get(), "Ancient Copper Pendant");
		add(RaItems.SACRIFICIAL_JADE.get(), "Sacrificial Jade");
		add(RaItems.CANGYIN_DAGGER.get(), "Cangyin Dagger");
		add(RaItems.HEMP_ROBE.get(), "Hemp Robe");
		//endregion

		//region 术法卷轴与铜珠
		add(RaItems.CLOUD_STEP_SCROLL.get(), "Cloud Step Scroll");
		add(RaItems.WIND_BLADE_SCROLL.get(), "Wind Blade Scroll");
		add(RaItems.WARD_EVIL_SCROLL.get(), "Warding Scroll");
		add(RaItems.HOLD_BREATH_SCROLL.get(), "Hold Breath Scroll");
		add(RaItems.LISTEN_WIND_SCROLL.get(), "Listen to the Wind Scroll");
		add(RaItems.GUIDE_LIGHT_SCROLL.get(), "Chase the Light Scroll");
		add(RaItems.MOUNTAIN_STEP_SCROLL.get(), "Mountain Step Scroll");
		add(RaItems.CLEANSE_SCROLL.get(), "Cleanse Scroll");
		add(RaItems.STONE_SENSE_SCROLL.get(), "Stone Sense Scroll");
		add(RaItems.SPIRIT_WARD_SCROLL.get(), "Spirit Ward Scroll");
		add(RaItems.SPIRIT_COPPER_BEAD.get(), "Copper Bead");
		add("technique.ruinatlas.cloud_step", "Cloud Step");
		add("technique.ruinatlas.wind_blade", "Wind Blade");
		add("technique.ruinatlas.ward_evil", "Warding");
		add("technique.ruinatlas.hold_breath", "Hold Breath");
		add("technique.ruinatlas.listen_wind", "Listen to the Wind");
		add("technique.ruinatlas.guide_light", "Chase the Light");
		add("technique.ruinatlas.mountain_step", "Mountain Step");
		add("technique.ruinatlas.cleanse", "Cleanse");
		add("technique.ruinatlas.stone_sense", "Stone Sense");
		add("technique.ruinatlas.spirit_ward", "Spirit Ward");
		add("item.ruinatlas.technique_scroll.no_spirit", "The marks on the scroll are only strokes. Borrow spirit first, then the technique can be understood.");
		add("item.ruinatlas.technique_scroll.learned", "This form is already recorded. No need to learn it again.");
		add("item.ruinatlas.technique_scroll.learn", "\"%s\" is now recorded in your book of techniques.");
		add("item.ruinatlas.technique_scroll.first_read", "The marks settle into your eyes: \"%s\" is first understood. Study it again to deepen the trace.");
		add("item.ruinatlas.technique_scroll.deepen", "The trace deepens: \"%s\" now stands at %s.");
		add("item.ruinatlas.technique_scroll.burnt", "The spirit-paper burns away. \"%s\" is complete; this scroll is spent.");
		add("item.ruinatlas.technique_scroll.full", "Its trace is already full; studying it again gains nothing.");
		add("item.ruinatlas.technique_scroll.rank_line", "Three layers of trace: First Trace · Trace Settled · Trace Complete; a complete scroll burns itself.");
		add("item.ruinatlas.technique_scroll.tooltip", "Right-click to understand: 1 spirit for the first trace, 3 spirit for each deeper layer.");
		add("item.ruinatlas.technique_scroll.detail", "Carries \"%s\" · spirit %s · cooldown %s s");
		add("item.ruinatlas.spirit_copper_bead.tooltip", "Hold it to aid casting: spirit and cooldown both reduced by a fifth.");
		add("item.ruinatlas.spirit_copper_bead.tooltip_detail", "Forms do not need this bead to be cast; a thought is enough. The bead is only an aid.");
		add("item.ruinatlas.crush_charm.tooltip", "Right-click to crush: spirit +%s.");
		add("item.ruinatlas.crush_charm.tooltip_detail", "Spirit is no status effect, only fuel for understanding and casting. Crushing destroys the relic.");
		add("item.ruinatlas.spirit_copper_bead.empty", "The copper bead spins on nothing — you have not understood a single form.");
		add("item.ruinatlas.spirit_copper_bead.no_spirit", "This form needs %s spirit; you have only %s.");
		add("item.ruinatlas.spirit_copper_bead.switched", "The copper bead now points to \"%s\". Sneak-use to change it again.");
		add("item.ruinatlas.spirit_copper_bead.cast", "A stir of spirit, and \"%s\" follows your thought.");
		add("item.ruinatlas.spirit_copper_bead.failed", "The form did not take. No spirit was spent.");
		//endregion

		//region 创造栏与配置
		add("itemGroup.ruinatlas", "Ruin Atlas");
		add("block.ruinatlas.boundary_stele.text", "The boundary stele reads: within this border there once were kingdoms. Now grass covers the road and no one guards it.");
		add("block.ruinatlas.spirit_platform.text", "The spirit patterns on the stone are long faded, leaving only a shallow hollow where spirits were once drawn.");
		add("block.ruinatlas.waystone.text", "The waystone records an old mileage: three li to the town, seven to the post. Half the strokes are worn away.");
		add("block.ruinatlas.cangyin_gate_stele.text", "The Cangyin gate stele lies across the wild road. On its back someone carved: guests may come, the departed leave no name. A smaller line reads: sneak and touch the stone again to cross into Cangyin.");
		add("block.ruinatlas.spirit_test_altar.result", "Breath drawn and measured... aptitude for cultivation: 0. This body has no foundation to carry the way.");
		add("block.ruinatlas.xun_niche.blessing", "The relic sinks into the niche and is folded into spirit +%s (now %s).");
		add("block.ruinatlas.xun_niche.hint", "The niche has stood empty. A scroll is unrolled for you, a copper bead takes one spirit-free cast, and rubbings, pendants or jades fold into spirit.");
		add("block.ruinatlas.xun_niche.learned_scroll", "The lingering will unrolls it for you: \"%s\" is recorded, at no cost of spirit.");
		add("block.ruinatlas.xun_niche.already_learned", "This form is already in your book; the niche takes no second scroll.");
		add("block.ruinatlas.xun_niche.bead_blessed", "The will settles on your copper bead: %s spirit-free cast(s).");
		add("block.ruinatlas.xun_niche.bead_full", "The bead already holds %s strands of will. Spend them first.");
		add("block.ruinatlas.xun_niche.rank_filled", "The lingering will renews your trace: \"%s\" is brought straight to completion.");
		add("technique.ruinatlas.rank.1", "First Trace");
		add("technique.ruinatlas.rank.2", "Trace Settled");
		add("technique.ruinatlas.rank.3", "Trace Complete");
		add("technique.ruinatlas.cast.success", "Spirit stirs: \"%s\" answers your thought (%s).");
		add("technique.ruinatlas.cast.not_learned", "This form is not yet understood. Find its scroll first.");
		add("technique.ruinatlas.cast.no_spirit", "This form needs %s spirit; you hold only %s.");
		add("technique.ruinatlas.cast.cooldown", "\"%s\" has not settled yet. Wait a moment.");
		add("technique.ruinatlas.cast.failed", "\"%s\" did not take; the spirit was returned.");
		add("key.categories.ruinatlas", "Ruin Atlas");
		add("key.ruinatlas.cast", "Cast Form");
		add("key.ruinatlas.wheel", "Form Wheel");
		add("hud.ruinatlas.spirit", "Spirit %s");
		add("hud.ruinatlas.technique", "\"%s\" %s");
		add("screen.ruinatlas.wheel", "Form Wheel");
		add("screen.ruinatlas.wheel.empty", "No scroll has been understood yet.");
		add("screen.ruinatlas.wheel.entry", "\"%s\" %s · spirit %s");
		add("screen.ruinatlas.wheel.hint", "Click a form to make it the current one.");
		add("block.ruinatlas.ancient_book_casket.opened", "The lid falls open and years of rubbings slide out into the dust.");
		add("block.ruinatlas.cangyin_gate_stele.entered", "The stele shudders and the wild road opens before you. You have stepped into the Cangyin Ruin Realm.");
		add("block.ruinatlas.cangyin_gate_stele.returned", "The stele flickers and you are back on familiar ground.");
		add("item.ruinatlas.spirit_holding_stone.effect", "The stone crumbles and spirit rushes in: +%s spirit (%s left). When it fades you are still the one with no foundation.");
		add("item.ruinatlas.breath_jade.effect", "The jade breaks and breath is hidden: +%s spirit (%s left). Strike once and the concealment is gone.");
		add("item.ruinatlas.calming_bone_tally.effect", "The tally presses the curse back: +%s spirit (%s left).");
		add("item.ruinatlas.glow_spar.effect", "The spar draws light: +%s spirit (%s left). When the light ends, the crystal is gone.");
		add("item.ruinatlas.mountain_tile.effect", "The tile carries the weight of the mountain: +%s spirit (%s left). Your steps are steady and falls are spared.");
		add("item.ruinatlas.banner_shard.effect", "The faded banner washes away the foulness: +%s spirit (%s left).");
		add("item.ruinatlas.wind_shell_pearl.effect", "The pearl stirs the flowing wind: +%s spirit (%s left).");
		add("item.ruinatlas.reusable_talisman.drawn", "The talisman paper burns a thin bright line. For an instant an old spell is drawn into you.");
		add("item.ruinatlas.hemp_robe.tooltip", "Coarse hemp cloth, barely enough to cover the body. Right-click to wear it, right-click again to take it off.");
		add("ruinatlas.configuration.title", "Ruin Atlas Configs");
		add("ruinatlas.configuration.section.ruinatlas.common.toml", "Ruin Atlas Common Configs");
		add("ruinatlas.configuration.section.ruinatlas.common.toml.title", "Ruin Atlas Common Configs");
		add("ruinatlas.configuration.items", "Item List");
		add("ruinatlas.configuration.logDirtBlock", "Log Dirt Block");
		add("ruinatlas.configuration.magicNumberIntroduction", "Magic Number Text");
		add("ruinatlas.configuration.magicNumber", "Magic Number");
		//endregion
	}
}
