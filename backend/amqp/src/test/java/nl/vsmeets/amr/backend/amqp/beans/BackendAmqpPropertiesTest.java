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

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/**
 * Unit tests for the class {@link BackendAmqpProperties}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class BackendAmqpPropertiesTest {

    /**
     * Values used during tests.
     */
    private static final String EXCHANG_NAME_1 = "Exchange Name 1";
    private static final String EXCHANG_NAME_2 = "Exchange Name 2";
    private static final String ROUTING_KEY_1 = "Routing Key 1";
    private static final String ROUTING_KEY_2 = "Routing Key 2";

    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();

    /**
     * The object under test.
     */
    @InjectMocks
    private BackendAmqpProperties backendAmqpProperties;

    private Validator validator;

    @BeforeEach
    void setUp() throws Exception {
        validator = VALIDATOR_FACTORY.getValidator();

        backendAmqpProperties.setExchangeName(EXCHANG_NAME_1);
        backendAmqpProperties.setRoutingKey(ROUTING_KEY_1);

        final Set<ConstraintViolation<BackendAmqpProperties>> constraintViolations = validator
                .validate(backendAmqpProperties);
        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    void testGetExchangeName() {
        backendAmqpProperties.setExchangeName(EXCHANG_NAME_2);

        final Set<ConstraintViolation<BackendAmqpProperties>> constraintViolations = validator
                .validate(backendAmqpProperties);
        assertTrue(constraintViolations.isEmpty());
        assertEquals(EXCHANG_NAME_2, backendAmqpProperties.getExchangeName());
    }

    @Test
    void testGetExchangeNameEmpty() {
        backendAmqpProperties.setExchangeName("");

        final Set<ConstraintViolation<BackendAmqpProperties>> constraintViolations = validator
                .validate(backendAmqpProperties);
        assertTrue(constraintViolations.isEmpty());
        assertEquals("", backendAmqpProperties.getExchangeName());
    }

    @Test
    void testGetExchangeNameNotNull() {
        backendAmqpProperties.setExchangeName(null);

        final Set<ConstraintViolation<BackendAmqpProperties>> constraintViolations = validator
                .validate(backendAmqpProperties);
        assertFalse(constraintViolations.isEmpty());
    }

    @Test
    void testGetRoutingKey() {
        backendAmqpProperties.setRoutingKey(ROUTING_KEY_2);

        final Set<ConstraintViolation<BackendAmqpProperties>> constraintViolations = validator
                .validate(backendAmqpProperties);
        assertTrue(constraintViolations.isEmpty());
        assertEquals(ROUTING_KEY_2, backendAmqpProperties.getRoutingKey());
    }

    @Test
    void testGetRoutingKeyNotEmpty() {
        backendAmqpProperties.setRoutingKey("");

        final Set<ConstraintViolation<BackendAmqpProperties>> constraintViolations = validator
                .validate(backendAmqpProperties);
        assertFalse(constraintViolations.isEmpty());
    }

    @Test
    void testGetRoutingKeyNotNull() {
        backendAmqpProperties.setRoutingKey(null);

        final Set<ConstraintViolation<BackendAmqpProperties>> constraintViolations = validator
                .validate(backendAmqpProperties);
        assertFalse(constraintViolations.isEmpty());
    }

    @Test
    void testToString() {
        final String toString = backendAmqpProperties.toString();
        assertAll( //
                () -> assertNotNull(toString), //
                () -> assertTrue(toString.contains(backendAmqpProperties.getClass().getSimpleName())), //
                () -> assertTrue(toString.contains(EXCHANG_NAME_1)), //
                () -> assertTrue(toString.contains(ROUTING_KEY_1)) //
        );
    }

}
