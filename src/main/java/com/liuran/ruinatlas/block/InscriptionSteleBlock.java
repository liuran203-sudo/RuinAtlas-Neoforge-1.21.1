package com.liuran.ruinatlas.block;

import com.liuran.ruinatlas.init.RaItems;
import com.liuran.ruinatlas.item.SteleRubbingItem;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 可触发叙事碑：右键读碑文；手持空碑文拓本右键可把碑文拓下来带走
 * <p>拓本把碑文键存进自定义数据，之后在别处也能翻阅同一段文字。
 */
public class InscriptionSteleBlock extends RotatableRuinBlock {
	private final String textKey;

	public InscriptionSteleBlock(Properties properties, VoxelShape shape, String textKey) {
		super(properties, shape);
		this.textKey = textKey;
	}

	public String getTextKey() {
		return this.textKey;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (!level.isClientSide) {
			player.displayClientMessage(Component.translatable(this.textKey), false);
			level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 0.5F, 1.1F);
		}

		return InteractionResult.sidedSuccess(level.isClientSide);
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
	                                          Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (!stack.is(RaItems.STELE_RUBBING.get())) {
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}

		if (level.isClientSide) {
			return ItemInteractionResult.CONSUME;
		}

		CompoundTag tag = new CompoundTag();
		tag.putString(SteleRubbingItem.TEXT_KEY, this.textKey);
		stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
		player.displayClientMessage(Component.translatable("block." + MODID + ".rubbing_stele.rubbed"), true);
		level.playSound(null, pos, SoundEvents.BOOK_PAGE_TURN, SoundSource.BLOCKS, 0.8F, 1.0F);

		return ItemInteractionResult.SUCCESS;
	}
}
