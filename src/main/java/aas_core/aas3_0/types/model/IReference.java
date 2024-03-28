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
 * Reference to either a model element of the same or another AAS or to an external entity.
 *
 * <p>A reference is an ordered list of keys.
 *
 * <p>A model reference is an ordered list of keys, each key referencing an element. The complete
 * list of keys may for example be concatenated to a path that then gives unique access to an
 * element.
 *
 * <p>An external reference is a reference to an external entity.
 *
 * <p>Constraints:
 *
 * <ul>
 *   <li>Constraint AASd-121: For {@link aas_core.aas3_0.types.impl.Reference}'s the value of {@link
 *       aas_core.aas3_0.types.impl.Key#getType()} of the first key of " {@link #getKeys()} shall be
 *       one of {@link aas_core.aas3_0.constants.Constants#globallyIdentifiables}.
 *   <li>Constraint AASd-122: For external references, i.e. {@link
 *       aas_core.aas3_0.types.impl.Reference}'s with {@link #getType()} = {@link
 *       aas_core.aas3_0.types.enums.ReferenceTypes#EXTERNAL_REFERENCE}, the value of {@link
 *       aas_core.aas3_0.types.impl.Key#getType()} of the first key of {@link #getKeys()} shall be
 *       one of {@link aas_core.aas3_0.constants.Constants#genericGloballyIdentifiables}.
 *   <li>Constraint AASd-123: For model references, i.e. {@link
 *       aas_core.aas3_0.types.impl.Reference}'s with {@link #getType()} = {@link
 *       aas_core.aas3_0.types.enums.ReferenceTypes#MODEL_REFERENCE}, the value of {@link
 *       aas_core.aas3_0.types.impl.Key#getType()} of the first key of {@link #getKeys()} shall be
 *       one of {@link aas_core.aas3_0.constants.Constants#aasIdentifiables}.
 *   <li>Constraint AASd-124: For external references, i.e. {@link
 *       aas_core.aas3_0.types.impl.Reference}'s with {@link #getType()} = {@link
 *       aas_core.aas3_0.types.enums.ReferenceTypes#EXTERNAL_REFERENCE}, the last key of {@link
 *       #getKeys()} shall be either one of {@link
 *       aas_core.aas3_0.constants.Constants#genericGloballyIdentifiables} or one of {@link
 *       aas_core.aas3_0.constants.Constants#genericFragmentKeys}.
 *   <li>Constraint AASd-125: For model references, i.e. {@link
 *       aas_core.aas3_0.types.impl.Reference}'s with {@link #getType()} = {@link
 *       aas_core.aas3_0.types.enums.ReferenceTypes#MODEL_REFERENCE}, with more than one key in
 *       {@link #getKeys()} the value of {@link aas_core.aas3_0.types.impl.Key#getType()} of each of
 *       the keys following the first key of {@link #getKeys()} shall be one of {@link
 *       aas_core.aas3_0.constants.Constants#fragmentKeys}.
 *       <p>Constraint AASd-125 ensures that the shortest path is used.
 *   <li>Constraint AASd-126: For model references, i.e. {@link
 *       aas_core.aas3_0.types.impl.Reference}'s with {@link #getType()} = {@link
 *       aas_core.aas3_0.types.enums.ReferenceTypes#MODEL_REFERENCE}, with more than one key in
 *       {@link #getKeys()} the value of {@link aas_core.aas3_0.types.impl.Key#getType()} of the
 *       last key in the reference key chain may be one of {@link
 *       aas_core.aas3_0.constants.Constants#genericFragmentKeys} or no key at all shall have a
 *       value out of {@link aas_core.aas3_0.constants.Constants#genericFragmentKeys}.
 *   <li>Constraint AASd-127: For model references, i.e. {@link
 *       aas_core.aas3_0.types.impl.Reference}'s with {@link #getType()} = {@link
 *       aas_core.aas3_0.types.enums.ReferenceTypes#MODEL_REFERENCE}, with more than one key in
 *       {@link #getKeys()} a key with {@link aas_core.aas3_0.types.impl.Key#getType()} {@link
 *       aas_core.aas3_0.types.enums.KeyTypes#FRAGMENT_REFERENCE} shall be preceded by a key with
 *       {@link aas_core.aas3_0.types.impl.Key#getType()} {@link
 *       aas_core.aas3_0.types.enums.KeyTypes#FILE} or {@link
 *       aas_core.aas3_0.types.enums.KeyTypes#BLOB}. All other AAS fragments, i.e. {@link
 *       aas_core.aas3_0.types.impl.Key#getType()} values out of {@link
 *       aas_core.aas3_0.constants.Constants#aasSubmodelElementsAsKeys}, do not support fragments.
 *       <p>Which kind of fragments are supported depends on the content type and the specification
 *       of allowed fragment identifiers for the corresponding resource being referenced via the
 *       reference.
 *   <li>Constraint AASd-128: For model references, i.e. {@link
 *       aas_core.aas3_0.types.impl.Reference}'s with {@link #getType()} = {@link
 *       aas_core.aas3_0.types.enums.ReferenceTypes#MODEL_REFERENCE}, the {@link
 *       aas_core.aas3_0.types.impl.Key#getValue()} of a {@link aas_core.aas3_0.types.impl.Key}
 *       preceded by a {@link aas_core.aas3_0.types.impl.Key} with {@link
 *       aas_core.aas3_0.types.impl.Key#getType()} = {@link
 *       aas_core.aas3_0.types.enums.KeyTypes#SUBMODEL_ELEMENT_LIST} is an integer number denoting
 *       the position in the array of the submodel element list.
 * </ul>
 */
public interface IReference extends IClass {
  /**
   * Type of the reference.
   *
   * <p>Denotes, whether reference is an external reference or a model reference.
   */
  ReferenceTypes getType();

  void setType(ReferenceTypes type);

  /**
   * {@link aas_core.aas3_0.types.model.IHasSemantics#getSemanticId()} of the referenced model
   * element ({@link #getType()} = {@link
   * aas_core.aas3_0.types.enums.ReferenceTypes#MODEL_REFERENCE}).
   *
   * <p>For external references there typically is no semantic ID.
   *
   * <p>It is recommended to use a external reference.
   */
  Optional<IReference> getReferredSemanticId();

  void setReferredSemanticId(IReference referredSemanticId);

  /** Unique references in their name space. */
  List<IKey> getKeys();

  void setKeys(List<IKey> keys);
}

/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */