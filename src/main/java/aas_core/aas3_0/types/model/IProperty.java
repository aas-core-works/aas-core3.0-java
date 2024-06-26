/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */

package aas_core.aas3_0.types.model;

import aas_core.aas3_0.types.enums.*;
import aas_core.aas3_0.types.impl.*;
import java.util.Optional;

/**
 * A property is a data element that has a single value.
 *
 * <p>Constraints:
 *
 * <ul>
 *   <li>Constraint AASd-007: If both, the {@link #getValue()} and the {@link #getValueId()} are
 *       present then the value of {@link #getValue()} needs to be identical to the value of the
 *       referenced coded value in {@link #getValueId()}.
 * </ul>
 */
public interface IProperty extends IDataElement {
  /** Data type of the value */
  DataTypeDefXsd getValueType();

  void setValueType(DataTypeDefXsd valueType);

  /** The value of the property instance. */
  Optional<String> getValue();

  void setValue(String value);

  /**
   * Reference to the global unique ID of a coded value.
   *
   * <p>It is recommended to use a global reference.
   */
  Optional<IReference> getValueId();

  void setValueId(IReference valueId);
}

/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */
