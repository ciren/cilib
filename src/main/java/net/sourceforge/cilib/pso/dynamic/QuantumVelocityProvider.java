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
package net.sourceforge.cilib.pso.dynamic;

import java.util.HashMap;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.pso.particle.ParameterizedParticle;
import net.sourceforge.cilib.pso.velocityprovider.StandardVelocityProvider;
import net.sourceforge.cilib.pso.velocityprovider.VelocityProvider;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * VelocityProvider for QSO (Quantum PSO). Implemented according
 * to paper by Blackwell and Branke, "Multiswarms, Exclusion, and
 * Anti-Convergence in Dynamic Environments."
 *
 *
 */
public class QuantumVelocityProvider implements VelocityProvider {

    private static final long serialVersionUID = -940568473388702506L;
    private static final double EPSILON = 0.000000001;
    
    private VelocityProvider delegate;

    /**
     * Create a new instance of {@linkplain QuantumVelocityProvider}.
     */
    public QuantumVelocityProvider() {
        this.delegate = new StandardVelocityProvider();
    }

    /**
     * Create an copy of the provided instance.
     * @param copy The instance to copy.
     */
    public QuantumVelocityProvider(QuantumVelocityProvider copy) {
        this.delegate = copy.delegate.getClone();
    }

    @Override
    public QuantumVelocityProvider getClone() {
        return new QuantumVelocityProvider(this);
    }

    /**
     * Update particle velocity; do it in a standard way if the particle is neutral, and
     * do not update it if the particle is quantum (charged), since quantum particles do
     * not use the velocity to update their positions.
     * @param particle the particle to update position of
     */
    @Override
    public Vector get(Particle particle) {
        ChargedParticle checkChargeParticle = (ChargedParticle) particle;
        if (checkChargeParticle.getCharge() < EPSILON) {    // the particle is neutral
            return this.delegate.get(particle);
        }
        return (Vector) particle.getVelocity().getClone();
    }

        public void setDelegate(VelocityProvider delegate) {
        this.delegate = delegate;
    }

    public VelocityProvider getDelegate() {
        return this.delegate;
    }

    @Override
    public void updateControlParameters(Particle particle) {
        this.delegate.updateControlParameters(particle);
    }
    
    /*
     * Not applicable
     */
    @Override
    public void setControlParameters(ParameterizedParticle particle) {
        //Not applicable
    }
    
    /*
     * Not applicable
     */
    @Override
    public HashMap<String, Double> getControlParameterVelocity(ParameterizedParticle particle) {
        HashMap<String, Double> parameterVelocity = new HashMap<String, Double> ();
        ChargedParticle checkChargeParticle = (ChargedParticle) particle;
        if (checkChargeParticle.getCharge() < EPSILON) {    
            parameterVelocity.put("InertiaVelocity", this.delegate.getControlParameterVelocity(particle).get("InertiaVelocity"));
        }
        parameterVelocity.put("InertiaVelocity", particle.getInertia().getVelocity());
        
        if (checkChargeParticle.getCharge() < EPSILON) {    
            parameterVelocity.put("SocialAccelerationVelocity", this.delegate.getControlParameterVelocity(particle).get("SocialAccelerationVelocity"));
        }
        parameterVelocity.put("SocialAccelerationVelocity", particle.getSocialAcceleration().getVelocity());
        
        if (checkChargeParticle.getCharge() < EPSILON) {    
            parameterVelocity.put("CognitiveAccelerationVelocity", this.delegate.getControlParameterVelocity(particle).get("CognitiveAccelerationVelocity"));
        }
        parameterVelocity.put("CognitiveAccelerationVelocity", particle.getCognitiveAcceleration().getVelocity());
        
        if (checkChargeParticle.getCharge() < EPSILON) {   
            parameterVelocity.put("VmaxVelocity", this.delegate.getControlParameterVelocity(particle).get("VmaxVelocity"));
        }
        parameterVelocity.put("VmaxVelocity", particle.getVmax().getVelocity());
        
        return parameterVelocity;
    }
}
