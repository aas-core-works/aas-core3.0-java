"""Generate the test code for the ``XOrDefault`` methods."""

import io
import os
import pathlib
import sys
import textwrap
from typing import List, Optional

import aas_core_codegen
import aas_core_codegen.common
import aas_core_codegen.java.naming
import aas_core_codegen.naming
import aas_core_codegen.parse
import aas_core_codegen.run
from aas_core_codegen import intermediate
from aas_core_codegen.common import Stripped
from aas_core_codegen.java import common as java_common

from test_codegen.common import load_symbol_table
from aas_core_codegen.java.common import (
    INDENT as I,
    INDENT2 as II,
    INDENT3 as III
)

def main() -> int:
    """Execute the main routine."""
    symbol_table = load_symbol_table()

    # noinspection PyListCreation
    blocks = []  # type: List[str]

    blocks.append(
        Stripped(
            f"""\
public static void compareOrRerecordValue(Object value, Path expectedPath) throws IOException {{
{I}final JsonNode got = mapper.readTree( "\\"" + value + "\\"" );
{I}if (Common.RECORD_MODE) {{
{II}Files.createDirectories(expectedPath.getParent());
{II}Files.write(expectedPath, got.toString().getBytes());
{I}}} else {{
{II}if (!Files.exists(expectedPath)) {{
{III}throw new FileNotFoundException("The file with the recorded value does not exist: " + expectedPath);
{II}}}
{II}final JsonNode expected = mapper.readTree(expectedPath.toFile());
{II}assertEquals(mapper.readTree(expected.toString()), mapper.readTree(got.toString()));
{I}}}
}}""")
    )

    for our_type in symbol_table.our_types:
        if not isinstance(our_type, intermediate.ConcreteClass):
            continue

        cls_name_java = aas_core_codegen.java.naming.class_name(our_type.name)
        cls_name_json = aas_core_codegen.naming.json_model_type(our_type.name)

        x_or_default_methods = []  # type: List[intermediate.Method]
        for method in our_type.methods:
            if method.name.endswith("_or_default"):
                x_or_default_methods.append(method)

        for method in x_or_default_methods:
            method_name_java = aas_core_codegen.java.naming.method_name(method.name)

            result_enum = None  # type: Optional[intermediate.Enumeration]
            assert method.returns is not None, (
                f"Expected all X_or_default to return something, "
                f"but got None for {our_type}.{method.name}"
            )

            if isinstance(
                method.returns, intermediate.OurTypeAnnotation
            ) and isinstance(method.returns.our_type, intermediate.Enumeration):
                result_enum = method.returns.our_type

            if result_enum is None:
                value_assignment_snippet = Stripped(
                    f"final Object value = instance.{method_name_java}();"
                )
            else:
                value_assignment_snippet = Stripped(
                    f"""\
final Optional<String> valueOpt = Stringification.toString(instance.{method_name_java}());
{I}if (!valueOpt.isPresent()) throw new IllegalStateException("Failed to stringify the enum");
{I}String value = valueOpt.get();"""
                )

            indent = ""

            # noinspection SpellCheckingInspection
            blocks.append(
                Stripped(
                    f"""\
@Test
public void test{cls_name_java}{method_name_java}NonDefault() throws IOException {{
{I}final {cls_name_java} instance = CommonJsonization.loadMaximal{cls_name_java}();
{I}{aas_core_codegen.common.indent_but_first_line(value_assignment_snippet, indent)}
{I}compareOrRerecordValue(
{II}value,
{II}Paths.get( Common.TEST_DATA_DIR,
{II}"XOrDefault",
{II}{java_common.string_literal(cls_name_json)},
{II}"{method_name_java}.non-default.json"));
}}  // public void test{cls_name_java}{method_name_java}NonDefault

@Test
public void test{cls_name_java}_{method_name_java}Default() throws IOException {{
{I}final {cls_name_java} instance = CommonJsonization.loadMinimal{cls_name_java}();
{I}{aas_core_codegen.common.indent_but_first_line(value_assignment_snippet, indent)}
{I}compareOrRerecordValue(
{II}value,
{II}Paths.get( Common.TEST_DATA_DIR,
{II}"XOrDefault",
{II}{java_common.string_literal(cls_name_json)},
{II}"{method_name_java}.default.json"));
}}  // public void test{cls_name_java}{method_name_java}Default"""
                )
            )

    writer = io.StringIO()
    writer.write(
        f"""\
/*
 * This code has been automatically generated by testgen.
 * Do NOT edit or append.
 */

import aas_core.aas3_0.stringification.Stringification;
import aas_core.aas3_0.types.impl.*;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestXOrDefault {{

{I}final static ObjectMapper mapper = new ObjectMapper();

"""
    )

    for i, block in enumerate(blocks):
        if i > 0:
            writer.write("\n\n")

        writer.write(textwrap.indent(block, I))

    writer.write(
        """
}  // class TestXOrDefault


/*
 * This code has been automatically generated by testgen.
 * Do NOT edit or append.
 */
"""
    )

    this_path = pathlib.Path(os.path.realpath(__file__))
    repo_root = this_path.parent.parent.parent

    target_pth = repo_root / "src/test/java/TestXOrDefault.java"
    target_pth.write_text(writer.getvalue(), encoding="utf-8")

    return 0


if __name__ == "__main__":
    sys.exit(main())
