package com.liuran.ruinatlas.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import com.liuran.ruinatlas.init.RaItems;

/**
 * 墟影：苍垠原生低威胁游荡灵体，由逝去墟地的灵韵凝成
 * <p>不主动追杀玩家，只在近处干扰灵息恢复；击杀有小概率掉落墟尘囊或拓本。
 */
public class XuShadowEntity extends PathfinderMob {
	public XuShadowEntity(EntityType<? extends XuShadowEntity> type, Level level) {
		super(type, level);
		this.xpReward = 6;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 0.75D));
		this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return createMobAttributes()
				.add(Attributes.MAX_HEALTH, 18.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.22D)
				.add(Attributes.FOLLOW_RANGE, 16.0D);
	}

	/** 附近有墟影时灵息恢复会被干扰 */
	public static boolean interferes(Player player, double radius) {
		return !player.level().getEntitiesOfClass(XuShadowEntity.class,
				player.getBoundingBox().inflate(radius), entity -> entity.isAlive()).isEmpty();
	}

	@Override
	protected void dropCustomDeathLoot(ServerLevel level, net.minecraft.world.damagesource.DamageSource source, boolean recentlyHit) {
		super.dropCustomDeathLoot(level, source, recentlyHit);
		RandomSource random = this.getRandom();

		if (random.nextFloat() < 0.18F) {
			this.spawnAtLocation(new ItemStack(RaItems.XU_DUST_POUCH.get()));
		}

		if (random.nextFloat() < 0.12F) {
			this.spawnAtLocation(new ItemStack(RaItems.STELE_RUBBING.get()));
		}

		if (random.nextFloat() < 0.06F) {
			this.spawnAtLocation(new ItemStack(RaItems.CONTRACT_FRAGMENT.get()));
		}
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return distanceToClosestPlayer > 96.0D;
	}
}
