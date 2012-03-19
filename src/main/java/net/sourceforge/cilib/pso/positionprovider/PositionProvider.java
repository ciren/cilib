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
package net.sourceforge.cilib.pso.positionprovider;

import java.io.Serializable;

import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.pso.particle.ParameterizedParticle;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Cloneable;


/**
 * TODO: Complete this javadoc.
 *
 */
public interface PositionProvider extends Cloneable, Serializable {

    /**
     * Clone the stategy by creating a new object with the same contents and values
     * as the current object.
     *
     * @return A clone of the current <tt>PositionProvider</tt>
     */
    PositionProvider getClone();

    /**
     * Update the position of the <tt>Particle</tt>.
     *
     * @param particle The <tt>Particle</tt> to perform the position update on.
     */
    Vector get(Particle particle);
    
    /*
     * Gets the value of the updated position of the inertia parameter
     * @param The particle whose parameters are being updated
     * @return inertia The new value of the inertia parameter
     */
    double getInertia(ParameterizedParticle particle);
    
    /*
     * Gets the value of the updated position of the social acceleration parameter
     * @param The particle whose parameters are being updated
     * @return inertia The new value of the social acceleration parameter
     */
    double getSocialAcceleration(ParameterizedParticle particle);
    
    /*
     * Gets the value of the updated position of the cognitive acceleration parameter
     * @param The particle whose parameters are being updated
     * @return inertia The new value of the cognitive acceleration parameter
     */
    double getCognitiveAcceleration(ParameterizedParticle particle);
    
    /*
     * Gets the value of the updated position of the vmax parameter
     * @param The particle whose parameters are being updated
     * @return inertia The new value of the vmax parameter
     */
    double getVmax(ParameterizedParticle particle);

}
