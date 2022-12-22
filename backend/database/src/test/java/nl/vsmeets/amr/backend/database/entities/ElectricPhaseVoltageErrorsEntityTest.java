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
 * Unit tests for the class {@link ElectricPhaseVoltageErrorsEntity}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class ElectricPhaseVoltageErrorsEntityTest {

    /**
     * Values used during tests.
     */
    private static final LocalDateTime localDateTime1 = LocalDateTime.MIN;
    private static final LocalDateTime localDateTime2 = LocalDateTime.MAX;
    private static final Byte phaseNumber1 = 1;
    private static final Byte phaseNumber2 = 2;
    private static final Integer nrOfVoltageSags1 = 3;
    private static final Integer nrOfVoltageSags2 = 4;
    private static final Integer nrOfVoltageSwells1 = 5;
    private static final Integer nrOfVoltageSwells2 = 6;

    @Mock
    private MeterEntity meter1;
    @Mock
    private MeterEntity meter2;

    @Test
    void testEquals() {
        final EqualsTester equalsTester = new EqualsTester();
        equalsTester.addEqualityGroup(new ElectricPhaseVoltageErrorsEntity());
        Stream.of(meter1, meter2).forEach( //
                meter -> Stream.of(localDateTime1, localDateTime2).forEach( //
                        localDateTime -> Stream.of(phaseNumber1, phaseNumber2).forEach( //
                                phaseNumber -> equalsTester.addEqualityGroup(Stream
                                        .of(nrOfVoltageSags1, nrOfVoltageSags2).flatMap( //
                                                nrOfVoltageSags -> Stream.of(nrOfVoltageSwells1, nrOfVoltageSwells2)
                                                        .map( //
                                                                nrOfVoltageSwells -> new ElectricPhaseVoltageErrorsEntity(
                                                                        meter, localDateTime, phaseNumber,
                                                                        nrOfVoltageSags, nrOfVoltageSwells)))
                                        .toArray()))));
        equalsTester.testEquals();
    }

    @Test
    void testGetters() {
        final ElectricPhaseVoltageErrorsEntity electricPhaseVoltageErrorsEntity = new ElectricPhaseVoltageErrorsEntity(
                meter1, localDateTime1, phaseNumber1, nrOfVoltageSags1, nrOfVoltageSwells1);

        assertAll( //
                () -> assertEquals(meter1, electricPhaseVoltageErrorsEntity.getMeter()), //
                () -> assertEquals(localDateTime1, electricPhaseVoltageErrorsEntity.getLocalDateTime()), //
                () -> assertEquals(phaseNumber1, electricPhaseVoltageErrorsEntity.getPhaseNumber()), //
                () -> assertEquals(nrOfVoltageSags1, electricPhaseVoltageErrorsEntity.getNrOfVoltageSags()), //
                () -> assertEquals(nrOfVoltageSwells1, electricPhaseVoltageErrorsEntity.getNrOfVoltageSwells()) //
        );
    }

    @Test
    void testRequiredParameters() {
        assertThrows(NullPointerException.class, () -> new ElectricPhaseVoltageErrorsEntity(null, localDateTime1,
                phaseNumber1, nrOfVoltageSags1, nrOfVoltageSwells1));
        assertThrows(NullPointerException.class, () -> new ElectricPhaseVoltageErrorsEntity(meter1, null, phaseNumber1,
                nrOfVoltageSags1, nrOfVoltageSwells1));
        assertThrows(NullPointerException.class, () -> new ElectricPhaseVoltageErrorsEntity(meter1, localDateTime1,
                null, nrOfVoltageSags1, nrOfVoltageSwells1));
        assertThrows(NullPointerException.class, () -> new ElectricPhaseVoltageErrorsEntity(meter1, localDateTime1,
                phaseNumber1, null, nrOfVoltageSwells1));
        assertThrows(NullPointerException.class, () -> new ElectricPhaseVoltageErrorsEntity(meter1, localDateTime1,
                phaseNumber1, nrOfVoltageSags1, null));
    }

    @Test
    void testToString() {
        final ElectricPhaseVoltageErrorsEntity electricPhaseVoltageErrorsEntity = new ElectricPhaseVoltageErrorsEntity();

        assertNotNull(electricPhaseVoltageErrorsEntity.toString());
    }

    @Test
    void testToStringWithParameters() {
        final ElectricPhaseVoltageErrorsEntity electricPhaseVoltageErrorsEntity = new ElectricPhaseVoltageErrorsEntity(
                meter1, localDateTime1, phaseNumber1, nrOfVoltageSags1, nrOfVoltageSwells1);

        assertAll( //
                () -> assertNotNull(electricPhaseVoltageErrorsEntity.toString()), //
                () -> assertTrue(electricPhaseVoltageErrorsEntity.toString()
                        .contains(electricPhaseVoltageErrorsEntity.getClass().getSimpleName())), //
                () -> assertTrue(electricPhaseVoltageErrorsEntity.toString().contains(localDateTime1.toString())), //
                () -> assertTrue(electricPhaseVoltageErrorsEntity.toString().contains(phaseNumber1.toString())), //
                () -> assertTrue(electricPhaseVoltageErrorsEntity.toString().contains(nrOfVoltageSags1.toString())), //
                () -> assertTrue(electricPhaseVoltageErrorsEntity.toString().contains(nrOfVoltageSwells1.toString())) //
        );
    }

}
