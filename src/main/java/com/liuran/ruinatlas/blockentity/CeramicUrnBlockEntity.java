package com.liuran.ruinatlas.blockentity;

import com.liuran.ruinatlas.init.RaBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.ticks.ContainerSingleItem;

/**
 * 陶瓮的单格仓储：一格、一栈，行为对齐原版饰纹陶罐（Decorated Pot）
 * <p>存入：手持物品右键，一次放一件，同种物品可以叠到该物品自己的最大堆叠；
 * 取出：原版饰纹陶罐只能靠破坏（内容物掉出）或漏斗抽取，这里保持一致；
 * 比较器按容器内容输出红石信号。
 */
public class CeramicUrnBlockEntity extends BlockEntity implements ContainerSingleItem.BlockContainerSingleItem {
	private static final String TAG_ITEM = "item";

	private ItemStack item = ItemStack.EMPTY;

	public CeramicUrnBlockEntity(BlockPos pos, BlockState state) {
		super(RaBlockEntities.CERAMIC_URN.get(), pos, state);
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		if (!this.item.isEmpty()) {
			tag.put(TAG_ITEM, this.item.save(registries));
		}
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		this.item = tag.contains(TAG_ITEM) ? ItemStack.parseOptional(registries, tag.getCompound(TAG_ITEM)) : ItemStack.EMPTY;
	}

	@Override
	public ItemStack getTheItem() {
		return this.item;
	}

	@Override
	public void setTheItem(ItemStack stack) {
		this.item = stack;
		this.setChanged();
	}

	@Override
	public BlockEntity getContainerBlockEntity() {
		return this;
	}
}
