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
package nl.vsmeets.amr.frontend.serial.beans;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nl.vsmeets.amr.libs.junit.RandomIntGenerator;

/**
 * Unit tests for the class {@link FrontendSerialProperties}.
 *
 * @author vincent
 */
@ExtendWith(MockitoExtension.class)
class FrontendSerialPropertiesTest implements RandomIntGenerator {

  @Mock
  private Path device;

  private final Integer baudRate = randomIntRange(0, 115_200);

  private final Integer dataBits = randomIntRange(5, 9);

  private final Integer parity = randomIntRange(0, 5);

  private final Integer stopBits = randomIntRange(1, 4);

  private FrontendSerialProperties frontendSerialProperties;

  @BeforeEach
  void setUp() throws Exception {
    frontendSerialProperties = new FrontendSerialProperties();
    frontendSerialProperties.setDevice(device);
    frontendSerialProperties.setBaudRate(baudRate);
    frontendSerialProperties.setDataBits(dataBits);
    frontendSerialProperties.setParity(parity);
    frontendSerialProperties.setStopBits(stopBits);
  }

  @Test
  void testGetBaudRate() {
    assertEquals(baudRate, frontendSerialProperties.getBaudRate());
  }

  @Test
  void testGetDataBits() {
    assertEquals(dataBits, frontendSerialProperties.getDataBits());
  }

  @Test
  void testGetDevice() {
    assertEquals(device, frontendSerialProperties.getDevice());
  }

  @Test
  void testGetParity() {
    assertEquals(parity, frontendSerialProperties.getParity());
  }

  @Test
  void testGetStopBits() {
    assertEquals(stopBits, frontendSerialProperties.getStopBits());
  }

  @Test
  void testToString() {
    final String toString = frontendSerialProperties.toString();
    assertAll( //
        () -> assertNotNull(toString), //
        () -> assertTrue(toString.contains(frontendSerialProperties.getClass().getSimpleName())) //
    );
  }

}
