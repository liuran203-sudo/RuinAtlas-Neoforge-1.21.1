package com.liuran.ruinatlas.item;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
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

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 灵息药盏：可反复使用，先缓回一口灵息，再让药性持续一小段时间
 * <p>冷却较长，不能当无脑续航道具。
 */
public class SpiritMedicLadleItem extends Item {
	private static final int COOLDOWN_TICKS = 1200;
	private static final int INSTANT_SPIRIT = 20;
	private static final int BREW_TICKS = 400;

	public SpiritMedicLadleItem(Properties properties) {
		super(properties.stacksTo(1));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (player.getCooldowns().isOnCooldown(this)) {
			return InteractionResultHolder.fail(stack);
		}

		if (!level.isClientSide) {
			RaAttachments.addSpirit(player, INSTANT_SPIRIT);
			long now = level.getGameTime();

			if (!RaAttachments.hasDustBoost(player, now)) {
				RaAttachments.setDustBoostUntil(player, now + BREW_TICKS);
			}

			player.displayClientMessage(Component.translatable("item." + MODID + ".spirit_medic_ladle.used",
					INSTANT_SPIRIT, RaAttachments.getSpirit(player)), true);
			level.playSound(null, player.getX(), player.getY(), player.getZ(),
					SoundEvents.BREWING_STAND_BREW, SoundSource.PLAYERS, 0.6F, 1.1F);
		}

		player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.translatable("item." + MODID + ".spirit_medic_ladle.tooltip",
				INSTANT_SPIRIT, COOLDOWN_TICKS / 20).withStyle(ChatFormatting.GRAY));
		super.appendHoverText(stack, context, tooltip, flag);
	}
}
