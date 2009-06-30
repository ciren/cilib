/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.util;

import java.util.Collection;

import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.StructuredType;

/**
 * Definition of how to determine the distance between two objects.
 *
 * @author Edwin Peer
 * @author Gary Pampara
 */
public interface DistanceMeasure {

    /**
     * Determine the distance between the two provided {@link StructuredType}
     * instances.
     * @param <T>
     * @param <U>
     * @param x The first object from which the calculation is to be performed.
     * @param y The second object from which the calculation is to be performed.
     * @return The distance between the provided instances.
     */
    public <T extends Type, U extends StructuredType<T>> double distance(U x, U y);

    /**
     * Determine the distance between the two provided {@linkplain Collection}s.
     * @param <T> The {@linkplain Collection} type.
     * @param x The first {@linkplain Collection}.
     * @param y The second {@linkplain Collection}.
     * @return The distance value.
     */
    public <T extends Collection<? extends Number>> double distance(T x, T y);

}
