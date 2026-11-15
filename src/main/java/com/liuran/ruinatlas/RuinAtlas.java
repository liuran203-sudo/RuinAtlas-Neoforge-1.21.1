package com.liuran.ruinatlas;

import com.liuran.ruinatlas.init.RaAttachments;
import com.liuran.ruinatlas.init.RaBlockEntities;
import com.liuran.ruinatlas.init.RaBlocks;
import com.liuran.ruinatlas.init.RaCreativeModeTabs;
import com.liuran.ruinatlas.init.RaEntities;
import com.liuran.ruinatlas.init.RaItems;
import com.liuran.ruinatlas.init.RaParticleTypes;
import com.liuran.ruinatlas.technique.RaTechniqueRegistry;
import com.liuran.ruinatlas.worldgen.ModWorldgen;
import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;


@Mod(RuinAtlas.MODID)
public class RuinAtlas {

	public static final String MODID = "ruinatlas";

	public static final Logger LOGGER = LogUtils.getLogger();

	public RuinAtlas(IEventBus modEventBus, ModContainer modContainer) {

		modEventBus.addListener(this::commonSetup);

		// 墟舆录 init 装配：每个类别一个 DeferredRegister，类内注册到 mod 事件总线
		RaBlocks.register(modEventBus);
		RaBlockEntities.register(modEventBus);
		RaItems.register(modEventBus);
		RaEntities.register(modEventBus);
		RaParticleTypes.register(modEventBus);
		RaAttachments.register(modEventBus);
		RaCreativeModeTabs.register(modEventBus);
		ModWorldgen.register(modEventBus);


		NeoForge.EVENT_BUS.register(this);


		modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(RaTechniqueRegistry::bootstrap);
		LOGGER.info("墟舆录 Ruin Atlas 初始化完成");
	}


	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event) {
		LOGGER.info("墟舆录维度加载中");
	}
}
