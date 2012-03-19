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
import net.sourceforge.cilib.pso.particle.ParameterizedParticle;
import net.sourceforge.cilib.type.types.container.StructuredType;

/**
 * <p>
 * A concrete implementation of {@link GuideProvider} where the neighbourhood
 * best position of a particle gets selected as a guide (usually global guide).
 * </p>
 *
 */
public class NBestGuideProvider implements GuideProvider {

    private static final long serialVersionUID = 6770044000445220658L;

    public NBestGuideProvider() {
    }

    public NBestGuideProvider(NBestGuideProvider copy) {
    }

    /*
     * {@inheritDoc}
     */
    @Override
    public NBestGuideProvider getClone() {
        return new NBestGuideProvider(this);
    }
    
    /*
     * {@inheritDoc}
     */
    @Override
    public StructuredType get(Particle particle) {
        return particle.getNeighbourhoodBest().getBestPosition();
    }
    
    /*
     * {@inheritDoc}
     */
    @Override
    public ControlParameter getInertia(ParameterizedParticle particle) {
        return particle.getNeighbourhoodBest().getBestInertia();
    }
    
    /*
     * {@inheritDoc}
     */
    @Override
    public ControlParameter getSocialAcceleration(ParameterizedParticle particle) {
        return particle.getNeighbourhoodBest().getBestSocialAcceleration();
    }
    
    /*
     * {@inheritDoc}
     */
    @Override
    public ControlParameter getCognitiveAcceleration(ParameterizedParticle particle) {
        return particle.getNeighbourhoodBest().getBestCognitiveAcceleration();
    }
    
    /*
     * {@inheritDoc}
     */
    @Override
    public ControlParameter getVmax(ParameterizedParticle particle) {
        return particle.getNeighbourhoodBest().getBestVmax();
    }
}
