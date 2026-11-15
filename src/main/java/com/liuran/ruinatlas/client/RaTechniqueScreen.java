package com.liuran.ruinatlas.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import net.neoforged.neoforge.network.PacketDistributor;

import com.liuran.ruinatlas.init.RaAttachments;
import com.liuran.ruinatlas.network.SelectTechniquePayload;
import com.liuran.ruinatlas.technique.RaTechniqueInfo;
import com.liuran.ruinatlas.technique.RaTechniqueRegistry;

import static com.liuran.ruinatlas.RuinAtlas.MODID;

/**
 * 灵契轮盘（占位版）
 * <p>正式轮盘要画成环形符阵，这里先做成一块半透明面板 + 竖排条目：
 * 列出已契悟的契法、契痕层数、灵息价，点一下就换当前所指。
 * 数据来自同步到客户端的附件，所以完全不用额外发包取数据。
 */
public final class RaTechniqueScreen extends Screen {
	private static final int ROW_HEIGHT = 22;
	private static final int PANEL_WIDTH = 240;

	private final List<RaTechniqueInfo> entries = new ArrayList<>();
	private int panelLeft;
	private int panelTop;

	public RaTechniqueScreen() {
		super(Component.translatable("screen." + MODID + ".wheel"));
	}

	@Override
	protected void init() {
		this.entries.clear();

		if (this.minecraft != null && this.minecraft.player != null) {
			for (ResourceLocation id : RaAttachments.getLearned(this.minecraft.player)) {
				RaTechniqueInfo info = RaTechniqueRegistry.byId(id.getPath());

				if (info != null) {
					this.entries.add(info);
				}
			}
		}

		this.panelLeft = (this.width - PANEL_WIDTH) / 2;

		// 整体略偏上：条目多时按内容居中再上提，条目少时不比原来的位置更低
		int contentHeight = 92 + this.entries.size() * ROW_HEIGHT;
		this.panelTop = Math.max(20, Math.min(this.height / 2 - 90, this.height / 2 - contentHeight / 2 - 14));
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		graphics.fill(0, 0, this.width, this.height, 0xA0121218);

		int panelHeight = 92 + this.entries.size() * ROW_HEIGHT;
		graphics.fill(this.panelLeft - 6, this.panelTop - 26, this.panelLeft + PANEL_WIDTH + 6,
				this.panelTop + panelHeight, 0xE0202A2E);
		graphics.fill(this.panelLeft - 6, this.panelTop - 26, this.panelLeft + PANEL_WIDTH + 6,
				this.panelTop - 24, 0x9FE0D8);

		graphics.drawCenteredString(this.font, this.title, this.width / 2, this.panelTop - 18, 0x9FE0D8);

		String spirit = Component.translatable("hud." + MODID + ".spirit",
				this.minecraft != null && this.minecraft.player != null
						? RaAttachments.getSpirit(this.minecraft.player) : 0).getString();
		graphics.drawCenteredString(this.font, spirit, this.width / 2, this.panelTop - 6, 0xD8CBA0);

		if (this.entries.isEmpty()) {
			graphics.drawCenteredString(this.font,
					Component.translatable("screen." + MODID + ".wheel.empty").getString(),
					this.width / 2, this.panelTop + 30, 0x8A8A8A);
			return;
		}

		String selected = this.minecraft != null && this.minecraft.player != null
				? RaAttachments.getSelected(this.minecraft.player) : "";

		for (int i = 0; i < this.entries.size(); i++) {
			RaTechniqueInfo info = this.entries.get(i);
			int rowLeft = this.panelLeft;
			int rowTop = this.panelTop + 14 + i * ROW_HEIGHT;
			boolean hovered = mouseX >= rowLeft && mouseX <= rowLeft + PANEL_WIDTH
					&& mouseY >= rowTop && mouseY <= rowTop + ROW_HEIGHT - 2;
			boolean current = info.path().equals(selected);

			if (current) {
				graphics.fill(rowLeft, rowTop, rowLeft + PANEL_WIDTH, rowTop + ROW_HEIGHT - 2, 0x5A9FE0D8);
			} else if (hovered) {
				graphics.fill(rowLeft, rowTop, rowLeft + PANEL_WIDTH, rowTop + ROW_HEIGHT - 2, 0x35FFFFFF);
			}

			int rank = this.minecraft != null && this.minecraft.player != null
					? RaAttachments.getRank(this.minecraft.player, info.id()) : 1;
			String line = Component.translatable("screen." + MODID + ".wheel.entry",
					Component.translatable(info.nameKey()).getString(),
					rankDots(rank),
					info.spiritCost()).getString();

			graphics.drawString(this.font, line, rowLeft + 8, rowTop + 5,
					rank >= 3 ? ChatFormatting.GOLD.getColor() : 0xE0E0DC, true);
		}

		graphics.drawCenteredString(this.font,
				Component.translatable("screen." + MODID + ".wheel.hint").getString(),
				this.width / 2, this.panelTop + 14 + this.entries.size() * ROW_HEIGHT + 12, 0x8A8A8A);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (button != 0) {
			return super.mouseClicked(mouseX, mouseY, button);
		}

		for (int i = 0; i < this.entries.size(); i++) {
			int rowTop = this.panelTop + 14 + i * ROW_HEIGHT;

			if (mouseX >= this.panelLeft && mouseX <= this.panelLeft + PANEL_WIDTH
					&& mouseY >= rowTop && mouseY <= rowTop + ROW_HEIGHT - 2) {
				PacketDistributor.sendToServer(new SelectTechniquePayload(this.entries.get(i).path()));
				this.onClose();
				return true;
			}
		}

		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	private static String rankDots(int rank) {
		StringBuilder builder = new StringBuilder();

		for (int i = 1; i <= 3; i++) {
			builder.append(i <= rank ? "\u25c6" : "\u25c7");
		}

		return builder.toString();
	}

	static Minecraft client() {
		return Minecraft.getInstance();
	}
}
