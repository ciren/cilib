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
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.GaussianDistribution;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFuction;
import net.sourceforge.cilib.pso.particle.ParameterizedParticle;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *  The <tt>VelocityProvider</tt> for the Bare Bones PSO as defined by Kennedy.
 *
 *  TODO: get the required references
 *
 */
public class BareBonesVelocityProvider implements VelocityProvider {

    private static final long serialVersionUID = -823686042197742768L;
    
    protected ProbabilityDistributionFuction randomDistribution;

    public BareBonesVelocityProvider() {
        this.randomDistribution = new GaussianDistribution();
    }

    public BareBonesVelocityProvider(BareBonesVelocityProvider copy) {
        this.randomDistribution = copy.randomDistribution;
    }

    @Override
    public BareBonesVelocityProvider getClone() {
        return new BareBonesVelocityProvider(this);
    }

    @Override
    public Vector get(Particle particle) {
        Vector localGuide = (Vector) particle.getLocalGuide();
        Vector globalGuide = (Vector) particle.getGlobalGuide();

        Vector.Builder builder = Vector.newBuilder();
        for (int i = 0; i < particle.getDimension(); ++i) {
            //double tmp1 = cognitive.getParameter();
            //double tmp2 = social.getParameter();

            double sigma = Math.abs(localGuide.doubleValueOf(i) - globalGuide.doubleValueOf(i));
            //according to Kennedy
            double mean = (localGuide.doubleValueOf(i) + globalGuide.doubleValueOf(i)) / 2;
            //andries proposal: double mean = (tmp1*personalBestPosition.getReal(i) + tmp2*nBestPosition.getReal(i)) / (tmp1+tmp2);

            builder.add(this.randomDistribution.getRandomNumber(mean, sigma));
        }
        return builder.build();
    }

    public ProbabilityDistributionFuction getRandomDistribution() {
        return this.randomDistribution;
    }

    public void setRandomDistribution(ProbabilityDistributionFuction pdf) {
        this.randomDistribution = pdf;
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
        HashMap<String, Double> result = new HashMap<String, Double>();
        
        double localGuide = particle.getLocalGuideInertia().getParameter();
        double globalGuide =  particle.getGlobalGuideInertia().getParameter();
        double sigma = Math.abs(localGuide - globalGuide);
        //according to Kennedy
        double mean = (localGuide + globalGuide) / 2;
        //andries proposal: double mean = (tmp1*personalBestPosition.getReal(i) + tmp2*nBestPosition.getReal(i)) / (tmp1+tmp2);
        double value = this.randomDistribution.getRandomNumber(mean, sigma);
        result.put("InertiaVelocity", value);
        
        localGuide = particle.getLocalGuideSocial().getParameter();
        globalGuide =  particle.getGlobalGuideSocial().getParameter();
        sigma = Math.abs(localGuide - globalGuide);
        mean = (localGuide + globalGuide) / 2;
        value = this.randomDistribution.getRandomNumber(mean, sigma);
        result.put("SocialAccelerationVelocity", value);
        
        localGuide = particle.getLocalGuidePersonal().getParameter();
        globalGuide =  particle.getGlobalGuidePersonal().getParameter();
        sigma = Math.abs(localGuide - globalGuide);
        mean = (localGuide + globalGuide) / 2;
        value = this.randomDistribution.getRandomNumber(mean, sigma);
        result.put("CognitiveAccelerationVelocity", value);
        
        localGuide = particle.getLocalGuideVmax().getParameter();
        globalGuide =  particle.getGlobalGuideVmax().getParameter();
        sigma = Math.abs(localGuide - globalGuide);
        mean = (localGuide + globalGuide) / 2;
        value = this.randomDistribution.getRandomNumber(mean, sigma);
        result.put("VmaxVelocity", value);
        
        return result;
    }
    
  
}
