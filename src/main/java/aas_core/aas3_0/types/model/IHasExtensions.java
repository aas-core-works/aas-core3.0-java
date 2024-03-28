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
 * Element that can be extended by proprietary extensions.
 *
 * <p>Extensions are proprietary, i.e. they do not support global interoperability.
 */
public interface IHasExtensions extends IClass {
  /** An extension of the element. */
  Optional<List<IExtension>> getExtensions();

  void setExtensions(List<IExtension> extensions);

  /** Iterate over extensions, if set, and otherwise return an empty enumerable. */
  Iterable<IExtension> overExtensionsOrEmpty();
}

/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */
