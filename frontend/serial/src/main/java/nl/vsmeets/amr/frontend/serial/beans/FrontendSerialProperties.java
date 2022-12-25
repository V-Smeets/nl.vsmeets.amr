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

import java.nio.file.Path;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The properties for the searial frontend.
 *
 * @author vincent
 */
@Component
@ConfigurationProperties("amr.frontend.serial")
@Getter
@Setter
@ToString
public class FrontendSerialProperties {

    /**
     * The path of the serial device.
     */
    @NotNull
    private Path device;

    /**
     * The baud rate of the device.
     */
    @NotNull
    @Positive
    private Integer baudRate;

    /**
     * The number of data bits.
     */
    @NotNull
    @Max(8)
    @Min(5)
    private Integer dataBits;

    /**
     * The type of parity.
     * <dl>
     * <dt>0</dt>
     * <dd>no parity</dd>
     * <dt>1</dt>
     * <dd>odd parity</dd>
     * <dt>2</dt>
     * <dd>even parity</dd>
     * <dt>3</dt>
     * <dd>mark parity</dd>
     * <dt>4</dt>
     * <dd>space parity</dd>
     * </dl>
     */
    @NotNull
    @Max(4)
    @Min(0)
    private Integer parity;

    /**
     * The number of stop bits.
     * <dl>
     * <dt>1</dt>
     * <dd>1 stop bit</dd>
     * <dt>2</dt>
     * <dd>1.5 stop bits</dd>
     * <dt>3</dt>
     * <dd>2 stop bits</dd>
     * </dl>
     */
    @NotNull
    @Max(3)
    @Min(1)
    private Integer stopBits;

}
