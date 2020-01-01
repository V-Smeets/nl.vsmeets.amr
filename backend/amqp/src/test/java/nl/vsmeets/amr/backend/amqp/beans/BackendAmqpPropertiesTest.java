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

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import nl.vsmeets.amr.libs.junit.RandomStringGenerator;

/**
 * Unit tests for the class {@link BackendAmqpProperties}.
 *
 * @author vincent
 */
class BackendAmqpPropertiesTest implements RandomStringGenerator {

  private static ValidatorFactory factory;

  @BeforeAll
  static void setUpBeforeClass() throws Exception {
    factory = Validation.buildDefaultValidatorFactory();
  }

  private Validator validator;

  private final String exchangeName = randomString();

  private final String routingKey = randomString();

  private BackendAmqpProperties backendAmqpProperties;

  @BeforeEach
  void setUp() throws Exception {
    validator = factory.getValidator();

    backendAmqpProperties = new BackendAmqpProperties();
    backendAmqpProperties.setExchangeName(exchangeName);
    backendAmqpProperties.setRoutingKey(routingKey);

    final Set<ConstraintViolation<BackendAmqpProperties>> constraintViolations =
        validator.validate(backendAmqpProperties);
    assertTrue(constraintViolations.isEmpty());
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
  void testToString() {
    final String toString = backendAmqpProperties.toString();
    assertAll( //
        () -> assertNotNull(toString), //
        () -> assertTrue(toString.contains(backendAmqpProperties.getClass().getSimpleName())), //
        () -> assertTrue(toString.contains(exchangeName)), //
        () -> assertTrue(toString.contains(routingKey)) //
    );
  }

}
