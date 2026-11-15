package com.liuran.ruinatlas.item;

import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 麻布蔽衣：右键直接披上身（占用胸甲槽），再右键收回
 */
public class HempRobeItem extends Item {
	public HempRobeItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack held = player.getItemInHand(hand);
		if (hand != InteractionHand.MAIN_HAND) {
			return InteractionResultHolder.pass(held);
		}

		ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
		if (chest.isEmpty()) {
			if (!level.isClientSide) {
				player.setItemSlot(EquipmentSlot.CHEST, held.split(1));
				player.playSound(SoundEvents.ARMOR_EQUIP_LEATHER.value(), 1.0F, 1.0F);
			}

			return InteractionResultHolder.sidedSuccess(held, level.isClientSide);
		} else if (chest.getItem() == this) {
			if (!level.isClientSide) {
				player.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
				player.getInventory().add(chest);
			}

			return InteractionResultHolder.sidedSuccess(held, level.isClientSide);
		} else {
			return InteractionResultHolder.pass(held);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.translatable("item." + MODID + ".hemp_robe.tooltip"));
	}
}
