/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */

package aas_core.aas3_0.enhancing;

import aas_core.aas3_0.types.enums.*;
import aas_core.aas3_0.types.impl.*;
import aas_core.aas3_0.types.model.*;
import aas_core.aas3_0.visitation.ITransformer;
import aas_core.aas3_0.visitation.ITransformerWithContext;
import aas_core.aas3_0.visitation.IVisitor;
import aas_core.aas3_0.visitation.IVisitorWithContext;

public class EnhancedLevelType<EnhancementT> extends Enhanced<EnhancementT> implements ILevelType {
  private final ILevelType instance;

  public EnhancedLevelType(ILevelType instance, EnhancementT enhancement) {
    super(enhancement);
    this.instance = instance;
  }

  @Override
  public Boolean getMin() {
    return instance.getMin();
  }

  @Override
  public void setMin(Boolean min) {
    instance.setMin(min);
  }

  @Override
  public Boolean getNom() {
    return instance.getNom();
  }

  @Override
  public void setNom(Boolean nom) {
    instance.setNom(nom);
  }

  @Override
  public Boolean getTyp() {
    return instance.getTyp();
  }

  @Override
  public void setTyp(Boolean typ) {
    instance.setTyp(typ);
  }

  @Override
  public Boolean getMax() {
    return instance.getMax();
  }

  @Override
  public void setMax(Boolean max) {
    instance.setMax(max);
  }

  public Iterable<IClass> descendOnce() {
    return instance.descendOnce();
  }

  public Iterable<IClass> descend() {
    return instance.descend();
  }

  public void accept(IVisitor visitor) {
    visitor.visitLevelType(instance);
  }

  public <ContextT> void accept(IVisitorWithContext<ContextT> visitor, ContextT context) {
    visitor.visitLevelType(instance, context);
  }

  public <T> T transform(ITransformer<T> transformer) {
    return transformer.transformLevelType(instance);
  }

  public <ContextT, T> T transform(
      ITransformerWithContext<ContextT, T> transformer, ContextT context) {
    return transformer.transformLevelType(instance, context);
  }
}

/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */
