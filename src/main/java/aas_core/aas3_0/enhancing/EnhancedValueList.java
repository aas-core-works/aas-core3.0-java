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
import java.util.List;

public class EnhancedValueList<EnhancementT> extends Enhanced<EnhancementT> implements IValueList {
  private final IValueList instance;

  public EnhancedValueList(IValueList instance, EnhancementT enhancement) {
    super(enhancement);
    this.instance = instance;
  }

  @Override
  public List<IValueReferencePair> getValueReferencePairs() {
    return instance.getValueReferencePairs();
  }

  @Override
  public void setValueReferencePairs(List<IValueReferencePair> valueReferencePairs) {
    instance.setValueReferencePairs(valueReferencePairs);
  }

  public Iterable<IClass> descendOnce() {
    return instance.descendOnce();
  }

  public Iterable<IClass> descend() {
    return instance.descend();
  }

  public void accept(IVisitor visitor) {
    visitor.visitValueList(instance);
  }

  public <ContextT> void accept(IVisitorWithContext<ContextT> visitor, ContextT context) {
    visitor.visitValueList(instance, context);
  }

  public <T> T transform(ITransformer<T> transformer) {
    return transformer.transformValueList(instance);
  }

  public <ContextT, T> T transform(
      ITransformerWithContext<ContextT, T> transformer, ContextT context) {
    return transformer.transformValueList(instance, context);
  }
}

/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */
