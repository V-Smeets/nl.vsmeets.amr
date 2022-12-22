/**
 * Copyright (C) 2018 Vincent Smeets
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

import java.time.ZoneId;
import java.util.Set;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.ZoneIdConverter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import nl.vsmeets.amr.backend.database.Site;

/**
 * The site where a meter is located.
 *
 * @author vincent
 */
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "site")
public class SiteEntity extends AbstractTableEntity implements Site {

    /**
     * The name of the site.
     */
    @NonNull
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(name = "name", nullable = false, length = 64)
    private String name;

    /**
     * The time zone of the site.
     */
    @NonNull
    @Getter
    @ToString.Include
    @Column(name = "time_zone", nullable = false, length = 64)
    @Convert(converter = ZoneIdConverter.class)
    private ZoneId timeZone;

    /**
     * The P1 telegrams.
     */
    @Getter
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "site")
    private Set<P1TelegramEntity> p1Telegrams;

}
