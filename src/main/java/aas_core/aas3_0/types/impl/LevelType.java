/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */

package aas_core.aas3_0.types.impl;

import aas_core.aas3_0.visitation.IVisitor;
import aas_core.aas3_0.visitation.IVisitorWithContext;
import aas_core.aas3_0.visitation.ITransformer;
import aas_core.aas3_0.visitation.ITransformerWithContext;
import aas_core.aas3_0.types.enums.*;
import aas_core.aas3_0.types.impl.*;
import aas_core.aas3_0.types.model.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Objects;
import javax.annotation.Generated;
import aas_core.aas3_0.types.model.ILevelType;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Value represented by up to four variants of a numeric value in a specific role:
 * {@literal MIN}, {@literal NOM}, {@literal TYP} and {@literal MAX}. True means that the value is available,
 * false means the value is not available.
 *
 * <p>EXAMPLE from [IEC61360-1]: In the case of having a property which is
 * of the LEVEL_TYPE min/max − expressing a range − only those two values
 * need to be provided.
 *
 * <p>This is how AAS deals with the following combinations of level types:
 * <ul>
 *   <li> Either all attributes are false. In this case the concept is mapped
 *   to a {@link Property} and level type is ignored.
 *   <li> At most one of the attributes is set to true. In this case
 *   the concept is mapped to a {@link Property}.
 *   <li> Min and max are set to true. In this case the concept is mapped
 *   to a {@link Range}.
 *   <li> More than one attribute is set to true but not min and max only
 *   (see second case). In this case the concept is mapped
 *   to a {@link SubmodelElementCollection} with the corresponding
 *   number of Properties.
 *   Example: If attribute {@link LevelType#getMin min} and {@link LevelType#getNom nom} are set to true
 *   then the concept is mapped to a {@link SubmodelElementCollection}
 *   with two Properties within: min and nom.
 *   The data type of both Properties is the same.
 * </ul>
 *
 * <p>In the cases 2. and 4. the {@link Property#getSemanticId semanticId} of the Property
 * or Properties within the {@link SubmodelElementCollection} needs to include
 * information about the level type. Otherwise, the semantics is not described
 * in a unique way. Please refer to the specification.
 */
@Generated("generated by aas-core-codegen")
public class LevelType implements ILevelType {
  /**
   * Minimum of the value
   */
  private Boolean min;

  /**
   * Nominal value (value as designated)
   */
  private Boolean nom;

  /**
   * Value as typically present
   */
  private Boolean typ;

  /**
   * Maximum of the value
   */
  private Boolean max;

  public LevelType(
    Boolean min,
    Boolean nom,
    Boolean typ,
    Boolean max) {
    this.min = Objects.requireNonNull(
      min,
      "Argument \"min\" must be non-null.");
    this.nom = Objects.requireNonNull(
      nom,
      "Argument \"nom\" must be non-null.");
    this.typ = Objects.requireNonNull(
      typ,
      "Argument \"typ\" must be non-null.");
    this.max = Objects.requireNonNull(
      max,
      "Argument \"max\" must be non-null.");
  }

  @Override
  public Boolean getMin() {
    return min;
  }

  @Override
  public void setMin(Boolean min) {
    this.min = Objects.requireNonNull(
      min,
      "Argument \"min\" must be non-null.");
  }

  @Override
  public Boolean getNom() {
    return nom;
  }

  @Override
  public void setNom(Boolean nom) {
    this.nom = Objects.requireNonNull(
      nom,
      "Argument \"nom\" must be non-null.");
  }

  @Override
  public Boolean getTyp() {
    return typ;
  }

  @Override
  public void setTyp(Boolean typ) {
    this.typ = Objects.requireNonNull(
      typ,
      "Argument \"typ\" must be non-null.");
  }

  @Override
  public Boolean getMax() {
    return max;
  }

  @Override
  public void setMax(Boolean max) {
    this.max = Objects.requireNonNull(
      max,
      "Argument \"max\" must be non-null.");
  }

  /**
   * Iterate recursively over all the class instances referenced from this instance.
   */
  public Iterable<IClass> descend() {
    return Collections.emptyList();
  }

  /**
   * Iterate over all the class instances referenced from this instance.
   */
  public Iterable<IClass> descendOnce() {
    return Collections.emptyList();
  }

  /**
   * Accept the {@code visitor} to visit this instance for double dispatch.
   **/
  @Override
  public void accept(IVisitor visitor) {
    visitor.visitLevelType(this);
  }

  /**
   * Accept the {@code visitor} to visit this instance for double dispatch
   * with the {@code context}.
   **/
  @Override
  public <ContextT> void accept(
      IVisitorWithContext<ContextT> visitor,
      ContextT context) {
    visitor.visitLevelType(this, context);
  }

  /**
   * Accept the {@code transformer} to visit this instance for double dispatch.
   **/
  @Override
  public <T> T transform(ITransformer<T> transformer) {
    return transformer.transformLevelType(this);
  }

  /**
   * Accept the {@code transformer} to visit this instance for double dispatch
   * with the {@code context}.
   **/
  @Override
  public <ContextT, T> T transform(
      ITransformerWithContext<ContextT, T> transformer,
      ContextT context) {
    return transformer.transformLevelType(this, context);
  }
}

/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */