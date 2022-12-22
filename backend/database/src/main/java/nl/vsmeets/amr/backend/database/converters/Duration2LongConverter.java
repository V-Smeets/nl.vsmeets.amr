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
package nl.vsmeets.amr.backend.database.converters;

import java.time.Duration;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Convert between {@link Duration} and {@link Long}.
 *
 * @author vincent
 */
@Converter
public class Duration2LongConverter implements AttributeConverter<Duration, Long> {

    @Override
    public Long convertToDatabaseColumn(final Duration attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getSeconds();
    }

    @Override
    public Duration convertToEntityAttribute(final Long dbData) {
        if (dbData == null) {
            return null;
        }
        return Duration.ofSeconds(dbData);
    }

}
