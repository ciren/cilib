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
package net.sourceforge.cilib.entity;

import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.pso.positionprovider.NeighbourhoodBestUpdateStrategy;
import net.sourceforge.cilib.util.Cloneable;

/**
 * <p>
 * An Entity that can recieve information from other Entities.
 * </p>
 * <p>
 * Social infomation sharing is very important. This is a marker interface
 * that will identify Entity objects that are able to transfer knowledge to
 * other Entities.
 * </p>
 *
 * @author gpampara
 */
public interface SocialEntity extends Cloneable {

     /**
     * Get the current social best fitness. This {@linkplain Fitness} value is dependent
     * on the current {@linkplain NeighbourhoodBestUpdateStrategy}.
     * @return The fitness based on the currently set {@linkplain NeighbourhoodBestUpdateStrategy}.
     */
    Fitness getSocialFitness();

    /**
     * Get the reference to the currently employed <code>NeighbourhoodBestUpdateStrategy</code>.
     * @return A reference to the current <code>NeighbourhoodBestUpdateStrategy</code> object
     */
    NeighbourhoodBestUpdateStrategy getNeighbourhoodBestUpdateStrategy();

    /**
     * Set the <code>NeighbourhoodBestUpdateStrategy</code> to be used by the {@linkplain Entity}.
     * @param neighbourhoodBestUpdateStrategy The <code>NeighbourhoodBestUpdateStrategy</code> to be used
     */
    void setNeighbourhoodBestUpdateStrategy(NeighbourhoodBestUpdateStrategy neighbourhoodBestUpdateStrategy);

}
