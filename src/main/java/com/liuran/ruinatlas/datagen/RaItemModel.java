package com.liuran.ruinatlas.datagen;

import com.liuran.ruinatlas.init.RaItems;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 物品模型生成器：方块物品的模型由 RaBlockModel 一并生成，这里只处理普通物品
 */
public class RaItemModel extends ItemModelProvider {
	public RaItemModel(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, MODID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		for (var entry : RaItems.ITEM_REGISTER.getEntries()) {
			Item item = entry.get();
			if (item instanceof BlockItem) {
				continue;
			}
			basicItem(item);
		}
	}
}
