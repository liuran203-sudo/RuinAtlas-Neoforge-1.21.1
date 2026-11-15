package com.liuran.ruinatlas.item;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 引符：可复用，带冷却，承载古人遗留的秘术
 */
public class ReusableTalismanItem extends Item {
	private static final DustParticleOptions TALISMAN_SPARK = new DustParticleOptions(new Vector3f(0.9F, 0.55F, 0.3F), 1.0F);

	public ReusableTalismanItem(Properties properties) {
		super(properties.stacksTo(1).fireResistant());
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!level.isClientSide) {
			if (player.getCooldowns().isOnCooldown(this)) {
				return InteractionResultHolder.fail(stack);
			}

			player.getCooldowns().addCooldown(this, 120);
			player.displayClientMessage(Component.translatable("item." + MODID + ".reusable_talisman.drawn"), true);
			level.playSound(null, player.blockPosition(), SoundEvents.NOTE_BLOCK_GUITAR.value(), SoundSource.PLAYERS, 0.8F, 1.2F);
		}

		if (level.isClientSide) {
			RandomSource random = level.getRandom();

			for (int i = 0; i < 10; i++) {
				level.addParticle(
						TALISMAN_SPARK,
						player.getX() + (random.nextFloat() - 0.5F) * 0.5,
						player.getEyeY() - 0.5 + random.nextFloat() * 0.7,
						player.getZ() + (random.nextFloat() - 0.5F) * 0.5,
						0.0,
						0.03,
						0.0
				);
			}
		}

		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
	}
}
