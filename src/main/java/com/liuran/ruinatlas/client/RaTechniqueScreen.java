package com.liuran.ruinatlas.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.ChatFormatting;
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
 * 灵契轮盘：已契满 / 正在修行 / 未习得 三段分类
 * <p>鼠标滚轮直接切换当前契法（Shift+滚轮翻列表），点条目也能换。
 * 面板宽高按窗口钳制，低分辨率下不再错位。
 */
public final class RaTechniqueScreen extends Screen {
	private static final int ROW_HEIGHT = 20;
	private static final int HEADER_HEIGHT = 16;
	private static final int PANEL_WIDTH = 260;

	private final List<Row> rows = new ArrayList<>();
	private int panelLeft;
	private int panelTop;
	private int panelWidth;
	private int visibleRows;
	private int scrollOffset;

	public RaTechniqueScreen() {
		super(Component.translatable("screen." + MODID + ".wheel"));
	}

	@Override
	protected void init() {
		this.rows.clear();

		if (this.minecraft != null && this.minecraft.player != null) {
			List<RaTechniqueInfo> maxed = new ArrayList<>();
			List<RaTechniqueInfo> training = new ArrayList<>();
			List<RaTechniqueInfo> unknown = new ArrayList<>();

			for (RaTechniqueInfo info : RaTechniqueRegistry.all()) {
				if (!RaAttachments.hasLearned(this.minecraft.player, info.id())) {
					unknown.add(info);
				} else if (RaAttachments.getRank(this.minecraft.player, info.id()) >= 3) {
					maxed.add(info);
				} else {
					training.add(info);
				}
			}

			addSection("screen." + MODID + ".wheel.section.maxed", maxed);
			addSection("screen." + MODID + ".wheel.section.training", training);
			addSection("screen." + MODID + ".wheel.section.unknown", unknown);
		}

		this.panelWidth = Math.min(PANEL_WIDTH, this.width - 16);
		this.panelLeft = (this.width - this.panelWidth) / 2;
		this.visibleRows = Math.max(3, (this.height - 130) / ROW_HEIGHT);
		this.scrollOffset = Math.min(this.scrollOffset, Math.max(0, this.rows.size() - this.visibleRows));

		int shown = Math.min(this.rows.size(), this.visibleRows);
		int panelHeight = 62 + shown * ROW_HEIGHT;
		this.panelTop = Math.max(8, Math.min(this.height - panelHeight - 8, (this.height - panelHeight) / 2));
	}

