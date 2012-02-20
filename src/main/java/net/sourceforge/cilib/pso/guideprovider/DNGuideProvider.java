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
package net.sourceforge.cilib.pso.guideprovider;

import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.pso.particle.ParametizedParticle;
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
public class DNGuideProvider implements GuideProvider {

    @Override
    public GuideProvider getClone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Vector get(Particle particle) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /*
     * Not applicable
     */
    @Override
    public ControlParameter getInertia(ParametizedParticle particle) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /*
     * Not applicable
     */
    @Override
    public ControlParameter getSocialAcceleration(ParametizedParticle particle) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /*
     * Not applicable
     */
    @Override
    public ControlParameter getCognitiveAcceleration(ParametizedParticle particle) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /*
     * Not applicable
     */
    @Override
    public ControlParameter getVmax(ParametizedParticle particle) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
