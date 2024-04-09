/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */

package aas_core.aas3_0.visitation;

import aas_core.aas3_0.types.model.*;

/**
 * Perform double-dispatch to visit the concrete instances with context.
 *
 * @param <ContextT> structure of the context
 */
public abstract class AbstractVisitorWithContext<ContextT>
    implements IVisitorWithContext<ContextT> {
  public void visit(IClass that, ContextT context) {
    that.accept(this, context);
  }

  public abstract void visitExtension(IExtension that, ContextT context);

  public abstract void visitAdministrativeInformation(
      IAdministrativeInformation that, ContextT context);

  public abstract void visitQualifier(IQualifier that, ContextT context);

  public abstract void visitAssetAdministrationShell(
      IAssetAdministrationShell that, ContextT context);

  public abstract void visitAssetInformation(IAssetInformation that, ContextT context);

  public abstract void visitResource(IResource that, ContextT context);

  public abstract void visitSpecificAssetId(ISpecificAssetId that, ContextT context);

  public abstract void visitSubmodel(ISubmodel that, ContextT context);

  public abstract void visitRelationshipElement(IRelationshipElement that, ContextT context);

  public abstract void visitSubmodelElementList(ISubmodelElementList that, ContextT context);

  public abstract void visitSubmodelElementCollection(
      ISubmodelElementCollection that, ContextT context);

  public abstract void visitProperty(IProperty that, ContextT context);

  public abstract void visitMultiLanguageProperty(IMultiLanguageProperty that, ContextT context);

  public abstract void visitRange(IRange that, ContextT context);

  public abstract void visitReferenceElement(IReferenceElement that, ContextT context);

  public abstract void visitBlob(IBlob that, ContextT context);

  public abstract void visitFile(IFile that, ContextT context);

  public abstract void visitAnnotatedRelationshipElement(
      IAnnotatedRelationshipElement that, ContextT context);

  public abstract void visitEntity(IEntity that, ContextT context);

  public abstract void visitEventPayload(IEventPayload that, ContextT context);

  public abstract void visitBasicEventElement(IBasicEventElement that, ContextT context);

  public abstract void visitOperation(IOperation that, ContextT context);

  public abstract void visitOperationVariable(IOperationVariable that, ContextT context);

  public abstract void visitCapability(ICapability that, ContextT context);

  public abstract void visitConceptDescription(IConceptDescription that, ContextT context);

  public abstract void visitReference(IReference that, ContextT context);

  public abstract void visitKey(IKey that, ContextT context);

  public abstract void visitLangStringNameType(ILangStringNameType that, ContextT context);

  public abstract void visitLangStringTextType(ILangStringTextType that, ContextT context);

  public abstract void visitEnvironment(IEnvironment that, ContextT context);

  public abstract void visitEmbeddedDataSpecification(
      IEmbeddedDataSpecification that, ContextT context);

  public abstract void visitLevelType(ILevelType that, ContextT context);

  public abstract void visitValueReferencePair(IValueReferencePair that, ContextT context);

  public abstract void visitValueList(IValueList that, ContextT context);

  public abstract void visitLangStringPreferredNameTypeIec61360(
      ILangStringPreferredNameTypeIec61360 that, ContextT context);

  public abstract void visitLangStringShortNameTypeIec61360(
      ILangStringShortNameTypeIec61360 that, ContextT context);

  public abstract void visitLangStringDefinitionTypeIec61360(
      ILangStringDefinitionTypeIec61360 that, ContextT context);

  public abstract void visitDataSpecificationIec61360(
      IDataSpecificationIec61360 that, ContextT context);
}

/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */