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
package nl.vsmeets.amr.frontend.serial;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fazecast.jSerialComm.SerialPortInvalidPortException;

import nl.vsmeets.amr.frontend.serial.beans.FrontendSerialProperties;

/**
 * Unit tests for the class {@link FrontendSerialConfig}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class FrontendSerialConfigTest {

    @InjectMocks
    private FrontendSerialConfig frontendSerialConfig;

    @Test
    void testSerialPort() {
        final FrontendSerialProperties properties = mock(FrontendSerialProperties.class);
        when(properties.getDevice()).thenReturn(Path.of("/dev/null"));
        when(properties.getBaudRate()).thenReturn(9600);
        when(properties.getDataBits()).thenReturn(8);
        when(properties.getStopBits()).thenReturn(1);
        when(properties.getParity()).thenReturn(0);

        assertThrows(SerialPortInvalidPortException.class, () -> frontendSerialConfig.serialPort(properties));
    }

}
