/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */

package aas_core.aas3_0.enhancing;

import java.util.Optional;
import javax.annotation.Generated;
import aas_core.aas3_0.types.model.*;

/**
 * Unwrap enhancements from the wrapped instances.
 */
@Generated("generated by aas-core-codegen")
public class Unwrapper<EnhancementT> {
  /**
   * Unwrap the given model instance.
   *
   * @param that model instance to be unwrapped
   * @return Enhancement, or {@link java.util.Optional#empty()} if {@code that}
   * has not been wrapped yet.
   */
  public Optional<EnhancementT> unwrap(IClass that)
  {
    if (that instanceof Enhanced) {
      @SuppressWarnings("unchecked")
      Enhanced<EnhancementT> enhanced = (Enhanced<EnhancementT>) that;
      return Optional.of(enhanced.getEnhancement());
    } else {
      return Optional.empty();
    }
  }

  /**
   * Unwrap the given model instance.
   *
   * @param that model instance to be unwrapped
   * @return Enhancement wrapped around {@code that}
   */
  public EnhancementT mustUnwrap(IClass that)
  {
    Optional<EnhancementT> value = unwrap(that);
    if (!value.isPresent()) {
      throw new IllegalArgumentException(
        "Expected the instance to have been wrapped, but it was not: " + that
      );
    }
    return value.get();
  }
}

/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */