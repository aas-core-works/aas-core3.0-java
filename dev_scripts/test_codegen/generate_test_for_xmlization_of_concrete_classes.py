"""Generate the test code for the de/serialization of instances in XML."""

import io
import os
import pathlib
import sys
import textwrap
from typing import List

import aas_core_codegen
import aas_core_codegen.csharp.naming
import aas_core_codegen.java.naming
import aas_core_codegen.naming
import aas_core_codegen.parse
import aas_core_codegen.run
from aas_core_codegen import intermediate
from aas_core_codegen.java import common as java_common
from icontract import require
from aas_core_codegen.common import (
    Identifier,
    Stripped,
)
from aas_core_codegen.java.common import (
    INDENT as I,
    INDENT2 as II,
    INDENT3 as III,
    INDENT4 as IIII,
    INDENT5 as IIIII,
    INDENT6 as IIIIII,
)

import test_codegen.common
from test_codegen import test_data_io


def _generate_assert_serialize_deserialize_equals_original() -> Stripped:
    """Generate the method to check serialize and deserialize matches original."""
    return Stripped(
        f"""\
private static void assertSerializeDeserializeEqualsOriginal(IClass instance, String path) throws XMLStreamException, IOException {{

{I}final StringWriter stringOut = new StringWriter();
{I}final XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
{I}final XMLStreamWriter xmlStreamWriter = outputFactory.createXMLStreamWriter(stringOut);

{I}Xmlization.Serialize.to(instance, xmlStreamWriter);

{I}final String outputText = stringOut.toString();

{I}// Compare expected == output
{I}final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
{I}final XMLEventReader outputReader = xmlInputFactory.createXMLEventReader(new StringReader(outputText));
{I}final Map<XMLEvent, String> outputMap = buildElementsMap(outputReader);

{I}// check output for aas-name-space
{I}for (XMLEvent event : outputMap.keySet()) {{
{II}if (event.isStartElement()) {{
{III}assertEquals(Xmlization.AAS_NAME_SPACE, event.asStartElement().getName().getNamespaceURI());
{II}}}
{II}if (event.isEndElement()) {{
{III}assertEquals(Xmlization.AAS_NAME_SPACE, event.asEndElement().getName().getNamespaceURI());
{II}}}
{I}}}

{I}final XMLEventReader expectedReader = xmlInputFactory.createXMLEventReader(Files.newInputStream(Paths.get(path)));
{I}final Map<XMLEvent, String> expectedMap = buildElementsMap(expectedReader);


{I}if (expectedMap.size() != outputMap.size()) {{
{II}fail("Mismatch in element size expected " + expectedMap.size() + " but got " + outputMap.size());
{I}}}

{I}expectedMap.forEach((xmlEvent, content) -> {{
{II}final Optional<Reporting.Error> inequalityError = checkElementsEqual(xmlEvent, content, outputMap);
{III}inequalityError.ifPresent(error -> fail(
{IIII}"The original XML from " + path + " is unequal the serialized XML: " + error.getCause()
{II}));
{I}}});

}}"""
    )


def _generate_build_elements_map() -> Stripped:
    """Generate the method create map of xml elements and its content."""
    return Stripped(
        f"""\
private static Map<XMLEvent, String> buildElementsMap(XMLEventReader reader) throws XMLStreamException {{
{I}final Map<XMLEvent, String> result = new LinkedHashMap<>();
{I}while (reader.hasNext()) {{
{II}final XMLEvent current = reader.nextEvent();
{II}if (current.isStartElement()) {{
{III}result.put(current, readContent(reader));
{II}}} else if (current.isEndElement()) {{
{III}result.put(current, "");
{II}}}
{I}}}
{I}return result;
}}"""
    )

def _generate_read_content() -> Stripped:
    """Generate the method for reading xml content."""
    return Stripped(
        f"""\
private static String readContent(XMLEventReader reader) throws XMLStreamException {{
{I}final StringBuilder content = new StringBuilder();
{I}
{I}while (reader.hasNext() && reader.peek().isCharacters() 
{III}&& !reader.peek().asCharacters().isWhiteSpace() 
{III}|| reader.peek().getEventType() == XMLStreamConstants.COMMENT) {{

{II}if (reader.peek().isCharacters()) {{
{III}content.append(reader.peek().asCharacters().getData());
{II}}}
{II}reader.nextEvent();
{I}}}
{I}return content.toString();
}}"""
    )


