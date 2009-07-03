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
package net.sourceforge.cilib.pso.moo.guideselectionstrategies;

import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Dynamic Neighbourhood Guide Selection Strategy
 *
 * Placeholder class for anybody interested in implementing this Multi-objective PSO.
 * The reference where the implementation details can be found is:
 *
 * <p>
 * References:
 * </p>
 * <p>
 * <ul>
 * <li> X. Hu and R. C. Eberhart, "Multiobjective Optimization using Dynamic Neighborhood Particle Swarm Optimization", in
 * Proceedings of the IEEE Congress on Evolutionary Computation, vol 2, pp. 1677-1681, 2002.
 * </li>
 * </ul>
 * </p>
 */
public class DNGuideSelectionStrategy implements GuideSelectionStrategy {

    @Override
    public GuideSelectionStrategy getClone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Vector selectGuide(Particle particle) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
