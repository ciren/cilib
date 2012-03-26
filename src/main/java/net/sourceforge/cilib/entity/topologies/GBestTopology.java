/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.entity.topologies;

import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;

/**
 * <p>
 * Implementation of the gbest neighbourhood topology. This topology is a special 
 * case of the LBestTopology where the neighbourhood size is the swarm size.
 * </p><p>
 * References:
 * </p><p><ul><li>
 * R.C. Eberhart, P. Simpson, and R. Drobbins, "Computational Intelligence PC Tools,"
 * chapter 6, pp. 212-226. Academic Press Professional, 1996.
 * </li></ul></p>
 *
 * @param <E> The {@linkplain Entity} type.
 */
public class GBestTopology<E extends Entity> extends LBestTopology<E> {
    private static final long serialVersionUID = 3190027340582769112L;

    /**
     * Creates a new instance of <code>GBestTopology</code>.
     */
    public GBestTopology() {
        super();
    }

    public GBestTopology(GBestTopology<E> copy) {
        super(copy);
    }

    @Override
    public GBestTopology<E> getClone() {
        return new GBestTopology<E>(this);
    }

    @Override
    public void setNeighbourhoodSize(ControlParameter neighbourhoodSize) {
        // Do nothing: neighbourhood size is the swarm size
    }

    @Override
    public int getNeighbourhoodSize() {
        return size();
    }
}

