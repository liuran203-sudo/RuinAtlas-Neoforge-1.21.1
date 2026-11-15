package com.liuran.ruinatlas.item;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

import com.liuran.ruinatlas.init.RaAttachments;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 捏碎类借灵道具：右键捏碎，消耗自身，把封存在遗物里的一点灵息吸进体内
 * <p>灵息不是状态效果，只是能支撑"契悟"与"施术"的余量，用完即散。
 */
public class CrushCharmItem extends Item {
	private static final DustParticleOptions SPIRIT_DUST = new DustParticleOptions(new Vector3f(0.45F, 0.85F, 0.8F), 1.1F);
	private final String messageKey;
	private final int spiritGain;

	public CrushCharmItem(Properties properties, String messageKey, int spiritGain) {
		super(properties.stacksTo(16));
		this.messageKey = messageKey;
		this.spiritGain = spiritGain;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (!level.isClientSide) {
			RaAttachments.addSpirit(player, this.spiritGain);
			player.displayClientMessage(Component.translatable(this.messageKey, this.spiritGain,
					RaAttachments.getSpirit(player)), true);
			stack.shrink(1);
		}

		level.playSound(player, player.getX(), player.getY(), player.getZ(),
				SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.PLAYERS, 0.7F, 1.3F);

		if (level.isClientSide) {
			RandomSource random = level.getRandom();

			for (int i = 0; i < 14; i++) {
				level.addParticle(
						SPIRIT_DUST,
						player.getX() + (random.nextFloat() - 0.5F) * 0.6,
						player.getEyeY() - 0.4 + random.nextFloat() * 0.6,
						player.getZ() + (random.nextFloat() - 0.5F) * 0.6,
						(random.nextFloat() - 0.5F) * 0.04,
						0.02,
						(random.nextFloat() - 0.5F) * 0.04
				);
			}
		}

		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.translatable("item." + MODID + ".crush_charm.tooltip", this.spiritGain)
				.withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("item." + MODID + ".crush_charm.tooltip_detail")
				.withStyle(ChatFormatting.DARK_GRAY));
		super.appendHoverText(stack, context, tooltip, flag);
	}
}
