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
package net.sourceforge.cilib.entity.comparator;

import java.io.Serializable;
import java.util.Comparator;
import net.sourceforge.cilib.entity.Entity;

/**
 * Perform a comparison based on the natural ordering of fitnesses.
 * @param <E> The {@code Entity} type.
 * @author gpampara
 */
public class NaturalOrderFitnessComparator<E extends Entity> implements Comparator<E>, Serializable {
    private static final long serialVersionUID = -7915197939462591121L;

    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(E o1, E o2) {
        return o1.getFitness().compareTo(o2.getFitness());
    }

}
