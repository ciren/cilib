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
package net.sourceforge.cilib.pso.velocityprovider;


import java.util.HashMap;
import java.util.Hashtable;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.pso.particle.ParameterizedParticle;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Cloneable;

/**
 *
 */
public interface VelocityProvider extends Cloneable {

    /**
     * Clone the <tt>VelocityProvider</tt> object.
     * @return A cloned <tt>VelocityProvider</tt>
     */
    @Override
    VelocityProvider getClone();

    /**
     * Perform the velocity update operation on the specified <tt>Particle</tt>.
     * @param particle The <tt>Particle</tt> to apply the operation on.
     */
    Vector get(Particle particle);

    /**
     * Update the needed control parameters for the <tt>VelocityProvider</tt>,
     * if needed.
     * @param particle The particle for whom the VelocityProvider parameters need to be updated.
     */
    void updateControlParameters(Particle particle);

    /*
     * Set the values of the control parameters to those held by a parametized particle
     * @param particle The aprticle holding the parameter values
     */
    void setControlParameters(ParameterizedParticle particle);
    
    /*
     * Get the velocity value for the control parameters held by a parametized particle
     * @param particle The particle holding the parameter and velocity values
     * @return The hashmap containing the new velocity values for each parameter. This 
     * hashmap holds the velocity for inertia as "InertiaVelocity", for social acceleration
     * as "SocialAccelerationVelocity", for cognitive acceleration as "CognitiveAccelerationVelocity"
     * and for vmax as "VmaxVelocity".
     */
    HashMap<String, Double> getControlParameterVelocity(ParameterizedParticle particle);
}
