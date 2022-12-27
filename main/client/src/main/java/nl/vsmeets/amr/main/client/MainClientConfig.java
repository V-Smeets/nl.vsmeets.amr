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
package nl.vsmeets.amr.main.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

import nl.vsmeets.amr.frontend.serial.FrontendSerialConfig;

/**
 * The main entry point for the client.
 *
 * @author vincent
 */
@SpringBootApplication
@Import(FrontendSerialConfig.class)
public class MainClientConfig {

    /**
     * The main entry point.
     *
     * @param args The command line arguments.
     */
    public static void main(final String[] args) {
        final ConfigurableApplicationContext context = SpringApplication.run(MainClientConfig.class, args);
        context.close();
    }

}
