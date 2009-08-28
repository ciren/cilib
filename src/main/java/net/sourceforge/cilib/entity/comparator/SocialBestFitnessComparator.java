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
import net.sourceforge.cilib.entity.SocialEntity;
import net.sourceforge.cilib.problem.Fitness;

/**
 * Compare two {@link SocialEntity} instances, based on the available social best
 * fitness.
 * @param <E> The {@code SocialEntity} type.
 * @see SocialEntity#getSocialBestFitness()
 * @author gpampara
 */
public class SocialBestFitnessComparator<E extends SocialEntity> implements Comparator<E>, Serializable {
    private static final long serialVersionUID = 9035734190908368266L;

    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(E o1, E o2) {
        Fitness f1 = o1.getSocialBestFitness();
        Fitness f2 = o2.getSocialBestFitness();

        return f1.compareTo(f2);
    }

}
