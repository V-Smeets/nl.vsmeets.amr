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
package nl.vsmeets.amr.backend.database.entities;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.common.testing.EqualsTester;

/**
 * Unit tests for the class {@link ElectricMessageEntity}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class ElectricMessageEntityTest {

    /**
     * Values used during tests.
     */
    private static final LocalDateTime localDateTime1 = LocalDateTime.MIN;
    private static final LocalDateTime localDateTime2 = LocalDateTime.MAX;
    private static final String textMessage1 = "Text Message 1";
    private static final String textMessage2 = "Text Message 2";

    @Mock
    private MeterEntity meter1;
    @Mock
    private MeterEntity meter2;

    @Test
    void testEquals() {
        final EqualsTester equalsTester = new EqualsTester();
        equalsTester.addEqualityGroup(new ElectricMessageEntity());
        Stream.of(meter1, meter2).forEach( //
                meter -> Stream.of(localDateTime1, localDateTime2).forEach( //
                        localDateTime -> equalsTester.addEqualityGroup(Stream.of(textMessage1, textMessage2).map( //
                                textMessage -> new ElectricMessageEntity(meter, localDateTime, textMessage))
                                .toArray())));
        equalsTester.testEquals();
    }

    @Test
    void testGetters() {
        final ElectricMessageEntity electricMessageEntity = new ElectricMessageEntity(meter1, localDateTime1,
                textMessage1);

        assertAll( //
                () -> assertEquals(meter1, electricMessageEntity.getMeter()), //
                () -> assertEquals(localDateTime1, electricMessageEntity.getLocalDateTime()), //
                () -> assertEquals(textMessage1, electricMessageEntity.getTextMessage()) //
        );
    }

    @Test
    void testRequiredParameters() {
        assertThrows(NullPointerException.class, () -> new ElectricMessageEntity(null, localDateTime1, textMessage1));
        assertThrows(NullPointerException.class, () -> new ElectricMessageEntity(meter1, null, textMessage1));
        assertThrows(NullPointerException.class, () -> new ElectricMessageEntity(meter1, localDateTime1, null));
    }

    @Test
    void testToString() {
        final ElectricMessageEntity electricMessageEntity = new ElectricMessageEntity();

        assertNotNull(electricMessageEntity.toString());
    }

    @Test
    void testToStringWithParameters() {
        final ElectricMessageEntity electricMessageEntity = new ElectricMessageEntity(meter1, localDateTime1,
                textMessage1);

        assertAll( //
                () -> assertNotNull(electricMessageEntity.toString()), //
                () -> assertTrue(
                        electricMessageEntity.toString().contains(electricMessageEntity.getClass().getSimpleName())), //
                () -> assertTrue(electricMessageEntity.toString().contains(localDateTime1.toString())), //
                () -> assertTrue(electricMessageEntity.toString().contains(textMessage1)) //
        );
    }

}
