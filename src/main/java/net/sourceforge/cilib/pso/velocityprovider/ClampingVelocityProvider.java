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
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.pso.particle.ParameterizedParticle;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 */
public class ClampingVelocityProvider implements VelocityProvider {

    private static final long serialVersionUID = -5995116445841750100L;

    private ControlParameter vMax;
    private VelocityProvider delegate;

    public ClampingVelocityProvider() {
        this(ConstantControlParameter.of(Double.MAX_VALUE), new StandardVelocityProvider());
    }

    public ClampingVelocityProvider(ControlParameter vMax, VelocityProvider delegate) {
        this.vMax = vMax;
        this.delegate = delegate;
    }

    public ClampingVelocityProvider(ClampingVelocityProvider copy) {
        this.vMax = copy.vMax.getClone();
        this.delegate = copy.delegate.getClone();
    }

    @Override
    public ClampingVelocityProvider getClone() {
        return new ClampingVelocityProvider(this);
    }

    @Override
    public Vector get(Particle particle) {
        Vector velocity = this.delegate.get(particle);
        Vector.Builder builder = Vector.newBuilder();
        for (Numeric value : velocity) {
            builder.add(Math.min(Math.max(-vMax.getParameter(), value.doubleValue()), vMax.getParameter()));
        }
        return builder.build();
    }

    public void setVMax(ControlParameter vMax) {
        this.vMax = vMax;
    }

    public ControlParameter getVMax() {
        return this.vMax;
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
        this.vMax.updateParameter();
    }
    
    /*
     * {@inheritDoc}
     */
    @Override
    public void setControlParameters(ParameterizedParticle particle) {
        vMax = particle.getVmax();
    }
    
    /*
     * {@inheritDoc}
     */
    @Override
    public HashMap<String, Double> getControlParameterVelocity(ParameterizedParticle particle) {
        HashMap<String, Double> parameterVelocity = new HashMap<String, Double>();
        double velocity = this.delegate.getControlParameterVelocity(particle).get("VmaxVelocity");
        
        if (velocity < -vMax.getParameter()) {
            parameterVelocity.put("VmaxVelocity", -vMax.getParameter());
        } else if (velocity > vMax.getParameter()) {
            parameterVelocity.put("VmaxVelocity", vMax.getParameter());
        } else {
            parameterVelocity.put("VmaxVelocity", velocity);
        }
        
        velocity = this.delegate.getControlParameterVelocity(particle).get("InertiaVelocity");
        
        if (velocity < -particle.getInertia().getParameter()) {
            parameterVelocity.put("InertiaVelocity", -particle.getInertia().getParameter());
        } else if (velocity > particle.getInertia().getParameter()) {
            parameterVelocity.put("InertiaVelocity", particle.getInertia().getParameter());
        } else {
            parameterVelocity.put("InertiaVelocity", velocity);
        }
        
        velocity = this.delegate.getControlParameterVelocity(particle).get("SocialAccelerationVelocity");
        
        if (velocity < -particle.getSocialAcceleration().getParameter()) {
            parameterVelocity.put("SocialAccelerationVelocity", -particle.getSocialAcceleration().getParameter());
        } else if (velocity > particle.getSocialAcceleration().getParameter()) {
            parameterVelocity.put("SocialAccelerationVelocity", particle.getSocialAcceleration().getParameter());
        } else {
            parameterVelocity.put("SocialAccelerationVelocity", velocity);
        }
        
        velocity = this.delegate.getControlParameterVelocity(particle).get("CognitiveAccelerationVelocity");
        
        if (velocity < -particle.getCognitiveAcceleration().getParameter()) {
            parameterVelocity.put("CognitiveAccelerationVelocity", -particle.getCognitiveAcceleration().getParameter());
        } else if (velocity > particle.getCognitiveAcceleration().getParameter()) {
            parameterVelocity.put("CognitiveAccelerationVelocity", particle.getCognitiveAcceleration().getParameter());
        } else {
            parameterVelocity.put("CognitiveAccelerationVelocity", velocity);
        }
        
        return parameterVelocity;
    }
}
