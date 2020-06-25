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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the class {@link ShovelProperties}.
 *
 * @author vincent
 */
class ShovelPropertiesTest {

  private static final String shovelName = "Shovel Name";
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
    shovelSourceUri = new URI("amqp", "source-host", "/source", null);
    shovelDestinationUri = new URI("amqp", "destination-host", "/destination", null);
    factory = Validation.buildDefaultValidatorFactory();
  }

  /**
   * The object under test.
   */
  private ShovelProperties shovelProperties;

  private Validator validator;

  @BeforeEach
  void setUp() throws Exception {
    validator = factory.getValidator();

    shovelProperties = new ShovelProperties(shovelName, shovelSourceUri, shovelSourceUsername, shovelSourcePassword,
        shovelSourceQueueName, shovelDestinationUri, shovelDestinationUsername, shovelDestinationPassword,
        shovelDestinationExchangeName, shovelDestinationRoutingKey);

    final Set<ConstraintViolation<ShovelProperties>> constraintViolations = validator.validate(shovelProperties);
    assertTrue(constraintViolations.isEmpty());
  }

  @Test
  void testGetDestinationExchangeName() {
    assertEquals(shovelDestinationExchangeName, shovelProperties.getDestinationExchangeName());
  }

  @Test
  void testGetDestinationExchangeNameNotNull() {
    shovelProperties = new ShovelProperties(shovelProperties.getName(), shovelProperties.getSourceUri(),
        shovelProperties.getSourceUsername(), shovelProperties.getSourcePassword(),
        shovelProperties.getSourceQueueName(), shovelProperties.getDestinationUri(),
        shovelProperties.getDestinationUsername(), shovelProperties.getDestinationPassword(), null,
        shovelProperties.getDestinationRoutingKey());

    final Set<ConstraintViolation<ShovelProperties>> constraintViolations = validator.validate(shovelProperties);
    assertFalse(constraintViolations.isEmpty());
  }

  @Test
  void testGetDestinationPassword() {
    assertEquals(shovelDestinationPassword, shovelProperties.getDestinationPassword());
  }

  @Test
  void testGetDestinationRoutingKey() {
    assertEquals(shovelDestinationRoutingKey, shovelProperties.getDestinationRoutingKey());
  }

  @Test
  void testGetDestinationRoutingKeyNotNull() {
    shovelProperties = new ShovelProperties(shovelProperties.getName(), shovelProperties.getSourceUri(),
        shovelProperties.getSourceUsername(), shovelProperties.getSourcePassword(),
        shovelProperties.getSourceQueueName(), shovelProperties.getDestinationUri(),
        shovelProperties.getDestinationUsername(), shovelProperties.getDestinationPassword(),
        shovelProperties.getDestinationExchangeName(), null);

    final Set<ConstraintViolation<ShovelProperties>> constraintViolations = validator.validate(shovelProperties);
    assertFalse(constraintViolations.isEmpty());
  }

  @Test
  void testGetDestinationUri() {
    assertEquals(shovelDestinationUri, shovelProperties.getDestinationUri());
  }

  @Test
  void testGetDestinationUriNotNull() {
    shovelProperties = new ShovelProperties(shovelProperties.getName(), shovelProperties.getSourceUri(),
        shovelProperties.getSourceUsername(), shovelProperties.getSourcePassword(),
        shovelProperties.getSourceQueueName(), null, shovelProperties.getDestinationUsername(),
        shovelProperties.getDestinationPassword(), shovelProperties.getDestinationExchangeName(),
        shovelProperties.getDestinationRoutingKey());

    final Set<ConstraintViolation<ShovelProperties>> constraintViolations = validator.validate(shovelProperties);
    assertFalse(constraintViolations.isEmpty());
  }

  @Test
  void testGetDestinationUsername() {
    assertEquals(shovelDestinationUsername, shovelProperties.getDestinationUsername());
  }

  @Test
  void testGetName() {
    assertEquals(shovelName, shovelProperties.getName());
  }

  @Test
  void testGetNameNotEmpty() {
    shovelProperties = new ShovelProperties("", shovelProperties.getSourceUri(), shovelProperties.getSourceUsername(),
        shovelProperties.getSourcePassword(), shovelProperties.getSourceQueueName(),
        shovelProperties.getDestinationUri(), shovelProperties.getDestinationUsername(),
        shovelProperties.getDestinationPassword(), shovelProperties.getDestinationExchangeName(),
        shovelProperties.getDestinationRoutingKey());

    final Set<ConstraintViolation<ShovelProperties>> constraintViolations = validator.validate(shovelProperties);
    assertFalse(constraintViolations.isEmpty());
  }

  @Test
  void testGetNameNotNull() {
    shovelProperties = new ShovelProperties(null, shovelProperties.getSourceUri(), shovelProperties.getSourceUsername(),
        shovelProperties.getSourcePassword(), shovelProperties.getSourceQueueName(),
        shovelProperties.getDestinationUri(), shovelProperties.getDestinationUsername(),
        shovelProperties.getDestinationPassword(), shovelProperties.getDestinationExchangeName(),
        shovelProperties.getDestinationRoutingKey());

    final Set<ConstraintViolation<ShovelProperties>> constraintViolations = validator.validate(shovelProperties);
    assertFalse(constraintViolations.isEmpty());
  }

  @Test
  void testGetSourcePassword() {
    assertEquals(shovelSourcePassword, shovelProperties.getSourcePassword());
  }

  @Test
  void testGetSourceQueueName() {
    assertEquals(shovelSourceQueueName, shovelProperties.getSourceQueueName());
  }

  @Test
  void testGetSourceQueueNameNotEmpty() {
    shovelProperties = new ShovelProperties(shovelProperties.getName(), shovelProperties.getSourceUri(),
        shovelProperties.getSourceUsername(), shovelProperties.getSourcePassword(), "",
        shovelProperties.getDestinationUri(), shovelProperties.getDestinationUsername(),
        shovelProperties.getDestinationPassword(), shovelProperties.getDestinationExchangeName(),
        shovelProperties.getDestinationRoutingKey());

    final Set<ConstraintViolation<ShovelProperties>> constraintViolations = validator.validate(shovelProperties);
    assertFalse(constraintViolations.isEmpty());
  }

  @Test
  void testGetSourceQueueNameNotNull() {
    shovelProperties = new ShovelProperties(shovelProperties.getName(), shovelProperties.getSourceUri(),
        shovelProperties.getSourceUsername(), shovelProperties.getSourcePassword(), null,
        shovelProperties.getDestinationUri(), shovelProperties.getDestinationUsername(),
        shovelProperties.getDestinationPassword(), shovelProperties.getDestinationExchangeName(),
        shovelProperties.getDestinationRoutingKey());

    final Set<ConstraintViolation<ShovelProperties>> constraintViolations = validator.validate(shovelProperties);
    assertFalse(constraintViolations.isEmpty());
  }

  @Test
  void testGetSourceUri() {
    assertEquals(shovelSourceUri, shovelProperties.getSourceUri());
  }

  @Test
  void testGetSourceUriNotNull() {
    shovelProperties = new ShovelProperties(shovelProperties.getName(), null, shovelProperties.getSourceUsername(),
        shovelProperties.getSourcePassword(), shovelProperties.getSourceQueueName(),
        shovelProperties.getDestinationUri(), shovelProperties.getDestinationUsername(),
        shovelProperties.getDestinationPassword(), shovelProperties.getDestinationExchangeName(),
        shovelProperties.getDestinationRoutingKey());

    final Set<ConstraintViolation<ShovelProperties>> constraintViolations = validator.validate(shovelProperties);
    assertFalse(constraintViolations.isEmpty());
  }

  @Test
  void testGetSourceUsername() {
    assertEquals(shovelSourceUsername, shovelProperties.getSourceUsername());
  }

}
