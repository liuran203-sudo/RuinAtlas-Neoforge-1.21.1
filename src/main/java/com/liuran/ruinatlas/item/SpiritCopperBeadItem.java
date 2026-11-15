package com.liuran.ruinatlas.item;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import com.liuran.ruinatlas.init.RaAttachments;
import com.liuran.ruinatlas.technique.RaCasting;
import com.liuran.ruinatlas.technique.RaTechniqueInfo;
import com.liuran.ruinatlas.technique.RaTechniqueRegistry;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 承契铜珠：助灵法器，不再是施法必需品
 * <p>契痕已经烙在脉络里，按施展键即可引动；手持此珠时灵息与冷却各减两成。
 * 潜行右键换一式，普通右键仍可借珠引动一次。
 */
public class SpiritCopperBeadItem extends Item {
	public SpiritCopperBeadItem(Properties properties) {
		super(properties.stacksTo(1).fireResistant());
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (level.isClientSide || !(player instanceof ServerPlayer serverPlayer)) {
			return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
		}

		if (RaAttachments.getLearned(player).isEmpty()) {
			player.displayClientMessage(Component.translatable("item." + MODID + ".spirit_copper_bead.empty"), true);
			return InteractionResultHolder.fail(stack);
		}

		if (player.isShiftKeyDown()) {
			String next = RaCasting.cycle(serverPlayer);
			RaTechniqueInfo info = RaTechniqueRegistry.byId(next);

			if (info == null) {
				return InteractionResultHolder.fail(stack);
			}

			player.displayClientMessage(Component.translatable("item." + MODID + ".spirit_copper_bead.switched",
					Component.translatable(info.nameKey())), true);
			return InteractionResultHolder.sidedSuccess(stack, false);
		}

		RaCasting.Result result = RaCasting.castAndNotify(serverPlayer, "");
		return result == RaCasting.Result.SUCCESS
				? InteractionResultHolder.sidedSuccess(stack, false)
				: InteractionResultHolder.fail(stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.translatable("item." + MODID + ".spirit_copper_bead.tooltip").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("item." + MODID + ".spirit_copper_bead.tooltip_detail").withStyle(ChatFormatting.DARK_GRAY));
		super.appendHoverText(stack, context, tooltip, flag);
	}
}
