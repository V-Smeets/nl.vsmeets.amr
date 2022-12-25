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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;

import nl.vsmeets.amr.service.p1telegram.reader.P1TelegramReader;

/**
 * Unit tests for the class {@link FileImporterBean}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class FileImporterBeanTest {

    @Mock
    private P1TelegramReader p1TelegramReader;

    @InjectMocks
    private FileImporterBean fileImporterBean;

    @Test
    void testRunErrorFile() throws Exception {
        final File tempFile = File.createTempFile("temp", "txt");
        tempFile.deleteOnExit();
        final String[] args = { tempFile.getPath() };
        final ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);
        doThrow(IOException.class).when(p1TelegramReader).save(any(BufferedReader.class));

        fileImporterBean.run(applicationArguments);

        assertEquals(1, fileImporterBean.getExitCode());
    }

    @Test
    void testRunNoArgument() throws Exception {
        final String[] args = {};
        final ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);

        fileImporterBean.run(applicationArguments);

        assertEquals(0, fileImporterBean.getExitCode());
    }

    @Test
    void testRunNotAFile() throws Exception {
        final String[] args = { "NotAFile.txt" };
        final ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);

        fileImporterBean.run(applicationArguments);

        assertEquals(1, fileImporterBean.getExitCode());
    }

    @Test
    void testRunOneFile() throws Exception {
        final File tempFile = File.createTempFile("temp", "txt");
        tempFile.deleteOnExit();
        final String[] args = { tempFile.getPath() };
        final ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);

        fileImporterBean.run(applicationArguments);

        verify(p1TelegramReader).save(any(BufferedReader.class));
        assertEquals(0, fileImporterBean.getExitCode());
    }

    @Test
    void testRunOptionNoRunner() throws Exception {
        final String[] args = { "--No-Runner" };
        final ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);

        fileImporterBean.run(applicationArguments);

        assertEquals(0, fileImporterBean.getExitCode());
    }

}
