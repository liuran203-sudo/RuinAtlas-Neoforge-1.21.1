"""审计模型贴图引用是否逐条落地到文件。

用法：python tools/audit_textures.py
"""
import json
from pathlib import Path

ASSETS = Path("src/main/resources/assets/ruinatlas")
GEN = Path("src/generated/resources/assets/ruinatlas")
TEX = ASSETS / "textures"


def collect_models():
    out = {}
    for root in (ASSETS / "models", GEN / "models"):
        for f in root.rglob("*.json"):
            # 生成目录优先：同名时以 generated 为准
            out[f.relative_to(root).as_posix()] = f
    return out


def walk_textures(node, acc):
    if isinstance(node, dict):
        for k, v in node.items():
            if k == "textures" and isinstance(v, dict):
                for ref in v.values():
                    if isinstance(ref, str):
                        acc.append(ref)
            else:
                walk_textures(v, acc)
    elif isinstance(node, list):
        for v in node:
            walk_textures(v, acc)


def main():
    models = collect_models()
    missing = []
    checked = 0

    for name, path in models.items():
        data = json.loads(path.read_text(encoding="utf-8"))
        refs = []
        walk_textures(data, refs)

        for ref in refs:
            if ref.startswith("#") or ref == "particle":
                continue
            ns, _, rest = ref.partition(":")
            if not rest:
                ns, rest = "minecraft", ns
            if ns != "ruinatlas":
                continue
            p = TEX / f"{rest}.png"
            checked += 1
            if not p.is_file():
                missing.append((name, ref))

    print(f"模型 {len(models)} 个，本模组贴图引用 {checked} 条，缺失 {len(missing)} 条")
    for m in missing[:40]:
        print("  缺:", m[0], "->", m[1])

    # 反向：贴图存在但没有任何模型引用
    used = set()
    for name, path in models.items():
        refs = []
        walk_textures(json.loads(path.read_text(encoding="utf-8")), refs)
        for ref in refs:
            if ref.startswith("#"):
                continue
            ns, _, rest = ref.partition(":")
            if not rest:
                ns, rest = "minecraft", ns
            if ns == "ruinatlas":
                used.add(rest)

    orphans = []
    for png in TEX.rglob("*.png"):
        key = png.relative_to(TEX).as_posix()[:-4]
        if key not in used and not key.startswith("particle/"):
            orphans.append(key)

    print(f"未被任何模型引用的贴图：{len(orphans)}")
    for o in orphans[:40]:
        print("  孤:", o)


if __name__ == "__main__":
    main()
