"""Generate the test code for the JSON de/serialization of classes."""

import io
import os
import pathlib
import sys
import textwrap
from typing import List

import aas_core_codegen
import aas_core_codegen.common
import aas_core_codegen.java.naming
import aas_core_codegen.naming
import aas_core_codegen.parse
import aas_core_codegen.run
from aas_core_codegen import intermediate
from aas_core_codegen.common import Stripped
from aas_core_codegen.java import common as java_common
from icontract import require

import test_codegen.common
from test_codegen import test_data_io
from aas_core_codegen.java.common import (
    INDENT as I,
    INDENT2 as II,
    INDENT3 as III,
    INDENT4 as IIII,
    INDENT5 as IIIII,
    INDENT6 as IIIIII,
)

def _generate_for_self_contained(
    cls_name_java: str,
    cls_name_json: str,
) -> List[Stripped]:
    """Generate the tests for a self-contained class."""
    # noinspection PyListCreation
    blocks = []  # type: List[Stripped]

    blocks.append(
        Stripped(
            f"""\
@Test
public void test{cls_name_java}Ok() throws IOException {{
{I}final Path searchPath = Paths.get(Common.TEST_DATA_DIR,
{II}"Json",
{II}"SelfContained",
{II}"Expected",
{II}"{cls_name_java}");
{I}final List<Path> paths = Common.findPaths(searchPath, ".json");
{I}
{I}for (Path path : paths) {{
{II}final JsonNode node = mapper.readTree(path.toFile());
{II}final {cls_name_java} instance = Jsonization.Deserialize.deserialize{cls_name_java}(node);
{II}final Iterable<Reporting.Error> errors = Verification.verify(instance);
{II}final List<Reporting.Error> errorList = Common.asList(errors);
{II}Common.assertNoVerificationErrors(errorList, path);
{II}assertSerializeDeserializeEqualsOriginal(node, instance, path);
{I}}}
}}  // public void test{cls_name_java}Ok"""
        )
    )

    blocks.append(
        Stripped(
            f"""\
@Test
public void test{cls_name_java}DeserializationFail() throws IOException {{
{I}for (Path causeDir : Common.findDirs(Paths.get(Common.TEST_DATA_DIR,
{II}"Json",
{II}"SelfContained",
{II}"Unexpected",
{II}"Unserializable"))) {{
{II}final Path clsDir = causeDir.resolve({java_common.string_literal(cls_name_json)});
{II}if (!Files.exists(clsDir)) {{
{III}// No examples of {cls_name_java} for the failure cause.
{III}continue;
{II}}}
{II}final List<Path> paths = Common.findPaths(clsDir, ".json");
{II}for (Path path : paths) {{
{III}final JsonNode node = mapper.readTree(path.toFile());
{III}Jsonization.DeserializeException exception = null;
{III}try {{
{IIII}Jsonization.Deserialize.deserialize{cls_name_java}(node);
{III}}} catch (Jsonization.DeserializeException observedException) {{
{IIII}exception = observedException;
{III}}}
{III}assertEqualsExpectedOrRerecordDeserializationException(exception, path);
{II}}}
{I}}}
}}  // public void test{cls_name_java}DeserializationFail"""
        )
    )

    blocks.append(
        Stripped(
            f"""\
@Test
public void test{cls_name_java}VerificationFail() throws IOException {{
{I}for (Path causeDir : Common.findDirs(Paths.get(Common.TEST_DATA_DIR,
{II}"Json",
{II}"SelfContained",
{II}"Unexpected",
{II}"Invalid"))) {{
{II}final Path clsDir = causeDir.resolve({java_common.string_literal(cls_name_json)});
{II}if (!Files.exists(clsDir)) {{
{III}// No examples of {cls_name_java} for the failure cause.
{III}continue;
{II}}}
{II}final List<Path> paths = Common.findPaths(clsDir, ".json");
{II}for (Path path : paths) {{
{III}final JsonNode node = mapper.readTree(path.toFile());
{III}final {cls_name_java} instance = Jsonization.Deserialize.deserialize{cls_name_java}(node);
{III}final Iterable<Reporting.Error> errors = Verification.verify(instance);
{III}final List<Reporting.Error> errorList = Common.asList(errors);
{III}Common.assertEqualsExpectedOrRerecordVerificationErrors(errorList, path);
{II}}}
{I}}}
}}  // public void test{cls_name_java}VerificationFail"""
        )
    )

    return blocks



