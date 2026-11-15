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
 * 墟尘囊：吸一口墟土灵尘，短时间提高灵息自然恢复，不叠加
 */
public class XuDustPouchItem extends Item {
	private static final int DURATION_TICKS = 1200;

	public XuDustPouchItem(Properties properties) {
		super(properties.stacksTo(16));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (!level.isClientSide) {
			long now = level.getGameTime();

			if (RaAttachments.hasDustBoost(player, now)) {
				player.displayClientMessage(Component.translatable("item." + MODID + ".xu_dust_pouch.already"), true);
				return InteractionResultHolder.fail(stack);
			}

			RaAttachments.setDustBoostUntil(player, now + DURATION_TICKS);
			stack.shrink(1);
			player.displayClientMessage(Component.translatable("item." + MODID + ".xu_dust_pouch.used"), true);
			level.playSound(null, player.getX(), player.getY(), player.getZ(),
					SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 0.7F, 1.4F);
		}

		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.translatable("item." + MODID + ".xu_dust_pouch.tooltip")
				.withStyle(ChatFormatting.GRAY));
		super.appendHoverText(stack, context, tooltip, flag);
	}
}
