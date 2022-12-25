/**
 * Copyright (C) 2022 Vincent Smeets
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
/**
 * @author vincent
 */
module nl.vsmeets.amr.frontend.fileimporter {

    requires static lombok;
    requires nl.vsmeets.amr.service.p1telegram.reader;
    requires org.apache.logging.log4j;
    requires spring.beans;
    requires transitive spring.boot;
    requires spring.context;

    exports nl.vsmeets.amr.frontend.fileimporter;

    // Enable integration tests.
    exports nl.vsmeets.amr.frontend.fileimporter.beans to spring.boot, spring.beans;

    opens nl.vsmeets.amr.frontend.fileimporter to spring.core;
    opens nl.vsmeets.amr.frontend.fileimporter.beans to spring.core;

}