def _generate_for_contained_in_environment(
    cls_name_java: str,
    cls_name_json: str,
) -> List[Stripped]:
    """Generate the tests for a class contained in an ``Environment`` instance."""
    # noinspection PyListCreation
    blocks = []  # type: List[Stripped]

    blocks.append(
        Stripped(
            f"""\
@Test
public void test{cls_name_java}Ok() throws IOException {{
{I}final Path searchPath = Paths.get(Common.TEST_DATA_DIR,
{II}"Json",
{II}"ContainedInEnvironment",
{II}"Expected",
{II}{java_common.string_literal(cls_name_json)});
{I}final List<Path> paths = Common.findPaths(searchPath, ".json");
{I}for (Path path : paths) {{
{II}testRoundTrip(path);
{I}}}
}}  // public void test{cls_name_java}Ok"""
        )
    )

    blocks.append(
        Stripped(
            f"""\
@Test
public void test{cls_name_java}DeserializationFromNonObjectFail() {{
{I}final JsonNode node = null;
{I}Jsonization.DeserializeException exception = null;
{I}try {{
{II}Jsonization.Deserialize.deserialize{cls_name_java}(node);
{I}}} catch (Jsonization.DeserializeException observedException) {{
{II}exception = observedException;
{I}}}
{I}if (exception == null) {{
{II}fail("Expected an exception, but got none");
{I}}}
{I}if (!exception.getMessage().startsWith("Expected a JsonObject, but got ")) {{
{II}fail("Unexpected exception message: " + exception.getMessage());
{I}}}
}}  // public void test{cls_name_java}DeserializationFromNonObjectFail"""
        )
    )

    blocks.append(
        Stripped(
            f"""\
@Test
public void test{cls_name_java}DeserializationFail() throws IOException {{
{I}for (Path causeDir : Common.findDirs(Paths.get(Common.TEST_DATA_DIR,
{II}"Json",
{III}"ContainedInEnvironment",
{II}"Unexpected",
{II}"Unserializable"))) {{
{II}final Path clsDir = causeDir.resolve({java_common.string_literal(cls_name_json)});
{II}if (!Files.exists(clsDir)) {{
{III}// No examples of {cls_name_java} for the failure cause.
{III}continue;
{II}}}
{II}final List<Path> paths = Common.findPaths(clsDir, ".json");
{II}for (Path path : paths) {{
{III}testDeserializationFail(path);
{II}}}
{I}}}
}}  // public void Test_{cls_name_java}_deserialization_fail"""
        )
    )

    blocks.append(
        Stripped(
            f"""\
@Test
public void test{cls_name_java}VerificationFail() throws IOException {{
{I}for (Path causeDir : Common.findDirs(Paths.get(Common.TEST_DATA_DIR,
{II}"Json",
{III}"ContainedInEnvironment",
{II}"Unexpected",
{II}"Invalid"))) {{
{II}final Path clsDir = causeDir.resolve({java_common.string_literal(cls_name_json)});
{II}if (!Files.exists(clsDir)) {{
{III}// No examples of {cls_name_java} for the failure cause.
{III}continue;
{II}}}
{II}final List<Path> paths = Common.findPaths(clsDir, ".json");
{II}for (Path path : paths) {{
{III}final JsonNode node = mapper.readTree(path.toFile());
{III}testVerificationFail(path);
{II}}}
{I}}}
}}  // public void Test_{cls_name_java}_verification_fail"""
        )
    )

    return blocks
def _generate_assert_serialize_deserialize_equals_original()-> Stripped:
    return Stripped(f"""\
private static void assertSerializeDeserializeEqualsOriginal(JsonNode originalNode, IClass instance, Path path) throws JsonProcessingException {{
{I}JsonNode serialized = null;
{I}try {{
{II}serialized = Jsonization.Serialize.toJsonObject(instance);
{I}}} catch (Exception exception) {{
{II}fail("Expected no exception upon serialization of an instance " +
{III}"de-serialized from " + path + ", but got: " + exception);
{I}}}
{I}
{I}if (serialized == null) {{
{II}fail("Unexpected null serialization of an instance from " + path);
{I}}}
{I}assertEquals(mapper.readTree(originalNode.toString()), mapper.readTree(serialized.toString()));
}}""")

@require(lambda container_cls_java: container_cls_java == "Environment")
def _generate_test_round_trip( container_cls_java: str)-> Stripped:
    return Stripped(f"""\
private static void testRoundTrip(Path path) throws IOException {{
{I}final JsonNode node = mapper.readTree(path.toFile());
{I}final {container_cls_java} instance = Jsonization.Deserialize.deserializeEnvironment(node);
{I}final Iterable<Reporting.Error> errors = Verification.verify(instance);
{I}final List<Reporting.Error> errorList = Common.asList(errors);
{I}Common.assertNoVerificationErrors(errorList, path);
{I}assertSerializeDeserializeEqualsOriginal(node, instance, path);
}}""")


