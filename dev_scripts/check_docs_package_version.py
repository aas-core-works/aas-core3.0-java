"""
Check and conditionally update package version in docs.
"""

from __future__ import annotations

import argparse
from dataclasses import dataclass
import os
import pathlib
from pathlib import Path
import sys

from lxml import etree


@dataclass
class DependencyInfo:
    group_id: str
    artifact_id: str
    version: str


def _load_dependency_info(repo_root: Path) -> DependencyInfo:
    pom_path = repo_root / "pom.xml"

    parser = etree.XMLParser()
    tree = etree.parse(pom_path, parser=parser)

    group_id = tree.xpath(
        "/pom:project/pom:groupId[1]",
        namespaces={
            "pom": "http://maven.apache.org/POM/4.0.0",
        },
    )[0].text
    artifact_id = tree.xpath(
        "/pom:project/pom:artifactId[1]",
        namespaces={
            "pom": "http://maven.apache.org/POM/4.0.0",
        },
    )[0].text
    version = tree.xpath(
        "/pom:project/pom:version[1]",
        namespaces={
            "pom": "http://maven.apache.org/POM/4.0.0",
        },
    )[0].text

    return DependencyInfo(group_id=group_id, artifact_id=artifact_id, version=version)


def _validate_docs_dependency(repo_root: Path) -> bool:
    docs_overview_path = repo_root / "doc/overview.html"

    parser = etree.HTMLParser()
    tree = etree.parse(docs_overview_path, parser=parser)

    group_id = tree.xpath(
        "//dependency[1]/groupid[1]",
    )[0].text
    artifact_id = tree.xpath(
        "//dependency[1]/artifactid[1]",
    )[0].text
    version = tree.xpath(
        "//dependency[1]/version[1]",
    )[0].text

    dependency_info = _load_dependency_info(repo_root)

    if dependency_info.group_id != group_id:
        print(
            f"ERROR: groupId did not match, found: {group_id}, expected {dependency_info.group_id}",
            file=sys.stderr,
        )

    if dependency_info.artifact_id != artifact_id:
        print(
            f"ERROR: artifactId did not match, found: {artifact_id}, expected {dependency_info.artifact_id}",
            file=sys.stderr,
        )

    if dependency_info.version != version:
        print(
            f"ERROR: version did not match, found: {version}, expected {dependency_info.version}",
            file=sys.stderr,
        )

    return (
        dependency_info.group_id == group_id
        and dependency_info.artifact_id == artifact_id
        and dependency_info.version == version
    )


def _update_docs_dependency(repo_root: Path) -> None:
    docs_overview_path = repo_root / "doc/overview.html"

    parser = etree.HTMLParser()
    tree = etree.parse(docs_overview_path, parser=parser)

    dependency_info = _load_dependency_info(repo_root)

    old_group_id = tree.xpath(
        "//dependency[1]/groupid[1]",
    )[0].text
    old_artifact_id = tree.xpath(
        "//dependency[1]/artifactid[1]",
    )[0].text
    old_version = tree.xpath(
        "//dependency[1]/version[1]",
    )[0].text

    with docs_overview_path.open("r+") as docs_overview_file:
        data = docs_overview_file.read()
        docs_overview_file.seek(0)

        data = data.replace(old_group_id, dependency_info.group_id)
        data = data.replace(old_artifact_id, dependency_info.artifact_id)
        data = data.replace(old_version, dependency_info.version)

        docs_overview_file.write(data)


def main() -> int:
    this_path = pathlib.Path(os.path.realpath(__file__))
    repo_root = this_path.parent.parent

    parser = argparse.ArgumentParser(
        description="Validate and update package dependency infos in the documentation"
    )

    group = parser.add_mutually_exclusive_group(required=True)
    group.add_argument(
        "--update",
        action="store_true",
        help="update the dependency info in the documentation",
    )
    group.add_argument(
        "--validate",
        action="store_true",
        help="check that dependency info in documentation matches pom.xml",
    )

    args = parser.parse_args()

    if args.update:
        _update_docs_dependency(repo_root)
        return 0

    if not _validate_docs_dependency(repo_root):
        return 1

    return 0


if __name__ == "__main__":
    sys.exit(main())
