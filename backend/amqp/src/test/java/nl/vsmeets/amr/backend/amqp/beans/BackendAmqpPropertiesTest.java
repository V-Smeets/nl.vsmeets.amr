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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the class {@link BackendAmqpProperties}.
 *
 * @author vincent
 */
class BackendAmqpPropertiesTest {

  /**
   * Values used during tests.
   */
  private static URL apiUrl;
  private static final String apiUsername = "API Username";
  private static final String apiPassword = "API Password";
  private static final String virtualHost = "Virtual Host";
  private static final String exchangeName = "Exchange Name";
  private static final String routingKey = "Routing Key";
  private static final String shovelName1 = "Shovel Name 1";
  private static final String shovelName2 = "Shovel Name 2";
  private static URI shovelSourceUri;
  private static final String shovelSourceUsername = "Source Username";
  private static final String shovelSourcePassword = "Source Password";
  private static final String shovelSourceQueueName = "Source Queue Name";
  private static URI shovelDestinationUri;
  private static final String shovelDestinationUsername = "Destination Username";
  private static final String shovelDestinationPassword = "Destination Password";
  private static final String shovelDestinationExchangeName = "Destination Exchange Name";
  private static final String shovelDestinationRoutingKey = "Destination Routing Key";

  private static ValidatorFactory factory;

  @BeforeAll
  static void setUpBeforeClass() throws Exception {
    apiUrl = new URL("http", "localhost", "/api");
    shovelSourceUri = new URI("amqp", "source-host", "/source", null);
    shovelDestinationUri = new URI("amqp", "destination-host", "/destination", null);
    factory = Validation.buildDefaultValidatorFactory();
  }

  /**
   * The object under test.
   */
  private BackendAmqpProperties backendAmqpProperties;

  private Validator validator;

  @BeforeEach
  void setUp() throws Exception {
    validator = factory.getValidator();

    final ShovelProperties shovel1 = new ShovelProperties(shovelName1, shovelSourceUri, shovelSourceUsername,
        shovelSourcePassword, shovelSourceQueueName, shovelDestinationUri, shovelDestinationUsername,
        shovelDestinationPassword, shovelDestinationExchangeName, shovelDestinationRoutingKey);
    final ShovelProperties shovel2 = new ShovelProperties(shovelName2, shovelSourceUri, shovelSourceUsername,
        shovelSourcePassword, shovelSourceQueueName, shovelDestinationUri, shovelDestinationUsername,
        shovelDestinationPassword, shovelDestinationExchangeName, shovelDestinationRoutingKey);
    final Collection<ShovelProperties> shovels = new ArrayList<>(List.of(shovel1, shovel2));
    backendAmqpProperties =
        new BackendAmqpProperties(apiUrl, apiUsername, apiPassword, virtualHost, exchangeName, routingKey, shovels);

    final Set<ConstraintViolation<BackendAmqpProperties>> constraintViolations =
        validator.validate(backendAmqpProperties);
    assertTrue(constraintViolations.isEmpty());
  }

  @Test
  void testGetApiPassword() {
    assertEquals(apiPassword, backendAmqpProperties.getApiPassword());
  }

  @Test
  void testGetApiPasswordNotEmpty() {
    backendAmqpProperties =
        new BackendAmqpProperties(backendAmqpProperties.getApiUrl(), backendAmqpProperties.getApiUsername(), "",
            backendAmqpProperties.getVirtualHost(), backendAmqpProperties.getExchangeName(),
            backendAmqpProperties.getRoutingKey(), backendAmqpProperties.getShovels());

    final Set<ConstraintViolation<BackendAmqpProperties>> constraintViolations =
        validator.validate(backendAmqpProperties);
    assertFalse(constraintViolations.isEmpty());
  }

  @Test
  void testGetApiPasswordNotNull() {
    backendAmqpProperties =
        new BackendAmqpProperties(backendAmqpProperties.getApiUrl(), backendAmqpProperties.getApiUsername(), null,
            backendAmqpProperties.getVirtualHost(), backendAmqpProperties.getExchangeName(),
            backendAmqpProperties.getRoutingKey(), backendAmqpProperties.getShovels());

    final Set<ConstraintViolation<BackendAmqpProperties>> constraintViolations =
        validator.validate(backendAmqpProperties);
    assertFalse(constraintViolations.isEmpty());
  }

  @Test
  void testGetApiUrl() {
    assertEquals(apiUrl, backendAmqpProperties.getApiUrl());
  }

  @Test
  void testGetApiUrlNotNull() {
    backendAmqpProperties =
        new BackendAmqpProperties(null, backendAmqpProperties.getApiUsername(), backendAmqpProperties.getApiPassword(),
            backendAmqpProperties.getVirtualHost(), backendAmqpProperties.getExchangeName(),
            backendAmqpProperties.getRoutingKey(), backendAmqpProperties.getShovels());

    final Set<ConstraintViolation<BackendAmqpProperties>> constraintViolations =
        validator.validate(backendAmqpProperties);
    assertFalse(constraintViolations.isEmpty());
  }

  @Test
  void testGetApiUsername() {
    assertEquals(apiUsername, backendAmqpProperties.getApiUsername());
  }

  @Test
  void testGetApiUsernameNotEmpty() {
    backendAmqpProperties =
        new BackendAmqpProperties(backendAmqpProperties.getApiUrl(), "", backendAmqpProperties.getApiPassword(),
            backendAmqpProperties.getVirtualHost(), backendAmqpProperties.getExchangeName(),
            backendAmqpProperties.getRoutingKey(), backendAmqpProperties.getShovels());

    final Set<ConstraintViolation<BackendAmqpProperties>> constraintViolations =
        validator.validate(backendAmqpProperties);
    assertFalse(constraintViolations.isEmpty());
  }

  @Test
  void testGetApiUsernameNotNull() {
    backendAmqpProperties =
        new BackendAmqpProperties(backendAmqpProperties.getApiUrl(), null, backendAmqpProperties.getApiPassword(),
            backendAmqpProperties.getVirtualHost(), backendAmqpProperties.getExchangeName(),
            backendAmqpProperties.getRoutingKey(), backendAmqpProperties.getShovels());

    final Set<ConstraintViolation<BackendAmqpProperties>> constraintViolations =
        validator.validate(backendAmqpProperties);
    assertFalse(constraintViolations.isEmpty());
  }

  @Test
  void testGetExchangeName() {
    assertEquals(exchangeName, backendAmqpProperties.getExchangeName());
  }

  @Test
  void testGetExchangeNameNotNull() {
    backendAmqpProperties =
        new BackendAmqpProperties(backendAmqpProperties.getApiUrl(), backendAmqpProperties.getApiUsername(),
            backendAmqpProperties.getApiPassword(), backendAmqpProperties.getVirtualHost(), null,
            backendAmqpProperties.getRoutingKey(), backendAmqpProperties.getShovels());

    final Set<ConstraintViolation<BackendAmqpProperties>> constraintViolations =
        validator.validate(backendAmqpProperties);
    assertFalse(constraintViolations.isEmpty());
  }

  @Test
  void testGetRoutingKey() {
    assertEquals(routingKey, backendAmqpProperties.getRoutingKey());
  }

  @Test
  void testGetRoutingKeyNotEmpty() {
    backendAmqpProperties =
        new BackendAmqpProperties(backendAmqpProperties.getApiUrl(), backendAmqpProperties.getApiUsername(),
            backendAmqpProperties.getApiPassword(), backendAmqpProperties.getVirtualHost(),
            backendAmqpProperties.getExchangeName(), "", backendAmqpProperties.getShovels());

    final Set<ConstraintViolation<BackendAmqpProperties>> constraintViolations =
        validator.validate(backendAmqpProperties);
    assertFalse(constraintViolations.isEmpty());
  }

  @Test
  void testGetRoutingKeyNotNull() {
    backendAmqpProperties =
        new BackendAmqpProperties(backendAmqpProperties.getApiUrl(), backendAmqpProperties.getApiUsername(),
            backendAmqpProperties.getApiPassword(), backendAmqpProperties.getVirtualHost(),
            backendAmqpProperties.getExchangeName(), null, backendAmqpProperties.getShovels());

    final Set<ConstraintViolation<BackendAmqpProperties>> constraintViolations =
        validator.validate(backendAmqpProperties);
    assertFalse(constraintViolations.isEmpty());
  }

  @Test
  void testGetShovels() {
    final Collection<ShovelProperties> shovels = backendAmqpProperties.getShovels();
    assertAll( //
        () -> assertNotNull(shovels), //
        () -> assertEquals(2, shovels.size()) //
    );
  }

  @Test
  void testGetShovelsNotNull() {
    backendAmqpProperties =
        new BackendAmqpProperties(backendAmqpProperties.getApiUrl(), backendAmqpProperties.getApiUsername(),
            backendAmqpProperties.getApiPassword(), backendAmqpProperties.getVirtualHost(),
            backendAmqpProperties.getExchangeName(), backendAmqpProperties.getRoutingKey(), null);

    final Set<ConstraintViolation<BackendAmqpProperties>> constraintViolations =
        validator.validate(backendAmqpProperties);
    assertFalse(constraintViolations.isEmpty());
  }

  @Test
  void testGetVirtualHosNotEmpty() {
    backendAmqpProperties =
        new BackendAmqpProperties(backendAmqpProperties.getApiUrl(), backendAmqpProperties.getApiUsername(),
            backendAmqpProperties.getApiPassword(), "", backendAmqpProperties.getExchangeName(),
            backendAmqpProperties.getRoutingKey(), backendAmqpProperties.getShovels());

    final Set<ConstraintViolation<BackendAmqpProperties>> constraintViolations =
        validator.validate(backendAmqpProperties);
    assertFalse(constraintViolations.isEmpty());
  }

  @Test
  void testGetVirtualHosNotNull() {
    backendAmqpProperties =
        new BackendAmqpProperties(backendAmqpProperties.getApiUrl(), backendAmqpProperties.getApiUsername(),
            backendAmqpProperties.getApiPassword(), null, backendAmqpProperties.getExchangeName(),
            backendAmqpProperties.getRoutingKey(), backendAmqpProperties.getShovels());

    final Set<ConstraintViolation<BackendAmqpProperties>> constraintViolations =
        validator.validate(backendAmqpProperties);
    assertFalse(constraintViolations.isEmpty());
  }

  @Test
  void testGetVirtualHost() {
    assertEquals(virtualHost, backendAmqpProperties.getVirtualHost());
  }

  @Test
  void testToString() {
    final String toString = backendAmqpProperties.toString();
    assertAll( //
        () -> assertNotNull(toString), //
        () -> assertTrue(toString.contains(backendAmqpProperties.getClass().getSimpleName())), //
        () -> assertTrue(toString.contains(apiUrl.toString())), //
        () -> assertTrue(toString.contains(apiUsername)), //
        () -> assertTrue(toString.contains(apiPassword)), //
        () -> assertTrue(toString.contains(virtualHost)), //
        () -> assertTrue(toString.contains(exchangeName)), //
        () -> assertTrue(toString.contains(routingKey)) //
    );
  }

}
