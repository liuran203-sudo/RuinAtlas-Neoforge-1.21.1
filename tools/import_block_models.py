"""把 Blockbench 导出的方块模型导进工程：清掉 Blockbench 专属字段并把命名空间改成 ruinatlas。

用法：
    python tools/import_block_models.py [源目录] [目标目录]

默认源目录 C:\\Users\\Liuran\\Desktop\\block，目标 src/main/resources/assets/ruinatlas/models/block。
处理内容：
    - 删除 format_version / credit（Blockbench 元信息，原版模型用不到）
    - 删除 display.on_shelf（Blockbench 的书架展示槽，原版模型没有这个槽位）
    - ruin_atlas -> ruinatlas
其余 elements / textures / display 原样保留。
"""

import json
import pathlib
import sys

SRC = pathlib.Path(sys.argv[1]) if len(sys.argv) > 1 else pathlib.Path(r"C:\Users\Liuran\Desktop\block")
DST = (
    pathlib.Path(sys.argv[2])
    if len(sys.argv) > 2
    else pathlib.Path(r"D:\Workspaces\RuinAtlas-NeoForge-1.21.1\src\main\resources\assets\ruinatlas\models\block")
)


def fix_namespace(node):
    if isinstance(node, str):
        return node.replace("ruin_atlas", "ruinatlas")
    if isinstance(node, list):
        return [fix_namespace(item) for item in node]
    if isinstance(node, dict):
        return {key: fix_namespace(value) for key, value in node.items()}
    return node


def main():
    if not SRC.is_dir():
        raise SystemExit(f"源目录不存在: {SRC}")
    DST.mkdir(parents=True, exist_ok=True)

    names = []
    for path in sorted(SRC.glob("*.json")):
        data = json.loads(path.read_text(encoding="utf-8"))
        data.pop("format_version", None)
        data.pop("credit", None)
        display = data.get("display")
        if isinstance(display, dict):
            display.pop("on_shelf", None)
            if not display:
                data.pop("display", None)

        data = fix_namespace(data)
        (DST / path.name).write_text(
            json.dumps(data, indent=2, ensure_ascii=False) + "\n", encoding="utf-8"
        )
        names.append(path.stem)

    print(f"已导入 {len(names)} 个模型 -> {DST}")
    print("\n".join(names))


if __name__ == "__main__":
    main()
