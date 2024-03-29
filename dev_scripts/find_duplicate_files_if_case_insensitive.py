"""
Find all the duplicate repository files on case-insensitive filesystems.

This is in particular important for Windows so that we do not mess up Git there.

You should run this script on a *case-sensitive* filesystem, *e.g.*, in Linux.
"""

import os.path
import pathlib
import sys
import textwrap
from typing import List, MutableMapping


def main() -> int:
    this_path = pathlib.Path(os.path.realpath(__file__))
    repo_root = this_path.parent.parent

    paths = []  # type: List[pathlib.Path]

    for path in repo_root.iterdir():
        if path.name == ".git":
            continue

        if path.is_file():
            paths.append(path)
        else:
            assert path.is_dir()
            paths.extend(
                another_path
                for another_path in path.glob("**/*")
                if another_path.is_file()
            )

    path_map = dict()  # type: MutableMapping[str, List[pathlib.Path]]
    for path in paths:
        key = str(path).lower()
        existing_paths = path_map.get(key, None)
        if existing_paths is None:
            path_map[key] = [path]
        else:
            existing_paths.append(path)

    for key in sorted(path_map.keys()):
        paths = path_map[key]
        if len(paths) > 1:
            paths_joined = "\n".join(
                str(path.relative_to(repo_root)) for path in sorted(paths)
            )
            paths_joined_indented = textwrap.indent(paths_joined, "  ")
            print(f"* Duplicates for {key}:\n{paths_joined_indented}")

    return 0


if __name__ == "__main__":
    sys.exit(main())
