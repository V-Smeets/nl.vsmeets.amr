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
package nl.vsmeets.amr.backend.database.entities;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.ZoneId;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.google.common.testing.EqualsTester;

/**
 * Unit tests for the class {@link SiteEntity}.
 *
 * @author vincent
 */
class SiteEntityTest {

    /**
     * Values used during tests.
     */
    private static final String name1 = "Name 1";
    private static final String name2 = "Name 2";
    private static final String[] ZoneIds = ZoneId.getAvailableZoneIds().toArray(String[]::new);
    private static final ZoneId timeZone1 = ZoneId.of(ZoneIds[0]);
    private static final ZoneId timeZone2 = ZoneId.of(ZoneIds[1]);

    @Test
    void testEquals() {
        final EqualsTester equalsTester = new EqualsTester();
        equalsTester.addEqualityGroup(new SiteEntity());
        Stream.of(name1, name2).forEach( //
                name -> equalsTester.addEqualityGroup(Stream.of(timeZone1, timeZone2).map( //
                        timeZone -> new SiteEntity(name, timeZone)).toArray()));
        equalsTester.testEquals();
    }

    @Test
    void testGetters() {
        final SiteEntity siteEntity = new SiteEntity(name1, timeZone1);

        assertAll( //
                () -> assertEquals(name1, siteEntity.getName()), //
                () -> assertEquals(timeZone1, siteEntity.getTimeZone()), //
                () -> assertNull(siteEntity.getP1Telegrams()) //
        );
    }

    @Test
    void testRequiredParameters() {
        assertThrows(NullPointerException.class, () -> new SiteEntity(null, timeZone1));
        assertThrows(NullPointerException.class, () -> new SiteEntity(name1, null));
    }

    @Test
    void testToString() {
        final SiteEntity siteEntity = new SiteEntity();

        assertNotNull(siteEntity.toString());
    }

    @Test
    void testToStringWithParameters() {
        final SiteEntity siteEntity = new SiteEntity(name1, timeZone1);

        assertAll( //
                () -> assertNotNull(siteEntity.toString()), //
                () -> assertTrue(siteEntity.toString().contains(siteEntity.getClass().getSimpleName())), //
                () -> assertTrue(siteEntity.toString().contains(name1)), //
                () -> assertTrue(siteEntity.toString().contains(timeZone1.toString())) //
        );
    }

}
