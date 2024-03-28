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

public class EnhancedLangStringDefinitionTypeIec61360<EnhancementT> extends Enhanced<EnhancementT>
    implements ILangStringDefinitionTypeIec61360 {
  private final ILangStringDefinitionTypeIec61360 instance;

  public EnhancedLangStringDefinitionTypeIec61360(
      ILangStringDefinitionTypeIec61360 instance, EnhancementT enhancement) {
    super(enhancement);
    this.instance = instance;
  }

  @Override
  public String getLanguage() {
    return instance.getLanguage();
  }

  @Override
  public void setLanguage(String language) {
    instance.setLanguage(language);
  }

  @Override
  public String getText() {
    return instance.getText();
  }

  @Override
  public void setText(String text) {
    instance.setText(text);
  }

  public Iterable<IClass> descendOnce() {
    return instance.descendOnce();
  }

  public Iterable<IClass> descend() {
    return instance.descend();
  }

  public void accept(IVisitor visitor) {
    visitor.visitLangStringDefinitionTypeIec61360(instance);
  }

  public <ContextT> void accept(IVisitorWithContext<ContextT> visitor, ContextT context) {
    visitor.visitLangStringDefinitionTypeIec61360(instance, context);
  }

  public <T> T transform(ITransformer<T> transformer) {
    return transformer.transformLangStringDefinitionTypeIec61360(instance);
  }

  public <ContextT, T> T transform(
      ITransformerWithContext<ContextT, T> transformer, ContextT context) {
    return transformer.transformLangStringDefinitionTypeIec61360(instance, context);
  }
}

/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */
