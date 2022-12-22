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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.common.testing.EqualsTester;

/**
 * Unit tests for the class {@link MeterEntity}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class MeterEntityTest {

    /**
     * Values used during tests.
     */
    private static final String equipmentIdentifier1 = "Equipment Identifier 1";
    private static final String equipmentIdentifier2 = "Equipment Identifier 2";

    @Mock
    private P1TelegramEntity p1Telegram1;
    @Mock
    private P1TelegramEntity p1Telegram2;
    @Mock
    private MeasuredMediumEntity measuredMedium1;
    @Mock
    private MeasuredMediumEntity measuredMedium2;

    @Test
    void testEquals() {
        final EqualsTester equalsTester = new EqualsTester();
        equalsTester.addEqualityGroup(new MeterEntity());
        Stream.of(p1Telegram1, p1Telegram2).forEach( //
                p1Telegram -> Stream.of(measuredMedium1, measuredMedium2).forEach( //
                        measuredMedium -> Stream.of(equipmentIdentifier1, equipmentIdentifier2).forEach( //
                                equipmentIdentifier -> equalsTester.addEqualityGroup(Stream.of(1, 2).map( //
                                        number -> new MeterEntity(p1Telegram, measuredMedium, equipmentIdentifier))
                                        .toArray()))));
        equalsTester.testEquals();
    }

    @Test
    void testGetters() {
        final MeterEntity meterEntity = new MeterEntity(p1Telegram1, measuredMedium1, equipmentIdentifier1);

        assertAll( //
                () -> assertEquals(p1Telegram1, meterEntity.getP1Telegram()), //
                () -> assertEquals(measuredMedium1, meterEntity.getMeasuredMedium()), //
                () -> assertEquals(equipmentIdentifier1, meterEntity.getEquipmentIdentifier()), //
                () -> assertNull(meterEntity.getElectricEnergyReadings()), //
                () -> assertNull(meterEntity.getElectricPowerReadings()), //
                () -> assertNull(meterEntity.getElectricPowerFailures()), //
                () -> assertNull(meterEntity.getElectricPhaseVoltageErrors()), //
                () -> assertNull(meterEntity.getElectricMessages()), //
                () -> assertNull(meterEntity.getElectricPhasePowerReadings()), //
                () -> assertNull(meterEntity.getGasVolumeReadings()), //
                () -> assertNull(meterEntity.getThermalEnergyReadings()), //
                () -> assertNull(meterEntity.getWaterVolumeReadings()), //
                () -> assertNull(meterEntity.getSlaveEnergyReadings()) //
        );
    }

    @Test
    void testRequiredParameters() {
        assertThrows(NullPointerException.class, () -> new MeterEntity(null, measuredMedium1, equipmentIdentifier1));
        assertThrows(NullPointerException.class, () -> new MeterEntity(p1Telegram1, null, equipmentIdentifier1));
        assertThrows(NullPointerException.class, () -> new MeterEntity(p1Telegram1, measuredMedium1, null));
    }

    @Test
    void testToString() {
        final MeterEntity meterEntity = new MeterEntity();

        assertNotNull(meterEntity.toString());
    }

    @Test
    void testToStringWithParameters() {
        final MeterEntity meterEntity = new MeterEntity(p1Telegram1, measuredMedium1, equipmentIdentifier1);

        assertAll( //
                () -> assertNotNull(meterEntity.toString()), //
                () -> assertTrue(meterEntity.toString().contains(meterEntity.getClass().getSimpleName())), //
                () -> assertTrue(meterEntity.toString().contains(equipmentIdentifier1)) //
        );
    }

}
