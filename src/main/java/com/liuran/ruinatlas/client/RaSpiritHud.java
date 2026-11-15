package com.liuran.ruinatlas.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import com.liuran.ruinatlas.init.RaAttachments;
import com.liuran.ruinatlas.technique.RaTechniqueInfo;
import com.liuran.ruinatlas.technique.RaTechniqueRegistry;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 左上角一小条：灵息余量 + 当前契法 + 契痕层数（占位样式，正式 UI 之后再换）
 */
public final class RaSpiritHud {
	private static final int SPIRIT_COLOR = 0x9FE0D8;
	private static final int SPELL_COLOR = 0xD8CBA0;

	private RaSpiritHud() {
	}

	public static void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
		Minecraft minecraft = Minecraft.getInstance();

		if (minecraft.player == null || minecraft.options.hideGui || minecraft.screen instanceof RaTechniqueScreen) {
			return;
		}

		int spirit = RaAttachments.getSpirit(minecraft.player);
		String selected = RaAttachments.getSelected(minecraft.player);
		RaTechniqueInfo info = RaTechniqueRegistry.byId(selected);

		if (info == null && RaAttachments.getLearned(minecraft.player).isEmpty()) {
			return;
		}

		int x = 8;
		int y = graphics.guiHeight() - 62;

		graphics.drawString(minecraft.font,
				Component.translatable("hud." + MODID + ".spirit", spirit).getString(),
				x, y, SPIRIT_COLOR, true);

		if (info != null) {
			int rank = RaAttachments.getRank(minecraft.player, info.id());
			String line = Component.translatable("hud." + MODID + ".technique",
					Component.translatable(info.nameKey()).getString(),
					rankDots(rank)).getString();

			graphics.drawString(minecraft.font, line, x, y + 11,
					rank >= 3 ? ChatFormatting.GOLD.getColor() : SPELL_COLOR, true);
		}
	}

	private static String rankDots(int rank) {
		StringBuilder builder = new StringBuilder();

		for (int i = 1; i <= 3; i++) {
			builder.append(i <= rank ? "\u25c6" : "\u25c7");
		}

		return builder.toString();
	}
}
