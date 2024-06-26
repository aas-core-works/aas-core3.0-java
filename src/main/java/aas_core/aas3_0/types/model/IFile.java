/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */

package aas_core.aas3_0.types.model;

import aas_core.aas3_0.types.enums.*;
import aas_core.aas3_0.types.impl.*;
import java.util.Optional;

/**
 * A File is a data element that represents an address to a file (a locator).
 *
 * <p>The value is an URI that can represent an absolute or relative path.
 */
public interface IFile extends IDataElement {
  /**
   * Path and name of the referenced file (with file extension).
   *
   * <p>The path can be absolute or relative.
   */
  Optional<String> getValue();

  void setValue(String value);

  /**
   * Content type of the content of the file.
   *
   * <p>The content type states which file extensions the file can have.
   */
  String getContentType();

  void setContentType(String contentType);
}

/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */
