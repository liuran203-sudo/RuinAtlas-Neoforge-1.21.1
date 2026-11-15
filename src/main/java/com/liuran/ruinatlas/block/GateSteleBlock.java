package com.liuran.ruinatlas.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.phys.BlockHitResult;

import com.liuran.ruinatlas.worldgen.RuinDimensions;

import java.util.Set;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 苍垠界碑：蹲下右键在苍垠古墟界与主世界之间往返，普通右键只念碑文
 */
public class GateSteleBlock extends RuinShapeBlock {
	private static final Component LORE = Component.translatable("block." + MODID + ".cangyin_gate_stele.text");
	private static final Component ENTERED = Component.translatable("block." + MODID + ".cangyin_gate_stele.entered");
	private static final Component RETURNED = Component.translatable("block." + MODID + ".cangyin_gate_stele.returned");

	public GateSteleBlock(Properties properties) {
		super(properties, Block.box(2.0, 0.0, 5.0, 14.0, 15.0, 11.0));
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (level.isClientSide) {
			return InteractionResult.SUCCESS;
		}

		handleUse(level, pos, player);
		return InteractionResult.CONSUME;
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
			Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (level.isClientSide) {
			return ItemInteractionResult.SUCCESS;
		}

		handleUse(level, pos, player);
		return ItemInteractionResult.CONSUME;
	}

	private void handleUse(Level level, BlockPos pos, Player player) {
		if (player instanceof ServerPlayer serverPlayer) {
			if (serverPlayer.isShiftKeyDown()) {
				this.crossDimension(serverPlayer);
				level.playSound(null, pos, SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.BLOCKS, 1.0F, 0.7F);
			} else {
				serverPlayer.displayClientMessage(LORE, false);
				level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 0.5F, 0.9F);
			}
		}
	}

	private void crossDimension(ServerPlayer player) {
		MinecraftServer server = player.server;
		if (player.level() == RuinDimensions.cangyinLevel(server)) {
			ServerLevel overworld = server.overworld();
			BlockPos spawn = overworld.getSharedSpawnPos();
			int y = surfaceY(overworld, spawn.getX(), spawn.getZ());
			player.teleportTo(overworld, spawn.getX() + 0.5, y, spawn.getZ() + 0.5, Set.of(), player.getYRot(), player.getXRot());
			player.displayClientMessage(RETURNED, false);
		} else {
			ServerLevel ruins = RuinDimensions.cangyinLevel(server);
			if (ruins == null) {
				player.displayClientMessage(LORE, false);
				return;
			}

			int cx = player.getBlockX();
			int cz = player.getBlockZ();
			BlockPos landing = findLanding(ruins, cx, cz);
			int y = surfaceY(ruins, landing.getX(), landing.getZ());
			player.teleportTo(ruins, landing.getX() + 0.5, y, landing.getZ() + 0.5, Set.of(), player.getYRot(), player.getXRot());
			player.displayClientMessage(ENTERED, false);
		}
	}

	private static BlockPos findLanding(ServerLevel level, int x, int z) {
		for (int r = 0; r < 20; r++) {
			for (int dx = -r; dx <= r; dx++) {
				for (int dz = -r; dz <= r; dz++) {
					if (Math.max(Math.abs(dx), Math.abs(dz)) == r) {
						int px = x + dx;
						int pz = z + dz;
						if (surfaceY(level, px, pz) > level.getSeaLevel() + 1) {
							return new BlockPos(px, 0, pz);
						}
					}
				}
			}
		}

		return new BlockPos(x, 0, z);
	}

	private static int surfaceY(ServerLevelAccessor level, int x, int z) {
		ChunkAccess chunk = level.getChunkSource().getChunk(x >> 4, z >> 4, ChunkStatus.FEATURES, true);
		if (chunk == null) {
			return level.getMinBuildHeight();
		}
		int y = chunk.getHeight(Types.OCEAN_FLOOR, x & 15, z & 15);
		return Math.max(y + 1, level.getMinBuildHeight());
	}
}
