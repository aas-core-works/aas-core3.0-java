"""Generate the common functions to de/serialize instances of a class."""

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
from aas_core_codegen.java.common import (
    INDENT as I,
    INDENT2 as II,
)

import test_codegen.common
from test_codegen import test_data_io

def main() -> int:
    """Execute the main routine."""
    symbol_table = test_codegen.common.load_symbol_table()

    this_path = pathlib.Path(os.path.realpath(__file__))
    repo_root = this_path.parent.parent.parent

    test_data_dir = repo_root / "test_data"

    # noinspection PyListCreation
    blocks = []  # type: List[str]

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
        cls_name_json = aas_core_codegen.naming.json_model_type(our_type.name)

        if container_cls is our_type:
            deserialization_snippet = Stripped(
                f"""\
final {cls_name_java} container = Jsonization.Deserialize.deserialize{cls_name_java}(node);"""
            )
            container_kind_directory = "SelfContained"
        else:
            deserialization_snippet = Stripped(
                f"""\
final Environment container = Jsonization.Deserialize.deserializeEnvironment(node);"""
            )
            assert (
                container_cls.name == "Environment"
            ), "Necessary for the container kind directory"
            container_kind_directory = "ContainedInEnvironment"

        indent = "   "
        blocks.append(
            Stripped(
                f"""\
public static {cls_name_java} loadMaximal{cls_name_java}() throws IOException {{
{I}final Path path = Paths.get(Common.TEST_DATA_DIR,
{II}"Json",
{II}{java_common.string_literal(container_kind_directory)},
{II}"Expected",
{II}{java_common.string_literal(cls_name_json)},
{II}"maximal.json");
{I}final ObjectMapper objectMapper = new ObjectMapper();
{I}final JsonNode node = objectMapper.readTree(path.toFile());
{I}{aas_core_codegen.common.indent_but_first_line(deserialization_snippet, indent)}
{I}return Common.mustFind("{cls_name_java}",container);
}}  // public static loadMaximal{cls_name_java}

public static {cls_name_java} loadMinimal{cls_name_java}() throws IOException {{
{I}final Path path = Paths.get(Common.TEST_DATA_DIR,
{II}"Json",
{II}{java_common.string_literal(container_kind_directory)},
{II}"Expected",
{II}{java_common.string_literal(cls_name_json)},
{II}"minimal.json");
{I}final ObjectMapper objectMapper = new ObjectMapper();
{I}final JsonNode node = objectMapper.readTree(path.toFile());
{I}{aas_core_codegen.common.indent_but_first_line(deserialization_snippet, indent)}
{I}return Common.mustFind("{cls_name_java}",container);
}}  // public static loadMinimal{cls_name_java}"""
            )
        )

    writer = io.StringIO()
    writer.write(
        """\
/*
 * This code has been automatically generated by testgen.
 * Do NOT edit or append.
 */
 
import aas_core.aas3_0.jsonization.Jsonization;
import aas_core.aas3_0.types.impl.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
* Provide methods to load instances from JSON test data.
*/
public class CommonJsonization{
"""
    )

    for i, block in enumerate(blocks):
        if i > 0:
            writer.write("\n\n")

        writer.write(textwrap.indent(block, I))

    writer.write(
        """
}  // class CommonJsonization


/*
 * This code has been automatically generated by testgen.
 * Do NOT edit or append.
 */
"""
    )

    target_pth = repo_root / "src/test/java/CommonJsonization.java"
    target_pth.write_text(writer.getvalue(), encoding="utf-8")

    return 0


if __name__ == "__main__":
    sys.exit(main())
