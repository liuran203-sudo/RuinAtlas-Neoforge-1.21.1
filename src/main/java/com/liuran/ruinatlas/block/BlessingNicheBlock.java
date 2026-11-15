package com.liuran.ruinatlas.block;

import java.util.List;

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

import com.liuran.ruinatlas.blockentity.SpiritConduitBlockEntity;
import com.liuran.ruinatlas.init.RaAttachments;
import com.liuran.ruinatlas.init.RaBlocks;
import com.liuran.ruinatlas.init.RaItems;
import com.liuran.ruinatlas.item.ContractFragmentItem;
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
	/** 短时间连续供奉的收益系数：越急越薄 */
	private static final double[] OFFER_FACTORS = {1.0D, 0.8D, 0.6D, 0.45D, 0.3D};

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
			String locked = RaAttachments.getLockedTechnique(player);

			if (!locked.isEmpty()) {
				tell(player, Component.translatable("block." + MODID + ".xun_niche.hint_locked",
						Component.translatable("technique." + MODID + "." + locked)));
			} else {
				tell(player, Component.translatable("block." + MODID + ".xun_niche.hint"));
			}
		}

		return InteractionResult.sidedSuccess(level.isClientSide);
	}

	private static void offer(Level level, BlockPos pos, Player player, ItemStack held) {
		RaTechniqueInfo scroll = RaTechniqueRegistry.byScroll(held);

		if (scroll != null) {
			offerScroll(level, pos, player, held, scroll);
			return;
		}

		if (held.is(RaItems.SPIRIT_COPPER_BEAD.get())) {
			// 潜行供奉铜珠：切换铜珠的两种灵相
			if (player.isShiftKeyDown()) {
				String mode = RaAttachments.toggleBeadMode(player);
				tell(player, Component.translatable("block." + MODID + ".xun_niche.bead_mode",
						Component.translatable("block." + MODID + ".xun_niche.mode." + mode)));
				level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 0.8F, 1.2F);
				return;
			}

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

		if (held.is(RaItems.WARD_JADE_SHARD.get())) {
			offerJade(level, pos, player, held);
			return;
		}

		if (held.is(RaItems.CONTRACT_FRAGMENT.get())) {
			offerFragment(level, pos, player, held);
			return;
		}

		int base = relicSpirit(held);

		if (base > 0) {
			long now = level.getGameTime();
			int streak = RaAttachments.getOfferLast(player) + 1200L > now
					? RaAttachments.getOfferStreak(player) + 1 : 1;
			double factor = OFFER_FACTORS[Math.min(streak, OFFER_FACTORS.length) - 1];
			int gain = Math.max(1, (int) Math.round(base * factor)) + conduitBonus(level, pos);

			// 灵潮漫墟时，龛前的供奉收益再高一成
			if (RaAttachments.isSpiritTide(player, now)) {
				gain += Math.max(1, gain / 5);
			}

			held.shrink(1);
			RaAttachments.addSpirit(player, gain);
			RaAttachments.setOfferStreak(player, streak);
			RaAttachments.setOfferLast(player, now);
			tell(player, Component.translatable("block." + MODID + ".xun_niche.blessing",
					gain, RaAttachments.getSpirit(player)));
			level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 0.8F, 1.4F);
			return;
		}

		tell(player, Component.translatable("block." + MODID + ".xun_niche.hint"));
	}

	/**
	 * 卷轴供奉：直接契满那一道；若锁了法，契痕只落在锁定的一门；
	 * 已经契满的卷轴改折成灵息，不再是一张没用的纸。
	 */
	private static void offerScroll(Level level, BlockPos pos, Player player, ItemStack held, RaTechniqueInfo scroll) {
		String locked = RaAttachments.getLockedTechnique(player);
		RaTechniqueInfo lockInfo = locked.isEmpty() ? null : RaTechniqueRegistry.byId(locked);
		boolean hasLock = lockInfo != null && RaAttachments.hasLearned(player, lockInfo.id());

		if (hasLock) {
			int lockRank = RaAttachments.getRank(player, lockInfo.id());

			if (lockRank < 3) {
				RaAttachments.setRank(player, lockInfo.id(), 3);
				held.shrink(1);
				tell(player, Component.translatable("block." + MODID + ".xun_niche.locked_filled",
						Component.translatable(lockInfo.nameKey())));
				level.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 0.8F, 1.3F);
				return;
			}

			// 锁定的契法已经契满，别的契痕不再进，卷轴折灵息
			held.shrink(1);
			RaAttachments.addSpirit(player, 30);
			tell(player, Component.translatable("block." + MODID + ".xun_niche.converted", 30,
					RaAttachments.getSpirit(player)));
			level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 0.8F, 1.1F);
			return;
		}

		boolean full = RaAttachments.hasLearned(player, scroll.id())
				&& RaAttachments.getRank(player, scroll.id()) >= 3;

		if (full) {
			held.shrink(1);
			RaAttachments.addSpirit(player, 30);
			tell(player, Component.translatable("block." + MODID + ".xun_niche.converted", 30,
					RaAttachments.getSpirit(player)));
			level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 0.8F, 1.1F);
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
	}

	/** 守墟碎玉：锁住当前所指契法；潜行供奉则解除锁定 */
	private static void offerJade(Level level, BlockPos pos, Player player, ItemStack held) {
		if (player.isShiftKeyDown()) {
			RaAttachments.setLockedTechnique(player, "");
			tell(player, Component.translatable("block." + MODID + ".xun_niche.locked_clear"));
			level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 0.7F, 0.9F);
			return;
		}

		String selected = RaAttachments.getSelected(player);

		if (selected.isEmpty() || !RaAttachments.hasLearned(player, net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(MODID, selected))) {
			tell(player, Component.translatable("block." + MODID + ".xun_niche.locked_need_selection"));
			return;
		}

		RaAttachments.setLockedTechnique(player, selected);
		held.shrink(1);
		tell(player, Component.translatable("block." + MODID + ".xun_niche.locked_set",
				Component.translatable("technique." + MODID + "." + selected)));
		level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 0.9F, 1.5F);
	}

	/** 契卷残页：同一种契法凑够三张，在龛前合成完整契卷 */
	private static void offerFragment(Level level, BlockPos pos, Player player, ItemStack held) {
		String path = ContractFragmentItem.techniqueOf(held);
		RaTechniqueInfo info = path.isEmpty() ? null : RaTechniqueRegistry.byId(path);

		if (info == null) {
			tell(player, Component.translatable("block." + MODID + ".xun_niche.hint"));
			return;
		}

		int have = countFragments(player, path);

		if (have < 3) {
			tell(player, Component.translatable("block." + MODID + ".xun_niche.fragment_more", 3 - have));
			return;
		}

		Item scrollItem = RaTechniqueRegistry.scrollFor(path);

		if (scrollItem == null) {
			tell(player, Component.translatable("block." + MODID + ".xun_niche.hint"));
			return;
		}

		consumeFragments(player, path, 3);
		player.getInventory().placeItemBackInInventory(new ItemStack(scrollItem));
		tell(player, Component.translatable("block." + MODID + ".xun_niche.fragment_combined",
				Component.translatable(info.nameKey())));
		level.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 0.8F, 1.5F);
	}

	private static int countFragments(Player player, String path) {
		int count = 0;

		for (ItemStack stack : player.getInventory().items) {
			if (stack.is(RaItems.CONTRACT_FRAGMENT.get()) && path.equals(ContractFragmentItem.techniqueOf(stack))) {
				count += stack.getCount();
			}
		}

		for (ItemStack stack : player.getInventory().offhand) {
			if (stack.is(RaItems.CONTRACT_FRAGMENT.get()) && path.equals(ContractFragmentItem.techniqueOf(stack))) {
				count += stack.getCount();
			}
		}

		return count;
	}

	private static void consumeFragments(Player player, String path, int amount) {
		int remaining = amount;
		List<ItemStack> stacks = new java.util.ArrayList<>(player.getInventory().items);
		stacks.addAll(player.getInventory().offhand);

		for (ItemStack stack : stacks) {
			if (remaining <= 0) {
				break;
			}

			if (stack.is(RaItems.CONTRACT_FRAGMENT.get()) && path.equals(ContractFragmentItem.techniqueOf(stack))) {
				int take = Math.min(remaining, stack.getCount());
				stack.shrink(take);
				remaining -= take;
			}
		}
	}

	/** 灵息引石加成：每次供奉最多吃三块引石的蓄灵，各抽两点 */
	private static int conduitBonus(Level level, BlockPos pos) {
		int bonus = 0;
		int used = 0;

		for (BlockPos p : BlockPos.betweenClosed(pos.offset(-12, -6, -12), pos.offset(12, 6, 12))) {
			if (used >= 3) {
				break;
			}

			if (p.distSqr(pos) > 144.0D) {
				continue;
			}

			if (!level.getBlockState(p).is(RaBlocks.SPIRIT_CONDUIT.get())) {
				continue;
			}

			if (level.getBlockEntity(p) instanceof SpiritConduitBlockEntity conduit && conduit.drain(2) > 0) {
				bonus += 2;
				used++;
			}
		}

		return bonus;
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

		if (item == RaItems.XU_DUST_POUCH.get()) {
			return 6;
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
