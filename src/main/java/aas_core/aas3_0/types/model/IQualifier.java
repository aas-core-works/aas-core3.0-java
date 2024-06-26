/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */

package aas_core.aas3_0.types.model;

import aas_core.aas3_0.types.enums.*;
import aas_core.aas3_0.types.impl.*;
import java.util.Optional;

/**
 * A qualifier is a type-value-pair that makes additional statements w.r.t. the value of the
 * element.
 *
 * <p>Constraints:
 *
 * <ul>
 *   <li>Constraint AASd-006: If both the {@link #getValue()} and the {@link #getValueId()} of a
 *       {@link aas_core.aas3_0.types.impl.Qualifier} are present then the {@link #getValue()} needs
 *       to be identical to the value of the referenced coded value in {@link #getValueId()}.
 *   <li>Constraint AASd-020: The value of {@link #getValue()} shall be consistent to the data type
 *       as defined in {@link #getValueType()}.
 * </ul>
 */
public interface IQualifier extends IHasSemantics {
  /**
   * The qualifier kind describes the kind of the qualifier that is applied to the element.
   *
   * <p>Default: {@link aas_core.aas3_0.types.enums.QualifierKind#CONCEPT_QUALIFIER}
   */
  Optional<QualifierKind> getKind();

  void setKind(QualifierKind kind);

  /**
   * The qualifier <em>type</em> describes the type of the qualifier that is applied to the element.
   */
  String getType();

  void setType(String type);

  /** Data type of the qualifier value. */
  DataTypeDefXsd getValueType();

  void setValueType(DataTypeDefXsd valueType);

  /** The qualifier value is the value of the qualifier. */
  Optional<String> getValue();

  void setValue(String value);

  /**
   * Reference to the global unique ID of a coded value.
   *
   * <p>It is recommended to use a global reference.
   */
  Optional<IReference> getValueId();

  void setValueId(IReference valueId);

  QualifierKind kindOrDefault();
}

/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */
