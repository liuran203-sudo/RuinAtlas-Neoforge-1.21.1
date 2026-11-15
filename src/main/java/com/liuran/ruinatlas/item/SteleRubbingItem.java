package com.liuran.ruinatlas.item;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 碑文拓本：空拓本在可拓碑上右键会记下碑文键，之后可反复翻阅
 */
public class SteleRubbingItem extends Item {
	public static final String TEXT_KEY = "text";

	public SteleRubbingItem(Properties properties) {
		super(properties);
	}

	public static String textKey(ItemStack stack) {
		CustomData data = stack.get(DataComponents.CUSTOM_DATA);
		return data == null ? "" : data.copyTag().getString(TEXT_KEY);
	}

	public static void setTextKey(ItemStack stack, String key) {
		CompoundTag tag = new CompoundTag();
		tag.putString(TEXT_KEY, key);
		stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		String key = textKey(stack);

		if (!level.isClientSide) {
			if (key.isEmpty()) {
				player.displayClientMessage(Component.translatable("item." + MODID + ".stele_rubbing.blank"), true);
			} else {
				player.displayClientMessage(Component.translatable(key), false);
			}
		}

		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		String key = textKey(stack);

		if (key.isEmpty()) {
			tooltip.add(Component.translatable("item." + MODID + ".stele_rubbing.tooltip_blank")
					.withStyle(ChatFormatting.GRAY));
		} else {
			tooltip.add(Component.translatable(key).withStyle(ChatFormatting.GRAY));
		}

		super.appendHoverText(stack, context, tooltip, flag);
	}
}
