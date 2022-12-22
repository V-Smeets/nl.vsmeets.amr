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
package nl.vsmeets.amr.backend.database.converters;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

/**
 * Unit tests for the class {@link Duration2LongConverter}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class Duration2LongConverterTest {

    /**
     * Values used during tests.
     */
    private static final long dbDataMin = -999_999_999L;
    private static final long dbDataZero = 0L;
    private static final long dbDataMax = 1_000_000_000L;

    /**
     * The object under test.
     */
    private Duration2LongConverter testObject;

    @BeforeEach
    void setUp(@Mock final ApplicationContext applicationContext) throws Exception {
        testObject = new Duration2LongConverter();
    }

    @ParameterizedTest
    @ValueSource(longs = { dbDataMin, dbDataZero, dbDataMax })
    void testConvertToDatabaseColumn(final long dbData) {
        final Duration attribute = Duration.ofSeconds(dbData);

        assertEquals(dbData, testObject.convertToDatabaseColumn(attribute));
        assertEquals(dbData, testObject.convertToDatabaseColumn(attribute));
    }

    @Test
    void testConvertToDatabaseColumnNull() {
        assertNull(testObject.convertToDatabaseColumn(null));
    }

    @ParameterizedTest
    @ValueSource(longs = { dbDataMin, dbDataZero, dbDataMax })
    void testConvertToEntityAttribute(final long dbData) {
        final Duration attribute = Duration.ofSeconds(dbData);

        assertEquals(attribute, testObject.convertToEntityAttribute(dbData));
    }

    @Test
    void testConvertToEntityAttributeNull() {
        assertNull(testObject.convertToEntityAttribute(null));
    }

}
