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
module nl.vsmeets.amr.test.fileimporter {

    requires transitive com.rabbitmq.client;
    requires net.bytebuddy;
    requires nl.vsmeets.amr.backend.amqp;
    requires nl.vsmeets.amr.frontend.fileimporter;
    requires org.junit.jupiter.api;
    requires org.junit.jupiter.engine;
    requires org.mockito;
    requires org.slf4j;
    requires transitive spring.amqp;
    requires spring.beans;
    requires spring.context;
    requires spring.core;
    requires transitive spring.rabbit;
    requires transitive spring.rabbit.test;

    exports nl.vsmeets.amr.test.fileimporter;

    opens nl.vsmeets.amr.test.fileimporter to org.junit.platform.commons, spring.core;

}