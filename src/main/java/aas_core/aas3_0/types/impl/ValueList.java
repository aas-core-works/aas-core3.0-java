/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */

package aas_core.aas3_0.types.impl;

import aas_core.aas3_0.visitation.IVisitor;
import aas_core.aas3_0.visitation.IVisitorWithContext;
import aas_core.aas3_0.visitation.ITransformer;
import aas_core.aas3_0.visitation.ITransformerWithContext;
import aas_core.aas3_0.types.enums.*;
import aas_core.aas3_0.types.impl.*;
import aas_core.aas3_0.types.model.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Objects;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import aas_core.aas3_0.types.model.IValueList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A set of value reference pairs.
 */
public class ValueList implements IValueList {
  /**
   * A pair of a value together with its global unique id.
   */
  private List<IValueReferencePair> valueReferencePairs;

  public ValueList(List<IValueReferencePair> valueReferencePairs) {
    this.valueReferencePairs = Objects.requireNonNull(
      valueReferencePairs,
      "Argument \"valueReferencePairs\" must be non-null.");
  }

  @Override
  public List<IValueReferencePair> getValueReferencePairs() {
    return valueReferencePairs;
  }

  @Override
  public void setValueReferencePairs(List<IValueReferencePair> valueReferencePairs) {
    this.valueReferencePairs = Objects.requireNonNull(
      valueReferencePairs,
      "Argument \"valueReferencePairs\" must be non-null.");
  }

  /**
   * Iterate recursively over all the class instances referenced from this instance.
   */
  public Iterable<IClass> descend() {
    return new ValueListRecursiveIterable();
  }

  /**
   * Iterate over all the class instances referenced from this instance.
   */
  public Iterable<IClass> descendOnce() {
    return new ValueListIterable();
  }

  /**
   * Accept the {@code visitor} to visit this instance for double dispatch.
   **/
  @Override
  public void accept(IVisitor visitor) {
    visitor.visitValueList(this);
  }

  /**
   * Accept the {@code visitor} to visit this instance for double dispatch
   * with the {@code context}.
   **/
  @Override
  public <ContextT> void accept(
      IVisitorWithContext<ContextT> visitor,
      ContextT context) {
    visitor.visitValueList(this, context);
  }

  /**
   * Accept the {@code transformer} to visit this instance for double dispatch.
   **/
  @Override
  public <T> T transform(ITransformer<T> transformer) {
    return transformer.transformValueList(this);
  }

  /**
   * Accept the {@code transformer} to visit this instance for double dispatch
   * with the {@code context}.
   **/
  @Override
  public <ContextT, T> T transform(
      ITransformerWithContext<ContextT, T> transformer,
      ContextT context) {
    return transformer.transformValueList(this, context);
  }

  private class ValueListIterable implements Iterable<IClass> {
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

      if (valueReferencePairs != null) {
        memberStream = Stream.concat(memberStream,
          ValueList.this.valueReferencePairs.stream());
      }

      return memberStream;
    }
  }

  private class ValueListRecursiveIterable implements Iterable<IClass> {
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

      if (valueReferencePairs != null) {
        memberStream = Stream.concat(memberStream,
          ValueList.this.valueReferencePairs.stream()
            .flatMap(item -> Stream.concat(Stream.<IClass>of(item),
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
