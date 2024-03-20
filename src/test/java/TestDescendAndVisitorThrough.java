/*
 * This code has been automatically generated by testgen.
 * Do NOT edit or append.
 */
 
import aas_core.aas3_0.types.impl.*;
import aas_core.aas3_0.types.model.IClass;
import aas_core.aas3_0.types.model.IIdentifiable;
import aas_core.aas3_0.types.model.IReferable;
import aas_core.aas3_0.visitation.VisitorThrough;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


import static org.junit.jupiter.api.Assertions.*;


public class TestDescendAndVisitorThrough {
        private void compareOrRerecordTrace(IClass instance, Path expectedPath) throws IOException  {
          final StringBuilder stringBuilder = new StringBuilder();
          for (IClass descendant : instance.descend()) {
            if (descendant instanceof IIdentifiable) {
              stringBuilder.append(descendant.getClass().getSimpleName())
              .append(" with ID ").append(((IIdentifiable) descendant).getId())
              .append("\n");
            } else if (descendant instanceof IReferable) {
              stringBuilder.append(descendant.getClass().getSimpleName())
              .append(" with ID-short ")
              .append(((IReferable) descendant).getIdShort())
              .append("\n");
            } else {
              stringBuilder
                .append(descendant.getClass().getSimpleName())
                .append("\n");
            }
          }
  
          final String got = stringBuilder.toString();
          if (Common.RECORD_MODE) {
            Files.createDirectories(expectedPath.getParent());
            Files.write(expectedPath, got.getBytes());
          }else {
              if (!Files.exists(expectedPath)) {
                throw new FileNotFoundException("The file with the recorded value does not exist: " + expectedPath);
              }
              final String expected = Files.readAllLines(expectedPath).stream().collect(Collectors.joining("\n"));
              assertEquals(expected.replace("\n",""), got.replace("\n",""));
          }
        }

        private void assertDescendAndVisitorThroughSame(IClass instance) {
          final List<String> logFromDescend = new ArrayList<>();
          for (IClass subInstance : instance.descend()) {
            logFromDescend.add(trace(subInstance));
          }
          final TracingVisitorThrough visitor = new TracingVisitorThrough();
          visitor.visit(instance);
          final List<String> traceFromVisitor = visitor.log;
          assertFalse(traceFromVisitor.isEmpty());
          assertEquals(trace(instance), traceFromVisitor.get(0));
          traceFromVisitor.remove(0);
          assertTrue(logFromDescend.equals(traceFromVisitor));
        }

        private String trace(IClass instance) {
          if (instance instanceof IIdentifiable) {
            return instance.getClass().getSimpleName() + " with ID " + (((IIdentifiable) instance).getId());
          } else if (instance instanceof IReferable) {
            return instance.getClass().getSimpleName() + " with ID-short " + (((IReferable) instance).getIdShort());
          } else {
            return instance.getClass().getSimpleName();
          }
        }

        private class TracingVisitorThrough extends VisitorThrough {
          public final List<String> log = new ArrayList<>();
  
          @Override
          public void visit(IClass that) {
            log.add(trace(that));
            super.visit(that);
          }
        }

        @Test
        public void testDescendOfExtension() throws IOException {
          final Extension instance = CommonJsonization.loadMaximalExtension();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "Extension",
              "maximal.json.trace"));
        }  // public void testDescendOfExtension

        @Test
        public void testDescendAgainstVisitorThroughForExtension() throws IOException {
          final Extension instance = CommonJsonization.loadMaximalExtension();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForExtension

        @Test
        public void testDescendOfAdministrativeInformation() throws IOException {
          final AdministrativeInformation instance = CommonJsonization.loadMaximalAdministrativeInformation();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "AdministrativeInformation",
              "maximal.json.trace"));
        }  // public void testDescendOfAdministrativeInformation

        @Test
        public void testDescendAgainstVisitorThroughForAdministrativeInformation() throws IOException {
          final AdministrativeInformation instance = CommonJsonization.loadMaximalAdministrativeInformation();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForAdministrativeInformation

        @Test
        public void testDescendOfQualifier() throws IOException {
          final Qualifier instance = CommonJsonization.loadMaximalQualifier();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "Qualifier",
              "maximal.json.trace"));
        }  // public void testDescendOfQualifier

        @Test
        public void testDescendAgainstVisitorThroughForQualifier() throws IOException {
          final Qualifier instance = CommonJsonization.loadMaximalQualifier();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForQualifier

        @Test
        public void testDescendOfAssetAdministrationShell() throws IOException {
          final AssetAdministrationShell instance = CommonJsonization.loadMaximalAssetAdministrationShell();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "AssetAdministrationShell",
              "maximal.json.trace"));
        }  // public void testDescendOfAssetAdministrationShell

        @Test
        public void testDescendAgainstVisitorThroughForAssetAdministrationShell() throws IOException {
          final AssetAdministrationShell instance = CommonJsonization.loadMaximalAssetAdministrationShell();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForAssetAdministrationShell

        @Test
        public void testDescendOfAssetInformation() throws IOException {
          final AssetInformation instance = CommonJsonization.loadMaximalAssetInformation();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "AssetInformation",
              "maximal.json.trace"));
        }  // public void testDescendOfAssetInformation

        @Test
        public void testDescendAgainstVisitorThroughForAssetInformation() throws IOException {
          final AssetInformation instance = CommonJsonization.loadMaximalAssetInformation();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForAssetInformation

        @Test
        public void testDescendOfResource() throws IOException {
          final Resource instance = CommonJsonization.loadMaximalResource();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "Resource",
              "maximal.json.trace"));
        }  // public void testDescendOfResource

        @Test
        public void testDescendAgainstVisitorThroughForResource() throws IOException {
          final Resource instance = CommonJsonization.loadMaximalResource();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForResource

        @Test
        public void testDescendOfSpecificAssetId() throws IOException {
          final SpecificAssetId instance = CommonJsonization.loadMaximalSpecificAssetId();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "SpecificAssetId",
              "maximal.json.trace"));
        }  // public void testDescendOfSpecificAssetId

        @Test
        public void testDescendAgainstVisitorThroughForSpecificAssetId() throws IOException {
          final SpecificAssetId instance = CommonJsonization.loadMaximalSpecificAssetId();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForSpecificAssetId

        @Test
        public void testDescendOfSubmodel() throws IOException {
          final Submodel instance = CommonJsonization.loadMaximalSubmodel();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "Submodel",
              "maximal.json.trace"));
        }  // public void testDescendOfSubmodel

        @Test
        public void testDescendAgainstVisitorThroughForSubmodel() throws IOException {
          final Submodel instance = CommonJsonization.loadMaximalSubmodel();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForSubmodel

        @Test
        public void testDescendOfRelationshipElement() throws IOException {
          final RelationshipElement instance = CommonJsonization.loadMaximalRelationshipElement();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "RelationshipElement",
              "maximal.json.trace"));
        }  // public void testDescendOfRelationshipElement

        @Test
        public void testDescendAgainstVisitorThroughForRelationshipElement() throws IOException {
          final RelationshipElement instance = CommonJsonization.loadMaximalRelationshipElement();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForRelationshipElement

        @Test
        public void testDescendOfSubmodelElementList() throws IOException {
          final SubmodelElementList instance = CommonJsonization.loadMaximalSubmodelElementList();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "SubmodelElementList",
              "maximal.json.trace"));
        }  // public void testDescendOfSubmodelElementList

        @Test
        public void testDescendAgainstVisitorThroughForSubmodelElementList() throws IOException {
          final SubmodelElementList instance = CommonJsonization.loadMaximalSubmodelElementList();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForSubmodelElementList

        @Test
        public void testDescendOfSubmodelElementCollection() throws IOException {
          final SubmodelElementCollection instance = CommonJsonization.loadMaximalSubmodelElementCollection();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "SubmodelElementCollection",
              "maximal.json.trace"));
        }  // public void testDescendOfSubmodelElementCollection

        @Test
        public void testDescendAgainstVisitorThroughForSubmodelElementCollection() throws IOException {
          final SubmodelElementCollection instance = CommonJsonization.loadMaximalSubmodelElementCollection();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForSubmodelElementCollection

        @Test
        public void testDescendOfProperty() throws IOException {
          final Property instance = CommonJsonization.loadMaximalProperty();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "Property",
              "maximal.json.trace"));
        }  // public void testDescendOfProperty

        @Test
        public void testDescendAgainstVisitorThroughForProperty() throws IOException {
          final Property instance = CommonJsonization.loadMaximalProperty();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForProperty

        @Test
        public void testDescendOfMultiLanguageProperty() throws IOException {
          final MultiLanguageProperty instance = CommonJsonization.loadMaximalMultiLanguageProperty();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "MultiLanguageProperty",
              "maximal.json.trace"));
        }  // public void testDescendOfMultiLanguageProperty

        @Test
        public void testDescendAgainstVisitorThroughForMultiLanguageProperty() throws IOException {
          final MultiLanguageProperty instance = CommonJsonization.loadMaximalMultiLanguageProperty();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForMultiLanguageProperty

        @Test
        public void testDescendOfRange() throws IOException {
          final Range instance = CommonJsonization.loadMaximalRange();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "Range",
              "maximal.json.trace"));
        }  // public void testDescendOfRange

        @Test
        public void testDescendAgainstVisitorThroughForRange() throws IOException {
          final Range instance = CommonJsonization.loadMaximalRange();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForRange

        @Test
        public void testDescendOfReferenceElement() throws IOException {
          final ReferenceElement instance = CommonJsonization.loadMaximalReferenceElement();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "ReferenceElement",
              "maximal.json.trace"));
        }  // public void testDescendOfReferenceElement

        @Test
        public void testDescendAgainstVisitorThroughForReferenceElement() throws IOException {
          final ReferenceElement instance = CommonJsonization.loadMaximalReferenceElement();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForReferenceElement

        @Test
        public void testDescendOfBlob() throws IOException {
          final Blob instance = CommonJsonization.loadMaximalBlob();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "Blob",
              "maximal.json.trace"));
        }  // public void testDescendOfBlob

        @Test
        public void testDescendAgainstVisitorThroughForBlob() throws IOException {
          final Blob instance = CommonJsonization.loadMaximalBlob();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForBlob

        @Test
        public void testDescendOfFile() throws IOException {
          final File instance = CommonJsonization.loadMaximalFile();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "File",
              "maximal.json.trace"));
        }  // public void testDescendOfFile

        @Test
        public void testDescendAgainstVisitorThroughForFile() throws IOException {
          final File instance = CommonJsonization.loadMaximalFile();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForFile

        @Test
        public void testDescendOfAnnotatedRelationshipElement() throws IOException {
          final AnnotatedRelationshipElement instance = CommonJsonization.loadMaximalAnnotatedRelationshipElement();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "AnnotatedRelationshipElement",
              "maximal.json.trace"));
        }  // public void testDescendOfAnnotatedRelationshipElement

        @Test
        public void testDescendAgainstVisitorThroughForAnnotatedRelationshipElement() throws IOException {
          final AnnotatedRelationshipElement instance = CommonJsonization.loadMaximalAnnotatedRelationshipElement();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForAnnotatedRelationshipElement

        @Test
        public void testDescendOfEntity() throws IOException {
          final Entity instance = CommonJsonization.loadMaximalEntity();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "Entity",
              "maximal.json.trace"));
        }  // public void testDescendOfEntity

        @Test
        public void testDescendAgainstVisitorThroughForEntity() throws IOException {
          final Entity instance = CommonJsonization.loadMaximalEntity();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForEntity

        @Test
        public void testDescendOfEventPayload() throws IOException {
          final EventPayload instance = CommonJsonization.loadMaximalEventPayload();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "EventPayload",
              "maximal.json.trace"));
        }  // public void testDescendOfEventPayload

        @Test
        public void testDescendAgainstVisitorThroughForEventPayload() throws IOException {
          final EventPayload instance = CommonJsonization.loadMaximalEventPayload();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForEventPayload

        @Test
        public void testDescendOfBasicEventElement() throws IOException {
          final BasicEventElement instance = CommonJsonization.loadMaximalBasicEventElement();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "BasicEventElement",
              "maximal.json.trace"));
        }  // public void testDescendOfBasicEventElement

        @Test
        public void testDescendAgainstVisitorThroughForBasicEventElement() throws IOException {
          final BasicEventElement instance = CommonJsonization.loadMaximalBasicEventElement();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForBasicEventElement

        @Test
        public void testDescendOfOperation() throws IOException {
          final Operation instance = CommonJsonization.loadMaximalOperation();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "Operation",
              "maximal.json.trace"));
        }  // public void testDescendOfOperation

        @Test
        public void testDescendAgainstVisitorThroughForOperation() throws IOException {
          final Operation instance = CommonJsonization.loadMaximalOperation();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForOperation

        @Test
        public void testDescendOfOperationVariable() throws IOException {
          final OperationVariable instance = CommonJsonization.loadMaximalOperationVariable();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "OperationVariable",
              "maximal.json.trace"));
        }  // public void testDescendOfOperationVariable

        @Test
        public void testDescendAgainstVisitorThroughForOperationVariable() throws IOException {
          final OperationVariable instance = CommonJsonization.loadMaximalOperationVariable();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForOperationVariable

        @Test
        public void testDescendOfCapability() throws IOException {
          final Capability instance = CommonJsonization.loadMaximalCapability();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "Capability",
              "maximal.json.trace"));
        }  // public void testDescendOfCapability

        @Test
        public void testDescendAgainstVisitorThroughForCapability() throws IOException {
          final Capability instance = CommonJsonization.loadMaximalCapability();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForCapability

        @Test
        public void testDescendOfConceptDescription() throws IOException {
          final ConceptDescription instance = CommonJsonization.loadMaximalConceptDescription();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "ConceptDescription",
              "maximal.json.trace"));
        }  // public void testDescendOfConceptDescription

        @Test
        public void testDescendAgainstVisitorThroughForConceptDescription() throws IOException {
          final ConceptDescription instance = CommonJsonization.loadMaximalConceptDescription();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForConceptDescription

        @Test
        public void testDescendOfReference() throws IOException {
          final Reference instance = CommonJsonization.loadMaximalReference();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "Reference",
              "maximal.json.trace"));
        }  // public void testDescendOfReference

        @Test
        public void testDescendAgainstVisitorThroughForReference() throws IOException {
          final Reference instance = CommonJsonization.loadMaximalReference();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForReference

        @Test
        public void testDescendOfKey() throws IOException {
          final Key instance = CommonJsonization.loadMaximalKey();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "Key",
              "maximal.json.trace"));
        }  // public void testDescendOfKey

        @Test
        public void testDescendAgainstVisitorThroughForKey() throws IOException {
          final Key instance = CommonJsonization.loadMaximalKey();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForKey

        @Test
        public void testDescendOfLangStringNameType() throws IOException {
          final LangStringNameType instance = CommonJsonization.loadMaximalLangStringNameType();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "LangStringNameType",
              "maximal.json.trace"));
        }  // public void testDescendOfLangStringNameType

        @Test
        public void testDescendAgainstVisitorThroughForLangStringNameType() throws IOException {
          final LangStringNameType instance = CommonJsonization.loadMaximalLangStringNameType();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForLangStringNameType

        @Test
        public void testDescendOfLangStringTextType() throws IOException {
          final LangStringTextType instance = CommonJsonization.loadMaximalLangStringTextType();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "LangStringTextType",
              "maximal.json.trace"));
        }  // public void testDescendOfLangStringTextType

        @Test
        public void testDescendAgainstVisitorThroughForLangStringTextType() throws IOException {
          final LangStringTextType instance = CommonJsonization.loadMaximalLangStringTextType();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForLangStringTextType

        @Test
        public void testDescendOfEnvironment() throws IOException {
          final Environment instance = CommonJsonization.loadMaximalEnvironment();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "Environment",
              "maximal.json.trace"));
        }  // public void testDescendOfEnvironment

        @Test
        public void testDescendAgainstVisitorThroughForEnvironment() throws IOException {
          final Environment instance = CommonJsonization.loadMaximalEnvironment();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForEnvironment

        @Test
        public void testDescendOfEmbeddedDataSpecification() throws IOException {
          final EmbeddedDataSpecification instance = CommonJsonization.loadMaximalEmbeddedDataSpecification();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "EmbeddedDataSpecification",
              "maximal.json.trace"));
        }  // public void testDescendOfEmbeddedDataSpecification

        @Test
        public void testDescendAgainstVisitorThroughForEmbeddedDataSpecification() throws IOException {
          final EmbeddedDataSpecification instance = CommonJsonization.loadMaximalEmbeddedDataSpecification();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForEmbeddedDataSpecification

        @Test
        public void testDescendOfLevelType() throws IOException {
          final LevelType instance = CommonJsonization.loadMaximalLevelType();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "LevelType",
              "maximal.json.trace"));
        }  // public void testDescendOfLevelType

        @Test
        public void testDescendAgainstVisitorThroughForLevelType() throws IOException {
          final LevelType instance = CommonJsonization.loadMaximalLevelType();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForLevelType

        @Test
        public void testDescendOfValueReferencePair() throws IOException {
          final ValueReferencePair instance = CommonJsonization.loadMaximalValueReferencePair();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "ValueReferencePair",
              "maximal.json.trace"));
        }  // public void testDescendOfValueReferencePair

        @Test
        public void testDescendAgainstVisitorThroughForValueReferencePair() throws IOException {
          final ValueReferencePair instance = CommonJsonization.loadMaximalValueReferencePair();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForValueReferencePair

        @Test
        public void testDescendOfValueList() throws IOException {
          final ValueList instance = CommonJsonization.loadMaximalValueList();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "ValueList",
              "maximal.json.trace"));
        }  // public void testDescendOfValueList

        @Test
        public void testDescendAgainstVisitorThroughForValueList() throws IOException {
          final ValueList instance = CommonJsonization.loadMaximalValueList();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForValueList

        @Test
        public void testDescendOfLangStringPreferredNameTypeIec61360() throws IOException {
          final LangStringPreferredNameTypeIec61360 instance = CommonJsonization.loadMaximalLangStringPreferredNameTypeIec61360();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "LangStringPreferredNameTypeIec61360",
              "maximal.json.trace"));
        }  // public void testDescendOfLangStringPreferredNameTypeIec61360

        @Test
        public void testDescendAgainstVisitorThroughForLangStringPreferredNameTypeIec61360() throws IOException {
          final LangStringPreferredNameTypeIec61360 instance = CommonJsonization.loadMaximalLangStringPreferredNameTypeIec61360();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForLangStringPreferredNameTypeIec61360

        @Test
        public void testDescendOfLangStringShortNameTypeIec61360() throws IOException {
          final LangStringShortNameTypeIec61360 instance = CommonJsonization.loadMaximalLangStringShortNameTypeIec61360();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "LangStringShortNameTypeIec61360",
              "maximal.json.trace"));
        }  // public void testDescendOfLangStringShortNameTypeIec61360

        @Test
        public void testDescendAgainstVisitorThroughForLangStringShortNameTypeIec61360() throws IOException {
          final LangStringShortNameTypeIec61360 instance = CommonJsonization.loadMaximalLangStringShortNameTypeIec61360();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForLangStringShortNameTypeIec61360

        @Test
        public void testDescendOfLangStringDefinitionTypeIec61360() throws IOException {
          final LangStringDefinitionTypeIec61360 instance = CommonJsonization.loadMaximalLangStringDefinitionTypeIec61360();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "LangStringDefinitionTypeIec61360",
              "maximal.json.trace"));
        }  // public void testDescendOfLangStringDefinitionTypeIec61360

        @Test
        public void testDescendAgainstVisitorThroughForLangStringDefinitionTypeIec61360() throws IOException {
          final LangStringDefinitionTypeIec61360 instance = CommonJsonization.loadMaximalLangStringDefinitionTypeIec61360();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForLangStringDefinitionTypeIec61360

        @Test
        public void testDescendOfDataSpecificationIec61360() throws IOException {
          final DataSpecificationIec61360 instance = CommonJsonization.loadMaximalDataSpecificationIec61360();
          compareOrRerecordTrace(instance,
            Paths.get(Common.TEST_DATA_DIR,
              "Descend",
              "DataSpecificationIec61360",
              "maximal.json.trace"));
        }  // public void testDescendOfDataSpecificationIec61360

        @Test
        public void testDescendAgainstVisitorThroughForDataSpecificationIec61360() throws IOException {
          final DataSpecificationIec61360 instance = CommonJsonization.loadMaximalDataSpecificationIec61360();
          assertDescendAndVisitorThroughSame(instance);
        }  // public void testDescendAgainstVisitorThroughForDataSpecificationIec61360
}  // class TestDescendAndVisitorThrough


/*
 * This code has been automatically generated by testgen.
 * Do NOT edit or append.
 */
