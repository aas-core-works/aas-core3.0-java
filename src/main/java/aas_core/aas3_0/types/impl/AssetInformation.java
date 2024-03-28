/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */

package aas_core.aas3_0.types.impl;

import aas_core.aas3_0.types.enums.*;
import aas_core.aas3_0.types.model.*;
import aas_core.aas3_0.types.model.IAssetInformation;
import aas_core.aas3_0.visitation.ITransformer;
import aas_core.aas3_0.visitation.ITransformerWithContext;
import aas_core.aas3_0.visitation.IVisitor;
import aas_core.aas3_0.visitation.IVisitorWithContext;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * In {@link aas_core.aas3_0.types.impl.AssetInformation} identifying meta data of the asset that is
 * represented by an AAS is defined.
 *
 * <p>The asset may either represent an asset type or an asset instance.
 *
 * <p>The asset has a globally unique identifier plus – if needed – additional domain specific
 * (proprietary) identifiers. However, to support the corner case of very first phase of lifecycle
 * where a stabilised/constant_set global asset identifier does not already exist, the corresponding
 * attribute {@link #getGlobalAssetId()} is optional.
 *
 * <p>Constraints:
 *
 * <ul>
 *   <li>Constraint AASd-116: {@code globalAssetId} is a reserved key. If used as value for {@link
 *       aas_core.aas3_0.types.impl.SpecificAssetId#getName()} then {@link
 *       aas_core.aas3_0.types.impl.SpecificAssetId#getValue()} shall be identical to {@link
 *       #getGlobalAssetId()}.
 *       <p>Constraint AASd-116 is important to enable a generic search across global and specific
 *       asset IDs.
 *       <p>In the book, Constraint AASd-116 imposes a case-insensitive equality against {@code
 *       globalAssetId}. This is culturally-dependent, and depends on the system settings. For
 *       example, the case-folding for the letters "i" and "I" is different in Turkish from English.
 *       <p>We implement the constraint as case-sensitive instead to allow for interoperability
 *       across different culture settings.
 *   <li>Constraint AASd-131: For {@link aas_core.aas3_0.types.impl.AssetInformation} either the
 *       {@link #getGlobalAssetId()} shall be defined or at least one item in {@link
 *       #getSpecificAssetIds()}.
 * </ul>
 */
public class AssetInformation implements IAssetInformation {
  /**
   * Denotes whether the Asset is of kind {@link aas_core.aas3_0.types.enums.AssetKind#TYPE} or
   * {@link aas_core.aas3_0.types.enums.AssetKind#INSTANCE}.
   */
  private AssetKind assetKind;

  /**
   * Global identifier of the asset the AAS is representing.
   *
   * <p>This attribute is required as soon as the AAS is exchanged via partners in the life cycle of
   * the asset. In a first phase of the life cycle the asset might not yet have a global ID but
   * already an internal identifier. The internal identifier would be modelled via {@link
   * #getSpecificAssetIds()}.
   *
   * <p>This is a global reference.
   */
  private String globalAssetId;

  /**
   * Additional domain-specific, typically proprietary identifier for the asset like e.g., serial
   * number etc.
   */
  private List<ISpecificAssetId> specificAssetIds;

  /**
   * In case {@link #getAssetKind()} is applicable the {@link #getAssetType()} is the asset ID of
   * the type asset of the asset under consideration as identified by {@link #getGlobalAssetId()}.
   *
   * <p>In case {@link #getAssetKind()} is "Instance" than the {@link #getAssetType()} denotes which
   * "Type" the asset is of. But it is also possible to have an {@link #getAssetType()} of an asset
   * of kind "Type".
   */
  private String assetType;

  /**
   * Thumbnail of the asset represented by the Asset Administration Shell.
   *
   * <p>Used as default.
   */
  private IResource defaultThumbnail;

  public AssetInformation(AssetKind assetKind) {
    this.assetKind = Objects.requireNonNull(assetKind, "Argument \"assetKind\" must be non-null.");
  }

  public AssetInformation(
      AssetKind assetKind,
      String globalAssetId,
      List<ISpecificAssetId> specificAssetIds,
      String assetType,
      IResource defaultThumbnail) {
    this.assetKind = Objects.requireNonNull(assetKind, "Argument \"assetKind\" must be non-null.");
    this.globalAssetId = globalAssetId;
    this.specificAssetIds = specificAssetIds;
    this.assetType = assetType;
    this.defaultThumbnail = defaultThumbnail;
  }

  @Override
  public AssetKind getAssetKind() {
    return assetKind;
  }

  @Override
  public void setAssetKind(AssetKind assetKind) {
    this.assetKind = Objects.requireNonNull(assetKind, "Argument \"assetKind\" must be non-null.");
  }

  @Override
  public Optional<String> getGlobalAssetId() {
    return Optional.ofNullable(globalAssetId);
  }

  @Override
  public void setGlobalAssetId(String globalAssetId) {
    this.globalAssetId = globalAssetId;
  }

  @Override
  public Optional<List<ISpecificAssetId>> getSpecificAssetIds() {
    return Optional.ofNullable(specificAssetIds);
  }

  @Override
  public void setSpecificAssetIds(List<ISpecificAssetId> specificAssetIds) {
    this.specificAssetIds = specificAssetIds;
  }

  @Override
  public Optional<String> getAssetType() {
    return Optional.ofNullable(assetType);
  }

  @Override
  public void setAssetType(String assetType) {
    this.assetType = assetType;
  }

  @Override
  public Optional<IResource> getDefaultThumbnail() {
    return Optional.ofNullable(defaultThumbnail);
  }

  @Override
  public void setDefaultThumbnail(IResource defaultThumbnail) {
    this.defaultThumbnail = defaultThumbnail;
  }

  /**
   * Iterate over {@link AssetInformation#specificAssetIds}, if set, and otherwise return an empty
   * iterator.
   */
  public Iterable<ISpecificAssetId> overSpecificAssetIdsOrEmpty() {
    return getSpecificAssetIds().orElseGet(Collections::emptyList);
  }

  /** Iterate recursively over all the class instances referenced from this instance. */
  public Iterable<IClass> descend() {
    return new AssetInformationRecursiveIterable();
  }

  /** Iterate over all the class instances referenced from this instance. */
  public Iterable<IClass> descendOnce() {
    return new AssetInformationIterable();
  }

  /** Accept the {@code visitor} to visit this instance for double dispatch. */
  @Override
  public void accept(IVisitor visitor) {
    visitor.visitAssetInformation(this);
  }

  /**
   * Accept the {@code visitor} to visit this instance for double dispatch with the {@code context}.
   */
  @Override
  public <ContextT> void accept(IVisitorWithContext<ContextT> visitor, ContextT context) {
    visitor.visitAssetInformation(this, context);
  }

  /** Accept the {@code transformer} to visit this instance for double dispatch. */
  @Override
  public <T> T transform(ITransformer<T> transformer) {
    return transformer.transformAssetInformation(this);
  }

  /**
   * Accept the {@code transformer} to visit this instance for double dispatch with the {@code
   * context}.
   */
  @Override
  public <ContextT, T> T transform(
      ITransformerWithContext<ContextT, T> transformer, ContextT context) {
    return transformer.transformAssetInformation(this, context);
  }

  private class AssetInformationIterable implements Iterable<IClass> {
    @Override
    public Iterator<IClass> iterator() {
      Stream<IClass> stream = stream();

      return stream.iterator();
    }

    @Override
    public void forEach(Consumer<? super IClass> action) {
      Stream<IClass> stream = stream();

      stream.forEach(action);
    }

    @Override
    public Spliterator<IClass> spliterator() {
      Stream<IClass> stream = stream();

      return stream.spliterator();
    }

    private Stream<IClass> stream() {
      Stream<IClass> memberStream = Stream.empty();

      if (specificAssetIds != null) {
        memberStream = Stream.concat(memberStream, AssetInformation.this.specificAssetIds.stream());
      }

      if (defaultThumbnail != null) {
        memberStream =
            Stream.concat(memberStream, Stream.<IClass>of(AssetInformation.this.defaultThumbnail));
      }

      return memberStream;
    }
  }

  private class AssetInformationRecursiveIterable implements Iterable<IClass> {
    @Override
    public Iterator<IClass> iterator() {
      Stream<IClass> stream = stream();

      return stream.iterator();
    }

    @Override
    public void forEach(Consumer<? super IClass> action) {
      Stream<IClass> stream = stream();

      stream.forEach(action);
    }

    @Override
    public Spliterator<IClass> spliterator() {
      Stream<IClass> stream = stream();

      return stream.spliterator();
    }

    private Stream<IClass> stream() {
      Stream<IClass> memberStream = Stream.empty();

      if (specificAssetIds != null) {
        memberStream =
            Stream.concat(
                memberStream,
                AssetInformation.this.specificAssetIds.stream()
                    .flatMap(
                        item ->
                            Stream.concat(
                                Stream.<IClass>of(item),
                                StreamSupport.stream(item.descend().spliterator(), false))));
      }

      if (defaultThumbnail != null) {
        memberStream =
            Stream.concat(
                memberStream,
                Stream.concat(
                    Stream.<IClass>of(AssetInformation.this.defaultThumbnail),
                    StreamSupport.stream(
                        AssetInformation.this.defaultThumbnail.descend().spliterator(), false)));
      }

      return memberStream;
    }
  }
}

/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */
