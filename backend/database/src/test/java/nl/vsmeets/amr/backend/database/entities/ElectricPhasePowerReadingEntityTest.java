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

import javax.measure.Quantity;
import javax.measure.quantity.ElectricCurrent;
import javax.measure.quantity.ElectricPotential;
import javax.measure.quantity.Power;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.common.testing.EqualsTester;

/**
 * Unit tests for the class {@link ElectricPhasePowerReadingEntity}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class ElectricPhasePowerReadingEntityTest {

    /**
     * Values used during tests.
     */
    private static final LocalDateTime localDateTime1 = LocalDateTime.MIN;
    private static final LocalDateTime localDateTime2 = LocalDateTime.MAX;
    private static final Byte phaseNumber1 = 1;
    private static final Byte phaseNumber2 = 2;

    @Mock
    private MeterEntity meter1;
    @Mock
    private MeterEntity meter2;
    @Mock
    private Quantity<ElectricPotential> instantaneousVoltage1;
    @Mock
    private Quantity<ElectricPotential> instantaneousVoltage2;
    @Mock
    private Quantity<ElectricCurrent> instantaneousCurrent1;
    @Mock
    private Quantity<ElectricCurrent> instantaneousCurrent2;
    @Mock
    private Quantity<Power> instantaneousConsumedPower1;
    @Mock
    private Quantity<Power> instantaneousConsumedPower2;
    @Mock
    private Quantity<Power> instantaneousProducedPower1;
    @Mock
    private Quantity<Power> instantaneousProducedPower2;

    @Test
    void testEquals() {
        final EqualsTester equalsTester = new EqualsTester();
        equalsTester.addEqualityGroup(new ElectricPhasePowerReadingEntity());
        Stream.of(meter1, meter2).forEach( //
                meter -> Stream.of(localDateTime1, localDateTime2).forEach( //
                        localDateTime -> Stream.of(phaseNumber1, phaseNumber2).forEach( //
                                phaseNumber -> equalsTester.addEqualityGroup(Stream
                                        .of(instantaneousVoltage1, instantaneousVoltage2).flatMap( //
                                                instantaneousVoltage -> Stream
                                                        .of(instantaneousCurrent1, instantaneousCurrent2).flatMap( //
                                                                instantaneousCurrent -> Stream.of(
                                                                        instantaneousConsumedPower1,
                                                                        instantaneousConsumedPower2).flatMap( //
                                                                                instantaneousConsumedPower -> Stream.of(
                                                                                        instantaneousProducedPower1,
                                                                                        instantaneousProducedPower1)
                                                                                        .map( //
                                                                                                instantaneousProducedPower -> new ElectricPhasePowerReadingEntity(
                                                                                                        meter,
                                                                                                        localDateTime,
                                                                                                        phaseNumber,
                                                                                                        instantaneousVoltage,
                                                                                                        instantaneousCurrent,
                                                                                                        instantaneousConsumedPower,
                                                                                                        instantaneousProducedPower)))))
                                        .toArray()))));
        equalsTester.testEquals();
    }

    @Test
    void testGetters() {
        final ElectricPhasePowerReadingEntity electricPhasePowerReadingEntity = new ElectricPhasePowerReadingEntity(
                meter1, localDateTime1, phaseNumber1, instantaneousVoltage1, instantaneousCurrent1,
                instantaneousConsumedPower1, instantaneousProducedPower1);

        assertAll( //
                () -> assertEquals(meter1, electricPhasePowerReadingEntity.getMeter()), //
                () -> assertEquals(localDateTime1, electricPhasePowerReadingEntity.getLocalDateTime()), //
                () -> assertEquals(phaseNumber1, electricPhasePowerReadingEntity.getPhaseNumber()), //
                () -> assertEquals(instantaneousVoltage1, electricPhasePowerReadingEntity.getInstantaneousVoltage()), //
                () -> assertEquals(instantaneousCurrent1, electricPhasePowerReadingEntity.getInstantaneousCurrent()), //
                () -> assertEquals(instantaneousConsumedPower1,
                        electricPhasePowerReadingEntity.getInstantaneousConsumedPower()), //
                () -> assertEquals(instantaneousProducedPower1,
                        electricPhasePowerReadingEntity.getInstantaneousProducedPower()) //
        );
    }

    @Test
    void testRequiredParameters() {
        assertThrows(NullPointerException.class,
                () -> new ElectricPhasePowerReadingEntity(null, localDateTime1, phaseNumber1, instantaneousVoltage1,
                        instantaneousCurrent1, instantaneousConsumedPower1, instantaneousProducedPower1));
        assertThrows(NullPointerException.class,
                () -> new ElectricPhasePowerReadingEntity(meter1, null, phaseNumber1, instantaneousVoltage1,
                        instantaneousCurrent1, instantaneousConsumedPower1, instantaneousProducedPower1));
        assertThrows(NullPointerException.class,
                () -> new ElectricPhasePowerReadingEntity(meter1, localDateTime1, null, instantaneousVoltage1,
                        instantaneousCurrent1, instantaneousConsumedPower1, instantaneousProducedPower1));
        assertThrows(NullPointerException.class, () -> new ElectricPhasePowerReadingEntity(meter1, localDateTime1,
                phaseNumber1, null, instantaneousCurrent1, instantaneousConsumedPower1, instantaneousProducedPower1));
        assertThrows(NullPointerException.class, () -> new ElectricPhasePowerReadingEntity(meter1, localDateTime1,
                phaseNumber1, instantaneousVoltage1, null, instantaneousConsumedPower1, instantaneousProducedPower1));
        assertThrows(NullPointerException.class, () -> new ElectricPhasePowerReadingEntity(meter1, localDateTime1,
                phaseNumber1, instantaneousVoltage1, instantaneousCurrent1, null, instantaneousProducedPower1));
        assertThrows(NullPointerException.class, () -> new ElectricPhasePowerReadingEntity(meter1, localDateTime1,
                phaseNumber1, instantaneousVoltage1, instantaneousCurrent1, instantaneousConsumedPower1, null));
    }

    @Test
    void testToString() {
        final ElectricPhasePowerReadingEntity electricPhasePowerReadingEntity = new ElectricPhasePowerReadingEntity();

        assertNotNull(electricPhasePowerReadingEntity.toString());
    }

    @Test
    void testToStringWithParameters() {
        final ElectricPhasePowerReadingEntity electricPhasePowerReadingEntity = new ElectricPhasePowerReadingEntity(
                meter1, localDateTime1, phaseNumber1, instantaneousVoltage1, instantaneousCurrent1,
                instantaneousConsumedPower1, instantaneousProducedPower1);

        assertAll( //
                () -> assertNotNull(electricPhasePowerReadingEntity.toString()), //
                () -> assertTrue(electricPhasePowerReadingEntity.toString()
                        .contains(electricPhasePowerReadingEntity.getClass().getSimpleName())), //
                () -> assertTrue(electricPhasePowerReadingEntity.toString().contains(localDateTime1.toString())), //
                () -> assertTrue(electricPhasePowerReadingEntity.toString().contains(phaseNumber1.toString())), //
                () -> assertTrue(electricPhasePowerReadingEntity.toString().contains(instantaneousVoltage1.toString())), //
                () -> assertTrue(electricPhasePowerReadingEntity.toString().contains(instantaneousCurrent1.toString())), //
                () -> assertTrue(
                        electricPhasePowerReadingEntity.toString().contains(instantaneousConsumedPower1.toString())), //
                () -> assertTrue(
                        electricPhasePowerReadingEntity.toString().contains(instantaneousProducedPower1.toString())) //
        );
    }

}
