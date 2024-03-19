/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */

package aas_core.aas3_0.types.model;

import aas_core.aas3_0.types.enums.*;
import aas_core.aas3_0.types.impl.*;
import aas_core.aas3_0.types.model.*;
import java.util.List;
import javax.annotation.Generated;
import aas_core.aas3_0.types.model.IClass;
import java.util.Optional;

/**
 * A set of value reference pairs.
 */
@Generated("generated by aas-core-codegen")
public interface IValueList extends IClass {
  /**
   * A pair of a value together with its global unique id.
   */
  List<IValueReferencePair> getValueReferencePairs();

  void setValueReferencePairs(List<IValueReferencePair> valueReferencePairs);
}

/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */