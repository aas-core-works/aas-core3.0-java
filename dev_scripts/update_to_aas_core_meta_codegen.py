"""
Update everything in this project to the latest aas-core-meta and -codegen.

Git, Java and Maven are expected to be installed.
"""

from __future__ import annotations

import argparse
import os
import pathlib
import re
import shutil
import subprocess
import sys
import tempfile
import time
from typing import Optional, List, Callable, AnyStr, Sequence, Mapping


def _execute(
    cmd: Sequence[str],
    stdout: Optional[int] = None,
    stderr: Optional[int] = None,
    cwd: Optional[str] = None,
    env: Optional[Mapping[str, str]] = None,
) -> Optional[int]:
    """
    Execute the command and print if something went wrong.

    Return the non-zero exit code, or None if the exit code was zero.
    """
    cmd_joined = " ".join(cmd)

    exit_code = None  # type: Optional[int]
    try:
        exit_code = subprocess.call(cmd, stdout=stdout, stderr=stderr, cwd=cwd, env=env)
    except FileNotFoundError as exception:
        print(f"Failed to execute {cmd_joined}: {exception}", file=sys.stderr)
        return 1

    assert exit_code is not None

    if exit_code != 0:
        print(
            f"Failed to execute {cmd_joined} with exit code: {exit_code}",
            file=sys.stderr,
        )
        return exit_code

    return None


def _check_all_tools_installed() -> Optional[int]:
    """Check that Git, Java and Maven are installed and work properly."""
    exit_code = _execute(
        cmd=["git", "--version"], stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL
    )
    if exit_code is not None:
        return exit_code

    exit_code = _execute(
        cmd=["java", "-version"], stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL
    )
    if exit_code is not None:
        return exit_code

    exit_code = _execute(
        cmd=["mvn", "-version"], stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL
    )
    if exit_code is not None:
        return exit_code

    return None


# noinspection RegExpSimplifiable
AAS_CORE_META_DEPENDENCY_RE = re.compile(
    r"aas-core-meta@git\+https://github.com/aas-core-works/aas-core-meta@([a-fA-F0-9]+)"
)

# noinspection RegExpSimplifiable
AAS_CORE_CODEGEN_DEPENDENCY_RE = re.compile(
    r"aas-core-codegen@git\+https://github.com/aas-core-works/aas-core-codegen@([a-fA-F0-9]+)"
)


def _make_sure_no_changed_files(
    repo_dir: pathlib.Path, expected_branch: str
) -> Optional[int]:
    """
    Make sure that no files are modified in the given repository.

    Return exit code if something is unexpected.
    """
    diff_name_status = subprocess.check_output(
        ["git", "diff", "--name-status", expected_branch],
        cwd=str(repo_dir),
        encoding="utf-8",
    ).strip()

    if len(diff_name_status.splitlines()) > 0:
        print(
            f"The following files are modified "
            f"compared to branch {expected_branch!r} in {repo_dir}:\n"
            f"{diff_name_status}\n"
            f"\n"
            f"Please stash the changes first before you update to aas-core-meta.",
            file=sys.stderr,
        )
        return 1

    return None


def _update_pyproject_toml(
    our_repo: pathlib.Path, aas_core_meta_revision: str, aas_core_codegen_revision: str
) -> None:
    """Update the aas-core-meta and aas-core-codegen in pyproject.toml."""
    pyproject_toml = our_repo / "dev_scripts" / "pyproject.toml"
    text = pyproject_toml.read_text(encoding="utf-8")

    aas_core_meta_dependency = (
        f"aas-core-meta@git+https://github.com/aas-core-works/aas-core-meta"
        f"@{aas_core_meta_revision}"
    )

    text = re.sub(AAS_CORE_META_DEPENDENCY_RE, aas_core_meta_dependency, text)

    aas_core_codegen_dependency = (
        f"aas-core-codegen@git+https://github.com/aas-core-works/aas-core-codegen"
        f"@{aas_core_codegen_revision}"
    )

    text = re.sub(AAS_CORE_CODEGEN_DEPENDENCY_RE, aas_core_codegen_dependency, text)

    pyproject_toml.write_text(text, encoding="utf-8")


def _uninstall_and_install_aas_core_meta(
    our_repo: pathlib.Path, aas_core_meta_revision: str
) -> Optional[int]:
    """
    Uninstall and install the latest aas-core-meta in the virtual environment.

    Return a non-zero exit code if something went wrong.
    """
    exit_code = _execute(
        cmd=[sys.executable, "-m", "pip", "uninstall", "-y", "aas-core-meta"],
        cwd=str(our_repo),
    )
    if exit_code is not None:
        return exit_code

    aas_core_meta_dependency = (
        f"aas-core-meta@git+https://github.com/aas-core-works/aas-core-meta"
        f"@{aas_core_meta_revision}"
    )

    exit_code = _execute(
        cmd=[sys.executable, "-m", "pip", "install", aas_core_meta_dependency],
        cwd=str(our_repo),
    )
    if exit_code is not None:
        return exit_code

    return None


def _uninstall_and_install_aas_core_codegen(
    our_repo: pathlib.Path, aas_core_codegen_revision: str
) -> Optional[int]:
    """
    Uninstall and install the latest aas-core-codegen in the virtual environment.

    Return a non-zero exit code if something went wrong.
    """
    exit_code = _execute(
        cmd=[sys.executable, "-m", "pip", "uninstall", "-y", "aas-core-codegen"],
        cwd=str(our_repo),
    )
    if exit_code is not None:
        return exit_code

    aas_core_codegen_dependency = (
        f"aas-core-codegen@git+https://github.com/aas-core-works/aas-core-codegen"
        f"@{aas_core_codegen_revision}"
    )

    exit_code = _execute(
        cmd=[sys.executable, "-m", "pip", "install", aas_core_codegen_dependency],
        cwd=str(our_repo),
    )
    if exit_code is not None:
        return exit_code

    return None


def _regenerate_code(
    our_repo: pathlib.Path,
    meta_repo: pathlib.Path,
) -> Optional[int]:
    """
    Call codegen script.

    Return an error code, if any.
    """
    codegen_dir = our_repo / "dev_scripts/codegen"

    meta_model_path = meta_repo / "aas_core_meta/v3.py"

    target_dir = our_repo

    print(f"Starting to run codegen script")
    start = time.perf_counter()

    proc = subprocess.run(
        [
            sys.executable,
            "codegen.py",
            "--meta_model",
            str(meta_model_path),
            "--target",
            str(target_dir),
        ],
        cwd=str(codegen_dir),
    )

    if proc.returncode != 0:
        return proc.returncode

    duration = time.perf_counter() - start
    print(f"Generating the code took: {duration:.2f} seconds.")

    return None


def _semantically_patch(our_repo: pathlib.Path) -> Optional[int]:
    """Run the scripts to semantically patch the generated code."""
    cwd = our_repo / "dev_scripts" / "semantic-patching"

    verification_java = (
        our_repo / "src/main/java/aas_core/aas3_0/verification/Verification.java"
    )
    assert verification_java.exists() and verification_java.is_file(), (
        f"No Verification.java found to patch: {verification_java=}"
    )

    exit_code = _execute(cmd=["mvn", "package"], cwd=str(cwd))
    if exit_code is not None:
        return exit_code

    exit_code = _execute(
        [
            "mvn",
            "exec:java",
            "-Dexec.mainClass=aas_core_works.PatchVerification",
            f"-Dexec.args={verification_java} {verification_java}",
        ],
        cwd=str(cwd),
    )
    if exit_code is not None:
        return exit_code

    return None


def _reformat_code(our_repo: pathlib.Path) -> Optional[int]:
    """Reformat the generated code."""
    print("Re-formatting the code...")
    return _execute(cmd=["mvn", "spotless:apply"], cwd=str(our_repo))


def _run_tests_and_rerecord(our_repo: pathlib.Path) -> Optional[int]:
    """
    Run the tests with the environment variables set to re-record.

    Return the non-zero exit code if something went wrong.
    """
    print("Running tests & re-recording the test traces...")

    env = os.environ.copy()
    env["AAS_CORE_AAS3_0_TESTS_RECORD_MODE"] = "true"

    return _execute(cmd=["mvn", "test"], env=env, cwd=str(our_repo))


def _run_check(our_repo: pathlib.Path) -> Optional[int]:
    """
    Run all the pre-commit checks.

    Return the non-zero exit code if something went wrong.
    """
    return _execute(cmd=["mvn", "spotless:check"], cwd=str(our_repo))


def _create_branch_commit_and_push(
    our_repo: pathlib.Path,
    aas_core_meta_revision: str,
    aas_core_codegen_revision: str,
    aas_core_testgen_revision: str,
) -> None:
    """Create a feature branch, commit the changes and push it."""
    branch = (
        f"Update-to-aas-core-meta-codegen-testgen-{aas_core_meta_revision}-"
        f"{aas_core_codegen_revision}-{aas_core_testgen_revision}"
    )
    print(f"Creating the branch {branch!r}...")
    subprocess.check_call(["git", "checkout", "-b", branch], cwd=our_repo)

    print("Adding files...")
    subprocess.check_call(["git", "add", "."], cwd=our_repo)

    # pylint: disable=line-too-long
    message = f"""\
Update to aas-core-meta, codegen, testgen {aas_core_meta_revision}, {aas_core_codegen_revision}, {aas_core_testgen_revision}

We update the development requirements to and re-generate everything
with:
* [aas-core-meta {aas_core_meta_revision}],
* [aas-core-codegen {aas_core_codegen_revision}] and
* [aas-core3.0-testgen {aas_core_testgen_revision}].

[aas-core-meta {aas_core_meta_revision}]: https://github.com/aas-core-works/aas-core-meta/commit/{aas_core_meta_revision}
[aas-core-codegen {aas_core_codegen_revision}]: https://github.com/aas-core-works/aas-core-codegen/commit/{aas_core_codegen_revision}
[aas-core3.0-testgen {aas_core_testgen_revision}]: https://github.com/aas-core-works/aas-core3.0-testgen/commit/{aas_core_testgen_revision}
"""

    # pylint: enable=line-too-long

    print("Committing...")
    with tempfile.TemporaryDirectory() as tmp_dir:
        tmp_file = pathlib.Path(tmp_dir) / "commit-message.txt"
        tmp_file.write_text(message, encoding="utf-8")

        subprocess.check_call(["git", "commit", "--file", str(tmp_file)], cwd=our_repo)

    print(f"Pushing to remote {branch}...")
    subprocess.check_call(["git", "push", "-u"], cwd=our_repo)


def _get_testgen_revision(our_repo: pathlib.Path) -> str | None:
    testgen_rev_path = our_repo / "src/test/java/testgen_rev.txt"

    testgen_rev: str | None = None

    try:
        with testgen_rev_path.open("r") as testgen_rev_file:
            testgen_rev = testgen_rev_file.read().strip()
    except OSError as os_error:
        print(f"Cannot read testgen revision: {os_error}.")

    if testgen_rev is None:
        print(f"Cannot read testgen revision.")

    return testgen_rev


