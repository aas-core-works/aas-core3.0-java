"""Generate the test code for making shallow and deep copies."""

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
from aas_core_codegen.common import Stripped, Identifier
from aas_core_codegen.java import (
    common as java_common,
    naming as java_naming,
)
from aas_core_codegen.java.common import (
    INDENT as I,
    INDENT2 as II,
    INDENT3 as III,
)

from test_codegen.common import load_symbol_table

def _generate_pair() -> Stripped:
    """Generate zip  method."""
    return Stripped(
        f"""\
private static class Pair<A, B> {{
{I}private final A first;
{I}private final B second;
{I}
{I}public Pair(A first, B second) {{
{II}this.first = first;
{II}this.second = second;
{I}}}
{I}
{I}public A getFirst() {{
{II}return first;
{I}}}
{I}
{I}public B getSecond() {{
{II}return second;
{I}}}
}}"""
    )

def _generate_zip() -> Stripped:
    """Generate zip  method."""
    return Stripped(
        f"""\
// Java 8 doesn't provide a split operation out of the box, so we have to ship our own.
// Adapted from: https://stackoverflow.com/a/23529010
private static <A, B> Stream<Pair<A, B>> zip(
{I}Stream<? extends A> a,
{I}Stream<? extends B> b) {{
{I}Spliterator<? extends A> aSplit = Objects.requireNonNull(a).spliterator();
{I}Spliterator<? extends B> bSplit = Objects.requireNonNull(b).spliterator();
{I}
{I}int characteristics = aSplit.characteristics() & bSplit.characteristics() &
{II}~(Spliterator.DISTINCT | Spliterator.SORTED);
{I}
{I}long zipSize = ((characteristics & Spliterator.SIZED) != 0)
{II}? Math.min(aSplit.getExactSizeIfKnown(), bSplit.getExactSizeIfKnown())
{II}: -1;
{I}
{I}Iterator<A> aIter = Spliterators.iterator(aSplit);
{I}Iterator<B> bIter = Spliterators.iterator(bSplit);
{I}Iterator<Pair<A, B>> cIter = new Iterator<Pair<A, B>>() {{
{II}@Override
{II}public boolean hasNext() {{
{III}return aIter.hasNext() && bIter.hasNext();
{II}}}
{II}
{II}@Override
{II}public Pair<A, B> next() {{
{III}return new Pair<>(aIter.next(), bIter.next());
{II}}}
{I}}};
{I}
{I}Spliterator<Pair<A, B>> split = Spliterators.spliterator(cIter, zipSize, characteristics);
{I}return StreamSupport.stream(split, false);
}}"""
    )

def _generate_shallow_equals(cls: intermediate.ConcreteClass) -> Stripped:
    """Generate the code for a static shallow ``Equals`` method."""
    if cls.is_implementation_specific:
        raise AssertionError(
            f"(mristin, 2022-11-04): "
            f"The class {cls.name!r} is implementation specific. "
            f"At the moment, we assume that all classes are not "
            f"implementation-specific, so that we can automatically generate the "
            f"shallow-equals methods. This way we can dispense of the whole "
            f"snippet/specific-implementation loading logic in "
            f"the unit test generation. Please notify the developers if you see this, "
            f"so that we can add the logic for implementation-specific classes "
            f"to this generation script."
        )

    exprs = []  # type: List[str]
    for prop in cls.properties:
        getter_name = java_naming.getter_name(prop.name)
        if isinstance(prop.type_annotation, intermediate.OptionalTypeAnnotation):
            exprs.append(f"( that.{getter_name}().isPresent() ? ( other.{getter_name}().isPresent() && that.{getter_name}().get() == other.{getter_name}().get()) : !other.{getter_name}().isPresent())")
        else:
            exprs.append(f"that.{getter_name}() == other.{getter_name}()")

    # NOTE (mristin, 2022-11-04):
    # This is a poor man's line re-flowing.
    exprs_joined = " && ".join(exprs)
    if len(exprs_joined) < 70:
        statement = Stripped(f"return {exprs_joined};")
    else:
        exprs_joined = "\n&& ".join(exprs)
        statement = Stripped(
            f"""\
return (
{I}{aas_core_codegen.common.indent_but_first_line(exprs_joined, I)});"""
        )

    cls_name_java = aas_core_codegen.java.naming.class_name(cls.name)

    return Stripped(
        f"""\
private static boolean check{cls_name_java}ShallowEquals(
{I}{cls_name_java} that,
{I}{cls_name_java} other)
{{
{I}{aas_core_codegen.common.indent_but_first_line(statement, I)}
}}"""
    )


