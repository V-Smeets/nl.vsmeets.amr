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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;

import com.fazecast.jSerialComm.SerialPort;

import nl.vsmeets.amr.service.p1telegram.reader.P1TelegramReader;

/**
 * Unit tests for the class {@link SerialReaderBean}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class SerialReaderBeanTest {

    /**
     * Values used during tests.
     */
    private static final String serialData = "Serial Data";

    @Mock
    private SerialPort serialPort;
    @Mock
    private P1TelegramReader p1TelegramReader;

    @InjectMocks
    private SerialReaderBean serialReaderBean;

    @Test
    void testRunError() throws Exception {
        final ApplicationArguments args = mock(ApplicationArguments.class);
        final InputStream inputStream = new ByteArrayInputStream(serialData.getBytes(StandardCharsets.UTF_8));
        when(serialPort.getInputStream()).then(i -> inputStream);
        doThrow(new IOException()).when(p1TelegramReader).save(any());

        assertThrows(IOException.class, () -> serialReaderBean.run(args));

        assertNotEquals(0, serialReaderBean.getExitCode());
    }

    @Test
    void testRunOk() throws Exception {
        final ApplicationArguments args = mock(ApplicationArguments.class);
        final InputStream inputStream = new ByteArrayInputStream(serialData.getBytes(StandardCharsets.UTF_8));
        when(serialPort.getInputStream()).then(i -> inputStream);

        serialReaderBean.run(args);

        assertEquals(0, serialReaderBean.getExitCode());
    }

    @Test
    void testRunOptionNoRunner() throws Exception {
        final String[] args = { "--No-Runner" };
        final ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);

        serialReaderBean.run(applicationArguments);

        assertEquals(0, serialReaderBean.getExitCode());
    }

}