def _generate_test_deserialization_fail()-> Stripped:
    return Stripped(f"""\
private static void testDeserializationFail(Path path) throws IOException {{
{I}final JsonNode node = mapper.readTree(path.toFile());
{I}Jsonization.DeserializeException exception = null;
{I}try {{
{II}Jsonization.Deserialize.deserializeEnvironment(node);
{I}}} catch (Jsonization.DeserializeException observedException) {{
{II}exception = observedException;
{I}}}
{I}assertEqualsExpectedOrRerecordDeserializationException(exception, path);
}}""")

def _generate_test_verification_fail()-> Stripped:
    return Stripped(f"""\
private static void testVerificationFail(Path path) throws IOException {{
{I}final JsonNode node = mapper.readTree(path.toFile());
{I}final Environment instance = Jsonization.Deserialize.deserializeEnvironment(node);
{I}final Iterable<Reporting.Error> errors = Verification.verify(instance);
{I}final List<Reporting.Error> errorList = Common.asList(errors);
{I}Common.assertEqualsExpectedOrRerecordVerificationErrors(errorList, path);
}}""")

def _generate_assert_equals_expected_or_rerecord_deserialization_exception()-> Stripped:
    return Stripped(f"""\
private static void assertEqualsExpectedOrRerecordDeserializationException(Jsonization.DeserializeException exception, Path path) throws IOException {{
{I}if (exception == null) {{
{II}fail("Expected a Jsonization exception when de-serializing " + path + ", but got none.");
{I}}} else {{
{II}final Path exceptionPath = Paths.get(path + ".exception");
{II}final String got = exception.getMessage();
{II}if (Common.RECORD_MODE) {{
{III}Files.write(exceptionPath, got.getBytes(StandardCharsets.UTF_8));
{II}}} else {{
{III}if (!Files.exists(exceptionPath)) {{
{IIII}throw new FileNotFoundException(
{IIIII}"The file with the recorded errors does not exist: " +
{IIIII}exceptionPath +
{IIIII}"; maybe you want to set the environment variable " +
{IIIII}"AAS_CORE_AAS3_0_TESTS_RECORD_MODE");
{III}}}
{III}final String expected = Files.readAllLines(exceptionPath).stream().collect(Collectors.joining("\\n"));
{III}assertEquals(expected, got, "The expected exception does not match the actual one for the file " + path);
{II}}}
{I}}}
}}""")


def main() -> int:
    """Execute the main routine."""
    symbol_table = test_codegen.common.load_symbol_table()

    this_path = pathlib.Path(os.path.realpath(__file__))
    repo_root = this_path.parent.parent.parent

    test_data_dir = repo_root / "test_data"
    environment_cls = symbol_table.must_find_concrete_class(
        aas_core_codegen.common.Identifier("Environment")
    )
    # noinspection PyListCreation
    blocks = [
        _generate_assert_serialize_deserialize_equals_original(),
        _generate_test_round_trip(environment_cls.name),
        _generate_test_verification_fail(),
        _generate_test_deserialization_fail(),
        _generate_assert_equals_expected_or_rerecord_deserialization_exception()
    ]  # type: List[str]

    for our_type in symbol_table.our_types:
        if not isinstance(our_type, intermediate.ConcreteClass):
            continue

        container_cls = test_data_io.determine_container_class(
            cls=our_type, test_data_dir=test_data_dir, environment_cls=environment_cls
        )

        cls_name_java = aas_core_codegen.java.naming.class_name(our_type.name)
        cls_name_json = aas_core_codegen.naming.json_model_type(our_type.name)

        if container_cls is our_type:
            blocks.extend(
                _generate_for_self_contained(
                    cls_name_java=cls_name_java, cls_name_json=cls_name_json
                )
            )
        else:
            blocks.extend(
                _generate_for_contained_in_environment(
                    cls_name_java=cls_name_java,
                    cls_name_json=cls_name_json,
                )
            )

    writer = io.StringIO()
    writer.write(
        f"""\
/*
 * This code has been automatically generated by testgen.
 * Do NOT edit or append.
 */
 
import aas_core.aas3_0.jsonization.Jsonization;
import aas_core.aas3_0.reporting.Reporting;
import aas_core.aas3_0.types.impl.*;
import aas_core.aas3_0.types.model.IClass;
import aas_core.aas3_0.verification.Verification;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestJsonizationOfConcreteClasses {{

{I}final static ObjectMapper mapper = new ObjectMapper();

"""
    )

    for i, block in enumerate(blocks):
        if i > 0:
            writer.write("\n\n")

        writer.write(textwrap.indent(block, I))

    writer.write(
        """
}  // class TestJsonizationOfConcreteClasses

/*
 * This code has been automatically generated by testgen.
 * Do NOT edit or append.
 */
"""
    )

    target_pth = (
        repo_root / "src/test/java/TestJsonizationOfConcreteClasses.java"
    )
    target_pth.write_text(writer.getvalue(), encoding="utf-8")

    return 0


if __name__ == "__main__":
    sys.exit(main())
