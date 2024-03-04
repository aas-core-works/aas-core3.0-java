"""Generate the test code for the XML de/serialization of interfaces."""

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

from test_codegen.common import load_symbol_table
from aas_core_codegen.java.common import (
    INDENT as I
)


def main() -> int:
    """Execute the main routine."""
    symbol_table = load_symbol_table()

    # noinspection PyListCreation
    blocks = []  # type: List[str]

    for our_type in symbol_table.our_types:
        if not isinstance(our_type, intermediate.Class):
            continue

        if our_type.interface is None or len(our_type.interface.implementers) == 0:
            continue

        if our_type.name == aas_core_codegen.common.Identifier("Event_payload"):
            # NOTE (mristin, 2022-06-21):
            # Event payload is a dangling class and can not be reached from
            # the environment. Hence, we skip it.
            continue

        for cls in our_type.interface.implementers:
            if cls.serialization is None or not cls.serialization.with_model_type:
                continue

            interface_name_java = aas_core_codegen.java.naming.interface_name(
                our_type.interface.name
            )

            cls_name_java = aas_core_codegen.java.naming.class_name(cls.name)

            blocks.append(
                Stripped(
                    f"""\
@Test
public void testRoundTrip{interface_name_java}From{cls_name_java}() throws IOException, XMLStreamException {{
{I}// We load from JSON here just to jump-start the round trip.
{I}// The round-trip goes then over XML.
{I}final {cls_name_java} instance = CommonJsonization.loadMaximal{cls_name_java}();
{I}final StringWriter stringOut = new StringWriter();
{I}final XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
{I}final XMLStreamWriter xmlWriter = outputFactory.createXMLStreamWriter(stringOut);
{I}
{I}Xmlization.Serialize.to(instance, xmlWriter);
{I}// De-serialize from XML
{I}final String outputText = stringOut.toString();
{I}final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
{I}final XMLEventReader xmlReader = xmlInputFactory.createXMLEventReader(new StringReader(outputText));
{I}final {interface_name_java} anotherInstance = Xmlization.Deserialize.deserialize{interface_name_java}(xmlReader);
{I}// Serialize back to XML
{I}final StringWriter anotherStringOut = new StringWriter();
{I}final XMLStreamWriter anotherXmlWriter = outputFactory.createXMLStreamWriter(anotherStringOut);
{I}
{I}Xmlization.Serialize.to(anotherInstance, anotherXmlWriter);
{I}assertEquals(outputText, anotherStringOut.toString());
}}  // void testRoundTrip{interface_name_java}From{cls_name_java}"""
                )
            )

    writer = io.StringIO()
    writer.write(
        """\
/*
 * This code has been automatically generated by testgen.
 * Do NOT edit or append.
 */

import aas_core.aas3_0.types.impl.*;
import aas_core.aas3_0.types.model.*;
import aas_core.aas3_0.xmlization.Xmlization;
import org.junit.jupiter.api.Test;
import javax.annotation.Generated;
import javax.xml.stream.*;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Generated("Generated by aas-test-gen")
public class TestXmlizationOfInterfaces {
"""
    )

    for i, block in enumerate(blocks):
        if i > 0:
            writer.write("\n\n")

        writer.write(textwrap.indent(block, "        "))

    writer.write(
        """
}  // class TestXmlizationOfInterfaces

/*
 * This code has been automatically generated by testgen.
 * Do NOT edit or append.
 */
"""
    )

    this_path = pathlib.Path(os.path.realpath(__file__))
    repo_root = this_path.parent.parent.parent

    target_pth = repo_root / "src/test/java/TestXmlizationOfInterfaces.java"
    target_pth.write_text(writer.getvalue(), encoding="utf-8")

    return 0


if __name__ == "__main__":
    sys.exit(main())
