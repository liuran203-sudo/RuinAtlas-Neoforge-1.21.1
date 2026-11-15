package com.liuran.ruinatlas.init;

import com.liuran.ruinatlas.entity.XuShadowEntity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 实体注册：墟影（澄海与沉泽墟的游荡灵体）
 */
public final class RaEntities {
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
			DeferredRegister.create(Registries.ENTITY_TYPE, MODID);

	public static final DeferredHolder<EntityType<?>, EntityType<XuShadowEntity>> XU_SHADOW =
			ENTITY_TYPES.register("xu_shadow",
					() -> EntityType.Builder.of(XuShadowEntity::new, MobCategory.AMBIENT)
							.sized(0.7F, 1.1F)
							.clientTrackingRange(8)
							.build("xu_shadow"));

	private RaEntities() {
	}

	public static void register(IEventBus bus) {
		ENTITY_TYPES.register(bus);
	}
}
