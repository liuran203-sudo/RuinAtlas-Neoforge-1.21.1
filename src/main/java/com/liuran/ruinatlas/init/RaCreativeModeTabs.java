package com.liuran.ruinatlas.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

public class RaCreativeModeTabs {
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
			DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> RUIN_ATLAS_TAB = CREATIVE_MODE_TABS.register("ruin_atlas", () -> CreativeModeTab.builder()
			.title(Component.translatable("itemGroup.ruinatlas"))
			.withTabsBefore(CreativeModeTabs.BUILDING_BLOCKS)
			.icon(() -> RaItems.SPIRIT_HOLDING_STONE.get().getDefaultInstance())
			.displayItems((parameters, output) -> RaItems.ITEM_REGISTER.getEntries().forEach(entry -> output.accept(entry.get())))
			.build());

	public static void register(IEventBus bus) {
		CREATIVE_MODE_TABS.register(bus);
	}
}
