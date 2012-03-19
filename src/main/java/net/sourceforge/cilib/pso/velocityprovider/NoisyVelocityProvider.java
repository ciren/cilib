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
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.GaussianDistribution;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFuction;
import net.sourceforge.cilib.pso.particle.ParameterizedParticle;
import net.sourceforge.cilib.util.Vectors;

/**
 * Decorates a {@link PositionUpdateVisitor} or a {@link VelocityProvider}
 * with random noise from any probability distribution function.
 *
 */
public class NoisyVelocityProvider implements VelocityProvider {

    private static final long serialVersionUID = -4398497101382747367L;
    private ProbabilityDistributionFuction distribution;
    private VelocityProvider delegate;

    public NoisyVelocityProvider() {
        this.distribution = new GaussianDistribution();
        this.delegate = new StandardVelocityProvider();
    }

    public NoisyVelocityProvider(NoisyVelocityProvider rhs) {
        this.distribution = rhs.distribution;
        this.delegate = rhs.delegate.getClone();
    }

    @Override
    public Vector get(Particle particle) {
        Vector velocity = this.delegate.get(particle);
        Vector.Builder builder = Vector.newBuilder();
        for (int i = 0; i < velocity.size(); i++) {
            builder.add(this.distribution.getRandomNumber());
        }
        return Vectors.sumOf(velocity, builder.build());
    }

    @Override
    public NoisyVelocityProvider getClone() {
        return new NoisyVelocityProvider(this);
    }

    public VelocityProvider getDelegate() {
        return this.delegate;
    }

    public void setDelegate(VelocityProvider delegate) {
        this.delegate = delegate;
    }

    public ProbabilityDistributionFuction getDistribution() {
        return this.distribution;
    }

    public void setDistribution(ProbabilityDistributionFuction distribution) {
        this.distribution = distribution;
    }
    
    /*
     * Not applicable
     */
    @Override
    public void setControlParameters(ParameterizedParticle particle) {
        this.delegate.setControlParameters(particle);
    }
    
    /*
     * Not applicable
     */
    @Override
    public HashMap<String, Double> getControlParameterVelocity(ParameterizedParticle particle){
        HashMap<String, Double> result = new HashMap<String, Double>();
        HashMap<String, Double> velocity = this.delegate.getControlParameterVelocity(particle);
        double inertiaVelocity = this.distribution.getRandomNumber() + velocity.get("InertiaVelocity");
        double socialAccelerationVelocity = this.distribution.getRandomNumber() + velocity.get("SocialAccelerationVelocity");
        double cognitiveAccelerationVelocity = this.distribution.getRandomNumber() + velocity.get("CognitiveAccelerationVelocity");
        double vmaxVelocity = this.distribution.getRandomNumber() + velocity.get("VmaxVelocity");
        
        result.put("InertiaVelocity", inertiaVelocity);
        result.put("SocialAccelerationVelocity", socialAccelerationVelocity);
        result.put("CognitiveAccelerationVelocity", cognitiveAccelerationVelocity);
        result.put("VmaxVelocity", vmaxVelocity);
        
        return result;
    }
}
