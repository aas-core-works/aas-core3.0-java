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

public class EnhancedBasicEventElement<EnhancementT> extends Enhanced<EnhancementT>
    implements IBasicEventElement {
  private final IBasicEventElement instance;

  public EnhancedBasicEventElement(IBasicEventElement instance, EnhancementT enhancement) {
    super(enhancement);
    this.instance = instance;
  }

  @Override
  public Optional<List<IExtension>> getExtensions() {
    return instance.getExtensions();
  }

  @Override
  public void setExtensions(List<IExtension> extensions) {
    instance.setExtensions(extensions);
  }

  @Override
  public Optional<String> getCategory() {
    return instance.getCategory();
  }

  @Override
  public void setCategory(String category) {
    instance.setCategory(category);
  }

  @Override
  public Optional<String> getIdShort() {
    return instance.getIdShort();
  }

  @Override
  public void setIdShort(String idShort) {
    instance.setIdShort(idShort);
  }

  @Override
  public Optional<List<ILangStringNameType>> getDisplayName() {
    return instance.getDisplayName();
  }

  @Override
  public void setDisplayName(List<ILangStringNameType> displayName) {
    instance.setDisplayName(displayName);
  }

  @Override
  public Optional<List<ILangStringTextType>> getDescription() {
    return instance.getDescription();
  }

  @Override
  public void setDescription(List<ILangStringTextType> description) {
    instance.setDescription(description);
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
  public Optional<List<IQualifier>> getQualifiers() {
    return instance.getQualifiers();
  }

  @Override
  public void setQualifiers(List<IQualifier> qualifiers) {
    instance.setQualifiers(qualifiers);
  }

  @Override
  public Optional<List<IEmbeddedDataSpecification>> getEmbeddedDataSpecifications() {
    return instance.getEmbeddedDataSpecifications();
  }

  @Override
  public void setEmbeddedDataSpecifications(
      List<IEmbeddedDataSpecification> embeddedDataSpecifications) {
    instance.setEmbeddedDataSpecifications(embeddedDataSpecifications);
  }

  @Override
  public IReference getObserved() {
    return instance.getObserved();
  }

  @Override
  public void setObserved(IReference observed) {
    instance.setObserved(observed);
  }

  @Override
  public Direction getDirection() {
    return instance.getDirection();
  }

  @Override
  public void setDirection(Direction direction) {
    instance.setDirection(direction);
  }

  @Override
  public StateOfEvent getState() {
    return instance.getState();
  }

  @Override
  public void setState(StateOfEvent state) {
    instance.setState(state);
  }

  @Override
  public Optional<String> getMessageTopic() {
    return instance.getMessageTopic();
  }

  @Override
  public void setMessageTopic(String messageTopic) {
    instance.setMessageTopic(messageTopic);
  }

  @Override
  public Optional<IReference> getMessageBroker() {
    return instance.getMessageBroker();
  }

  @Override
  public void setMessageBroker(IReference messageBroker) {
    instance.setMessageBroker(messageBroker);
  }

  @Override
  public Optional<String> getLastUpdate() {
    return instance.getLastUpdate();
  }

  @Override
  public void setLastUpdate(String lastUpdate) {
    instance.setLastUpdate(lastUpdate);
  }

  @Override
  public Optional<String> getMinInterval() {
    return instance.getMinInterval();
  }

  @Override
  public void setMinInterval(String minInterval) {
    instance.setMinInterval(minInterval);
  }

  @Override
  public Optional<String> getMaxInterval() {
    return instance.getMaxInterval();
  }

  @Override
  public void setMaxInterval(String maxInterval) {
    instance.setMaxInterval(maxInterval);
  }

  public Iterable<IExtension> overExtensionsOrEmpty() {
    return instance.overExtensionsOrEmpty();
  }

  public Iterable<ILangStringNameType> overDisplayNameOrEmpty() {
    return instance.overDisplayNameOrEmpty();
  }

  public Iterable<ILangStringTextType> overDescriptionOrEmpty() {
    return instance.overDescriptionOrEmpty();
  }

  public Iterable<IReference> overSupplementalSemanticIdsOrEmpty() {
    return instance.overSupplementalSemanticIdsOrEmpty();
  }

  public Iterable<IQualifier> overQualifiersOrEmpty() {
    return instance.overQualifiersOrEmpty();
  }

  public Iterable<IEmbeddedDataSpecification> overEmbeddedDataSpecificationsOrEmpty() {
    return instance.overEmbeddedDataSpecificationsOrEmpty();
  }

  public Iterable<IClass> descendOnce() {
    return instance.descendOnce();
  }

  public Iterable<IClass> descend() {
    return instance.descend();
  }

  public void accept(IVisitor visitor) {
    visitor.visitBasicEventElement(instance);
  }

  public <ContextT> void accept(IVisitorWithContext<ContextT> visitor, ContextT context) {
    visitor.visitBasicEventElement(instance, context);
  }

  public <T> T transform(ITransformer<T> transformer) {
    return transformer.transformBasicEventElement(instance);
  }

  public <ContextT, T> T transform(
      ITransformerWithContext<ContextT, T> transformer, ContextT context) {
    return transformer.transformBasicEventElement(instance, context);
  }
}

/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */
