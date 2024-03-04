"""Generate the test code for the ``OverXOrEmpty`` methods."""

import io
import os
import pathlib
import sys
import textwrap
from typing import List

from aas_core_codegen import intermediate
from aas_core_codegen.common import Stripped, Identifier
from aas_core_codegen.java import naming as java_naming
from aas_core_codegen.python import naming as python_naming

from test_codegen import test_data_io
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

    this_path = pathlib.Path(os.path.realpath(__file__))
    repo_root = this_path.parent.parent.parent

    test_data_dir = repo_root / "test_data"

    for our_type in symbol_table.our_types:
        if not isinstance(our_type, intermediate.ConcreteClass):
            continue

        cls_name_java = java_naming.class_name(our_type.name)

        # NOTE (mristin, 2023-03-16):
        # Our test data generation procedure is quite crude: we sample the properties,
        # and then brutishly fix them after the sampling.
        #
        # There are classes whose invariants prevent them from having all
        # the properties set in their maximal instances. Therefore, we have
        # to generate different code for those cases.
        #
        # Similar for minimal examples. Though some properties are not required,
        # the invariants of the class might mandate that one or more of the optional
        # properties are set based, say, on a required property.

        maximal_instance = test_data_io.load_maximal(
            test_data_dir=test_data_dir, cls=our_type
        )

        minimal_instance = test_data_io.load_minimal(
            test_data_dir=test_data_dir, cls=our_type
        )

        for prop in our_type.properties:
            method_name_java = java_naming.method_name(
                Identifier(f"Over_{prop.name}_or_empty")
            )

            prop_name_java = java_naming.property_name(prop.name)

            prop_name_python = python_naming.property_name(prop.name)

            getter_name = java_naming.getter_name(prop.name)

            if isinstance(
                prop.type_annotation, intermediate.OptionalTypeAnnotation
            ) and isinstance(
                prop.type_annotation.value, intermediate.ListTypeAnnotation
            ):
                if getattr(maximal_instance, prop_name_python) is not None:
                    # noinspection SpellCheckingInspection
                    blocks.append(
                        Stripped(
                            f"""\
@Test
public void test{cls_name_java}{method_name_java}NonDefault() throws IOException {{
{I}final {cls_name_java} instance = CommonJsonization.loadMaximal{cls_name_java}();
{I}if(!instance.{getter_name}().isPresent()){{
{II}throw new IllegalArgumentException(
{III}"Unexpected " +
{III}"{prop_name_java} is not present " +
{III}"in the maximal example of " +
{III}"{cls_name_java}"
{II});
{I}}}
{I}AtomicInteger count = new AtomicInteger();
{I}instance.{method_name_java}().forEach(i -> count.getAndIncrement());
{I}assertEquals(count.get(),instance.{getter_name}().get().size());
}}  // public void test{cls_name_java}{method_name_java}NonDefault"""
                        )
                    )
                else:
                    blocks.append(
                        Stripped(
                            f"""\
// The maximal example of {cls_name_java} contains no {prop_name_java},
// so we can not generate the corresponding test case 
// Test_{cls_name_java}_{method_name_java}_non_default."""
                        )
                    )

                if not getattr(minimal_instance, prop_name_python) is not None:
                    blocks.append(
                        Stripped(
                            f"""\
@Test
public void test{cls_name_java}{method_name_java}Default() throws IOException {{
{I}final {cls_name_java} instance = CommonJsonization.loadMinimal{cls_name_java}();
{I}if(instance.{getter_name}().isPresent()){{
{II}throw new IllegalArgumentException(
{III}"Unexpected " +
{III}"{prop_name_java} is present " +
{III}"in the minimal example of " +
{III}"{cls_name_java}"
{II});
{I}}}
{I}AtomicInteger count = new AtomicInteger();
{I}instance.{method_name_java}().forEach(i -> count.getAndIncrement());
{I}assertEquals(0 , count.get());
}}  // public void Test_{cls_name_java}_{method_name_java}_default"""
                        )
                    )
                else:
                    blocks.append(
                        Stripped(
                            f"""\
// The minimal example of {cls_name_java} contains no {prop_name_java},
// so we can not generate the corresponding test case 
// Test_{cls_name_java}_{method_name_java}_default."""
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
import org.junit.jupiter.api.Test;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Generated;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Generated("Generated by aas-test-gen")
public class TestOverXOrEmpty {
"""
    )

    for i, block in enumerate(blocks):
        if i > 0:
            writer.write("\n\n")

        writer.write(textwrap.indent(block, "        "))

    writer.write(
        """
}  // class TestOverXOrEmpty

/*
 * This code has been automatically generated by testgen.
 * Do NOT edit or append.
 */
"""
    )

    target_pth = repo_root / "/home/mboehm/IdeaProjects/TestGen/src/test/java/TestOverXOrEmpty.java"
    target_pth.write_text(writer.getvalue(), encoding="utf-8")

    return 0


if __name__ == "__main__":
    sys.exit(main())
