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

import org.springframework.stereotype.Service;

import com.github.snksoft.crc.CRC;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import nl.vsmeets.amr.backend.jms.P1TelegramSender;
import nl.vsmeets.amr.service.p1telegram.reader.P1TelegramReader;

/**
 * A bean to split a stream of characters into separate P1-Telegrams and forward
 * them to the server for storage.
 *
 * @author vincent
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class P1TelegramReaderBean implements P1TelegramReader {

  /**
   * The size of a P1 telegram. This will be used to preallocate a string buffer.
   */
  private static final int P1_TELEGRAM_SIZE = 1024;

  /**
   * A bean to calculate the CRC.
   */
  private final CRC crc;

  /**
   * The properties for this module.
   */
  private final ServiceP1TelegramReaderProperties properties;

  /**
   * A bean to send a P1 telegram to the message queue for storage.
   */
  private final P1TelegramSender p1TelegramSender;

  @Override
  public void save(final BufferedReader bufferedReader) throws IOException {
    final String site = properties.getSite();
    StringBuilder p1TelegramBuffer = null;
    boolean startOfLine = true;
    for (int i = bufferedReader.read(); i >= 0; i = bufferedReader.read()) {
      char c = (char) i;
      if (startOfLine) {
        switch (c) {
          // Start of the Header line.
          case '/':
            if (p1TelegramBuffer != null) {
              log.warn("No CRC line found in the P1 telegram: {}", p1TelegramBuffer);
            }
            p1TelegramBuffer = new StringBuilder(P1_TELEGRAM_SIZE);
            break;

          // Start of the CRC line.
          case '!':
            if (p1TelegramBuffer == null) {
              log.warn("CRC line found without a header line");
              break;
            }
            p1TelegramBuffer.append(c);
            final String p1TelegramWithoutCrc = p1TelegramBuffer.toString();
            final long calculatedCrc = crc.calculateCRC(p1TelegramWithoutCrc.getBytes(StandardCharsets.US_ASCII));
            // Read the CRC16 in hex without \r\n
            final String crcCharacters = bufferedReader.readLine();
            if (crcCharacters == null || crcCharacters.length() != 4) {
              log.warn("Incomplete CRC found. {}", p1TelegramBuffer);
              p1TelegramBuffer = null;
              c = '\n';
              break;
            }
            p1TelegramBuffer.append(crcCharacters).append("\r\n");
            final long expectedCrc = Long.parseLong(crcCharacters, 16);
            if (calculatedCrc != expectedCrc) {
              log.warn("Incorrect CRC. Expected: {}, Calculated: {}", expectedCrc, calculatedCrc);
              c = '\n';
              break;
            }
            p1TelegramSender.send(site, p1TelegramBuffer.toString());
            p1TelegramBuffer = null;
            startOfLine = true;
            continue;

          default:
            break;
        }
      }
      if (p1TelegramBuffer != null) {
        p1TelegramBuffer.append(c);
      }
      startOfLine = c == '\r' || c == '\n';
    }
  }

}
