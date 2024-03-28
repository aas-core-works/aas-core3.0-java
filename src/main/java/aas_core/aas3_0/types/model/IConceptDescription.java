/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */

package aas_core.aas3_0.types.model;

import aas_core.aas3_0.types.enums.*;
import aas_core.aas3_0.types.impl.*;
import java.util.List;
import java.util.Optional;

/**
 * The semantics of a property or other elements that may have a semantic description is defined by
 * a concept description.
 *
 * <p>The description of the concept should follow a standardized schema (realized as data
 * specification template).
 *
 * <p>Constraints:
 *
 * <ul>
 *   <li>Constraint AASc-3a-004: For a {@link aas_core.aas3_0.types.impl.ConceptDescription} with
 *       {@link #getCategory()} {@code PROPERTY} or {@code VALUE} using data specification IEC61360,
 *       the {@link aas_core.aas3_0.types.impl.DataSpecificationIec61360#getDataType()} is mandatory
 *       and shall be one of: {@code DATE}, {@code STRING}, {@code STRING_TRANSLATABLE}, {@code
 *       INTEGER_MEASURE}, {@code INTEGER_COUNT}, {@code INTEGER_CURRENCY}, {@code REAL_MEASURE},
 *       {@code REAL_COUNT}, {@code REAL_CURRENCY}, {@code BOOLEAN}, {@code RATIONAL}, {@code
 *       RATIONAL_MEASURE}, {@code TIME}, {@code TIMESTAMP}.
 *       <p>Note: categories are deprecated since V3.0 of Part 1a of the document series "Details of
 *       the Asset Administration Shell".
 *   <li>Constraint AASc-3a-005: For a {@link aas_core.aas3_0.types.impl.ConceptDescription} with
 *       {@link #getCategory()} {@code REFERENCE} using data specification template IEC61360, the
 *       {@link aas_core.aas3_0.types.impl.DataSpecificationIec61360#getDataType()} shall be one of:
 *       {@code STRING}, {@code IRI}, {@code IRDI}.
 *       <p>Note: categories are deprecated since V3.0 of Part 1a of the document series "Details of
 *       the Asset Administration Shell".
 *   <li>Constraint AASc-3a-006: For a {@link aas_core.aas3_0.types.impl.ConceptDescription} with
 *       {@link #getCategory()} {@code DOCUMENT} using data specification IEC61360, the {@link
 *       aas_core.aas3_0.types.impl.DataSpecificationIec61360#getDataType()} shall be one of {@code
 *       FILE}, {@code BLOB}, {@code HTML}
 *       <p>Categories are deprecated since V3.0 of Part 1a of the document series "Details of the
 *       Asset Administration Shell".
 *   <li>Constraint AASc-3a-007: For a {@link aas_core.aas3_0.types.impl.ConceptDescription} with
 *       {@link #getCategory()} {@code QUALIFIER_TYPE} using data specification IEC61360, the {@link
 *       aas_core.aas3_0.types.impl.DataSpecificationIec61360#getDataType()} is mandatory and shall
 *       be defined.
 *       <p>Categories are deprecated since V3.0 of Part 1a of the document series "Details of the
 *       Asset Administration Shell".
 *   <li>Constraint AASc-3a-008: For a {@link aas_core.aas3_0.types.impl.ConceptDescription} using
 *       data specification template IEC61360, {@link
 *       aas_core.aas3_0.types.impl.DataSpecificationIec61360#getDefinition()} is mandatory and
 *       shall be defined at least in English.
 *       <p>Exception: The concept description describes a value, i.e. {@link
 *       aas_core.aas3_0.types.impl.DataSpecificationIec61360#getValue()} is defined.
 *   <li>Constraint AASc-3a-003: For a {@link aas_core.aas3_0.types.impl.ConceptDescription} using
 *       data specification template IEC61360, referenced via {@link
 *       aas_core.aas3_0.types.impl.DataSpecificationIec61360#getValueList()} {@link
 *       aas_core.aas3_0.types.impl.ValueReferencePair#getValueId()} the {@link
 *       aas_core.aas3_0.types.impl.DataSpecificationIec61360#getValue()} shall be set.
 * </ul>
 */
public interface IConceptDescription extends IIdentifiable, IHasDataSpecification {
  /**
   * Reference to an external definition the concept is compatible to or was derived from.
   *
   * <p>It is recommended to use a global reference.
   *
   * <p>Compare to is-case-of relationship in ISO 13584-32 &amp; IEC EN 61360
   */
  Optional<List<IReference>> getIsCaseOf();

  void setIsCaseOf(List<IReference> isCaseOf);

  /** Iterate over isCaseOf, if set, and otherwise return an empty enumerable. */
  Iterable<IReference> overIsCaseOfOrEmpty();
}

/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */
