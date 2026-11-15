package com.liuran.ruinatlas.init;

import com.liuran.ruinatlas.blockentity.CeramicUrnBlockEntity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 方块实体注册：目前只有陶瓮（存物用的单格容器）
 */
public final class RaBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
			DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CeramicUrnBlockEntity>> CERAMIC_URN =
			BLOCK_ENTITIES.register("ceramic_urn",
					() -> BlockEntityType.Builder.of(CeramicUrnBlockEntity::new,
							RaBlocks.EMPTY_CERAMIC_URN.get(), RaBlocks.SEALED_CERAMIC_URN.get()).build(null));

	private RaBlockEntities() {
	}

	public static void register(IEventBus bus) {
		BLOCK_ENTITIES.register(bus);
	}
}