def _generate_check_elements_equal() -> Stripped:
    """Generate the method for checking if elements are equal."""
    return Stripped(
        f"""\
public static Optional<Reporting.Error> checkElementsEqual(XMLEvent expected, String expectedContent, Map<XMLEvent, String> outputMap) {{

{I}switch (expected.getEventType()) {{
{II}case XMLStreamConstants.START_ELEMENT: {{
{III}final String expectedName = expected.asStartElement()
{IIII}.getName()
{IIII}.getLocalPart();
{III}final Optional<Map.Entry<XMLEvent, String>> got = outputMap
{IIII}.entrySet()
{IIII}.stream()
{IIII}.filter(entry -> entry.getKey().isStartElement() && entry.getKey().asStartElement().getName().getLocalPart().equals(expectedName))
{IIII}.filter(entry -> entry.getValue().equals(expectedContent))
{IIII}.findAny();
{III}if (!got.isPresent()) {{
{IIII}final Reporting.Error error = new Reporting.Error(
{IIIII}"Missing start element " + expectedName + " in with content: " + expectedContent);
{IIII}return Optional.of(error);
{III}}}
{III}outputMap.remove(got.get().getKey());
{III}return Optional.empty();
{II}}}
{II}case XMLStreamConstants.END_ELEMENT: {{
{III}final String expectedName = expected.asEndElement()
{IIII}.getName()
{IIII}.getLocalPart();
{III}final Optional<Map.Entry<XMLEvent, String>> got = outputMap
{IIII}.entrySet()
{IIII}.stream()
{IIII}.filter(entry -> entry.getKey().isEndElement() && entry.getKey().asEndElement().getName().getLocalPart().equals(expectedName))
{IIII}.findAny();
{III}if (!got.isPresent()){{
{IIII}final Reporting.Error error = new Reporting.Error(
{IIIII}"Missing end element " + expectedName);
{IIII}return Optional.of(error);
{III}}}
{III}outputMap.remove(got.get().getKey());
{III}return Optional.empty();
{II}}}
{II}default:
{III}throw new IllegalStateException("Unexpected event type in check elements equal.");
{II}}}
}}"""
    )

def _generate_test_round_trip() -> Stripped:
    """Generate the method for testing the deserialize -> serialize round trip."""
    return Stripped(
        f"""\
private static void testRoundTrip(String path) throws XMLStreamException, IOException {{
{I}final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
{I}final XMLEventReader xmlReader = xmlInputFactory.createXMLEventReader(Files.newInputStream(Paths.get(path)));
{I}final Environment instance = Xmlization.Deserialize.deserializeEnvironment(xmlReader);
{I}final Iterable<Reporting.Error> errors = Verification.verify(instance);
{I}final List<Reporting.Error> errorList = Common.asList(errors);
{I}Common.assertNoVerificationErrors(errorList, path);
{I}assertSerializeDeserializeEqualsOriginal(instance, path);
}}"""
    )

def _generate_test_verification_fail() -> Stripped:
    """Generate the method for testing the deserialize -> serialize round trip."""
    return Stripped(
        f"""\
private static void testVerificationFail(String path) throws XMLStreamException, IOException {{
{I}final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
{I}final XMLEventReader xmlReader = xmlInputFactory.createXMLEventReader(Files.newInputStream(Paths.get(path)));
{I}final Environment instance = Xmlization.Deserialize.deserializeEnvironment(xmlReader);
{I}final Iterable<Reporting.Error> errors = Verification.verify(instance);
{I}final List<Reporting.Error> errorList = Common.asList(errors);
{I}Common.assertEqualsExpectedOrRerecordVerificationErrors(errorList, path);
}}"""
    )

