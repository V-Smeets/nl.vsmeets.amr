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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortInvalidPortException;

import jakarta.validation.Valid;
import nl.vsmeets.amr.frontend.serial.beans.FrontendSerialProperties;
import nl.vsmeets.amr.service.p1telegram.reader.ServiceP1TelegramReaderConfig;

/**
 * The configuration class for the frontend serial module.
 *
 * @author vincent
 */
@Configuration
@ComponentScan
@Import(ServiceP1TelegramReaderConfig.class)
public class FrontendSerialConfig {

    /**
     * Create a bean to communicate with the serial port.
     *
     * @param properties The properties that define the serial port and it's
     *                   settings.
     * @return The initialized bean.
     */
    @Bean
    @Profile("!test")
    public SerialPort serialPort(@Valid final FrontendSerialProperties properties) {
        final SerialPort serialPort = SerialPort.getCommPort(properties.getDevice().toString());
        serialPort.setComPortParameters(properties.getBaudRate(), properties.getDataBits(), properties.getStopBits(),
                properties.getParity());
        if (!serialPort.openPort()) {
            throw new SerialPortInvalidPortException(properties.toString(), new Throwable());
        }
        return serialPort;
    }

}
