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

import com.google.common.testing.EqualsTester;

import nl.vsmeets.amr.libs.junit.RandomIntGenerator;

/**
 * Unit tests for the class {@link AbstractTableEntity}.
 *
 * @author vincent
 */
class AbstractTableEntityTest implements RandomIntGenerator {

  private final Integer id1 = randomInt();
  private final Integer id2 = randomInt(id1);

  @Test
  void testEquals() {
    final EqualsTester equalsTester = new EqualsTester();
    equalsTester.addEqualityGroup(new AbstractTableEntity() {});
    Stream.of(id1, id2).forEach(id -> //
    equalsTester.addEqualityGroup( //
        Stream.of(1, 2).map(number -> //
        new AbstractTableEntity(id) {}).toArray()));
    equalsTester.testEquals();
  }

  @Test
  void testGetters() {
    final AbstractTableEntity abstractTableEntity = new AbstractTableEntity(id1) {};

    assertEquals(id1, abstractTableEntity.getId());
  }

  @Test
  void testRequiredParameters() {
    assertThrows(NullPointerException.class, () -> new AbstractTableEntity(null) {});
  }

  @Test
  void testToString() {
    final AbstractTableEntity abstractTableEntity = new AbstractTableEntity() {};

    assertNotNull(abstractTableEntity.toString());
  }

  @Test
  void testToStringWithParameters() {
    final AbstractTableEntity abstractTableEntity = new AbstractTableEntity(id1) {};

    // @formatter:off
    assertAll(
        () -> assertNotNull(abstractTableEntity.toString()),
        () -> assertTrue(abstractTableEntity.toString().contains(abstractTableEntity.getClass().getSimpleName())),
        () -> assertTrue(abstractTableEntity.toString().contains(id1.toString())));
    // @formatter:on
  }

}