	private void addSection(String key, List<RaTechniqueInfo> entries) {
		if (entries.isEmpty()) {
			return;
		}

		this.rows.add(Row.header(key));

		for (RaTechniqueInfo info : entries) {
			this.rows.add(Row.entry(info));
		}
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		graphics.fill(0, 0, this.width, this.height, 0xA0121218);

		int shown = Math.min(this.rows.size() - this.scrollOffset, this.visibleRows);
		int panelHeight = 62 + Math.max(0, shown) * ROW_HEIGHT;
		graphics.fill(this.panelLeft - 6, this.panelTop - 30, this.panelLeft + this.panelWidth + 6,
				this.panelTop + panelHeight, 0xE0202A2E);
		graphics.fill(this.panelLeft - 6, this.panelTop - 30, this.panelLeft + this.panelWidth + 6,
				this.panelTop - 28, 0x9FE0D8);

		graphics.drawCenteredString(this.font, this.title, this.width / 2, this.panelTop - 22, 0x9FE0D8);
		String spirit = Component.translatable("hud." + MODID + ".spirit",
				this.minecraft != null && this.minecraft.player != null
						? RaAttachments.getSpirit(this.minecraft.player) : 0, RaAttachments.MAX_SPIRIT).getString();
		graphics.drawCenteredString(this.font, spirit, this.width / 2, this.panelTop - 10, 0xD8CBA0);

		if (this.rows.isEmpty()) {
			graphics.drawCenteredString(this.font,
					Component.translatable("screen." + MODID + ".wheel.empty").getString(),
					this.width / 2, this.panelTop + 30, 0x8A8A8A);
			return;
		}

		String selected = this.minecraft != null && this.minecraft.player != null
				? RaAttachments.getSelected(this.minecraft.player) : "";
		String locked = this.minecraft != null && this.minecraft.player != null
				? RaAttachments.getLockedTechnique(this.minecraft.player) : "";

		int rowTop = this.panelTop + 8;

		for (int i = this.scrollOffset; i < this.rows.size() && i < this.scrollOffset + this.visibleRows; i++) {
			Row row = this.rows.get(i);

			if (row.header) {
				graphics.drawString(this.font, Component.translatable(row.headerKey).getString(),
						this.panelLeft + 4, rowTop + 4, 0x7FD8C9, false);
				rowTop += HEADER_HEIGHT;
				continue;
			}

			RaTechniqueInfo info = row.info;
			boolean learned = this.minecraft != null && this.minecraft.player != null
					&& RaAttachments.hasLearned(this.minecraft.player, info.id());
			boolean current = info.path().equals(selected);
			boolean isLocked = info.path().equals(locked);
			int rank = learned && this.minecraft.player != null
					? RaAttachments.getRank(this.minecraft.player, info.id()) : 0;
			boolean hovered = mouseX >= this.panelLeft && mouseX <= this.panelLeft + this.panelWidth
					&& mouseY >= rowTop && mouseY <= rowTop + ROW_HEIGHT - 2;

			if (current) {
				graphics.fill(this.panelLeft, rowTop, this.panelLeft + this.panelWidth, rowTop + ROW_HEIGHT - 2, 0x5A9FE0D8);
			} else if (hovered && learned) {
				graphics.fill(this.panelLeft, rowTop, this.panelLeft + this.panelWidth, rowTop + ROW_HEIGHT - 2, 0x35FFFFFF);
			}

			String line = Component.translatable("screen." + MODID + ".wheel.entry",
					Component.translatable(info.nameKey()).getString(),
					learned ? rankDots(rank) : "\u25c7\u25c7\u25c7",
					info.spiritCost()).getString();

			if (isLocked) {
				line = line + " " + Component.translatable("screen." + MODID + ".wheel.locked").getString();
			}

			int color = !learned ? 0x6E7A78 : rank >= 3 ? ChatFormatting.GOLD.getColor() : 0xE0E0DC;
			graphics.drawString(this.font, line, this.panelLeft + 8, rowTop + 5, color, true);
			rowTop += ROW_HEIGHT;
		}

		String hint = this.rows.size() > this.visibleRows
				? Component.translatable("screen." + MODID + ".wheel.hint_scroll").getString()
				: Component.translatable("screen." + MODID + ".wheel.hint").getString();
		graphics.drawCenteredString(this.font, hint, this.width / 2, this.panelTop + panelHeight - 18, 0x8A8A8A);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (button != 0) {
			return super.mouseClicked(mouseX, mouseY, button);
		}

		int rowTop = this.panelTop + 8;

		for (int i = this.scrollOffset; i < this.rows.size() && i < this.scrollOffset + this.visibleRows; i++) {
			Row row = this.rows.get(i);

			if (row.header) {
				rowTop += HEADER_HEIGHT;
				continue;
			}

			if (mouseX >= this.panelLeft && mouseX <= this.panelLeft + this.panelWidth
					&& mouseY >= rowTop && mouseY <= rowTop + ROW_HEIGHT - 2) {
				if (this.minecraft != null && this.minecraft.player != null
						&& RaAttachments.hasLearned(this.minecraft.player, row.info.id())) {
					PacketDistributor.sendToServer(new SelectTechniquePayload(row.info.path()));
					this.onClose();
				}

				return true;
			}

			rowTop += ROW_HEIGHT;
		}

		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
		if (scrollY == 0.0D) {
			return false;
		}

		if (this.minecraft != null && this.minecraft.player != null && hasShiftDown()) {
			// Shift+滚轮翻列表
			this.scrollOffset = Math.max(0, Math.min(this.rows.size() - this.visibleRows,
					this.scrollOffset + (scrollY > 0 ? -1 : 1)));
			return true;
		}

		// 裸滚轮：在已习得的契法之间切换
		List<RaTechniqueInfo> learned = new ArrayList<>();

		if (this.minecraft != null && this.minecraft.player != null) {
			for (RaTechniqueInfo info : RaTechniqueRegistry.all()) {
				if (RaAttachments.hasLearned(this.minecraft.player, info.id())) {
					learned.add(info);
				}
			}
		}

		if (learned.isEmpty()) {
			return false;
		}

		String selected = RaAttachments.getSelected(this.minecraft.player);
		int index = 0;

		for (int i = 0; i < learned.size(); i++) {
			if (learned.get(i).path().equals(selected)) {
				index = i;
				break;
			}
		}

		int delta = scrollY > 0 ? -1 : 1;
		int next = Math.floorMod(index + delta, learned.size());
		PacketDistributor.sendToServer(new SelectTechniquePayload(learned.get(next).path()));
		return true;
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	private static String rankDots(int rank) {
		boolean[] dots = new boolean[]{rank >= 1, rank >= 2, rank >= 3};
		StringBuilder builder = new StringBuilder();

		for (boolean dot : dots) {
			builder.append(dot ? "\u25c6" : "\u25c7");
		}

		return builder.toString();
	}

	private static final class Row {
		private final RaTechniqueInfo info;
		private final boolean header;
		private final String headerKey;

		private Row(RaTechniqueInfo info, boolean header, String headerKey) {
			this.info = info;
			this.header = header;
			this.headerKey = headerKey;
		}

		private static Row entry(RaTechniqueInfo info) {
			return new Row(info, false, "");
		}

		private static Row header(String key) {
			return new Row(null, true, key);
		}
	}
}
