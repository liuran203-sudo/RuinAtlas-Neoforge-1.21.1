"""为术法卷轴与铜珠生成 16x16 像素图标。

用法：python tools/gen_technique_icons.py
输出到 src/main/resources/assets/ruinatlas/textures/item/
"""
from pathlib import Path

from PIL import Image

OUT = Path("src/main/resources/assets/ruinatlas/textures/item")

PAPER = (234, 223, 194, 255)
PAPER_EDGE = (208, 190, 158, 255)
ROLLER = (138, 106, 63, 255)
ROLLER_END = (104, 78, 45, 255)
INK = (58, 50, 42, 255)

COPPER = (192, 138, 78, 255)
COPPER_RIM = (132, 84, 43, 255)
COPPER_LIGHT = (240, 210, 160, 255)

SCROLLS = {
    "cloud_step_scroll": (159, 217, 224),
    "wind_blade_scroll": (168, 216, 201),
    "ward_evil_scroll": (230, 200, 110),
    "hold_breath_scroll": (183, 167, 217),
    "listen_wind_scroll": (143, 200, 232),
    "guide_light_scroll": (242, 226, 155),
    "mountain_step_scroll": (201, 163, 107),
    "cleanse_scroll": (232, 237, 230),
    "stone_sense_scroll": (169, 192, 201),
    "spirit_ward_scroll": (217, 183, 201),
}


def scroll_icon(accent):
    img = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    px = img.load()

    # 卷身纸面
    for x in range(4, 12):
        for y in range(3, 14):
            px[x, y] = PAPER

    # 纸面两侧暗边
    for y in range(3, 14):
        px[4, y] = PAPER_EDGE
        px[11, y] = PAPER_EDGE

    # 上下轴
    for x in range(3, 13):
        px[x, 1] = ROLLER
        px[x, 2] = ROLLER
        px[x, 13] = ROLLER
        px[x, 14] = ROLLER
    for x in (3, 12):
        px[x, 1] = ROLLER_END
        px[x, 13] = ROLLER_END

    # 竖排符纹（技式配色）
    tint = accent + (255,) if len(accent) == 3 else accent
    for y in range(5, 12):
        px[6, y] = tint
        px[9, y] = tint
    for x in (6, 9):
        px[x, 4] = INK
        px[x, 12] = INK
    px[7, 8] = INK
    px[8, 8] = INK

    return img


def bead_icon():
    img = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    px = img.load()

    for dx in range(-6, 7):
        for dy in range(-6, 7):
            d = dx * dx + dy * dy
            if d > 33:
                continue
            x, y = 8 + dx, 8 + dy
            if 20 < d <= 33:
                px[x, y] = COPPER_RIM
            else:
                px[x, y] = COPPER

    for dx, dy in ((-2, -3), (-1, -3), (-3, -2), (-2, -2), (-1, -2)):
        px[8 + dx, 8 + dy] = COPPER_LIGHT

    px[8, 8] = COPPER_RIM

    return img


def main():
    OUT.mkdir(parents=True, exist_ok=True)

    for name, accent in SCROLLS.items():
        scroll_icon(accent).save(OUT / f"{name}.png")

    bead_icon().save(OUT / "spirit_copper_bead.png")

    print(f"已生成 {len(SCROLLS)} 张卷轴 + 1 张铜珠图标 -> {OUT}")


if __name__ == "__main__":
    main()
