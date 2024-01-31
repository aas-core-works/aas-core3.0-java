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
 * Element that can be extended by using data specification templates.
 *
 * <p>A data specification template defines a named set of additional attributes an
 * element may or shall have. The data specifications used are explicitly specified
 * with their global ID.
 */
@Generated("generated by aas-core-codegen")
public interface IHasDataSpecification extends IClass {
  /**
   * Embedded data specification.
   */
  Optional<List<IEmbeddedDataSpecification>> getEmbeddedDataSpecifications();

  void setEmbeddedDataSpecifications(List<IEmbeddedDataSpecification> embeddedDataSpecifications);

  /**
   * Iterate over embeddedDataSpecifications, if set, and otherwise return an empty enumerable.
   */
  Iterable<IEmbeddedDataSpecification> overEmbeddedDataSpecificationsOrEmpty();
}

/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */