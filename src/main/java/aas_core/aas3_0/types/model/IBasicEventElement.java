/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */

package aas_core.aas3_0.types.model;

import aas_core.aas3_0.types.enums.*;
import aas_core.aas3_0.types.impl.*;
import java.util.Optional;

/**
 * A basic event element.
 *
 * <p>This element is experimental and therefore may be subject to change or may be removed
 * completely in future versions of the meta-model.
 */
public interface IBasicEventElement extends IEventElement {
  /**
   * Reference to the {@link aas_core.aas3_0.types.model.IReferable}, which defines the scope of the
   * event. Can be {@link aas_core.aas3_0.types.impl.AssetAdministrationShell}, {@link
   * aas_core.aas3_0.types.impl.Submodel}, or {@link aas_core.aas3_0.types.model.ISubmodelElement}.
   *
   * <p>Reference to a referable, e.g., a data element or a submodel, that is being observed.
   */
  IReference getObserved();

  void setObserved(IReference observed);

  /**
   * Direction of event.
   *
   * <p>Can be {@code { Input, Output }}.
   */
  Direction getDirection();

  void setDirection(Direction direction);

  /**
   * State of event.
   *
   * <p>Can be {@code { On, Off }}.
   */
  StateOfEvent getState();

  void setState(StateOfEvent state);

  /**
   * Information for the outer message infrastructure for scheduling the event to the respective
   * communication channel.
   */
  Optional<String> getMessageTopic();

  void setMessageTopic(String messageTopic);

  /**
   * Information, which outer message infrastructure shall handle messages for the {@link
   * aas_core.aas3_0.types.model.IEventElement}. Refers to a {@link
   * aas_core.aas3_0.types.impl.Submodel}, {@link aas_core.aas3_0.types.impl.SubmodelElementList},
   * {@link aas_core.aas3_0.types.impl.SubmodelElementCollection} or {@link
   * aas_core.aas3_0.types.impl.Entity}, which contains {@link
   * aas_core.aas3_0.types.model.IDataElement}'s describing the proprietary specification for the
   * message broker.
   *
   * <p>For different message infrastructure, e.g., OPC UA or MQTT or AMQP, this proprietary
   * specification could be standardized by having respective Submodels.
   */
  Optional<IReference> getMessageBroker();

  void setMessageBroker(IReference messageBroker);

  /**
   * Timestamp in UTC, when the last event was received (input direction) or sent (output
   * direction).
   */
  Optional<String> getLastUpdate();

  void setLastUpdate(String lastUpdate);

  /**
   * For input direction, reports on the maximum frequency, the software entity behind the
   * respective Referable can handle input events.
   *
   * <p>For output events, specifies the maximum frequency of outputting this event to an outer
   * infrastructure.
   *
   * <p>Might be not specified, that is, there is no minimum interval.
   */
  Optional<String> getMinInterval();

  void setMinInterval(String minInterval);

  /**
   * For input direction: not applicable.
   *
   * <p>For output direction: maximum interval in time, the respective Referable shall send an
   * update of the status of the event, even if no other trigger condition for the event was not
   * met.
   *
   * <p>Might be not specified, that is, there is no maximum interval
   */
  Optional<String> getMaxInterval();

  void setMaxInterval(String maxInterval);
}

/*
 * This code has been automatically generated by aas-core-codegen.
 * Do NOT edit or append.
 */