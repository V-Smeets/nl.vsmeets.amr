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

import java.net.URL;
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

  private static ValidatorFactory factory;

  @BeforeAll
  static void setUpBeforeClass() throws Exception {
    apiUrl = new URL("http", "localhost", "/api");
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

    backendAmqpProperties = new BackendAmqpProperties();
    backendAmqpProperties.setApiUrl(apiUrl);
    backendAmqpProperties.setApiUsername(apiUsername);
    backendAmqpProperties.setApiPassword(apiPassword);
    backendAmqpProperties.setVirtualHost(virtualHost);
    backendAmqpProperties.setExchangeName(exchangeName);
    backendAmqpProperties.setRoutingKey(routingKey);

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
    backendAmqpProperties.setApiPassword("");

    final Set<ConstraintViolation<BackendAmqpProperties>> constraintViolations =
        validator.validate(backendAmqpProperties);
    assertFalse(constraintViolations.isEmpty());
  }

  @Test
  void testGetApiPasswordNotNull() {
    backendAmqpProperties.setApiPassword(null);

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
    backendAmqpProperties.setApiUrl(null);

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
    backendAmqpProperties.setApiUsername("");

    final Set<ConstraintViolation<BackendAmqpProperties>> constraintViolations =
        validator.validate(backendAmqpProperties);
    assertFalse(constraintViolations.isEmpty());
  }

  @Test
  void testGetApiUsernameNotNull() {
    backendAmqpProperties.setApiUsername(null);

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
    backendAmqpProperties.setExchangeName(null);

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
    backendAmqpProperties.setRoutingKey("");

    final Set<ConstraintViolation<BackendAmqpProperties>> constraintViolations =
        validator.validate(backendAmqpProperties);
    assertFalse(constraintViolations.isEmpty());
  }

  @Test
  void testGetRoutingKeyNotNull() {
    backendAmqpProperties.setRoutingKey(null);

    final Set<ConstraintViolation<BackendAmqpProperties>> constraintViolations =
        validator.validate(backendAmqpProperties);
    assertFalse(constraintViolations.isEmpty());
  }

  @Test
  void testGetVirtualHosNotEmpty() {
    backendAmqpProperties.setVirtualHost("");

    final Set<ConstraintViolation<BackendAmqpProperties>> constraintViolations =
        validator.validate(backendAmqpProperties);
    assertFalse(constraintViolations.isEmpty());
  }

  @Test
  void testGetVirtualHosNotNull() {
    backendAmqpProperties.setVirtualHost(null);

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