def _generate_transform_as_deep_equals(cls: intermediate.ConcreteClass) -> Stripped:
    """Generate the transform method that checks for deep equality."""
    if cls.is_implementation_specific:
        raise AssertionError(
            f"(mristin, 2022-11-04): "
            f"The class {cls.name!r} is implementation specific. "
            f"At the moment, we assume that all classes are not "
            f"implementation-specific, so that we can automatically generate the "
            f"shallow-equals methods. This way we can dispense of the whole "
            f"snippet/specific-implementation loading logic in "
            f"the unit test generation. Please notify the developers if you see this, "
            f"so that we can add the logic for implementation-specific classes "
            f"to this generation script."
        )

    cls_name = java_naming.class_name(cls.name)

    exprs = []  # type: List[Stripped]

    for prop in cls.properties:
        optional = isinstance(prop.type_annotation, intermediate.OptionalTypeAnnotation)
        type_anno = intermediate.beneath_optional(prop.type_annotation)
        getter_name = java_naming.getter_name(prop.name)
        primitive_type = intermediate.try_primitive_type(type_anno)

        # fmt: off
        if (
                isinstance(type_anno, intermediate.PrimitiveTypeAnnotation)
                or (
                    isinstance(type_anno, intermediate.OurTypeAnnotation)
                    and isinstance(
                        type_anno.our_type, intermediate.ConstrainedPrimitive
                    )
                )
        ):
            # fmt: on
            assert primitive_type is not None
            if (
                    primitive_type is intermediate.PrimitiveType.BOOL
                or primitive_type is intermediate.PrimitiveType.INT
                or primitive_type is intermediate.PrimitiveType.FLOAT
                or primitive_type is intermediate.PrimitiveType.STR
            ):
                if optional: expr = Stripped(f"""\
that.{getter_name}().isPresent()
{I}? casted.{getter_name}().isPresent()
{I}&& that.{getter_name}().get() == casted.{getter_name}().get()
{I}: ! casted.{getter_name}().isPresent()""")
                else: expr = Stripped(f"that.{getter_name}() == casted.{getter_name}()")
            elif primitive_type is intermediate.PrimitiveType.BYTEARRAY:
                expr = Stripped(
                        f"""\
Arrays.equals(that.{getter_name}().get(),casted.{getter_name}().get())"""
                )
            else:
                aas_core_codegen.common.assert_never(primitive_type)
        elif isinstance(type_anno, intermediate.OurTypeAnnotation):
            if isinstance(type_anno.our_type, intermediate.Enumeration):
                if optional:
                    expr = Stripped(f"""\
that.{getter_name}().isPresent() ?
{I}( casted.{getter_name}().isPresent()
{I}&& that.{getter_name}().get() == casted.{getter_name}().get() )
{I}: ! casted.{getter_name}().isPresent()""")
                else:
                    expr = Stripped(f"that.{getter_name}() == casted.{getter_name}()")
            elif isinstance(type_anno.our_type, intermediate.ConstrainedPrimitive):
                raise AssertionError("Expected to handle this case above")
            elif isinstance(
                    type_anno.our_type,
                    (intermediate.AbstractClass, intermediate.ConcreteClass)
            ):
                if optional: expr = Stripped(
                    f"""\
( that.{getter_name}().isPresent()
{I}? casted.{getter_name}().isPresent()
{I}&& transform( that.{getter_name}().get(), casted.{getter_name}().get())
{I}: ! casted.{getter_name}().isPresent())"""
                )
                else: expr = Stripped(
                    f"""\
transform(
{I}that.{getter_name}(),
{I}casted.{getter_name}())"""
                )
            else:
                aas_core_codegen.common.assert_never(type_anno.our_type)
        elif isinstance(type_anno, intermediate.ListTypeAnnotation):
            assert (
                    isinstance(type_anno.items, intermediate.OurTypeAnnotation)
                    and isinstance(type_anno.items.our_type, intermediate.Class)
            ), (
                f"(mristin, 2022-11-03): We handle only lists of classes in the deep "
                f"equality checks at the moment. The meta-model does not contain "
                f"any other lists, so we wanted to keep the code as simple as "
                f"possible, and avoid unrolling. Please contact the developers "
                f"if you need this feature. The class in question was {cls.name!r} and "
                f"the property {prop.name!r}."
            )
            if optional: expr = Stripped(
                f"""\
that.{getter_name}().isPresent()
{I}? ( casted.{getter_name}().isPresent()
{I}&& that.{getter_name}().get().size() == casted.{getter_name}().get().size()
{I}&& zip(
{II}that.{getter_name}().get().stream(),
{II}casted.{getter_name}().get().stream())
{III}.map(pair -> transform(pair.getFirst(),pair.getSecond()))
{III}.collect(Collectors.toList()).stream().allMatch(Boolean.TRUE::equals))
{I}: ! casted.{getter_name}().isPresent()"""
            )
            else: expr = Stripped(
                f"""\
that.{getter_name}().size() == casted.{getter_name}().size()
{I}&& ( zip(
{II}that.{getter_name}().stream(),
{II}casted.{getter_name}().stream())
{III}.map(pair -> transform(pair.getFirst(), pair.getSecond()))
{III}.collect(Collectors.toList()).stream().allMatch(Boolean.TRUE::equals))"""
            )
        else:
            aas_core_codegen.common.assert_never(type_anno)

        exprs.append(expr)

    body_writer = io.StringIO()
    body_writer.write("return (")
    for i, expr in enumerate(exprs):
        body_writer.write("\n")
        if i > 0:
            body_writer.write(
                f"{I}&& {aas_core_codegen.common.indent_but_first_line(expr, I)}"
            )
        else:
            body_writer.write(
                f"{I}{aas_core_codegen.common.indent_but_first_line(expr, I)}"
            )

    body_writer.write(");")

    interface_name = java_naming.interface_name(cls.name)
    transform_name = java_naming.method_name(Identifier(f"transform_{cls.name}"))

    return Stripped(
        f"""\
@Override
public Boolean {transform_name}(
{I}{interface_name} that,
{I}IClass other)
{{
{I}final {cls_name} casted;
{I}try {{
{II}casted = ({cls_name}) other;
{I}}} catch (ClassCastException exception) {{
{I}return false;
{I}}}

{I}{aas_core_codegen.common.indent_but_first_line(body_writer.getvalue(), I)}
}}"""
    )


def _generate_deep_equals_transformer(
    symbol_table: intermediate.SymbolTable,
) -> Stripped:
    """Generate the transformer that checks for deep equality."""
    blocks = []  # type: List[Stripped]

    for our_type in symbol_table.our_types:
        if not isinstance(our_type, intermediate.ConcreteClass):
            continue

        if our_type.is_implementation_specific:
            raise AssertionError(
                f"(mristin, 2022-11-04): "
                f"The class {our_type.name!r} is implementation specific. "
                f"At the moment, we assume that all classes are not "
                f"implementation-specific, so that we can automatically generate the "
                f"deep-equals methods. This way we can dispense of the whole "
                f"snippet/specific-implementation loading logic in "
                f"the unit test generation. Please notify the developers if you see "
                f"this, so that we can add the logic for implementation-specific "
                f"classes to this generation script."
            )

        blocks.append(_generate_transform_as_deep_equals(cls=our_type))

    writer = io.StringIO()
    writer.write(
        f"""\
private static class DeepEqualTransformer extends AbstractTransformerWithContext<IClass, Boolean> {{
"""
    )

    for i, block in enumerate(blocks):
        if i > 0:
            writer.write("\n\n")

        writer.write(textwrap.indent(block, I))

    writer.write("\n}  // inner class DeepEqualTransformer")

    return Stripped(writer.getvalue())


def _generate_deep_equals(cls: intermediate.ConcreteClass) -> Stripped:
    """Generate the code for a static deep ``Equals`` method."""
    if cls.is_implementation_specific:
        raise AssertionError(
            f"(mristin, 2022-11-04): "
            f"The class {cls.name!r} is implementation specific. "
            f"At the moment, we assume that all classes are not "
            f"implementation-specific, so that we can automatically generate the "
            f"shallow-equals methods. This way we can dispense of the whole "
            f"snippet/specific-implementation loading logic in "
            f"the unit test generation. Please notify the developers if you see this, "
            f"so that we can add the logic for implementation-specific classes "
            f"to this generation script."
        )

    cls_name = java_naming.class_name(cls.name)

    return Stripped(
        f"""\
private static boolean check{cls_name}DeepEquals(
{I}{cls_name} that,
{I}{cls_name} other)
{{
{I}return deepEqualTransformerInstance.transform(that, other);
}} // public void test{cls_name}DeepCopy"""
    )


def main() -> int:
    """Execute the main routine."""
    symbol_table = load_symbol_table()
    blocks = []  # type: List[Stripped]

    # noinspection PyListCreation
    blocks = [
        _generate_pair(),
        _generate_zip(),
        _generate_deep_equals_transformer(symbol_table=symbol_table),
        Stripped(
            f"""\
private static final DeepEqualTransformer deepEqualTransformerInstance = new DeepEqualTransformer();"""
        ),
    ]  # type: List[Stripped]

    for our_type in symbol_table.our_types:
        if not isinstance(our_type, intermediate.ConcreteClass):
            continue

        blocks.append(_generate_shallow_equals(cls=our_type))

    for our_type in symbol_table.our_types:
        if not isinstance(our_type, intermediate.ConcreteClass):
            continue

        blocks.append(_generate_deep_equals(cls=our_type))

    for our_type in symbol_table.our_types:
        if not isinstance(our_type, intermediate.ConcreteClass):
            continue

        cls_name = java_naming.class_name(our_type.name)

        blocks.append(
            Stripped(
                f"""\
@Test
public void test{cls_name}ShallowCopy() throws IOException {{
{I}final {cls_name} instance = CommonJsonization.loadMaximal{cls_name}();
{I}final {cls_name} instanceCopy = Copying.shallow(instance);
{I}assertTrue(check{cls_name}ShallowEquals(instance,instanceCopy),"{cls_name}");
}}  // public void test{cls_name}ShallowCopy"""
            )
        )

        blocks.append(
            Stripped(
                f"""\
@Test
public void test{cls_name}DeepCopy() throws IOException {{
{I}final {cls_name} instance = CommonJsonization.loadMaximal{cls_name}();
{I}final {cls_name} instanceCopy = Copying.deep(instance);
{I}assertTrue(check{cls_name}DeepEquals(instance,instanceCopy),"{cls_name}");
}}  // public void test{cls_name}DeepCopy"""
            )
        )

    blocks.append(
        Stripped(
            F"""\
@SuppressWarnings("OptionalGetWithoutIsPresent")
@Test
public void testSnippetInDocs() {{
{I}// Prepare the environment
{I}final Property someProperty = new Property(
{II}DataTypeDefXsd.BOOLEAN,
{II}null,
{II}null,
{II}null,
{II}null,
{II}null,
{II}null,
{II}null,
{II}null,
{II}null,
{II}null,
{II}null
{I});
{I}someProperty.setIdShort("someProperty");
{I}List<ISubmodelElement> submodelElements = new ArrayList<>();
{I}submodelElements.add(someProperty);
{I}
{I}
{I}final Submodel submodel = new Submodel(
{II}"some-unique-global-identifier",
{II}null,
{II}null,
{II}null,
{II}null,
{II}null,
{II}null,
{II}null,
{II}null,
{II}null,
{II}null,
{II}null,
{II}submodelElements
{I});
{I}final List<ISubmodel> submodels = new ArrayList<>();
{I}submodels.add(submodel);
{I}final Environment environment = new Environment(null, submodels, null);
{I}// Make a deep copy
{I}final Environment deepCopy = Copying.deep(environment);
{I}// Make a shallow copy
{I}final Environment shallowCopy = Copying.shallow(environment);
{I}// Changes to the property affect only the shallow copy,
{I}// but not the deep one
{I}environment.getSubmodels().get().get(0).getSubmodelElements().get().get(0).setIdShort("changed");
{I}
{I}assertEquals("changed",shallowCopy.getSubmodels().get().get(0).getSubmodelElements().get().get(0).getIdShort().get());
{I}assertEquals("someProperty",deepCopy.getSubmodels().get().get(0).getSubmodelElements().get().get(0).getIdShort().get());
}}  // public void Test_snippet_in_docs"""
        )
    )

    writer = io.StringIO()
    writer.write(
        """\
/*
 * This code has been automatically generated by testgen.
 * Do NOT edit or append.
 */

import aas_core.aas3_0.copying.Copying;
import aas_core.aas3_0.types.enums.DataTypeDefXsd;
import aas_core.aas3_0.types.impl.*;
import aas_core.aas3_0.types.model.*;
import org.junit.jupiter.api.Test;
import aas_core.aas3_0.types.model.IClass;
import aas_core.aas3_0.visitation.AbstractTransformerWithContext;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCopying {
"""
    )

    for i, block in enumerate(blocks):
        if i > 0:
            writer.write("\n\n")

        writer.write(textwrap.indent(block, I))

    writer.write(
        """
}  // class TestCopying


/*
 * This code has been automatically generated by testgen.
 * Do NOT edit or append.
 */
"""
    )

    this_path = pathlib.Path(os.path.realpath(__file__))
    repo_root = this_path.parent.parent.parent

    target_pth = repo_root / "src/test/java/TestCopying.java"
    target_pth.write_text(writer.getvalue(), encoding="utf-8")

    return 0


if __name__ == "__main__":
    sys.exit(main())
