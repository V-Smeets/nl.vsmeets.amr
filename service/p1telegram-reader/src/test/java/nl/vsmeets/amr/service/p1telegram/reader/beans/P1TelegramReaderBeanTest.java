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
package nl.vsmeets.amr.service.p1telegram.reader.beans;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.snksoft.crc.CRC;

import nl.vsmeets.amr.backend.amqp.P1TelegramSender;

/**
 * Unit tests for the class {@link P1TelegramReaderBean}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class P1TelegramReaderBeanTest {

    /**
     * Values used during tests.
     */
    private static final String site = "Site";

    private static final String CR_NL = "\r\n";

    @Mock
    private CRC crc;

    @Mock
    private ServiceP1TelegramReaderProperties properties;

    @Mock
    private P1TelegramSender p1TelegramSender;

    @InjectMocks
    private P1TelegramReaderBean p1TelegramReaderBean;

    @Test
    void testSave() throws IOException {
        final StringBuilder p1Telegram = new StringBuilder();
        p1Telegram.append("/Header").append(CR_NL);
        p1Telegram.append(CR_NL);
        p1Telegram.append("Data").append(CR_NL);
        p1Telegram.append("!0101").append(CR_NL);
        final StringBuilder inputData = new StringBuilder();
        inputData.append(p1Telegram);
        final BufferedReader bufferedReader = new BufferedReader(new StringReader(inputData.toString()));

        Mockito.when(properties.getSite()).thenReturn(site);
        Mockito.when(crc.calculateCRC(any())).thenReturn(257L);

        p1TelegramReaderBean.save(bufferedReader);

        verify(p1TelegramSender).send(eq(site), eq(p1Telegram.toString()));
    }

    @Test
    void testSaveEmptyCrcFound() throws IOException {
        final StringBuilder inputData = new StringBuilder();
        inputData.append("/Header").append(CR_NL);
        inputData.append(CR_NL);
        inputData.append("Data").append(CR_NL);
        inputData.append("!");
        final BufferedReader bufferedReader = new BufferedReader(new StringReader(inputData.toString()));

        p1TelegramReaderBean.save(bufferedReader);

        verify(p1TelegramSender, never()).send(anyString(), anyString());
    }

    @Test
    void testSaveIncompleteCrcFound() throws IOException {
        final StringBuilder inputData = new StringBuilder();
        inputData.append("/Header").append(CR_NL);
        inputData.append(CR_NL);
        inputData.append("Data").append(CR_NL);
        inputData.append("!01").append(CR_NL);
        final BufferedReader bufferedReader = new BufferedReader(new StringReader(inputData.toString()));

        Mockito.when(properties.getSite()).thenReturn(site);

        p1TelegramReaderBean.save(bufferedReader);

        verify(p1TelegramSender, never()).send(anyString(), anyString());
    }

    @Test
    void testSaveIncorrectCrcFound() throws IOException {
        final StringBuilder inputData = new StringBuilder();
        inputData.append("/Header").append(CR_NL);
        inputData.append(CR_NL);
        inputData.append("Data").append(CR_NL);
        inputData.append("!0101").append(CR_NL);
        final BufferedReader bufferedReader = new BufferedReader(new StringReader(inputData.toString()));

        Mockito.when(properties.getSite()).thenReturn(site);
        Mockito.when(crc.calculateCRC(any())).thenReturn(123L);

        p1TelegramReaderBean.save(bufferedReader);

        verify(p1TelegramSender, never()).send(anyString(), anyString());
    }

    @Test
    void testSaveNoCrcLineFound() throws IOException {
        final StringBuilder inputData = new StringBuilder();
        inputData.append("/Header").append(CR_NL);
        inputData.append(CR_NL);
        inputData.append("Data").append(CR_NL);
        inputData.append("/Header").append(CR_NL);
        final BufferedReader bufferedReader = new BufferedReader(new StringReader(inputData.toString()));

        p1TelegramReaderBean.save(bufferedReader);

        verify(p1TelegramSender, never()).send(anyString(), anyString());
    }

    @Test
    void testSaveNoHeaderLineFound() throws IOException {
        final StringBuilder inputData = new StringBuilder();
        inputData.append(CR_NL);
        inputData.append("Data").append(CR_NL);
        inputData.append("!0101").append(CR_NL);
        final BufferedReader bufferedReader = new BufferedReader(new StringReader(inputData.toString()));

        p1TelegramReaderBean.save(bufferedReader);

        verify(p1TelegramSender, never()).send(anyString(), anyString());
    }

}
