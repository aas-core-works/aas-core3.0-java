"""Generate the test code for enhancing the model instances."""

import io
import os
import pathlib
import sys
import textwrap
from typing import List
from aas_core_codegen import intermediate
from aas_core_codegen.common import Stripped
from aas_core_codegen.java import (
    naming as java_naming,
)
from aas_core_codegen.java.common import (
    INDENT as I,
    INDENT2 as II
)

from test_codegen.common import load_symbol_table


def main() -> int:
    """Execute the main routine."""
    symbol_table = load_symbol_table()

    # noinspection PyListCreation
    blocks = [
        Stripped(
            f"""\
public static class Enhancement {{
{I}private final long someCustomId;
{I}public Enhancement(long someCustomId) {{
{II}this.someCustomId = someCustomId;
{I}}}
}}"""
        ),
        Stripped(
            f"""\
private Enhancer<Enhancement> createEnhancer() {{
{I}AtomicLong lastCustomId = new AtomicLong();
{I}Function<IClass, Optional<Enhancement>> enhancementFactory = iClass -> Optional.of(new Enhancement(lastCustomId.incrementAndGet()));
{I}return new Enhancer<>(enhancementFactory);
}}"""
        ),
        Stripped(
            f"""\
private static void testWrapped(IClass wrapped, Set<Long> idSet) {{
{I}assertNotNull(wrapped);
{I}assertEquals(1L, idSet.stream()
{II}.min(Comparator.comparing(Long::valueOf))
{II}.orElseThrow(() -> new IllegalStateException("Missing min value for wrapped.")));
{I}assertEquals(idSet.size(), idSet.stream()
{II}.max(Comparator.comparing(Long::valueOf))
{II}.orElseThrow(() -> new IllegalStateException("Missing max value for wrapped.")));
}}"""
        )
    ]  # type: List[Stripped]

    for our_type in symbol_table.our_types:
        if not isinstance(our_type, intermediate.ConcreteClass):
            continue

        cls_name = java_naming.class_name(our_type.name)

        blocks.append(
            Stripped(
                f"""\
@Test
public void test{cls_name}() throws IOException {{
{I}final {cls_name} instance = CommonJsonization.loadMaximal{cls_name}();
{I}final Enhancer<Enhancement> enhancer = createEnhancer();
{I}final IClass wrapped = enhancer.wrap(instance);
{I}final Set<Long> idSet = new HashSet<>();
{I}idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
{I}wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
{I}assertFalse(enhancer.unwrap(instance).isPresent());
{I}testWrapped(wrapped, idSet);
}}  // public void test{cls_name}"""
            )
        )

    writer = io.StringIO()
    writer.write(
        f"""\
/*
 * This code has been automatically generated by testgen.
 * Do NOT edit or append.
 */

import aas_core.aas3_0.enhancing.Enhancer;
import aas_core.aas3_0.types.impl.*;
import aas_core.aas3_0.types.model.IClass;
import org.junit.jupiter.api.Test;

import javax.annotation.Generated;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

@Generated("Generated by aas-test-gen")
public class TestEnhancing {{
"""
    )

    for i, block in enumerate(blocks):
        if i > 0:
            writer.write("\n\n")

        writer.write(textwrap.indent(block, II))

    writer.write(
        """
}  // class TestEnhancing

/*
 * This code has been automatically generated by testgen.
 * Do NOT edit or append.
 */
"""
    )

    this_path = pathlib.Path(os.path.realpath(__file__))
    repo_root = this_path.parent.parent.parent

    target_pth = repo_root / "src/test/java/TestEnhancing.java"
    target_pth.write_text(writer.getvalue(), encoding="utf-8")

    return 0


if __name__ == "__main__":
    sys.exit(main())
