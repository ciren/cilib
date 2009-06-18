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
package net.sourceforge.cilib.pso.niching;

import java.util.List;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;

/**
 * Identification of niches within a Niching process.
 *
 * @author gpampara
 */
public interface NicheIdentificationStrategy {

    /**
     * Identify any Entity instances that, based on the decision strategy,
     * have found niches. These entities are returned, with the expectation
     * that the callee will use them in some or other manner.
     * @param topology The topology containing the list of Entity instances.
     * @return A list of niches. The list may be empty.
     */
    public List<Entity> identify(Topology<? extends Entity> topology);

}
