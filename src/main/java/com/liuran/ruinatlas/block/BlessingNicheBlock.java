package com.liuran.ruinatlas.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.joml.Vector3f;

import com.liuran.ruinatlas.init.RaAttachments;
import com.liuran.ruinatlas.init.RaItems;
import com.liuran.ruinatlas.technique.RaTechniqueInfo;
import com.liuran.ruinatlas.technique.RaTechniqueRegistry;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 墟龛：献上藏品，换一份残存的护佑
 * <p>三类供奉，都不给原版状态效果：
 * <ul>
 *     <li>卷轴 → 龛中残念替你解卷，免灵息直接契悟；</li>
 *     <li>铜珠 → 残念附珠，记一次不耗灵息的施术（最多攒三次）；</li>
 *     <li>旧物（拓本、铜佩、玉饰、玉碎、铜铢、原石、残简、骨片）→ 折成灵息。</li>
 * </ul>
 * 手持物品走 {@link #useItemOn}，空手才走 {@link #useWithoutItem}——原先只覆写后者，
 * 并且其中还读主手物品，所以手里拿着东西时墟龛永远不响应。
 */
public class BlessingNicheBlock extends Block {
	private static final DustParticleOptions BLESSING_SMOKE = new DustParticleOptions(new Vector3f(0.4F, 0.85F, 0.75F), 1.2F);
	private static final int MAX_FREE_CASTS = 3;

	public BlessingNicheBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
			Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (stack.isEmpty()) {
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}

		if (!level.isClientSide) {
			offer(level, pos, player, stack);
		} else {
			blessingSmoke(level, pos);
		}

		return ItemInteractionResult.sidedSuccess(level.isClientSide);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (!level.isClientSide) {
			tell(player, Component.translatable("block." + MODID + ".xun_niche.hint"));
		}

		return InteractionResult.sidedSuccess(level.isClientSide);
	}

	private static void offer(Level level, BlockPos pos, Player player, ItemStack held) {
		RaTechniqueInfo scroll = RaTechniqueRegistry.byScroll(held);

		if (scroll != null) {
			if (RaAttachments.hasLearned(player, scroll.id()) && RaAttachments.getRank(player, scroll.id()) >= 3) {
				tell(player, Component.translatable("block." + MODID + ".xun_niche.already_learned"));
				return;
			}

			boolean fresh = !RaAttachments.hasLearned(player, scroll.id());
			RaAttachments.learn(player, scroll.id());
			RaAttachments.setRank(player, scroll.id(), 3);
			held.shrink(1);
			tell(player, Component.translatable(fresh
					? "block." + MODID + ".xun_niche.learned_scroll"
					: "block." + MODID + ".xun_niche.rank_filled",
					Component.translatable(scroll.nameKey())));
			level.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 0.8F, 1.3F);
			return;
		}

		if (held.is(RaItems.SPIRIT_COPPER_BEAD.get())) {
			int free = RaAttachments.getFreeCasts(player);

			if (free >= MAX_FREE_CASTS) {
				tell(player, Component.translatable("block." + MODID + ".xun_niche.bead_full", MAX_FREE_CASTS));
				return;
			}

			RaAttachments.setFreeCasts(player, free + 1);
			tell(player, Component.translatable("block." + MODID + ".xun_niche.bead_blessed", free + 1));
			level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 0.8F, 1.6F);
			return;
		}

		int gain = relicSpirit(held);

		if (gain > 0) {
			held.shrink(1);
			RaAttachments.addSpirit(player, gain);
			tell(player, Component.translatable("block." + MODID + ".xun_niche.blessing",
					gain, RaAttachments.getSpirit(player)));
			level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 0.8F, 1.4F);
			return;
		}

		tell(player, Component.translatable("block." + MODID + ".xun_niche.hint"));
	}

	/**
	 * 旧物可折的灵息：拓本、铜佩、玉饰这类“有人经手过”的东西最值钱
	 */
	private static int relicSpirit(ItemStack stack) {
		Item item = stack.getItem();

		if (item == RaItems.SACRIFICIAL_JADE.get()) {
			return 30;
		}

		if (item == RaItems.ANCIENT_COPPER_PENDANT.get()) {
			return 25;
		}

		if (item == RaItems.STELE_RUBBING.get()) {
			return 20;
		}

		if (item == RaItems.CLOUD_STEP_RUBBING.get()
				|| item == RaItems.WIND_BLADE_RUBBING.get()
				|| item == RaItems.WARD_EVIL_RUBBING.get()) {
			return 15;
		}

		if (item == RaItems.RAW_SPIRIT_STONE.get() || item == RaItems.TATTERED_MESSAGE.get()) {
			return 10;
		}

		if (item == RaItems.PALE_JADE_SHARD.get()) {
			return 8;
		}

		if (item == RaItems.OLD_COPPER_TOKEN.get()) {
			return 5;
		}

		if (item == RaItems.BONE_SHARD.get()) {
			return 4;
		}

		return 0;
	}

	private static void tell(Player player, Component message) {
		player.displayClientMessage(message, true);
	}

	private static void blessingSmoke(Level level, BlockPos pos) {
		RandomSource random = level.getRandom();

		for (int i = 0; i < 6; i++) {
			level.addParticle(
					BLESSING_SMOKE,
					pos.getX() + 0.2 + random.nextFloat() * 0.6,
					pos.getY() + 0.4 + random.nextFloat() * 0.4,
					pos.getZ() + 0.2 + random.nextFloat() * 0.6,
					0.0,
					0.03,
					0.0
			);
		}
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (random.nextFloat() < 0.05F) {
			level.addParticle(
					BLESSING_SMOKE,
					pos.getX() + 0.3 + random.nextFloat() * 0.4,
					pos.getY() + 0.5 + random.nextFloat() * 0.3,
					pos.getZ() + 0.55 + random.nextFloat() * 0.2,
					0.0,
					0.02,
					0.0
			);
		}
	}
}
