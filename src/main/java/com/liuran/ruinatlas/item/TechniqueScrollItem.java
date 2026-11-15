package com.liuran.ruinatlas.item;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import com.liuran.ruinatlas.init.RaAttachments;
import com.liuran.ruinatlas.technique.RaTechniqueInfo;
import com.liuran.ruinatlas.technique.RaTechniqueRegistry;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 一阶契卷：初阅记名，反复研读可深契痕
 * <p>第一次右键（需 1 点灵息）只是识得契名，卷留下；
 * 之后每读一次耗 3 点灵息，契痕加深一层（初契 → 道凝 → 契满）；
 * 契满之后灵纸燃尽，卷自毁。也可直接送入墟龛，一卷换一道契满。
 */
public class TechniqueScrollItem extends Item {
	public TechniqueScrollItem(Properties properties) {
		super(properties.stacksTo(16));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		RaTechniqueInfo info = RaTechniqueRegistry.byScroll(stack);

		if (info == null) {
			return InteractionResultHolder.pass(stack);
		}

		if (level.isClientSide || !(player instanceof ServerPlayer)) {
			return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
		}

		Component name = Component.translatable(info.nameKey());

		if (!RaAttachments.hasLearned(player, info.id())) {
			if (!RaAttachments.spendSpirit(player, 1)) {
				player.displayClientMessage(Component.translatable("item." + MODID + ".technique_scroll.no_spirit"), true);
				return InteractionResultHolder.fail(stack);
			}

			RaAttachments.learn(player, info.id());
			player.displayClientMessage(Component.translatable("item." + MODID + ".technique_scroll.first_read", name), true);
			level.playSound(null, player.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.7F, 1.4F);
			return InteractionResultHolder.sidedSuccess(stack, false);
		}

		int rank = RaAttachments.getRank(player, info.id());

		if (rank >= 3) {
			player.displayClientMessage(Component.translatable("item." + MODID + ".technique_scroll.full"), true);
			return InteractionResultHolder.fail(stack);
		}

		if (!RaAttachments.spendSpirit(player, 3)) {
			player.displayClientMessage(Component.translatable("item." + MODID + ".technique_scroll.no_spirit"), true);
			return InteractionResultHolder.fail(stack);
		}

		int newRank = RaAttachments.raiseRank(player, info.id());
		level.playSound(null, player.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS,
				0.7F, 1.2F + newRank * 0.1F);

		if (newRank >= 3) {
			stack.shrink(1);
			player.displayClientMessage(Component.translatable("item." + MODID + ".technique_scroll.burnt", name), true);
		} else {
			player.displayClientMessage(Component.translatable("item." + MODID + ".technique_scroll.deepen",
					name, Component.translatable("technique." + MODID + ".rank." + newRank)), true);
		}

		return InteractionResultHolder.sidedSuccess(stack, false);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		RaTechniqueInfo info = RaTechniqueRegistry.byScroll(stack);

		if (info != null) {
			tooltip.add(Component.translatable("item." + MODID + ".technique_scroll.detail",
					Component.translatable(info.nameKey()),
					info.spiritCost(),
					String.format("%.1f", info.cooldown() / 20.0F)).withStyle(ChatFormatting.AQUA));
		}

		tooltip.add(Component.translatable("item." + MODID + ".technique_scroll.rank_line").withStyle(ChatFormatting.DARK_AQUA));
		tooltip.add(Component.translatable("item." + MODID + ".technique_scroll.tooltip").withStyle(ChatFormatting.GRAY));
		super.appendHoverText(stack, context, tooltip, flag);
	}
}
