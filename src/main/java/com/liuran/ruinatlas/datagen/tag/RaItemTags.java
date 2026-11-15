package com.liuran.ruinatlas.datagen.tag;

import java.util.concurrent.CompletableFuture;

import javax.annotation.CheckForNull;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import com.liuran.ruinatlas.init.RaItems;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 物品标签生成器：让古木与古墟木板进入原版木料物品标签
 */
public class RaItemTags extends ItemTagsProvider {
	public RaItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
	                  CompletableFuture<TagLookup<Block>> blockTags,
	                  @CheckForNull ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, blockTags, MODID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.@NotNull Provider capability) {
		tag(ItemTags.LOGS).add(RaItems.CANGYIN_LOG.get(), RaItems.CANGYIN_BARK.get());
		tag(ItemTags.LOGS_THAT_BURN).add(RaItems.CANGYIN_LOG.get(), RaItems.CANGYIN_BARK.get());
		tag(ItemTags.PLANKS).add(RaItems.CANGYIN_PLANKS.get());
		tag(ItemTags.FENCES).add(RaItems.XUN_BRICK_FENCE.get());
	}
}
