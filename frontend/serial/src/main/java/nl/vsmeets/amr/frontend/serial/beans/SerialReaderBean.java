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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Service;

import com.fazecast.jSerialComm.SerialPort;

import lombok.Getter;
import nl.vsmeets.amr.service.p1telegram.reader.P1TelegramReader;

/**
 * A bean to read the P1 telegrams from a serial line and send them to the
 * message queue for storage.
 *
 * @author vincent
 */
@Service
public class SerialReaderBean implements ApplicationRunner, ExitCodeGenerator {

    /**
     * The exit code of the running command.
     */
    @Getter
    private int exitCode = 1;

    /**
     * The serial port.
     */
    @Autowired
    private SerialPort serialPort;

    /**
     * The P1 telegram reader.
     */
    @Autowired
    private P1TelegramReader p1TelegramReader;

    @Override
    public void run(final ApplicationArguments args) throws Exception {
        if (args.containsOption("No-Runner")) {
            exitCode = 0;
            return;
        }
        try (InputStream inputStream = serialPort.getInputStream()) {
            final Reader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            p1TelegramReader.save(bufferedReader);
        }
        exitCode = 0;
    }

}
