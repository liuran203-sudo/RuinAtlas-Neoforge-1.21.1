package com.liuran.ruinatlas.item;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 守墟碎玉：稀有遗物，供奉给墟龛时锁定当前所指的契法
 * <p>锁定之后，研读与供奉的契痕只会落在那一门上，避免升错功法。
 */
public class WardJadeShardItem extends Item {
	public WardJadeShardItem(Properties properties) {
		super(properties.stacksTo(16));
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.translatable("item." + MODID + ".ward_jade_shard.tooltip")
				.withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("item." + MODID + ".ward_jade_shard.tooltip_detail")
				.withStyle(ChatFormatting.DARK_GRAY));
		super.appendHoverText(stack, context, tooltip, flag);
	}
}
