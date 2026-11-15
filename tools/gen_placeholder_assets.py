"""生成墟舆录的占位贴图（仅 PNG）。

blockstate / 方块模型 / 物品模型 / 语言文件现在全部由 datagen 生成（runData 输出到
src/generated/resources），这个脚本只负责占位贴图，后续美术替换时直接覆盖同名文件即可。
用法：python tools/gen_placeholder_assets.py
"""

import colorsys
import json
import os
import re
import struct
import zlib

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
NS = "ruinatlas"
ASSETS = os.path.join(ROOT, "src", "main", "resources", "assets", NS)
INIT_DIR = os.path.join(ROOT, "src", "main", "java", "com", "liuran", "ruinatlas", "init")

BLOCK_RE = re.compile(r'BLOCK_REGISTER\.\w+\(\s*"([a-z0-9_]+)"')
ITEM_RE = re.compile(r'ITEM_REGISTER\.\w+\(\s*"([a-z0-9_]+)"')

# 按关键词给一个近似色调，保证同一类东西颜色接近
PALETTE = [
    (("log", "bark", "plank", "wood", "pillar", "brace", "screen", "casket", "kiosk", "board", "offering", "table"), (112, 88, 64)),
    (("grass", "plant", "sprout", "thicket", "vine", "lily", "reed", "moss", "lichen", "petal", "thorn"), (92, 116, 74)),
    (("brick", "tile", "paving", "step", "skirt", "quoin", "curb", "ridge", "eave", "bracket", "baluster", "newel", "lattice", "window", "door", "frame"), (124, 122, 116)),
    (("stone", "rock", "rubble", "pile", "plinth", "base", "lantern", "altar", "stele", "niche", "platform", "silt", "loam", "soil", "thatch", "waystone"), (110, 110, 106)),
    (("jade", "crystal", "spar", "pearl", "glow", "spirit", "frost", "star"), (124, 176, 184)),
    (("rubbing", "message", "token", "pendant", "paper", "sacrificial", "copper"), (196, 178, 140)),
    (("dagger", "robe", "hemp", "sword"), (86, 92, 108)),
]


def pick_color(name: str):
    for words, color in PALETTE:
        for word in words:
            if word in name:
                return color
    h = zlib.crc32(name.encode("utf-8")) % 360
    r, g, b = colorsys.hsv_to_rgb(h / 360.0, 0.18, 0.55)
    return int(r * 255), int(g * 255), int(b * 255)


def write_png(path: str, color):
    """写一张 16x16 的纯色 PNG，带一点点噪点，免得整块死板。"""
    w = h = 16
    r, g, b = color
    rows = []
    for y in range(h):
        row = bytearray()
        for x in range(w):
            delta = ((x * 7 + y * 13) % 5) - 2
            row.extend((
                max(0, min(255, r + delta)),
                max(0, min(255, g + delta)),
                max(0, min(255, b + delta)),
            ))
        rows.append(b"\x00" + bytes(row))

    def chunk(tag: bytes, data: bytes):
        return tag + data + struct.pack(">I", zlib.crc32(tag + data) & 0xFFFFFFFF)

    png = b"\x89PNG\r\n\x1a\n"
    png += chunk(b"IHDR", struct.pack(">IIBBBBB", w, h, 8, 2, 0, 0, 0))
    png += chunk(b"IDAT", zlib.compress(b"".join(rows), 9))
    png += chunk(b"IEND", b"")
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "wb") as fp:
        fp.write(png)


def write_json(path: str, data):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as fp:
        json.dump(data, fp, ensure_ascii=False, indent=2)
        fp.write("\n")


def write_soft_dot(path: str, color, peak_alpha: int = 255):
    """写一张 16x16 的圆形柔边 RGBA 贴图，给粒子用（避免方块边缘露馅）。"""
    w = h = 16
    r, g, b = color
    rows = []
    for y in range(h):
        row = bytearray()
        for x in range(w):
            dist = ((x - 7.5) ** 2 + (y - 7.5) ** 2) ** 0.5
            falloff = max(0.0, 1.0 - (dist / 7.6) ** 2)
            alpha = int(peak_alpha * falloff)
            row.extend((r, g, b, alpha))
        rows.append(b"\x00" + bytes(row))

    def chunk(tag: bytes, data: bytes):
        return tag + data + struct.pack(">I", zlib.crc32(tag + data) & 0xFFFFFFFF)

    png = b"\x89PNG\r\n\x1a\n"
    png += chunk(b"IHDR", struct.pack(">IIBBBBB", w, h, 8, 6, 0, 0, 0))
    png += chunk(b"IDAT", zlib.compress(b"".join(rows), 9))
    png += chunk(b"IEND", b"")
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "wb") as fp:
        fp.write(png)


PARTICLES = [
    ("spirit_mist", (199, 235, 242), 255),
    ("spirit_glow", (158, 235, 235), 255),
]


def particle_assets():
    for name, color, peak in PARTICLES:
        write_soft_dot(os.path.join(ASSETS, "textures", "particle", name + ".png"), color, peak)
        write_json(os.path.join(ASSETS, "particles", name + ".json"), {
            "textures": [f"{NS}:{name}"],
        })


def main():
    with open(os.path.join(INIT_DIR, "RaBlocks.java"), encoding="utf-8") as fp:
        block_src = fp.read()
    with open(os.path.join(INIT_DIR, "RaItems.java"), encoding="utf-8") as fp:
        item_src = fp.read()

    blocks = sorted(set(BLOCK_RE.findall(block_src)))
    items = sorted(set(ITEM_RE.findall(item_src)) - set(blocks))

    for name in blocks:
        write_png(os.path.join(ASSETS, "textures", "block", name + ".png"), pick_color(name))
    for name in items:
        write_png(os.path.join(ASSETS, "textures", "item", name + ".png"), pick_color(name))

    particle_assets()

    print(f"block textures={len(blocks)} item textures={len(items)} particles={len(PARTICLES)} written to {ASSETS}")


if __name__ == "__main__":
    main()
