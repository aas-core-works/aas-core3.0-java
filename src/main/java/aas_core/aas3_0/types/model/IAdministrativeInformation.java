/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */

package aas_core.aas3_0.types.model;

import aas_core.aas3_0.types.enums.*;
import aas_core.aas3_0.types.impl.*;
import java.util.Optional;

/**
 * Administrative meta-information for an element like version information.
 *
 * <p>Constraints:
 *
 * <ul>
 *   <li>Constraint AASd-005: If {@link #getVersion()} is not specified then also {@link
 *       #getRevision()} shall be unspecified. This means, a revision requires a version. If there
 *       is no version there is no revision neither. Revision is optional.
 * </ul>
 */
public interface IAdministrativeInformation extends IHasDataSpecification {
  /** Version of the element. */
  Optional<String> getVersion();

  void setVersion(String version);

  /** Revision of the element. */
  Optional<String> getRevision();

  void setRevision(String revision);

  /** The subject ID of the subject responsible for making the element. */
  Optional<IReference> getCreator();

  void setCreator(IReference creator);

  /**
   * Identifier of the template that guided the creation of the element.
   *
   * <p>In case of a submodel the {@link #getTemplateId()} is the identifier of the submodel
   * template ID that guided the creation of the submodel
   *
   * <p>The {@link #getTemplateId()} is not relevant for validation in Submodels. For validation the
   * {@link aas_core.aas3_0.types.impl.Submodel#getSemanticId()} shall be used.
   *
   * <p>Usage of {@link #getTemplateId()} is not restricted to submodel instances. So also the
   * creation of submodel templates can be guided by another submodel template.
   */
  Optional<String> getTemplateId();

  void setTemplateId(String templateId);
}

/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */
