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
import java.util.Iterator;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.ParameterizedParticle;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 */
public class FIPSVelocityProvider implements VelocityProvider {

    private static final long serialVersionUID = 6391914534943249737L;

    private ControlParameter inertiaWeight;
    private ControlParameter socialAcceleration;
    private ControlParameter cognitiveAcceleration;
    private RandomProvider randomProvider;

    public FIPSVelocityProvider() {
        this.inertiaWeight = ConstantControlParameter.of(0.729844);
        this.socialAcceleration = ConstantControlParameter.of(1.496180);
        this.cognitiveAcceleration = ConstantControlParameter.of(1.496180);
        this.randomProvider = new MersenneTwister();
    }

    public FIPSVelocityProvider(FIPSVelocityProvider copy) {
        this.inertiaWeight = copy.inertiaWeight.getClone();
        this.socialAcceleration = copy.socialAcceleration.getClone();
        this.cognitiveAcceleration = copy.cognitiveAcceleration.getClone();
        this.randomProvider = copy.randomProvider;
    }

    @Override
    public FIPSVelocityProvider getClone() {
        return new FIPSVelocityProvider(this);
    }

    @Override
    public Vector get(Particle particle) {
        Vector velocity = (Vector) particle.getVelocity();
        Vector position = (Vector) particle.getPosition();

        Topology<Particle> topology = ((PSO) AbstractAlgorithm.get()).getTopology();
        Iterator<Particle> swarmIterator = topology.iterator();

        while (swarmIterator.hasNext()) {
            Particle currentTarget = swarmIterator.next();
            if (currentTarget.getId() == particle.getId()) {
                break;
            }
        }

        Vector.Builder builder = Vector.newBuilder();
        for (int i = 0; i < particle.getDimension(); ++i) {
            double informationSum = 0.0;
            int numberOfNeighbours = 0;

            Iterator<Particle> neighborhoodIterator = topology.neighbourhood(swarmIterator);

            while (neighborhoodIterator.hasNext()) {
                Particle currentTarget = neighborhoodIterator.next();
                Vector currentTargetPosition = (Vector) currentTarget.getBestPosition();

                double randomComponent = (this.cognitiveAcceleration.getParameter() + this.socialAcceleration.getParameter()) * this.randomProvider.nextDouble();

                informationSum += randomComponent * (currentTargetPosition.doubleValueOf(i) - position.doubleValueOf(i));

                numberOfNeighbours++;
            }

            double value = this.inertiaWeight.getParameter() * (velocity.doubleValueOf(i) + (informationSum / numberOfNeighbours));

            builder.add(value);
        }

        return builder.build();
    }
    
    @Override
    public void updateControlParameters(Particle particle) {
        this.inertiaWeight.updateParameter();
        this.socialAcceleration.updateParameter();
        this.cognitiveAcceleration.updateParameter();
    }
    
    
    /*
     * {@inheritDoc}
     */
    @Override
    public void setControlParameters(ParameterizedParticle particle) {
        inertiaWeight = particle.getInertia();
        socialAcceleration = particle.getSocialAcceleration();
        cognitiveAcceleration = particle.getCognitiveAcceleration();
    }
    
    /*
     * {@inheritDoc}
     */
    @Override
    public HashMap<String, Double> getControlParameterVelocity(ParameterizedParticle particle) {
        HashMap<String, Double> parameterVelocity = new HashMap<String, Double>();
        
        //Inertia
        double inertiaVelocity = particle.getInertia().getVelocity();
        double inertiaPosition = particle.getInertia().getParameter();
        double socialVelocity = particle.getSocialAcceleration().getVelocity();
        double socialPosition = particle.getSocialAcceleration().getParameter();
        double personalVelocity = particle.getCognitiveAcceleration().getVelocity();
        double personalPosition = particle.getCognitiveAcceleration().getParameter();
        double vmaxVelocity = particle.getVmax().getVelocity();
        double vmaxPosition = particle.getVmax().getParameter();
        
        Topology<Particle> topology = ((PSO) AbstractAlgorithm.get()).getTopology();
        Iterator<Particle> swarmIterator = topology.iterator();

        while (swarmIterator.hasNext()) {
            Particle currentTarget = swarmIterator.next();
            if (currentTarget.getId() == particle.getId()) {
                break;
            }
        }

        double inertiaInformationSum = 0.0;
        int numberOfNeighbours = 0;
        double socialInformationSum = 0.0;
        double personalInformationSum = 0.0;
        double vmaxInformationSum = 0.0;

        Iterator<Particle> neighborhoodIterator = topology.neighbourhood(swarmIterator);

        while (neighborhoodIterator.hasNext()) {
            ParameterizedParticle currentTarget = (ParameterizedParticle) neighborhoodIterator.next();
            double inertiaCurrentTargetPosition = currentTarget.getBestInertia().getParameter();
            double socialCurrentTargetPosition = currentTarget.getBestSocialAcceleration().getParameter();
            double personalCurrentTargetPosition = currentTarget.getBestCognitiveAcceleration().getParameter();
            double vmaxCurrentTargetPosition = currentTarget.getBestVmax().getParameter();

            double inertiaRandomComponent = (this.cognitiveAcceleration.getParameter() + this.socialAcceleration.getParameter()) * this.randomProvider.nextDouble();
            double socialRandomComponent = (this.cognitiveAcceleration.getParameter() + this.socialAcceleration.getParameter()) * this.randomProvider.nextDouble();
            double personalRandomComponent = (this.cognitiveAcceleration.getParameter() + this.socialAcceleration.getParameter()) * this.randomProvider.nextDouble();
            double vmaxRandomComponent = (this.cognitiveAcceleration.getParameter() + this.socialAcceleration.getParameter()) * this.randomProvider.nextDouble();

            inertiaInformationSum += inertiaRandomComponent * (inertiaCurrentTargetPosition - inertiaPosition);
            personalInformationSum += personalRandomComponent * (personalCurrentTargetPosition - personalPosition);
            socialInformationSum += socialRandomComponent * (socialCurrentTargetPosition - socialPosition);
            vmaxInformationSum += vmaxRandomComponent * (vmaxCurrentTargetPosition - vmaxPosition);

            numberOfNeighbours++;
        }

        double inertiaValue = this.inertiaWeight.getParameter() * (inertiaVelocity + (inertiaInformationSum / numberOfNeighbours));
        double socialValue = this.inertiaWeight.getParameter() * (socialVelocity + (socialInformationSum / numberOfNeighbours));
        double personalValue = this.inertiaWeight.getParameter() * (personalVelocity + (personalInformationSum / numberOfNeighbours));
        double vmaxValue = this.inertiaWeight.getParameter() * (vmaxVelocity + (vmaxInformationSum / numberOfNeighbours));

        parameterVelocity.put("InertiaVelocity", inertiaValue);
        parameterVelocity.put("SocialAccelerationVelocity", socialValue);
        parameterVelocity.put("CognitiveAccelerationVelocity", personalValue);
        parameterVelocity.put("VmaxVelocity", vmaxValue);
        
        return parameterVelocity;
       
    }
}
