package com.liuran.ruinatlas.event;

import com.liuran.ruinatlas.entity.XuShadowEntity;
import com.liuran.ruinatlas.init.RaEntities;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 实体事件：给墟影挂上生命、移速与索敌范围
 */
@EventBusSubscriber(modid = MODID)
public final class RaEntityEvents {
	private RaEntityEvents() {
	}

	@SubscribeEvent
	public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
		event.put(RaEntities.XU_SHADOW.get(), XuShadowEntity.createAttributes().build());
	}
}
