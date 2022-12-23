/**
 * Copyright (C) 2019 Vincent Smeets
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
package nl.vsmeets.amr.frontend.amqp.beans;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for the class {@link FrontendAmqpProperties}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class FrontendAmqpPropertiesTest {

    /**
     * Values used during tests.
     */
    private static final String queueName = "Queue Name";
    private static final String deadLetterQueueName = "Dead Letter Queue Name";

    @InjectMocks
    private FrontendAmqpProperties frontendAmqpProperties;

    @BeforeEach
    void setUp() throws Exception {
        frontendAmqpProperties.setQueueName(queueName);
        frontendAmqpProperties.setDeadLetterQueueName(deadLetterQueueName);
    }

    @Test
    void testGetDeadLetterQueueName() {
        assertEquals(deadLetterQueueName, frontendAmqpProperties.getDeadLetterQueueName());
    }

    @Test
    void testGetQueueName() {
        assertEquals(queueName, frontendAmqpProperties.getQueueName());
    }

    @Test
    void testToString() {
        final String toString = frontendAmqpProperties.toString();
        assertAll( //
                () -> assertNotNull(toString), //
                () -> assertTrue(toString.contains(frontendAmqpProperties.getClass().getSimpleName())) //
        );
    }

}
