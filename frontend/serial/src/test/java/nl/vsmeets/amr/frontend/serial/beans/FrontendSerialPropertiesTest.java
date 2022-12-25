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
package nl.vsmeets.amr.frontend.serial.beans;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for the class {@link FrontendSerialProperties}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class FrontendSerialPropertiesTest {

    /**
     * Values used during tests.
     */
    private static final Integer baudRate = 115_200;
    private static final Integer dataBits = 8;
    private static final Integer parity = 0;
    private static final Integer stopBits = 1;

    @Mock
    private Path device;

    @InjectMocks
    private FrontendSerialProperties frontendSerialProperties;

    @BeforeEach
    void setUp() throws Exception {
        frontendSerialProperties.setDevice(device);
        frontendSerialProperties.setBaudRate(baudRate);
        frontendSerialProperties.setDataBits(dataBits);
        frontendSerialProperties.setParity(parity);
        frontendSerialProperties.setStopBits(stopBits);
    }

    @Test
    void testGetBaudRate() {
        assertEquals(baudRate, frontendSerialProperties.getBaudRate());
    }

    @Test
    void testGetDataBits() {
        assertEquals(dataBits, frontendSerialProperties.getDataBits());
    }

    @Test
    void testGetDevice() {
        assertEquals(device, frontendSerialProperties.getDevice());
    }

    @Test
    void testGetParity() {
        assertEquals(parity, frontendSerialProperties.getParity());
    }

    @Test
    void testGetStopBits() {
        assertEquals(stopBits, frontendSerialProperties.getStopBits());
    }

    @Test
    void testToString() {
        final String toString = frontendSerialProperties.toString();
        assertAll( //
                () -> assertNotNull(toString), //
                () -> assertTrue(toString.contains(frontendSerialProperties.getClass().getSimpleName())) //
        );
    }

}
