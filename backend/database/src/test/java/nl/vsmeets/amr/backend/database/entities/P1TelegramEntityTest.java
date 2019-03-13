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

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.common.testing.EqualsTester;

import nl.vsmeets.amr.backend.database.AbstractTestBase;

/**
 * Unit tests for the class {@link P1TelegramEntity}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class P1TelegramEntityTest extends AbstractTestBase {

  @Mock
  private SiteEntity siteEntity1;
  @Mock
  private SiteEntity siteEntity2;
  private final String headerInformation1 = randomString();
  private final String headerInformation2 = randomString();
  private final Byte versionInformation1 = randomByte();
  private final Byte versionInformation2 = randomByte();

  @Test
  void testEquals() {
    final EqualsTester equalsTester = new EqualsTester();
    equalsTester.addEqualityGroup(new P1TelegramEntity());
    Stream.of(siteEntity1, siteEntity2).forEach(siteEntity -> //
    Stream.of(headerInformation1, headerInformation2).forEach(headerInformation -> //
    Stream.of(versionInformation1, versionInformation2).forEach(versionInformation -> //
    equalsTester.addEqualityGroup( //
        Stream.of(1, 2).map(number -> //
        new P1TelegramEntity(siteEntity, headerInformation, versionInformation)).toArray()))));
    equalsTester.testEquals();
  }

  @Test
  void testGetters() {
    final P1TelegramEntity p1TelegramEntity =
        new P1TelegramEntity(siteEntity1, headerInformation1, versionInformation1);

    // @formatter:off
    assertAll(
        () -> assertEquals(siteEntity1, p1TelegramEntity.getSite()),
        () -> assertEquals(headerInformation1, p1TelegramEntity.getHeaderInformation()),
        () -> assertEquals(versionInformation1, p1TelegramEntity.getVersionInformation()),
        () -> assertNull(p1TelegramEntity.getMeters()));
    // @formatter:on
  }

  @Test
  void testRequiredParameters() {
    assertThrows(NullPointerException.class, () -> new P1TelegramEntity(null, headerInformation1, versionInformation1));
    assertThrows(NullPointerException.class, () -> new P1TelegramEntity(siteEntity1, null, versionInformation1));
    assertThrows(NullPointerException.class, () -> new P1TelegramEntity(siteEntity1, headerInformation1, null));
  }

  @Test
  void testToString() {
    final P1TelegramEntity p1TelegramEntity = new P1TelegramEntity();

    assertNotNull(p1TelegramEntity.toString());
  }

  @Test
  void testToStringWithParameters() {
    final P1TelegramEntity p1TelegramEntity =
        new P1TelegramEntity(siteEntity1, headerInformation1, versionInformation1);

    // @formatter:off
    assertAll(
        () -> assertNotNull(p1TelegramEntity.toString()),
        () -> assertTrue(p1TelegramEntity.toString().contains(p1TelegramEntity.getClass().getSimpleName())),
        () -> assertTrue(p1TelegramEntity.toString().contains(headerInformation1)),
        () -> assertTrue(p1TelegramEntity.toString().contains(versionInformation1.toString())));
    // @formatter:on
  }

}
