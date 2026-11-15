package com.liuran.ruinatlas.blockentity;

import com.liuran.ruinatlas.init.RaBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 灵息引石方块实体：内部存一份引石灵息，供奉时被墟龛抽走
 */
public class SpiritConduitBlockEntity extends BlockEntity {
	public static final int MAX_CHARGE = 100;

	private int charge;

	public SpiritConduitBlockEntity(BlockPos pos, BlockState state) {
		super(RaBlockEntities.SPIRIT_CONDUIT.get(), pos, state);
	}

	public int getCharge() {
		return this.charge;
	}

	public void setCharge(int charge) {
		this.charge = Math.max(0, Math.min(MAX_CHARGE, charge));
		this.setChanged();
	}

	/** 抽取若干点引石灵息，返回实际抽到的数量 */
	public int drain(int amount) {
		int taken = Math.min(amount, this.charge);
		if (taken > 0) {
			setCharge(this.charge - taken);
		}

		return taken;
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		tag.putInt("charge", this.charge);
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		this.charge = tag.getInt("charge");
	}
}
