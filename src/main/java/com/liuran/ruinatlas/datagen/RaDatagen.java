package com.liuran.ruinatlas.datagen;

import java.util.concurrent.CompletableFuture;

import net.minecraft.DetectedVersion;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import com.liuran.ruinatlas.datagen.loot.RaLootTableProvider;
import com.liuran.ruinatlas.datagen.recipe.RaRecipeProvider;
import com.liuran.ruinatlas.datagen.tag.RaBlockTags;
import com.liuran.ruinatlas.datagen.tag.RaItemTags;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 数据生成主类：runData 时挂载全部 Provider
 */
@EventBusSubscriber(modid = MODID)
public class RaDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		boolean client = event.includeClient();
		boolean server = event.includeServer();
		DataGenerator dataGenerator = event.getGenerator();
		PackOutput output = dataGenerator.getPackOutput();
		ExistingFileHelper helper = event.getExistingFileHelper();
		CompletableFuture<HolderLookup.Provider> completableFuture = event.getLookupProvider();

		dataGenerator.addProvider(true, new PackMetadataGenerator(output)
				.add(PackMetadataSection.TYPE, new PackMetadataSection(
						Component.translatable("pack." + MODID + ".description"),
						DetectedVersion.BUILT_IN.getPackVersion(PackType.SERVER_DATA))));

		// 客户端数据生成
		dataGenerator.addProvider(client, new RaBlockModel(output, helper));
		dataGenerator.addProvider(client, new RaItemModel(output, helper));
		dataGenerator.addProvider(client, new RaI18ZhCn(output));
		dataGenerator.addProvider(client, new RaI18EnUs(output));

		// 服务端数据生成
		RaBlockTags blockTags = new RaBlockTags(output, completableFuture, helper);
		dataGenerator.addProvider(server, blockTags);
		dataGenerator.addProvider(server, new RaItemTags(output, completableFuture, blockTags.contentsGetter(), helper));
		dataGenerator.addProvider(server, new RaLootTableProvider(output, completableFuture));
		dataGenerator.addProvider(server, new RaRecipeProvider(output, completableFuture));
	}
}
