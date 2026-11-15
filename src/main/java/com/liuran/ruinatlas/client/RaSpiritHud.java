package com.liuran.ruinatlas.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import com.liuran.ruinatlas.init.RaAttachments;
import com.liuran.ruinatlas.init.RaItems;
import com.liuran.ruinatlas.technique.RaTechniqueInfo;
import com.liuran.ruinatlas.technique.RaTechniqueRegistry;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 左上角一小条：灵息余量 + 当前契法 + 契痕层数 + 状态图标
 * <p>状态图标分别是灵潮、静封、墟尘、敛息、护息、踏岳，以及手里铜珠的灵相。
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
				Component.translatable("hud." + MODID + ".spirit", spirit, RaAttachments.MAX_SPIRIT).getString(),
				x, y, SPIRIT_COLOR, true);

		if (info != null) {
			int rank = RaAttachments.getRank(minecraft.player, info.id());
			String line = Component.translatable("hud." + MODID + ".technique",
					Component.translatable(info.nameKey()).getString(),
					rankDots(rank)).getString();

			graphics.drawString(minecraft.font, line, x, y + 11,
					rank >= 3 ? ChatFormatting.GOLD.getColor() : SPELL_COLOR, true);
		}

		long now = minecraft.player.level().getGameTime();
		int statusX = x;
		int statusY = y + 22;

		for (Status status : statuses(minecraft, now)) {
			String text = "\u25c6 " + Component.translatable(status.key()).getString()
					+ (status.seconds() > 0 ? " " + status.seconds() + "s" : "");
			graphics.drawString(minecraft.font, text, statusX, statusY, status.color(), true);
			statusX += minecraft.font.width(text) + 10;
		}
	}

	private static List<Status> statuses(Minecraft minecraft, long now) {
		List<Status> list = new ArrayList<>();

		if (minecraft.player == null) {
			return list;
		}

		add(list, "hud." + MODID + ".status.tide", RaAttachments.getSpiritTideUntil(minecraft.player) - now, 0x7FD8C9);
		add(list, "hud." + MODID + ".status.seal", RaAttachments.getDomainRadius(minecraft.player) > 0
				? RaAttachments.getDomainUntil(minecraft.player) - now : 0, 0xB7A6FF);
		add(list, "hud." + MODID + ".status.dust", RaAttachments.getDustBoostUntil(minecraft.player) - now, 0xD8CBA0);
		add(list, "hud." + MODID + ".status.silence", RaAttachments.getSilencedUntil(minecraft.player) - now, 0x8FA79C);
		add(list, "hud." + MODID + ".status.ward", RaAttachments.getWardUntil(minecraft.player) - now, 0x9FE0D8);
		add(list, "hud." + MODID + ".status.fall", RaAttachments.getFallGuardUntil(minecraft.player) - now, 0xC0A36A);

		boolean holdsBead = minecraft.player.getMainHandItem().is(RaItems.SPIRIT_COPPER_BEAD.get())
				|| minecraft.player.getOffhandItem().is(RaItems.SPIRIT_COPPER_BEAD.get());

		if (holdsBead) {
			list.add(new Status("hud." + MODID + ".status.bead."
					+ RaAttachments.getBeadMode(minecraft.player), 0, 0xE7C87F));
		}

		return list;
	}

	private static void add(List<Status> list, String key, long ticks, int color) {
		if (ticks > 0) {
			list.add(new Status(key, (int) (ticks / 20L), color));
		}
	}

	private static String rankDots(int rank) {
		StringBuilder builder = new StringBuilder();

		for (int i = 1; i <= 3; i++) {
			builder.append(i <= rank ? "\u25c6" : "\u25c7");
		}

		return builder.toString();
	}

	private record Status(String key, int seconds, int color) {
	}
}
