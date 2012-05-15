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
import net.sourceforge.cilib.util.Cloneable;

/**
 */
public interface GuideProvider extends Cloneable {

    @Override
    GuideProvider getClone();

    /**
     * Selects a guide for {@code particle}.
     * @param particle The particle who's guide will be selected.
     * @return The selected guide.
     */
    StructuredType get(Particle particle);
    
    /*
     * Gets the guide of the inertia control parameter for the particle provided
     * @param particle The particle whose guide is returned
     * @return The guide
     */
    ControlParameter getInertia(ParameterizedParticle particle);
    
     /*
     * Gets the guide of the social acceleration control parameter for the particle provided
     * @param particle The particle whose guide is returned
     * @return The guide
     */
    ControlParameter getSocialAcceleration(ParameterizedParticle particle);
    
     /*
     * Gets the guide of the cognitive acceleration control parameter for the particle provided
     * @param particle The particle whose guide is returned
     * @return The guide
     */
    ControlParameter getCognitiveAcceleration(ParameterizedParticle particle);
    
     /*
     * Gets the guide of the vmax control parameter for the particle provided
     * @param particle The particle whose guide is returned
     * @return The guide
     */
    ControlParameter getVmax(ParameterizedParticle particle);
}
