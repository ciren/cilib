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
package net.sourceforge.cilib.entity;

import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.pso.positionupdatestrategies.NeighbourhoodBestUpdateStrategy;
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
public interface SocialEntity extends Entity, Cloneable {

     /**
     * Get the current social best fitness. This {@linkplain Fitness} value is dependent
     * on the current {@linkplain NeighbourhoodBestUpdateStrategy}.
     * @return The fitness based on the currently set {@linkplain NeighbourhoodBestUpdateStrategy}.
     */
    public Fitness getSocialBestFitness();

    /**
     * Get the reference to the currently employed <code>NeighbourhoodBestUpdateStrategy</code>.
     * @return A reference to the current <code>NeighbourhoodBestUpdateStrategy</code> object
     */
    public NeighbourhoodBestUpdateStrategy getNeighbourhoodBestUpdateStrategy();

    /**
     * Set the <code>NeighbourhoodBestUpdateStrategy</code> to be used by the {@linkplain Entity}.
     * @param neighbourhoodBestUpdateStrategy The <code>NeighbourhoodBestUpdateStrategy</code> to be used
     */
    public void setNeighbourhoodBestUpdateStrategy(NeighbourhoodBestUpdateStrategy neighbourhoodBestUpdateStrategy);

}
