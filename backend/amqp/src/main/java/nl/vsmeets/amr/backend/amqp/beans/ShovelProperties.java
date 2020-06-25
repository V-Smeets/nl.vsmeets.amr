/**
 * Copyright (C) 2020 Vincent Smeets
 * <p>
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <https://www.gnu.org/licenses/>.
 */
package nl.vsmeets.amr.backend.amqp.beans;

import java.net.URI;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * The properties to define a shovel.
 *
 * @author vincent
 */
@RequiredArgsConstructor
@Getter
@ToString
public class ShovelProperties {

  /**
   * The name of the shovel.
   */
  @NotEmpty
  private final String name;

  /**
   * The URI of the source AMQP server.
   */
  @NotNull
  private final URI sourceUri;

  /**
   * The user name to connect to the source AMQP server.
   */
  private final String sourceUsername;

  /**
   * The password to connect to the source AMQP server.
   */
  private final String sourcePassword;

  /**
   * The queue to read messages from.
   */
  @NotEmpty
  private final String sourceQueueName;

  /**
   * The URI of the destination AMQP server.
   */
  @NotNull
  private final URI destinationUri;

  /**
   * The user name to connect to the destination AMQP server.
   */
  private final String destinationUsername;

  /**
   * The password to connect to the destination AMQP server.
   */
  private final String destinationPassword;

  /**
   * The exchange to where the messages will be sent.
   */
  @NotNull
  private final String destinationExchangeName;

  /**
   * The routing key that will be used with the exchange to send the messages.
   */
  @NotNull
  private final String destinationRoutingKey;

}
