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
package nl.vsmeets.amr.frontend.fileimporter.beans;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import nl.vsmeets.amr.service.p1telegram.reader.P1TelegramReader;

/**
 * A bean to import a file of P1 telegrams and send it to the message queue for
 * storage.
 *
 * @author vincent
 */
@Service
@Log4j2
public class FileImporterBean implements ApplicationRunner, ExitCodeGenerator {

    /**
     * The exit code of the running command.
     */
    @Getter
    private int exitCode = 0;

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
        for (final String fileName : args.getNonOptionArgs()) {
            log.debug("Importing file {}", fileName);
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8))) {
                p1TelegramReader.save(reader);
            } catch (final IOException e) {
                log.error("Can't import the file", e);
                usage(1);
                return;
            }
        }
    }

    /**
     * Output the usage of the command and set the exit code.
     *
     * @param exitCode The exit code for this command.
     */
    private void usage(final int exitCode) {
        log.info("Usage: fileImporter <filename>...");
        this.exitCode = exitCode;
    }

}
