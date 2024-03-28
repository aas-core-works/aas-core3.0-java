/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */

package aas_core.aas3_0.types.model;

import aas_core.aas3_0.types.enums.*;
import aas_core.aas3_0.types.impl.*;
import java.util.List;
import java.util.Optional;

/**
 * Content of data specification template for concept descriptions for properties, values and value
 * lists conformant to IEC 61360.
 *
 * <p>IEC61360 requires also a globally unique identifier for a concept description. This ID is not
 * part of the data specification template. Instead the {@link
 * aas_core.aas3_0.types.impl.ConceptDescription#getId()} as inherited via {@link
 * aas_core.aas3_0.types.model.IIdentifiable} is used. Same holds for administrative information
 * like the version and revision.
 *
 * <p>{@link aas_core.aas3_0.types.impl.ConceptDescription#getIdShort()} and {@link #getShortName()}
 * are very similar. However, in this case the decision was to add {@link #getShortName()}
 * explicitly to the data specification. Same holds for {@link
 * aas_core.aas3_0.types.impl.ConceptDescription#getDisplayName()} and {@link #getPreferredName()}.
 * Same holds for {@link aas_core.aas3_0.types.impl.ConceptDescription#getDescription()} and {@link
 * #getDefinition()}.
 *
 * <p>Constraints:
 *
 * <ul>
 *   <li>Constraint AASc-3a-010: If {@link #getValue()} is not empty then {@link #getValueList()}
 *       shall be empty and vice versa.
 *       <p>It is also possible that both {@link #getValue()} and {@link #getValueList()} are empty.
 *       This is the case for concept descriptions that define the semantics of a property but do
 *       not have an enumeration ({@link #getValueList()}) as data type.
 *       <p>Although it is possible to define a {@link
 *       aas_core.aas3_0.types.impl.ConceptDescription} for a :attr:´value_list`, it is not possible
 *       to reuse this {@link #getValueList()}. It is only possible to directly add a {@link
 *       #getValueList()} as data type to a specific semantic definition of a property.
 *   <li>Constraint AASc-3a-009: If {@link #getDataType()} one of: {@link
 *       aas_core.aas3_0.types.enums.DataTypeIec61360#INTEGER_MEASURE}, {@link
 *       aas_core.aas3_0.types.enums.DataTypeIec61360#REAL_MEASURE}, {@link
 *       aas_core.aas3_0.types.enums.DataTypeIec61360#RATIONAL_MEASURE}, {@link
 *       aas_core.aas3_0.types.enums.DataTypeIec61360#INTEGER_CURRENCY}, {@link
 *       aas_core.aas3_0.types.enums.DataTypeIec61360#REAL_CURRENCY}, then {@link #getUnit()} or
 *       {@link #getUnitId()} shall be defined.
 * </ul>
 */
public interface IDataSpecificationIec61360 extends IDataSpecificationContent {
  /**
   * Preferred name
   *
   * <p>It is advised to keep the length of the name limited to 35 characters.
   *
   * <p>Constraints:
   *
   * <ul>
   *   <li>Constraint AASc-3a-002: {@link #getPreferredName()} shall be provided at least in
   *       English.
   * </ul>
   */
  List<ILangStringPreferredNameTypeIec61360> getPreferredName();

  void setPreferredName(List<ILangStringPreferredNameTypeIec61360> preferredName);

  /** Short name */
  Optional<List<ILangStringShortNameTypeIec61360>> getShortName();

  void setShortName(List<ILangStringShortNameTypeIec61360> shortName);

  /** Unit */
  Optional<String> getUnit();

  void setUnit(String unit);

  /**
   * Unique unit id
   *
   * <p>{@link #getUnit()} and {@link #getUnitId()} need to be consistent if both attributes are set
   *
   * <p>It is recommended to use an external reference ID.
   */
  Optional<IReference> getUnitId();

  void setUnitId(IReference unitId);

  /** Source of definition */
  Optional<String> getSourceOfDefinition();

  void setSourceOfDefinition(String sourceOfDefinition);

  /** Symbol */
  Optional<String> getSymbol();

  void setSymbol(String symbol);

  /** Data Type */
  Optional<DataTypeIec61360> getDataType();

  void setDataType(DataTypeIec61360 dataType);

  /** Definition in different languages */
  Optional<List<ILangStringDefinitionTypeIec61360>> getDefinition();

  void setDefinition(List<ILangStringDefinitionTypeIec61360> definition);

  /**
   * Value Format
   *
   * <p>The value format is based on ISO 13584-42 and IEC 61360-2.
   */
  Optional<String> getValueFormat();

  void setValueFormat(String valueFormat);

  /** List of allowed values */
  Optional<IValueList> getValueList();

  void setValueList(IValueList valueList);

  /** Value */
  Optional<String> getValue();

  void setValue(String value);

  /** Set of levels. */
  Optional<ILevelType> getLevelType();

  void setLevelType(ILevelType levelType);

  /** Iterate over shortName, if set, and otherwise return an empty enumerable. */
  Iterable<ILangStringShortNameTypeIec61360> overShortNameOrEmpty();

  /** Iterate over definition, if set, and otherwise return an empty enumerable. */
  Iterable<ILangStringDefinitionTypeIec61360> overDefinitionOrEmpty();
}

/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */
