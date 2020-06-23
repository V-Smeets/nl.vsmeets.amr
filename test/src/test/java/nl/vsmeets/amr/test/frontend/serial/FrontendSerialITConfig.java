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
package nl.vsmeets.amr.test.frontend.serial;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.InputStream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fazecast.jSerialComm.SerialPort;

/**
 * The configuration for the {@link FrontendSerialIT}.
 *
 * @author vincent
 */
@Configuration
public class FrontendSerialITConfig {

  @Bean
  public SerialPort mockedSerialPort() {
    final InputStream inputStream = getClass().getResourceAsStream("/frontend/serial/p1-telegram.txt");

    final SerialPort serialPort = mock(SerialPort.class);
    when(serialPort.getInputStream()).then(i -> inputStream);
    return serialPort;
  }

}
