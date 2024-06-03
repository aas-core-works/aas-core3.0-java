"""Generate the common functions to de/serialize instances of a class."""

import io
import os
import pathlib
import sys
import textwrap
from typing import List
from aas_core_codegen.common import Stripped
from aas_core_codegen.java.common import (
    INDENT as I,
    INDENT2 as II,
    INDENT3 as III,
    INDENT4 as IIII,
    INDENT5 as IIIII,
)

def _generate_find_paths() -> Stripped:
    return Stripped(f"""\
public static List<Path> findPaths(Path path, String fileExtension) throws IOException {{
{I}if (!Files.isDirectory(path)) {{
{II}throw new IllegalArgumentException("Path must be a directory!");
{I}}}

{I}List<Path> result;
{I}try (Stream<Path> walk = Files.walk(path)) {{
{II}result = walk
{III}.filter(p -> !Files.isDirectory(p))
{III}.filter(f -> f.toString().endsWith(fileExtension))
{III}.collect(Collectors.toList());
{I}}}
{I}return result;
}}""")

def _generate_find_dirs() -> Stripped:
    return Stripped(f"""\
public static List<Path> findDirs(Path path) throws IOException {{
{I}if (!Files.isDirectory(path)) {{
{II}throw new IllegalArgumentException("Path must be a directory!");
{I}}}

{I}List<Path> result;
{I}try (Stream<Path> walk = Files.walk(path)) {{
{II}result = walk
{III}.filter(p -> Files.isDirectory(p))
{III}.collect(Collectors.toList());
{I}}}
{I}return result;
}}""")

def _generate_must_find()-> Stripped:
    return Stripped(f"""\
@SuppressWarnings("unchecked")
public static <T extends IClass> T mustFind(String type, IClass container) {{
{I}if (type.equals(container.getClass().getSimpleName())) {{
{II}return (T)container;
{I}}}
{I}IClass instance = null;
{I}for (IClass current : container.descend()) {{
{II}if (type.equals(current.getClass().getSimpleName())) {{
{III}instance = current;
{III}break;
{I}}}
{I}}}
{I}if(instance == null){{
{II}throw new IllegalStateException("No instance of " + type + " could be found");
{I}}}
{I}return (T)instance;
}}""")

def _generate_assert_no_verification_errors()-> Stripped:
    return Stripped(f"""\
public static void assertNoVerificationErrors(List<Reporting.Error> errors, Path path){{

{I}if(!errors.isEmpty()){{
{II}StringBuilder stringBuilder = new StringBuilder();
{III}stringBuilder.append("Expected no errors when verifying the instance de-serialized from ")
{III}.append(path.toString()).append(", ")
{III}.append("but got ")
{III}.append(errors.size())
{III}.append(" error(s):")
{III}.append(System.lineSeparator());
{II}for(Reporting.Error error : errors){{
{III}stringBuilder.append(Reporting.generateJsonPath(error.getPathSegments()))
{III}.append(": ")
{III}.append(error.getCause());
{II}}}
{II}fail(stringBuilder.toString());
{I}}}
}}""")

def _generate_as_list()-> Stripped:
    return Stripped(f"""\
public static <T> List<T> asList(Iterable<T> iterable) {{
{I}return StreamSupport.stream(iterable.spliterator(), false)
{II}.collect(Collectors.toList());
}}""")

def _generate_assert_equals_expected_or_rerecord_verification_errors()-> Stripped:
    return Stripped(f"""\
public static void assertEqualsExpectedOrRerecordVerificationErrors(List<Reporting.Error> errors, Path path) throws IOException {{
{I}if (errors.isEmpty()) {{
{II}fail("Expected at least one verification error when verifying " + path + ", but got none");
{I}}}
{I}final String got = errors.stream().map(error -> Reporting.generateJsonPath(error.getPathSegments()) + ": " + error.getCause()).collect(Collectors.joining(";\\n"));
{I}final Path errorsPath = Paths.get(path + ".errors");
{I}if (RECORD_MODE) {{
{II}Files.write(errorsPath, got.getBytes(StandardCharsets.UTF_8));
{I}}} else {{
{II}if (!Files.exists(errorsPath)) {{
{III}throw new FileNotFoundException(
{IIII}"The file with the recorded errors does not exist: " +
{IIII}errorsPath +
{IIII}"; maybe you want to set the environment variable " +
{IIII}"AAS_CORE_AAS3_0_TESTS_RECORD_MODE");
{II}}}
{II}final String expected = String.join("\\n", Files.readAllLines(errorsPath));
{II}assertEquals(expected,got,"The expected verification errors do not match the actual ones for the file " + path);
{I}}}
}}""")


def _generate_assert_equals_expected_or_rerecord_deserialization_exception()-> Stripped:
    return Stripped(f"""\
public static void assertEqualsExpectedOrRerecordDeserializationException(Xmlization.DeserializeException exception, Path path) throws IOException {{
{I}if (exception == null) {{
{II}fail("Expected a Xmlization exception when de-serializing " + path + ", but got none.");
{I}}} else {{
{II}final Path exceptionPath = Paths.get(path + ".exception");
{II}final String got = exception.getMessage();
{II}if (RECORD_MODE) {{
{III}Files.write(exceptionPath, got.getBytes(StandardCharsets.UTF_8));
{II}}} else{{
{III}if (!Files.exists(exceptionPath)) {{
{IIII}throw new FileNotFoundException(
{IIIII}"The file with the recorded exception does not exist: " +
{IIIII}exceptionPath +
{IIIII}"; maybe you want to set the environment variable " +
{IIIII}"AAS_CORE_AAS3_0_TESTS_RECORD_MODE");
{III}}}
{III}final String expected = String.join("\\n", Files.readAllLines(exceptionPath));
{III}assertEquals(expected, got, "The expected exception does not match the actual one for the file " + path);
{II}}}
{I}}}
}}""")


def main() -> int:
    """Execute the main routine."""
    this_path = pathlib.Path(os.path.realpath(__file__))
    repo_root = this_path.parent.parent.parent
    blocks = [
        _generate_find_paths(),
        _generate_find_dirs(),
        _generate_must_find(),
        _generate_assert_no_verification_errors(),
        _generate_as_list(),
        _generate_assert_equals_expected_or_rerecord_verification_errors(),
        _generate_assert_equals_expected_or_rerecord_deserialization_exception()
    ]  # type: List[str]

    writer = io.StringIO()
    writer.write(
        f"""\
/*
 * This code has been automatically generated by testgen.
 * Do NOT edit or append.
 */

import aas_core.aas3_0.reporting.Reporting;
import aas_core.aas3_0.xmlization.Xmlization;
import aas_core.aas3_0.types.model.IClass;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
/**
* Provide methods for testing.
*/
public class Common{{
{I}public static final boolean RECORD_MODE = System.getenv("AAS_CORE_AAS3_0_TESTS_RECORD_MODE") != null && System.getenv("AAS_CORE_AAS3_0_TESTS_RECORD_MODE")
{II}.equalsIgnoreCase("true");

{I}public static String TEST_DATA_DIR = Paths.get("test_data").toAbsolutePath().toString();

{I}public static final List<String> CAUSES_FOR_VERIFICATION_FAILURE =
{II}Collections.unmodifiableList(Arrays.asList(
{III}"DateTimeStampUtcViolationOnFebruary29th",
{III}"MaxLengthViolation",
{III}"MinLengthViolation",
{III}"PatternViolation",
{III}"InvalidValueExample",
{III}"InvalidMinMaxExample",
{III}"SetViolation",
{III}"ConstraintViolation"));

"""
    )

    for i, block in enumerate(blocks):
        if i > 0:
            writer.write("\n\n")

        writer.write(textwrap.indent(block, I))

    writer.write(
        """
}  // class Common


/*
 * This code has been automatically generated by testgen.
 * Do NOT edit or append.
 */
"""
    )

    target_pth = repo_root / "src/test/java/Common.java"
    target_pth.write_text(writer.getvalue(), encoding="utf-8")

    return 0


if __name__ == "__main__":
    sys.exit(main())
