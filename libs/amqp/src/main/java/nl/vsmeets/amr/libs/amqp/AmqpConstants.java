/**
 * Copyright (C) 2020 Vincent Smeets
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
package nl.vsmeets.amr.libs.amqp;

/**
 * Constants that are defined for the AMQP interface.
 *
 * @author vincent
 */
public final class AmqpConstants {

    /**
     * The name of the header entry that defines the site.
     */
    public static final String HEADER_SITE = "Site";

    /**
     * No instances can be created from this class.
     */
    private AmqpConstants() {
    }

}