def _generate_for_self_contained(
        cls_name_java: str,
        cls_name_xml: str,
) -> List[Stripped]:
    """Generate the tests for a self-contained class."""
    # noinspection PyListCreation
    blocks = []  # type: List[Stripped]

    blocks.append(
        Stripped(
            f"""\
@Test
public void test{cls_name_java}Ok() throws IOException, XMLStreamException {{

{I}final Path searchPath = Paths.get(Common.TEST_DATA_DIR,
{II}"Xml",
{II}"SelfContained",
{II}"Expected",
{II}{java_common.string_literal(cls_name_xml)});
{II}final List<String> paths = Common.findFiles(searchPath, ".xml");
{II}for (String path : paths) {{
{III}final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
{III}final XMLEventReader xmlReader = xmlInputFactory.createXMLEventReader(Files.newInputStream(Paths.get(path)));
{III}final {cls_name_java} instance = Xmlization.Deserialize.deserialize{cls_name_java}(xmlReader);
{III}final Iterable<Reporting.Error> errors = Verification.verify(instance);
{III}final List<Reporting.Error> errorList = Common.asList(errors);
{III}Common.assertNoVerificationErrors(errorList, path);
{III}assertSerializeDeserializeEqualsOriginal(instance, path);
{II}}}
}}  // public void test{cls_name_java}Ok"""
        )
    )

    blocks.append(
        Stripped(
            f"""\
@Test
public void test{cls_name_java}DeserializationFail() throws IOException, XMLStreamException {{

{I}for (String cause : Common.CAUSES_DESERIALIZATION_FAILURE) {{
{II}final Path searchPath = Paths.get(Common.TEST_DATA_DIR,
{III}"Xml",
{III}"SelfContained",
{III}"Unexpected",
{III}cause,
{III}{java_common.string_literal(cls_name_xml)});
{II}if (!Files.exists(searchPath)) {{
{III}// No examples of Environment for the failure cause.
{III}continue;
{I}}}
{II}final List<String> paths = Common.findFiles(searchPath, ".xml");
{II}for (String path : paths) {{
{III}final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
{III}final XMLEventReader xmlReader = xmlInputFactory.createXMLEventReader(Files.newInputStream(Paths.get(path)));
{III}Xmlization.DeserializeException exception = null;
{III}try{{
{IIII}Xmlization.Deserialize.deserialize{cls_name_java}(xmlReader);
{III}}}catch (Xmlization.DeserializeException observedException){{
{IIII}exception = observedException;
{III}}}
{III}Common.assertEqualsExpectedOrRerecordDeserializationException(exception,path);
{II}}}
{I}}}
}}  // public void test{cls_name_java}DeserializationFail"""
        )
    )

    blocks.append(
        Stripped(
            f"""\
@Test
public void test{cls_name_java}VerificationFail() throws IOException, XMLStreamException {{
{I}for (String cause : Common.CAUSES_FOR_VERIFICATION_FAILURE) {{
{II}final Path searchPath = Paths.get(Common.TEST_DATA_DIR,
{III}"Xml",
{III}"SelfContained",
{III}"Unexpected",
{III}cause,
{III}{java_common.string_literal(cls_name_xml)});
{II}if (!Files.exists(searchPath)) {{
{III}// No examples of Environment for the failure cause.
{III}continue;
{II}}}
{II}final List<String> paths = Common.findFiles(searchPath, ".xml");
{II}for (String path : paths) {{
{III}final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
{III}final XMLEventReader xmlReader = xmlInputFactory.createXMLEventReader(Files.newInputStream(Paths.get(path)));
{III}final {cls_name_java} instance = Xmlization.Deserialize.deserialize{cls_name_java}(xmlReader);
{III}final Iterable<Reporting.Error> errors = Verification.verify(instance);
{III}final List<Reporting.Error> errorList = Common.asList(errors);
{III}Common.assertEqualsExpectedOrRerecordVerificationErrors(errorList,path);
{II}}}
{I}}}
}}  // public void test{cls_name_java}VerificationFail"""
        )
    )

    return blocks


@require(lambda container_cls_java: container_cls_java == "Environment")
def _generate_for_contained_in_environment(
        cls_name_java: str,
        cls_name_xml: str,
        container_cls_java: str,
) -> List[Stripped]:
    """Generate the tests for a class contained in an ``Environment`` instance."""
    # noinspection PyListCreation
    blocks = []  # type: List[Stripped]

    blocks.append(
        Stripped(
            f"""\
@Test
public void test{cls_name_java}Ok() throws IOException, XMLStreamException {{

{I}final Path searchPath = Paths.get(Common.TEST_DATA_DIR,
{II}"Xml",
{II}"ContainedInEnvironment",
{II}"Expected",
{II}{java_common.string_literal(cls_name_xml)});
{II}final List<String> paths = Common.findFiles(searchPath, ".xml");
{II}for (String path : paths) {{
{III}testRoundTrip(path);
{II}}}
}}  // public void test{cls_name_java}DeserializationOk"""
        )
    )

    blocks.append(
        Stripped(
            f"""\
@Test
public void test{cls_name_java}DeserializationFail() throws IOException, XMLStreamException {{

{I}for (String cause : Common.CAUSES_DESERIALIZATION_FAILURE) {{
{II}final Path searchPath = Paths.get(Common.TEST_DATA_DIR,
{III}"Xml",
{III}"ContainedInEnvironment",
{III}"Unexpected",
{III}cause,
{III}{java_common.string_literal(cls_name_xml)});
{II}if (!Files.exists(searchPath)) {{
{III}// No examples of Environment for the failure cause.
{III}continue;
{I}}}
{II}final List<String> paths = Common.findFiles(searchPath, ".xml");
{II}for (String path : paths) {{
{III}final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
{III}final XMLEventReader xmlReader = xmlInputFactory.createXMLEventReader(Files.newInputStream(Paths.get(path)));
{III}Xmlization.DeserializeException exception = null;
{III}try{{
{IIII}Xmlization.Deserialize.deserializeEnvironment(xmlReader);
{III}}}catch (Xmlization.DeserializeException observedException){{
{IIII}exception = observedException;
{III}}}
{III}Common.assertEqualsExpectedOrRerecordDeserializationException(exception,path);
{II}}}
{I}}}
}}  // public void test{cls_name_java}DeserializationFail"""
        )
    )

    blocks.append(
        Stripped(
            f"""\
@Test
public void test{cls_name_java}VerificationFail() throws IOException, XMLStreamException {{
{I}for (String cause : Common.CAUSES_FOR_VERIFICATION_FAILURE) {{
{II}final Path searchPath = Paths.get(Common.TEST_DATA_DIR,
{III}"Xml",
{III}"ContainedInEnvironment",
{III}"Unexpected",
{III}cause,
{III}{java_common.string_literal(cls_name_xml)});
{II}if (!Files.exists(searchPath)) {{
{II}// No examples of Environment for the failure cause.
{III}continue;
{II}}}
{II}final List<String> paths = Common.findFiles(searchPath, ".xml");
{II}for (String path : paths) {{
{III}testVerificationFail(path);
{II}}}
{I}}}
}}  // public void test{cls_name_java}VerificationFail"""
        )
    )
    return blocks


def main() -> int:
    """Execute the main routine."""
    symbol_table = test_codegen.common.load_symbol_table()

    this_path = pathlib.Path(os.path.realpath(__file__))
    repo_root = this_path.parent.parent.parent

    test_data_dir = repo_root / "test_data"

    # noinspection PyListCreation
    blocks = [
        _generate_assert_serialize_deserialize_equals_original(),
        _generate_build_elements_map(),
        _generate_read_content(),
        _generate_check_elements_equal(),
        _generate_test_round_trip(),
        _generate_test_verification_fail()
    ]  # type: List[str]

    xml_namespace_literal = java_common.string_literal(
        symbol_table.meta_model.xml_namespace
    )

    environment_cls = symbol_table.must_find_concrete_class(
        aas_core_codegen.common.Identifier("Environment")
    )

    for our_type in symbol_table.our_types:
        if not isinstance(our_type, intermediate.ConcreteClass):
            continue

        container_cls = test_data_io.determine_container_class(
            cls=our_type, test_data_dir=test_data_dir, environment_cls=environment_cls
        )
        container_cls_java = aas_core_codegen.java.naming.class_name(
            container_cls.name
        )

        cls_name_java = aas_core_codegen.java.naming.class_name(Identifier(f"{our_type.name}"))
        cls_name_xml = aas_core_codegen.naming.xml_class_name(our_type.name)

        if container_cls is our_type:
            blocks.extend(
                _generate_for_self_contained(
                    cls_name_java=cls_name_java, cls_name_xml=cls_name_xml
                )
            )
        else:
            blocks.extend(
                _generate_for_contained_in_environment(
                    cls_name_java=cls_name_java,
                    cls_name_xml=cls_name_xml,
                    container_cls_java=container_cls_java,
                )
            )

    writer = io.StringIO()
    writer.write(
        """\
/*
 * This code has been automatically generated by testgen.
 * Do NOT edit or append.
 */

import aas_core.aas3_0.reporting.Reporting;
import javax.annotation.Generated;

import aas_core.aas3_0.types.impl.Environment;
import aas_core.aas3_0.types.impl.EventPayload;
import aas_core.aas3_0.types.model.IClass;
import aas_core.aas3_0.verification.Verification;
import aas_core.aas3_0.xmlization.Xmlization;
import org.junit.jupiter.api.Test;

import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@Generated("Generated by aas-test-gen")
public class TestXmlizationOfConcreteClasses{
"""
    )

    for i, block in enumerate(blocks):
        if i > 0:
            writer.write("\n\n")

        writer.write(textwrap.indent(block, "    "))

    writer.write(
        """
}     // class TestXmlizationOfConcreteClasses


/*
 * This code has been automatically generated by test-gen.
 * Do NOT edit or append.
 */
"""
    )

    target_pth = (
            repo_root / "/home/mboehm/IdeaProjects/TestGen/src/test/java/TestXmlizationOfConcreteClasses.java"
    )
    target_pth.write_text(writer.getvalue(), encoding="utf-8")

    return 0


if __name__ == "__main__":
    sys.exit(main())