def main() -> int:
    """Execute the main routine."""
    this_path = pathlib.Path(os.path.realpath(__file__))
    our_repo = this_path.parent.parent

    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument(
        "--aas_core_meta_repo",
        help="path to the aas-core-meta repository",
        default=str(our_repo.parent / "aas-core-meta"),
    )
    parser.add_argument(
        "--expected_aas_core_meta_branch",
        help="Git branch expected in the aas-core-meta repository",
        default="main",
    )
    parser.add_argument(
        "--aas_core_codegen_repo",
        help="path to the aas-core-codegen repository",
        default=str(our_repo.parent / "aas-core-codegen"),
    )
    parser.add_argument(
        "--expected_aas_core_codegen_branch",
        help="Git branch expected in the aas-core-meta repository",
        default="main",
    )
    parser.add_argument(
        "--expected_our_branch",
        help="Git branch expected in this repository",
        default="main",
    )

    args = parser.parse_args()

    aas_core_meta_repo = pathlib.Path(args.aas_core_meta_repo)
    expected_aas_core_meta_branch = str(args.expected_aas_core_meta_branch)

    aas_core_codegen_repo = pathlib.Path(args.aas_core_codegen_repo)
    expected_aas_core_codegen_branch = str(args.expected_aas_core_codegen_branch)

    expected_our_branch = str(args.expected_our_branch)

    exit_code = _check_all_tools_installed()
    if exit_code is not None:
        return exit_code

    # region aas-core-meta repo

    if not aas_core_meta_repo.exists():
        print(
            f"--aas_core_meta_repo does not exist: {aas_core_meta_repo}",
            file=sys.stderr,
        )
        return 1

    if not aas_core_meta_repo.is_dir():
        print(
            f"--aas_core_meta_repo is not a directory: {aas_core_meta_repo}",
            file=sys.stderr,
        )
        return 1

    aas_core_meta_branch = subprocess.check_output(
        ["git", "rev-parse", "--abbrev-ref", "HEAD"],
        cwd=str(aas_core_meta_repo),
        encoding="utf-8",
    ).strip()
    if aas_core_meta_branch != expected_aas_core_meta_branch:
        print(
            f"--expected_aas_core_meta_branch is {expected_aas_core_meta_branch}, "
            f"but got {aas_core_meta_branch} "
            f"in --aas_core_meta_repo: {aas_core_meta_repo}",
            file=sys.stderr,
        )
        return 1

    aas_core_meta_revision = subprocess.check_output(
        ["git", "rev-parse", "--short", "HEAD"],
        cwd=str(aas_core_meta_repo),
        encoding="utf-8",
    ).strip()

    # endregion

    # region aas-core-codegen repo

    if not aas_core_codegen_repo.exists():
        print(
            f"--aas_core_codegen_repo does not exist: {aas_core_codegen_repo}",
            file=sys.stderr,
        )
        return 1

    if not aas_core_codegen_repo.is_dir():
        print(
            f"--aas_core_codegen_repo is not a directory: {aas_core_codegen_repo}",
            file=sys.stderr,
        )
        return 1

    aas_core_codegen_branch = subprocess.check_output(
        ["git", "rev-parse", "--abbrev-ref", "HEAD"],
        cwd=str(aas_core_codegen_repo),
        encoding="utf-8",
    ).strip()
    if aas_core_codegen_branch != expected_aas_core_codegen_branch:
        print(
            f"--expected_aas_core_codegen_branch is {expected_aas_core_codegen_branch}, "
            f"but got {aas_core_codegen_branch} "
            f"in --aas_core_codegen_repo: {aas_core_codegen_repo}",
            file=sys.stderr,
        )
        return 1

    aas_core_codegen_revision = subprocess.check_output(
        ["git", "rev-parse", "--short", "HEAD"],
        cwd=str(aas_core_codegen_repo),
        encoding="utf-8",
    ).strip()

    # endregion

    # region Our repo

    our_branch = subprocess.check_output(
        ["git", "rev-parse", "--abbrev-ref", "HEAD"],
        cwd=str(our_repo),
        encoding="utf-8",
    ).strip()
    if our_branch != expected_our_branch:
        print(
            f"--expected_our_branch is {expected_our_branch}, "
            f"but got {our_branch} in: {our_repo}",
            file=sys.stderr,
        )
        return 1

    # endregion

    for repo_dir, expected_branch in [
        (our_repo, expected_our_branch),
        (aas_core_meta_repo, expected_aas_core_meta_branch),
        (aas_core_codegen_repo, expected_aas_core_codegen_branch),
    ]:
        exit_code = _make_sure_no_changed_files(
            repo_dir=repo_dir, expected_branch=expected_branch
        )
        if exit_code is not None:
            return exit_code

    _update_pyproject_toml(
        our_repo=our_repo,
        aas_core_meta_revision=aas_core_meta_revision,
        aas_core_codegen_revision=aas_core_codegen_revision,
    )

    exit_code = _uninstall_and_install_aas_core_meta(
        our_repo=our_repo, aas_core_meta_revision=aas_core_meta_revision
    )
    if exit_code is not None:
        return exit_code

    exit_code = _uninstall_and_install_aas_core_codegen(
        our_repo=our_repo, aas_core_codegen_revision=aas_core_codegen_revision
    )
    if exit_code is not None:
        return exit_code

    exit_code = _regenerate_code(our_repo=our_repo, meta_repo=aas_core_meta_repo)
    if exit_code is not None:
        return exit_code

    exit_code = _semantically_patch(our_repo=our_repo)
    if exit_code is not None:
        return exit_code

    exit_code = _reformat_code(our_repo=our_repo)
    if exit_code is not None:
        return exit_code

    exit_code = _run_tests_and_rerecord(our_repo=our_repo)
    if exit_code is not None:
        return exit_code

    exit_code = _run_check(our_repo=our_repo)
    if exit_code is not None:
        return exit_code

    aas_core_testgen_revision = _get_testgen_revision(our_repo=our_repo)
    if aas_core_testgen_revision is None:
        return 1

    _create_branch_commit_and_push(
        our_repo=our_repo,
        aas_core_meta_revision=aas_core_meta_revision,
        aas_core_codegen_revision=aas_core_codegen_revision,
        aas_core_testgen_revision=aas_core_testgen_revision,
    )

    return 0


if __name__ == "__main__":
    sys.exit(main())
