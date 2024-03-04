/*
 * This code has been automatically generated by testgen.
 * Do NOT edit or append.
 */

import aas_core.aas3_0.enhancing.Enhancer;
import aas_core.aas3_0.types.impl.*;
import aas_core.aas3_0.types.model.IClass;
import org.junit.jupiter.api.Test;

import javax.annotation.Generated;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

@Generated("Generated by aas-test-gen")
public class TestEnhancing {
    public static class Enhancement {
      private final long someCustomId;
      public Enhancement(long someCustomId) {
        this.someCustomId = someCustomId;
      }
    }

    private Enhancer<Enhancement> createEnhancer() {
      AtomicLong lastCustomId = new AtomicLong();
      Function<IClass, Optional<Enhancement>> enhancementFactory = iClass -> Optional.of(new Enhancement(lastCustomId.incrementAndGet()));
      return new Enhancer<>(enhancementFactory);
    }

    private static void testWrapped(IClass wrapped, Set<Long> idSet) {
      assertNotNull(wrapped);
      assertEquals(1L, idSet.stream()
        .min(Comparator.comparing(Long::valueOf))
        .orElseThrow(() -> new IllegalStateException("Missing min value for wrapped.")));
      assertEquals(idSet.size(), idSet.stream()
        .max(Comparator.comparing(Long::valueOf))
        .orElseThrow(() -> new IllegalStateException("Missing max value for wrapped.")));
    }

    @Test
    public void testExtension() throws IOException {
      final Extension instance = CommonJsonization.loadMaximalExtension();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testExtension

    @Test
    public void testAdministrativeInformation() throws IOException {
      final AdministrativeInformation instance = CommonJsonization.loadMaximalAdministrativeInformation();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testAdministrativeInformation

    @Test
    public void testQualifier() throws IOException {
      final Qualifier instance = CommonJsonization.loadMaximalQualifier();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testQualifier

    @Test
    public void testAssetAdministrationShell() throws IOException {
      final AssetAdministrationShell instance = CommonJsonization.loadMaximalAssetAdministrationShell();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testAssetAdministrationShell

    @Test
    public void testAssetInformation() throws IOException {
      final AssetInformation instance = CommonJsonization.loadMaximalAssetInformation();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testAssetInformation

    @Test
    public void testResource() throws IOException {
      final Resource instance = CommonJsonization.loadMaximalResource();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testResource

    @Test
    public void testSpecificAssetId() throws IOException {
      final SpecificAssetId instance = CommonJsonization.loadMaximalSpecificAssetId();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testSpecificAssetId

    @Test
    public void testSubmodel() throws IOException {
      final Submodel instance = CommonJsonization.loadMaximalSubmodel();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testSubmodel

    @Test
    public void testRelationshipElement() throws IOException {
      final RelationshipElement instance = CommonJsonization.loadMaximalRelationshipElement();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testRelationshipElement

    @Test
    public void testSubmodelElementList() throws IOException {
      final SubmodelElementList instance = CommonJsonization.loadMaximalSubmodelElementList();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testSubmodelElementList

    @Test
    public void testSubmodelElementCollection() throws IOException {
      final SubmodelElementCollection instance = CommonJsonization.loadMaximalSubmodelElementCollection();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testSubmodelElementCollection

    @Test
    public void testProperty() throws IOException {
      final Property instance = CommonJsonization.loadMaximalProperty();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testProperty

    @Test
    public void testMultiLanguageProperty() throws IOException {
      final MultiLanguageProperty instance = CommonJsonization.loadMaximalMultiLanguageProperty();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testMultiLanguageProperty

    @Test
    public void testRange() throws IOException {
      final Range instance = CommonJsonization.loadMaximalRange();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testRange

    @Test
    public void testReferenceElement() throws IOException {
      final ReferenceElement instance = CommonJsonization.loadMaximalReferenceElement();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testReferenceElement

    @Test
    public void testBlob() throws IOException {
      final Blob instance = CommonJsonization.loadMaximalBlob();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testBlob

    @Test
    public void testFile() throws IOException {
      final File instance = CommonJsonization.loadMaximalFile();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testFile

    @Test
    public void testAnnotatedRelationshipElement() throws IOException {
      final AnnotatedRelationshipElement instance = CommonJsonization.loadMaximalAnnotatedRelationshipElement();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testAnnotatedRelationshipElement

    @Test
    public void testEntity() throws IOException {
      final Entity instance = CommonJsonization.loadMaximalEntity();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testEntity

    @Test
    public void testEventPayload() throws IOException {
      final EventPayload instance = CommonJsonization.loadMaximalEventPayload();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testEventPayload

    @Test
    public void testBasicEventElement() throws IOException {
      final BasicEventElement instance = CommonJsonization.loadMaximalBasicEventElement();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testBasicEventElement

    @Test
    public void testOperation() throws IOException {
      final Operation instance = CommonJsonization.loadMaximalOperation();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testOperation

    @Test
    public void testOperationVariable() throws IOException {
      final OperationVariable instance = CommonJsonization.loadMaximalOperationVariable();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testOperationVariable

    @Test
    public void testCapability() throws IOException {
      final Capability instance = CommonJsonization.loadMaximalCapability();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testCapability

    @Test
    public void testConceptDescription() throws IOException {
      final ConceptDescription instance = CommonJsonization.loadMaximalConceptDescription();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testConceptDescription

    @Test
    public void testReference() throws IOException {
      final Reference instance = CommonJsonization.loadMaximalReference();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testReference

    @Test
    public void testKey() throws IOException {
      final Key instance = CommonJsonization.loadMaximalKey();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testKey

    @Test
    public void testLangStringNameType() throws IOException {
      final LangStringNameType instance = CommonJsonization.loadMaximalLangStringNameType();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testLangStringNameType

    @Test
    public void testLangStringTextType() throws IOException {
      final LangStringTextType instance = CommonJsonization.loadMaximalLangStringTextType();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testLangStringTextType

    @Test
    public void testEnvironment() throws IOException {
      final Environment instance = CommonJsonization.loadMaximalEnvironment();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testEnvironment

    @Test
    public void testEmbeddedDataSpecification() throws IOException {
      final EmbeddedDataSpecification instance = CommonJsonization.loadMaximalEmbeddedDataSpecification();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testEmbeddedDataSpecification

    @Test
    public void testLevelType() throws IOException {
      final LevelType instance = CommonJsonization.loadMaximalLevelType();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testLevelType

    @Test
    public void testValueReferencePair() throws IOException {
      final ValueReferencePair instance = CommonJsonization.loadMaximalValueReferencePair();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testValueReferencePair

    @Test
    public void testValueList() throws IOException {
      final ValueList instance = CommonJsonization.loadMaximalValueList();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testValueList

    @Test
    public void testLangStringPreferredNameTypeIec61360() throws IOException {
      final LangStringPreferredNameTypeIec61360 instance = CommonJsonization.loadMaximalLangStringPreferredNameTypeIec61360();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testLangStringPreferredNameTypeIec61360

    @Test
    public void testLangStringShortNameTypeIec61360() throws IOException {
      final LangStringShortNameTypeIec61360 instance = CommonJsonization.loadMaximalLangStringShortNameTypeIec61360();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testLangStringShortNameTypeIec61360

    @Test
    public void testLangStringDefinitionTypeIec61360() throws IOException {
      final LangStringDefinitionTypeIec61360 instance = CommonJsonization.loadMaximalLangStringDefinitionTypeIec61360();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testLangStringDefinitionTypeIec61360

    @Test
    public void testDataSpecificationIec61360() throws IOException {
      final DataSpecificationIec61360 instance = CommonJsonization.loadMaximalDataSpecificationIec61360();
      final Enhancer<Enhancement> enhancer = createEnhancer();
      final IClass wrapped = enhancer.wrap(instance);
      final Set<Long> idSet = new HashSet<>();
      idSet.add(enhancer.mustUnwrap(wrapped).someCustomId);
      wrapped.descend().forEach(descendant -> idSet.add(enhancer.mustUnwrap(descendant).someCustomId));
      assertFalse(enhancer.unwrap(instance).isPresent());
      testWrapped(wrapped, idSet);
    }  // public void testDataSpecificationIec61360
}  // class TestEnhancing

/*
 * This code has been automatically generated by testgen.
 * Do NOT edit or append.
 */
