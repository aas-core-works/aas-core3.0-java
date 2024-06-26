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
import java.util.Optional;

public class EnhancedSpecificAssetId<EnhancementT> extends Enhanced<EnhancementT>
    implements ISpecificAssetId {
  private final ISpecificAssetId instance;

  public EnhancedSpecificAssetId(ISpecificAssetId instance, EnhancementT enhancement) {
    super(enhancement);
    this.instance = instance;
  }

  @Override
  public Optional<IReference> getSemanticId() {
    return instance.getSemanticId();
  }

  @Override
  public void setSemanticId(IReference semanticId) {
    instance.setSemanticId(semanticId);
  }

  @Override
  public Optional<List<IReference>> getSupplementalSemanticIds() {
    return instance.getSupplementalSemanticIds();
  }

  @Override
  public void setSupplementalSemanticIds(List<IReference> supplementalSemanticIds) {
    instance.setSupplementalSemanticIds(supplementalSemanticIds);
  }

  @Override
  public String getName() {
    return instance.getName();
  }

  @Override
  public void setName(String name) {
    instance.setName(name);
  }

  @Override
  public String getValue() {
    return instance.getValue();
  }

  @Override
  public void setValue(String value) {
    instance.setValue(value);
  }

  @Override
  public Optional<IReference> getExternalSubjectId() {
    return instance.getExternalSubjectId();
  }

  @Override
  public void setExternalSubjectId(IReference externalSubjectId) {
    instance.setExternalSubjectId(externalSubjectId);
  }

  public Iterable<IReference> overSupplementalSemanticIdsOrEmpty() {
    return instance.overSupplementalSemanticIdsOrEmpty();
  }

  public Iterable<IClass> descendOnce() {
    return instance.descendOnce();
  }

  public Iterable<IClass> descend() {
    return instance.descend();
  }

  public void accept(IVisitor visitor) {
    visitor.visitSpecificAssetId(instance);
  }

  public <ContextT> void accept(IVisitorWithContext<ContextT> visitor, ContextT context) {
    visitor.visitSpecificAssetId(instance, context);
  }

  public <T> T transform(ITransformer<T> transformer) {
    return transformer.transformSpecificAssetId(instance);
  }

  public <ContextT, T> T transform(
      ITransformerWithContext<ContextT, T> transformer, ContextT context) {
    return transformer.transformSpecificAssetId(instance, context);
  }
}

/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */
