/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */

package aas_core.aas3_0.types.enums;

/** Enumeration of different key value types within a key. */
public enum KeyTypes {
  ANNOTATED_RELATIONSHIP_ELEMENT,
  ASSET_ADMINISTRATION_SHELL,
  BASIC_EVENT_ELEMENT,
  BLOB,
  CAPABILITY,
  CONCEPT_DESCRIPTION,
  /**
   * Data element.
   *
   * <p>Data Element is abstract, <em>i.e.</em> if a key uses {@link #DATA_ELEMENT} the reference
   * may be a Property, a File etc.
   */
  DATA_ELEMENT,
  ENTITY,
  /**
   * Event.
   *
   * <p>{@link aas_core.aas3_0.types.model.IEventElement} is abstract.
   */
  EVENT_ELEMENT,
  FILE,
  /** Bookmark or a similar local identifier of a subordinate part of a primary resource */
  FRAGMENT_REFERENCE,
  GLOBAL_REFERENCE,
  /**
   * Identifiable.
   *
   * <p>Identifiable is abstract, i.e. if a key uses “Identifiable” the reference may be an Asset
   * Administration Shell, a Submodel or a Concept Description.
   */
  IDENTIFIABLE,
  /** Property with a value that can be provided in multiple languages */
  MULTI_LANGUAGE_PROPERTY,
  OPERATION,
  PROPERTY,
  /** Range with min and max */
  RANGE,
  REFERABLE,
  /** Reference */
  REFERENCE_ELEMENT,
  /** Relationship */
  RELATIONSHIP_ELEMENT,
  SUBMODEL,
  /**
   * Submodel Element
   *
   * <p>Submodel Element is abstract, <em>i.e.</em> if a key uses {@link #SUBMODEL_ELEMENT} the
   * reference may be a {@link aas_core.aas3_0.types.impl.Property}, an {@link
   * aas_core.aas3_0.types.impl.Operation} etc.
   */
  SUBMODEL_ELEMENT,
  /** Struct of Submodel Elements */
  SUBMODEL_ELEMENT_COLLECTION,
  /** List of Submodel Elements */
  SUBMODEL_ELEMENT_LIST
}

/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */
