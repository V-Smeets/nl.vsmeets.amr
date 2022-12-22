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

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.snksoft.crc.CRC;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import nl.vsmeets.amr.backend.amqp.P1TelegramSender;
import nl.vsmeets.amr.service.p1telegram.reader.P1TelegramReader;

/**
 * A bean to split a stream of characters into separate P1-Telegrams and forward
 * them to the server for storage.
 *
 * @author vincent
 */
@Service
@Log4j2
public class P1TelegramReaderBean implements P1TelegramReader {

    /**
     * The size of a P1 telegram. This will be used to preallocate a string buffer.
     */
    private static final int P1_TELEGRAM_SIZE = 1024;

    /**
     * A bean to calculate the CRC.
     */
    @Autowired
    private CRC crc;

    /**
     * The properties for this module.
     */
    @Autowired
    @Valid
    private ServiceP1TelegramReaderProperties properties;

    /**
     * A bean to send a P1 telegram to the message queue for storage.
     */
    @Autowired
    private P1TelegramSender p1TelegramSender;

    @Override
    public void save(final BufferedReader bufferedReader) throws IOException {
        final StringBuilder trashBuffer = new StringBuilder();
        final StringBuilder dataBuffer = new StringBuilder(P1_TELEGRAM_SIZE);
        final StringBuilder crcBuffer = new StringBuilder();
        StringBuilder p1TelegramBuffer = trashBuffer;
        for (int characterValue = bufferedReader.read(); characterValue >= 0; characterValue = bufferedReader.read()) {
            final char character = (char) characterValue;

            // Before the character has been appended to the buffer.
            if (p1TelegramBuffer == trashBuffer && character == '/') {
                trashBuffer.setLength(0);
                dataBuffer.setLength(0);
                crcBuffer.setLength(0);
                p1TelegramBuffer = dataBuffer;
            }

            // Append the character to the buffer.
            p1TelegramBuffer.append(character);

            // After the character has been appended to the buffer.
            switch (character) {
            case '!':
                if (p1TelegramBuffer == dataBuffer) {
                    p1TelegramBuffer = crcBuffer;
                }
                break;

            case '\n':
                if (p1TelegramBuffer == crcBuffer) {
                    sendTelegram(dataBuffer, crcBuffer);
                    p1TelegramBuffer = trashBuffer;
                }
                break;

            default:
                break;
            }
        }
    }

    /**
     * Check and send the P1 telegram.
     *
     * @param dataBuffer The buffer that holds the data part of the telegram.
     * @param crcBuffer  The buffer that holds the expected CRC of the dataBuffer.
     */
    private void sendTelegram(final StringBuilder dataBuffer, final StringBuilder crcBuffer) {
        final String site = properties.getSite();

        final String data = dataBuffer.toString();
        final byte[] dataBytes = data.getBytes(StandardCharsets.US_ASCII);
        final long calculatedCrc = crc.calculateCRC(dataBytes);

        final String crcString = crcBuffer.toString().trim();
        final long expectedCrc = Long.parseLong(crcString, 16);

        if (expectedCrc == calculatedCrc) {
            final StringBuilder p1TelegramBuffer = new StringBuilder(dataBuffer.length() + crcBuffer.length());
            p1TelegramBuffer.append(dataBuffer);
            p1TelegramBuffer.append(crcBuffer);
            p1TelegramSender.send(site, p1TelegramBuffer.toString());
        } else {
            log.warn("Incorrect CRC. Expected: {}, Calculated: {}", expectedCrc, calculatedCrc);
        }
    }

}
