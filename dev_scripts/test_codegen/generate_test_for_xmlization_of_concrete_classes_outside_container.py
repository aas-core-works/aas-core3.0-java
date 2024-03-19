"""Generate the test code for the xmlization of classes outside a container."""

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

import test_codegen.common
from test_codegen import test_data_io
from aas_core_codegen.java.common import (
    INDENT as I
)


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
    blocks = []  # type: List[str]

    for our_type in symbol_table.our_types:
        if not isinstance(our_type, intermediate.ConcreteClass):
            continue

        container_cls = test_data_io.determine_container_class(
            cls=our_type, test_data_dir=test_data_dir, environment_cls=environment_cls
        )

        if container_cls is our_type:
            # NOTE (mristin, 2022-06-27):
            # These classes are tested already in TestXmlizationOfConcreteClasses.
            # We only need to test for class instances contained in a container.
            continue

        cls_name_java = aas_core_codegen.java.naming.class_name(our_type.name)

        blocks.append(
            Stripped(
                f"""\
@Test
public void testRoundTrip{cls_name_java}() throws IOException, XMLStreamException {{
{I}// We load from JSON here just to jump-start the round trip.
{I}// The round-trip goes then over XML.
{I}final {cls_name_java} instance = CommonJsonization.loadMaximal{cls_name_java}();
{I}
{I}final StringWriter stringOut = new StringWriter();
{I}final XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
{I}final XMLStreamWriter xmlWriter = outputFactory.createXMLStreamWriter(stringOut);
{I}
{I}Xmlization.Serialize.to(instance, xmlWriter);
{I}// De-serialize from XML
{I}final String outputText = stringOut.toString();
{I}final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
{I}final XMLEventReader xmlReader = xmlInputFactory.createXMLEventReader(new StringReader(outputText));
{I}final {cls_name_java}  anotherInstance = Xmlization.Deserialize.deserialize{cls_name_java} (xmlReader);
{I}// Serialize back to XML
{I}final StringWriter anotherStringOut = new StringWriter();
{I}final XMLStreamWriter anotherXmlWriter = outputFactory.createXMLStreamWriter(anotherStringOut);
{I}Xmlization.Serialize.to(anotherInstance, anotherXmlWriter);
{I}assertEquals(outputText, anotherStringOut.toString());
}}  // public void testRoundTrip{cls_name_java}"""
            )
        )

    writer = io.StringIO()
    writer.write(
        """\
/*
 * This code has been automatically generated by testgen.
 * Do NOT edit or append.
 */

import javax.annotation.Generated;
import javax.xml.stream.*;

import aas_core.aas3_0.types.impl.*;
import aas_core.aas3_0.xmlization.Xmlization;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;


/** 
* Test de/serialization of classes contained in a container <i>outside</i>
* of that container.
* This is necessary so that we also test the methods that directly de/serialize
* an instance in rare use cases where it does not reside within a container such
* as {@link Environment}.
*/
@Generated("Generated by aas-test-gen")
public class TestXmlizationOfConcreteClassesOutsideContainer {
"""
    )

    for i, block in enumerate(blocks):
        if i > 0:
            writer.write("\n\n")

        writer.write(textwrap.indent(block, "        "))

    writer.write(
        """
}  // class TestXmlizationOfConcreteClassesOutsideContainer

/*
 * This code has been automatically generated by testgen.
 * Do NOT edit or append.
 */
"""
    )

    target_pth = (
        repo_root / "src/test/java/TestXmlizationOfConcreteClassesOutsideContainer.java"
    )
    target_pth.write_text(writer.getvalue(), encoding="utf-8")

    return 0


if __name__ == "__main__":
    sys.exit(main())