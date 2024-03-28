/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */

package aas_core.aas3_0.types.enums;

/**
 * Enumeration for kinds of qualifiers.
 *
 * <p>This element is experimental and therefore may be subject to change or may be removed
 * completely in future versions of the meta-model.
 */
public enum QualifierKind {
  /**
   * qualifies the value of the element and can change during run-time.
   *
   * <p>Value qualifiers are only applicable to elements with kind {@link
   * aas_core.aas3_0.types.enums.ModellingKind#INSTANCE}.
   */
  VALUE_QUALIFIER,
  /**
   * qualifies the semantic definition the element is referring to ({@link
   * aas_core.aas3_0.types.model.IHasSemantics#getSemanticId()})
   */
  CONCEPT_QUALIFIER,
  /**
   * qualifies the elements within a specific submodel on concept level.
   *
   * <p>Template qualifiers are only applicable to elements with kind {@link
   * aas_core.aas3_0.types.enums.ModellingKind#TEMPLATE}.
   */
  TEMPLATE_QUALIFIER
}

/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */