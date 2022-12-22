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
package nl.vsmeets.amr.service.p1telegram.reader;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * A bean to split a stream of characters into separate P1-Telegrams and forward
 * them to the server for storage.
 *
 * @author vincent
 */
public interface P1TelegramReader {

    /**
     * Read P1-Telegrams from the stream and forward them to the server. This method
     * will return after all data has been read.
     *
     * @param bufferedReader The stream. All the data will be read. The stream will
     *                       not be closed at the end.
     * @throws IOException There was a problem reading from the stream.
     */
    void save(final BufferedReader bufferedReader) throws IOException;

}
