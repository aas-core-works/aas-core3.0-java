/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */

package aas_core.aas3_0.types.impl;

import aas_core.aas3_0.types.enums.*;
import aas_core.aas3_0.types.model.*;
import aas_core.aas3_0.types.model.IEnvironment;
import aas_core.aas3_0.visitation.ITransformer;
import aas_core.aas3_0.visitation.ITransformerWithContext;
import aas_core.aas3_0.visitation.IVisitor;
import aas_core.aas3_0.visitation.IVisitorWithContext;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Container for the sets of different identifiables.
 *
 * <p>w.r.t. file exchange: There is exactly one environment independent on how many files the
 * contained elements are split. If the file is split then there shall be no element with the same
 * identifier in two different files.
 */
public class Environment implements IEnvironment {
  /** Asset administration shell */
  private List<IAssetAdministrationShell> assetAdministrationShells;

  /** Submodel */
  private List<ISubmodel> submodels;

  /** Concept description */
  private List<IConceptDescription> conceptDescriptions;

  public Environment(
      List<IAssetAdministrationShell> assetAdministrationShells,
      List<ISubmodel> submodels,
      List<IConceptDescription> conceptDescriptions) {
    this.assetAdministrationShells = assetAdministrationShells;
    this.submodels = submodels;
    this.conceptDescriptions = conceptDescriptions;
  }

  @Override
  public Optional<List<IAssetAdministrationShell>> getAssetAdministrationShells() {
    return Optional.ofNullable(assetAdministrationShells);
  }

  @Override
  public void setAssetAdministrationShells(
      List<IAssetAdministrationShell> assetAdministrationShells) {
    this.assetAdministrationShells = assetAdministrationShells;
  }

  @Override
  public Optional<List<ISubmodel>> getSubmodels() {
    return Optional.ofNullable(submodels);
  }

  @Override
  public void setSubmodels(List<ISubmodel> submodels) {
    this.submodels = submodels;
  }

  @Override
  public Optional<List<IConceptDescription>> getConceptDescriptions() {
    return Optional.ofNullable(conceptDescriptions);
  }

  @Override
  public void setConceptDescriptions(List<IConceptDescription> conceptDescriptions) {
    this.conceptDescriptions = conceptDescriptions;
  }

  /**
   * Iterate over {@link Environment#assetAdministrationShells}, if set, and otherwise return an
   * empty iterator.
   */
  public Iterable<IAssetAdministrationShell> overAssetAdministrationShellsOrEmpty() {
    return getAssetAdministrationShells().orElseGet(Collections::emptyList);
  }

  /** Iterate over {@link Environment#submodels}, if set, and otherwise return an empty iterator. */
  public Iterable<ISubmodel> overSubmodelsOrEmpty() {
    return getSubmodels().orElseGet(Collections::emptyList);
  }

  /**
   * Iterate over {@link Environment#conceptDescriptions}, if set, and otherwise return an empty
   * iterator.
   */
  public Iterable<IConceptDescription> overConceptDescriptionsOrEmpty() {
    return getConceptDescriptions().orElseGet(Collections::emptyList);
  }

  /** Iterate recursively over all the class instances referenced from this instance. */
  public Iterable<IClass> descend() {
    return new EnvironmentRecursiveIterable();
  }

  /** Iterate over all the class instances referenced from this instance. */
  public Iterable<IClass> descendOnce() {
    return new EnvironmentIterable();
  }

  /** Accept the {@code visitor} to visit this instance for double dispatch. */
  @Override
  public void accept(IVisitor visitor) {
    visitor.visitEnvironment(this);
  }

  /**
   * Accept the {@code visitor} to visit this instance for double dispatch with the {@code context}.
   */
  @Override
  public <ContextT> void accept(IVisitorWithContext<ContextT> visitor, ContextT context) {
    visitor.visitEnvironment(this, context);
  }

  /** Accept the {@code transformer} to visit this instance for double dispatch. */
  @Override
  public <T> T transform(ITransformer<T> transformer) {
    return transformer.transformEnvironment(this);
  }

  /**
   * Accept the {@code transformer} to visit this instance for double dispatch with the {@code
   * context}.
   */
  @Override
  public <ContextT, T> T transform(
      ITransformerWithContext<ContextT, T> transformer, ContextT context) {
    return transformer.transformEnvironment(this, context);
  }

  private class EnvironmentIterable implements Iterable<IClass> {
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

      if (assetAdministrationShells != null) {
        memberStream =
            Stream.concat(memberStream, Environment.this.assetAdministrationShells.stream());
      }

      if (submodels != null) {
        memberStream = Stream.concat(memberStream, Environment.this.submodels.stream());
      }

      if (conceptDescriptions != null) {
        memberStream = Stream.concat(memberStream, Environment.this.conceptDescriptions.stream());
      }

      return memberStream;
    }
  }

  private class EnvironmentRecursiveIterable implements Iterable<IClass> {
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

      if (assetAdministrationShells != null) {
        memberStream =
            Stream.concat(
                memberStream,
                Environment.this.assetAdministrationShells.stream()
                    .flatMap(
                        item ->
                            Stream.concat(
                                Stream.<IClass>of(item),
                                StreamSupport.stream(item.descend().spliterator(), false))));
      }

      if (submodels != null) {
        memberStream =
            Stream.concat(
                memberStream,
                Environment.this.submodels.stream()
                    .flatMap(
                        item ->
                            Stream.concat(
                                Stream.<IClass>of(item),
                                StreamSupport.stream(item.descend().spliterator(), false))));
      }

      if (conceptDescriptions != null) {
        memberStream =
            Stream.concat(
                memberStream,
                Environment.this.conceptDescriptions.stream()
                    .flatMap(
                        item ->
                            Stream.concat(
                                Stream.<IClass>of(item),
                                StreamSupport.stream(item.descend().spliterator(), false))));
      }

      return memberStream;
    }
  }
}

/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */