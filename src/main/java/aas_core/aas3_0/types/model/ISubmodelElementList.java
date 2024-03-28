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
 * A submodel element list is an ordered list of submodel elements.
 *
 * <p>The numbering starts with zero (0).
 *
 * <p>Constraints:
 *
 * <ul>
 *   <li>Constraint AASd-107: If a first level child element in a {@link
 *       aas_core.aas3_0.types.impl.SubmodelElementList} has a {@link
 *       aas_core.aas3_0.types.model.IHasSemantics#getSemanticId()} it shall be identical to {@link
 *       #getSemanticIdListElement()}.
 *   <li>Constraint AASd-114: If two first level child elements in a {@link
 *       aas_core.aas3_0.types.impl.SubmodelElementList} have a {@link
 *       aas_core.aas3_0.types.model.IHasSemantics#getSemanticId()} then they shall be identical.
 *   <li>Constraint AASd-115: If a first level child element in a {@link
 *       aas_core.aas3_0.types.impl.SubmodelElementList} does not specify a {@link
 *       aas_core.aas3_0.types.model.IHasSemantics#getSemanticId()} then the value is assumed to be
 *       identical to {@link #getSemanticIdListElement()}.
 *   <li>Constraint AASd-120: The {@link #getIdShort()} of a {@link
 *       aas_core.aas3_0.types.model.ISubmodelElement} being a direct child of a {@link
 *       aas_core.aas3_0.types.impl.SubmodelElementList} shall not be specified.
 *   <li>Constraint AASd-108: All first level child elements in a {@link
 *       aas_core.aas3_0.types.impl.SubmodelElementList} shall have the same submodel element type
 *       as specified in {@link #getTypeValueListElement()}.
 *   <li>Constraint AASd-109: If {@link #getTypeValueListElement()} is equal to {@link
 *       aas_core.aas3_0.types.enums.AasSubmodelElements#PROPERTY} or {@link
 *       aas_core.aas3_0.types.enums.AasSubmodelElements#RANGE} {@link #getValueTypeListElement()}
 *       shall be set and all first level child elements in the {@link
 *       aas_core.aas3_0.types.impl.SubmodelElementList} shall have the value type as specified in
 *       {@link #getValueTypeListElement()}.
 * </ul>
 */
public interface ISubmodelElementList extends ISubmodelElement {
  /**
   * Defines whether order in list is relevant. If {@link #getOrderRelevant()} = {@code False} then
   * the list is representing a set or a bag.
   *
   * <p>Default: {@code True}
   */
  Optional<Boolean> getOrderRelevant();

  void setOrderRelevant(Boolean orderRelevant);

  /**
   * Semantic ID the submodel elements contained in the list match to.
   *
   * <p>It is recommended to use a global reference.
   */
  Optional<IReference> getSemanticIdListElement();

  void setSemanticIdListElement(IReference semanticIdListElement);

  /** The submodel element type of the submodel elements contained in the list. */
  AasSubmodelElements getTypeValueListElement();

  void setTypeValueListElement(AasSubmodelElements typeValueListElement);

  /** The value type of the submodel element contained in the list. */
  Optional<DataTypeDefXsd> getValueTypeListElement();

  void setValueTypeListElement(DataTypeDefXsd valueTypeListElement);

  /**
   * Submodel element contained in the list.
   *
   * <p>The list is ordered.
   */
  Optional<List<ISubmodelElement>> getValue();

  void setValue(List<ISubmodelElement> value);

  Boolean orderRelevantOrDefault();

  /** Iterate over value, if set, and otherwise return an empty enumerable. */
  Iterable<ISubmodelElement> overValueOrEmpty();
}

/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */
