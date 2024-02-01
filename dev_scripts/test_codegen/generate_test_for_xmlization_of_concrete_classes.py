"""Generate the test code for the de/serialization of instances in XML."""

import io
import os
import pathlib
import sys
import textwrap
from typing import List

import aas_core_codegen
import aas_core_codegen.common
import aas_core_codegen.csharp.naming
import aas_core_codegen.java.naming
import aas_core_codegen.naming
import aas_core_codegen.parse
import aas_core_codegen.run
from aas_core_codegen import intermediate
from aas_core_codegen.common import Stripped
from aas_core_codegen.java import common as java_common
from icontract import require
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

def _generate_assert_serialize_deserialize_equals_original()-> Stripped:
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
{I}final List<XMLEvent> outputList = createElementsAndContentList(outputReader);
{I}// check output for aas-name-space
{I}for (XMLEvent event : outputList) {{
{II}if (event.isStartElement()) {{
{III}assertEquals(Xmlization.AAS_NAME_SPACE, event.asStartElement().getName().getNamespaceURI());
{II}}}
{I}}}
        
{I}final XMLEventReader expectedReader = xmlInputFactory.createXMLEventReader(Files.newInputStream(Paths.get(path)));
{I}final List<XMLEvent> expectedList = createElementsAndContentList(expectedReader);

{I}if(expectedList.size() != outputList.size()){{
{II}fail("Mismatch in element size expected " + expectedList.size() + " but got " + outputList.size());
{I}}}

{I}for(int i = 0; i < expectedList.size();i++){{
{II}final Optional<Reporting.Error> inequalityError = checkElementsEqual(expectedList.get(i), outputList.get(i));
{III}inequalityError.ifPresent(error -> fail(
{IIII}"The original XML from " + path + " is unequal the serialized XML: " + error.getCause()
{III}));
{I}}}
}}"""
        )



def _generate_create_elements_and_content_list() -> Stripped:
    """Generate the method create list of xml elements and content."""
    return Stripped(
            f"""\
private static List<XMLEvent> createElementsAndContentList(XMLEventReader expectedReader) throws XMLStreamException {{
{I}final List<XMLEvent> result = new ArrayList<>();

{I}while (expectedReader.hasNext()) {{
{II}final XMLEvent event = expectedReader.nextEvent();
{II}if (event.isStartElement() || event.isEndElement() || (event.isCharacters() && !event.asCharacters().isWhiteSpace())) {{
{III}result.add(event);
{II}}}
{I}}}
{I}return result;
}}"""
        )

def _generate_check_elements_equal() -> Stripped:
    """Generate the method for checking if elements are equal."""
    return Stripped(
            f"""\
public static Optional<Reporting.Error> checkElementsEqual(XMLEvent expected, XMLEvent got) {{

{I}switch (expected.getEventType()){{
{II}case XMLStreamConstants.START_ELEMENT:{{
{III}final String expectedName = expected.asStartElement().getName().getLocalPart();
{III}final String gotName = got.asStartElement().getName().getLocalPart();
{III}if(!expectedName.equals(gotName)){{
{IIII}final Reporting.Error error = new Reporting.Error("Mismatch in element names: "
{IIII}+ expectedName + " != " + gotName);
{IIII}return Optional.of(error);
{III}}}
{III}return Optional.empty();
{II}}}
{II}case XMLStreamConstants.END_ELEMENT:{{
{III}final String expectedName = expected.asEndElement().getName().getLocalPart();
{III}final String gotName = got.asEndElement().getName().getLocalPart();
{III}if(!expectedName.equals(gotName)){{
{IIII}final Reporting.Error error = new Reporting.Error("Mismatch in element names: "
{IIII}+ expectedName + " != " + gotName);
{IIII}return Optional.of(error);
{III}}}
{III}return Optional.empty();
{II}}}
{II}case XMLStreamConstants.CHARACTERS:{{
{III}final String expectedContent = expected.asCharacters().getData();
{III}final String gotContent = got.asCharacters().getData();
{III}if(!expectedContent.equals(gotContent)){{
{IIII}final Reporting.Error error = new Reporting.Error("Mismatch in element contents: "
{IIII}+ expectedContent + " != " + gotContent);
{IIII}return Optional.of(error);
{III}}}
{III}return Optional.empty();
{II}}}
{II}default:
{III}throw new IllegalStateException("Unexpected event type in check elements equal.");
{I}}}
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

{I}final Path searchPath = Paths.get(TestUtil.TEST_DATA_DIR,
{II}"Xml",
{II}"SelfContained",
{II}"Expected",
{II}{java_common.string_literal(cls_name_xml)});
{II}final List<String> paths = TestUtil.findFiles(searchPath, ".xml");
{II}for (String path : paths) {{
{III}final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
{III}final XMLEventReader xmlReader = xmlInputFactory.createXMLEventReader(Files.newInputStream(Paths.get(path)));
{III}final {cls_name_java} instance = Xmlization.Deserialize.deserialize{cls_name_java}(xmlReader);
{III}final Iterable<Reporting.Error> errors = Verification.verify(instance);
{III}final List<Reporting.Error> errorList = TestUtil.asList(errors);
{III}TestUtil.assertNoVerificationErrors(errorList, path);
{III}assertSerializeDeserializeEqualsOriginal(instance, path);
{II}}}

}}"""
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
        _generate_create_elements_and_content_list(),
        _generate_check_elements_equal()
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

        cls_name_java = aas_core_codegen.java.naming.class_name(our_type.name)
        cls_name_xml = aas_core_codegen.naming.xml_class_name(our_type.name)

        if container_cls is our_type:
            blocks.extend(
                _generate_for_self_contained(
                    cls_name_java=cls_name_java, cls_name_xml=cls_name_xml
                )
            )
        # else:
            # blocks.extend(
            #     _generate_for_contained_in_environment(
            #         cls_name_csharp=cls_name_csharp,
            #         cls_name_xml=cls_name_xml,
            #         container_cls_csharp=container_cls_csharp,
            #     )
            # )

    writer = io.StringIO()
    writer.write(
        """\
/*
 * This code has been automatically generated by testgen.
 * Do NOT edit or append.
 */

import aas_core.aas3_0.reporting.Reporting;
import javax.annotation.Generated;
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
import java.util.ArrayList;
import java.util.List;
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
