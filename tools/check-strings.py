#!/usr/bin/env python3
"""Fails (exit 1) if the EN and RU strings.xml files do not define the same keys.

Usage: python tools/check-strings.py
"""
import sys
import xml.etree.ElementTree as ET
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
RES = ROOT / "composeApp" / "src" / "commonMain" / "composeResources"
FILES = {"en": RES / "values" / "strings.xml", "ru": RES / "values-ru" / "strings.xml"}


def load(path: Path) -> dict:
    if not path.exists():
        sys.exit(f"ERROR: missing file {path}")
    return {el.get("name"): (el.text or "").strip() for el in ET.parse(path).getroot().iter("string")}


def main() -> int:
    en, ru = load(FILES["en"]), load(FILES["ru"])
    ok = True
    for lang, own, other in (("RU", ru, en), ("EN", en, ru)):
        missing = sorted(set(other) - set(own))
        if missing:
            ok = False
            print(f"ERROR: keys missing in {lang}: {', '.join(missing)}")
    empty = sorted(k for d in (en, ru) for k, v in d.items() if not v)
    if empty:
        ok = False
        print(f"ERROR: empty values: {', '.join(empty)}")
    if ok:
        print(f"OK: {len(en)} keys, EN and RU match")
    return 0 if ok else 1


if __name__ == "__main__":
    sys.exit(main())
